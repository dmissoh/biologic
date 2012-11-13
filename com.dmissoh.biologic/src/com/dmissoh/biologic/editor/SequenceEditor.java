package com.dmissoh.biologic.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.dmissoh.biologic.Activator;
import com.dmissoh.biologic.editor.SequenceEditorInput.Purpose;
import com.dmissoh.biologic.internal.PluginService;
import com.dmissoh.biologic.listeners.ILogEventListener;
import com.dmissoh.biologic.models.Entry;
import com.dmissoh.biologic.models.Family;
import com.dmissoh.biologic.models.ISequence;
import com.dmissoh.biologic.models.RunningEvent;
import com.dmissoh.biologic.models.Sequence;
import com.dmissoh.biologic.models.Entry.TYPE;
import com.dmissoh.biologic.preferences.BioLogicPreferences;
import com.dmissoh.biologic.time.Stopwatch;
import com.dmissoh.biologic.utils.ExportUtils;
import com.dmissoh.biologic.utils.ModelUtils;
import com.dmissoh.biologic.utils.TimeUtils;
import com.dmissoh.biologic.viewers.SequenceEditorTableViewer;

public class SequenceEditor extends EditorPart {

	private boolean isDirty;
	private Listener listener;
	private CLabel errorLabel;
	private boolean sequenceTerminated;
	private Button startSequenceButton;
	private Button pauseSequenceButton;
	private Button analyseSequenceButton;
	private ListenerList logEventListeners;
	private IPropertyChangeListener preferenceListener;
	private SequenceEditorTableViewer sequenceEditorTableViewer;

	private Map<String, Family> nameToFamilyMap = new HashMap<String, Family>();

	@Override
	public void doSave(IProgressMonitor monitor) {
		autoSave();
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		if (!(input instanceof SequenceEditorInput)) {
			throw new PartInitException(
					"Invalid Input: Must be a SequenceEditorInput"); //$NON-NLS-1$
		}
		this.setInput(input);

		preferenceListener = new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(
						BioLogicPreferences.PREF_VALUE_VARIABLES)) {
					updateEnablement();
				}
			}
		};

		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(
				preferenceListener);

		setPartName(getEditorInput().getName());
		setTitleImage(getEditorInput().getImageDescriptor().createImage());
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void createPartControl(Composite parent) {

		logEventListeners = new ListenerList();

		Composite top = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		top.setLayout(layout);
		// top banner
		Composite banner = new Composite(top, SWT.NONE);
		banner.setLayout(new GridLayout(4, false));
		banner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		startSequenceButton = new Button(banner, SWT.TOGGLE);

		startSequenceButton.setImage(PluginService.getInstance().getImage(
				"icons/startSequence.png"));

		pauseSequenceButton = new Button(banner, SWT.TOGGLE);

		pauseSequenceButton.setEnabled(!isLive());

		pauseSequenceButton.setImage(PluginService.getInstance().getImage(
				"icons/pauseSequence.png"));

		pauseSequenceButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean pause = pauseSequenceButton.getSelection();
				if (pause) {
					sequenceEditorTableViewer.pauseTimer();
				} else {
					sequenceEditorTableViewer.resumeTimer();
				}
			}

		});

		analyseSequenceButton = new Button(banner, SWT.NONE);
		Activator.getDefault();
		analyseSequenceButton.setImage(PluginService.getInstance().getImage(
				"icons/proceed.png"));

		listener = new Listener() {
			public void handleEvent(Event event) {
				if (startSequenceButton.getSelection()) {
					if (amIActive() && !isPaused()) {
						proceedKeyPress(event);
					}
				}
			}
		};

		errorLabel = new CLabel(banner, SWT.NONE);
		GridData gridData = new GridData();
		errorLabel.setLayoutData(gridData);
		errorLabel.setLayoutData(new GridData(GridData.FILL_BOTH));
		Font font = JFaceResources.getFontRegistry().getBold("Courier");
		errorLabel.setFont(font);
		errorLabel.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER,
				true, true));

		Display.getDefault().addFilter(SWT.KeyDown, listener);

		startSequenceButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				boolean run = startSequenceButton.getSelection();
				long time = System.currentTimeMillis();
				if (run) {
					sequenceEditorTableViewer.startTimer();
					sequenceEditorTableViewer.setStartTime(TimeUtils
							.formatToTime(time));
					getInputAsSequence().setStartTime(time);
					isDirty = true;
					firePropertyChange(EditorPart.PROP_DIRTY);

				} else {
					if (!isConsistence()) {
						MessageDialog.openWarning(getSite().getShell(),
								"Running event(s)",
								"Some events remain in the running state!");
					}
					sequenceEditorTableViewer.stopTimer();
					setSequenceTerminated(true);
					getInputAsSequence().setEndTime(time);
					autoSave();
				}
				setFocus();
				startSequenceButton.setEnabled(!isSequenceTerminated());
				if (!isLive()) {
					pauseSequenceButton.setEnabled(!isSequenceTerminated());
				}
			}

		});

		analyseSequenceButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkbenchWindow window = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();
				if (window != null) {
					try {

						SequenceEditorInput input = new SequenceEditorInput(
								false, isLive());
						input.setSequence(getInputAsSequence());
						input.setPurpose(Purpose.GRAPHICAL);
						window.getActivePage().openEditor(input,
								SequenceGraphicalEditor.class.getName(), true);
					} catch (PartInitException exception) {
						MessageDialog.openError(window.getShell(), "Error",
								"Error opening view:" + exception.getMessage());
					}
				}

			}

		});
		sequenceEditorTableViewer = new SequenceEditorTableViewer(top,
				getInputAsSequence());
		updateEnablement();
		restoreSequenceData();
	}

	private boolean isPaused() {
		return (this.pauseSequenceButton.getSelection());
	}

	private void showErrorMessage(boolean isVisible, String messages) {
		errorLabel.setText(messages);
		if (isVisible) {
			errorLabel.setImage(PluginService.getInstance().getImage(
					"icons/error.png"));
		} else {
			errorLabel.setImage(null);
		}
		errorLabel.setVisible(isVisible);
		errorLabel.getParent().layout();
	}

	private void restoreSequenceData() {
		if (isReadOnly()) {
			Sequence sequence = getInputAsSequence();
			sequenceEditorTableViewer.setStartTime(TimeUtils
					.formatToTime(sequence.getStartTime()));
			sequence.setFamilies(ModelUtils.spitIntoFamilies(sequence));
		}
	}

	private boolean hasConfig() {
		return (ModelUtils.getFamiliesFromPreferences().size() > 0);
	}

	private void updateEnablement() {
		boolean isReadOnly = isReadOnly();
		boolean hasConfig = hasConfig();
		boolean enabled = !isReadOnly && hasConfig;
		if (!hasConfig) {
			showErrorMessage(
					true,
					"No events have been configured.\nUse the preference page to add at least one configuration.");
		} else {
			showErrorMessage(false, "");
		}
		startSequenceButton.setEnabled(enabled);
		pauseSequenceButton.setEnabled(enabled && !isLive() && startSequenceButton.getSelection());
	}

	private void autoSave() {
		String dateTimePrefix = String.valueOf(getInputAsSequence()
				.getStartTime());
		String logFolderPath = ExportUtils.getInstance().getLogFolderPath();
		String path = ExportUtils.getInstance().getLogFolderPath()
				+ File.separator + "log_" + dateTimePrefix + ".log";
		File logFolder = new File(logFolderPath);
		if (!logFolder.exists()) {
			logFolder.mkdir();
		}
		File file = new File(path);
		save(file.getAbsolutePath());
		isDirty = false;
		firePropertyChange(EditorPart.PROP_DIRTY);
	}

	private boolean isConsistence() {
		List<Entry> runningEntries = getRunningEvent();
		if (runningEntries != null && runningEntries.size() > 0) {
			return false;
		}
		return true;
	}

	private boolean amIActive() {
		IWorkbenchPart part = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		if (part == this) {
			return true;
		}
		return false;
	}

	private void proceedKeyPress(Event event) {
		List<Family> families = getFamilies();
		for (Family family : families) {
			handleKeyEventForFamily(event, family);
		}
	}

	private long getElapsedTime() {
		return getStopwatch().elapsed();
	}

	private long getTime() {
		long now = System.currentTimeMillis();
		return now;
	}

	private Stopwatch getStopwatch() {
		return getSequenceEditorTableViewer().getTimer().getStopwatch();
	}

	private boolean isLive() {
		return getInputAsSequence().isLive();
	}

	private void handleKeyEventForFamily(Event event, Family family) {

		long time = isLive() ? getTime() : getElapsedTime();

		Entry logEntry = new Entry(time);

		char startKey = String.valueOf(family.getStartKey()).toLowerCase()
				.charAt(0);
		char endKey = String.valueOf(family.getEndKey()).toLowerCase()
				.charAt(0);
		char keyPressed = String.valueOf(event.character).toLowerCase().charAt(
				0);

		if (keyPressed == startKey || keyPressed == endKey) {
			String name = family.getName();
			logEntry.setName(name);

			// fillFamily(family);
			Stack<Entry> logEntries = family.getLogEntries();

			if (keyPressed == startKey) {
				if (family.isPunctual()) {
					logEntry.setType(TYPE.PUNCTUAL);
				} else {
					logEntry.setType(TYPE.START);
				}
				if (!family.isPunctual()) {
					if (logEntries.empty()) {
						addLogEntry(logEntry);
					} else {
						if (logEntries.peek().getType() != TYPE.START) {
							addLogEntry(logEntry);
							clearErrorMessage();
						} else {
							showSameEntryTypeError(logEntry);
						}
					}
				} else {
					addLogEntry(logEntry);
				}

			} else if (keyPressed == endKey) {
				if (!family.isPunctual()) {
					logEntry.setType(TYPE.END);
					if (logEntries.empty()) {
						showFirstLogEntryError(logEntry);
					} else {
						if (logEntries.peek().getType() != TYPE.END) {
							addLogEntry(logEntry);
							clearErrorMessage();
						} else {
							showSameEntryTypeError(logEntry);
						}
					}
				}
			}
		}
	}

	private void showSameEntryTypeError(Entry entry) {
		String info = (entry.getType() == TYPE.START) ? "started" : "stopped";
		showErrorMessage(true, "Log entry '" + entry.getName()
				+ "' has already been " + info);
	}

	private void clearErrorMessage() {
		showErrorMessage(false, "");
	}

	private void showFirstLogEntryError(Entry entry) {
		MessageDialog.openError(getSite().getShell(), "First log entry",
				"First log entry for '" + entry.getName() + "' cannot be '"
						+ entry.getType().name() + "'.");
	}

	public List<Entry> getRunningEvent() {
		List<Entry> running = new ArrayList<Entry>();
		List<Family> families = getFamilies();
		if (families != null) {
			for (Family family : families) {
				Stack<Entry> logEntries = family.getLogEntries();
				if (!logEntries.empty()) {
					Entry top = logEntries.peek();
					if (top.getType() == TYPE.START) {
						running.add(top);
					}
				}
			}
		}
		return running;
	}

	public List<RunningEvent> getRunningEventConfiguration() {
		List<RunningEvent> running = new ArrayList<RunningEvent>();
		List<Family> families = getFamilies();
		if (families != null) {
			for (Family family : families) {
				Stack<Entry> logEntries = family.getLogEntries();
				if (!logEntries.empty()) {
					Entry top = logEntries.peek();
					if (top.getType() == TYPE.START) {
						RunningEvent runningEvent = new RunningEvent(top,
								family.getStartKey(), family.getEndKey());
						running.add(runningEvent);
					}
				}
			}
		}
		sortRunningEvents(running);
		return running;
	}

	private void sortRunningEvents(List<RunningEvent> runningEvents) {
		Collections.sort(runningEvents);
	}

	private void addLogEntry(Entry logEntry) {
		String logEntryName = logEntry.getName();
		Family family = ModelUtils
				.getFamilyForName(getFamilies(), logEntryName);
		if (family != null) {
			family.getLogEntries().push(logEntry);
		}
		sequenceEditorTableViewer.getInput().addLogEntry(logEntry);
		sequenceEditorTableViewer
				.setSelection(new StructuredSelection(logEntry));
		notifyLogEntryListeners();
	}

	public void addLogEntryListener(ILogEventListener listener) {
		logEventListeners.add(listener);
	}

	public void removeLogEntryListener(ILogEventListener listener) {
		logEventListeners.remove(listener);
	}

	private void notifyLogEntryListeners() {
		for (Object listenerObj : logEventListeners.getListeners()) {
			if (listenerObj instanceof ILogEventListener) {
				ILogEventListener listener = (ILogEventListener) listenerObj;
				listener
						.handleRunningEventsChanges(getRunningEventConfiguration());
			}
		}
	}

	// TODO remove this method and access families directly from the sequence
	// object
	public List<Family> getFamilies() {
		return getInputAsSequence().getFamilies();
	}

	public void save(String selectedFile) {
		ISequence sequence = ((SequenceEditorInput) getEditorInput())
				.getSequence();
		ExportUtils.getInstance().save(selectedFile, sequence);
	}

	public SequenceEditorTableViewer getSequenceEditorTableViewer() {
		return sequenceEditorTableViewer;
	}

	public void setSequenceEditorTableViewer(
			SequenceEditorTableViewer sequenceEditorTableViewer) {
		this.sequenceEditorTableViewer = sequenceEditorTableViewer;
	}

	private boolean isSequenceTerminated() {
		return sequenceTerminated;
	}

	private void setSequenceTerminated(boolean sequenceTerminated) {
		this.sequenceTerminated = sequenceTerminated;
	}

	@Override
	public void setFocus() {
		sequenceEditorTableViewer.setFocus();
	}

	@Override
	public void dispose() {
		sequenceEditorTableViewer.stopTimer();
		Display.getDefault().removeFilter(SWT.KeyDown, listener);
		nameToFamilyMap.clear();
		Activator.getDefault().getPreferenceStore()
				.removePropertyChangeListener(preferenceListener);
		super.dispose();
	}

	public final boolean isReadOnly() {
		return ((SequenceEditorInput) getEditorInput()).isReadOnly();
	}

	public Sequence getInputAsSequence() {
		return ((SequenceEditorInput) getEditorInput()).getSequence();
	}

}
