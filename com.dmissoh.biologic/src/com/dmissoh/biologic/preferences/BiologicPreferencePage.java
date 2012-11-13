package com.dmissoh.biologic.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.dmissoh.biologic.Activator;
import com.dmissoh.biologic.dialog.EventDialog;
import com.dmissoh.biologic.models.EventConfiguration;

public class BiologicPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	private Button deleteButton;
	private Composite composite;
	private Button newTypeButton;
	private Button loadSettingsButton;
	private TableViewer tableViewer;
	private Composite buttonComposite;

	private List<EventConfiguration> eventConfigs;

	private BioLogicPreferences bioLogicPreferences;

	private ComboFieldEditor timeUnitFieldEditor;

	protected Control createContents(Composite parent) {

		bioLogicPreferences = BioLogicPreferences.getInstance();
		setEventConfigs(bioLogicPreferences.loadPersistedValueVariables());

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);

		Label label = new Label(parent, SWT.LEFT | SWT.WRAP);
		label.setText("Hint");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 100;
		label.setLayoutData(gridData);

		composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(2, false));

		final Table resultTable = new Table(composite, SWT.FULL_SELECTION
				| SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		resultTable.setHeaderVisible(true);
		resultTable.setLinesVisible(true);

		final TableColumn tc0 = new TableColumn(resultTable, SWT.NONE);
		tc0.setText("Event name");
		tc0.setWidth(200);
		tc0.setMoveable(true);

		final TableColumn tc1 = new TableColumn(resultTable, SWT.NONE);
		tc1.setText("Start Key");
		tc1.setData(new ColumnWeightData(25));
		tc1.setMoveable(true);
		tc1.setWidth(80);

		final TableColumn tc2 = new TableColumn(resultTable, SWT.NONE);
		tc2.setText("End Key");
		tc2.setData(new ColumnWeightData(25));
		tc2.setMoveable(true);
		tc2.setWidth(80);

		this.tableViewer = new TableViewer(resultTable);
		this.tableViewer.setContentProvider(new ViewContentProvider());
		this.tableViewer.setLabelProvider(new ViewLabelProvider());
		this.tableViewer.setInput(getEventConfigs());
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		this.tableViewer.getTable().setLayoutData(gd);

		tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						updateEnablement();
					}
				});

		// create the buttons
		buttonComposite = new Composite(composite, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(
				GridData.VERTICAL_ALIGN_CENTER
						| GridData.HORIZONTAL_ALIGN_BEGINNING));
		buttonComposite.setLayout(new GridLayout(1, false));

		newTypeButton = new Button(buttonComposite, SWT.PUSH);
		newTypeButton.setText("New...");
		newTypeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		loadSettingsButton = new Button(buttonComposite, SWT.PUSH);
		loadSettingsButton.setText("Load settings...");

		deleteButton = new Button(buttonComposite, SWT.PUSH);
		deleteButton.setText("Remove");
		deleteButton.setEnabled(false);
		deleteButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite timeUnitsComposite = new Composite(composite, SWT.NONE);

		timeUnitFieldEditor = new ComboFieldEditor(
				Activator.P_TIME_UNIT_PREFERENCE, "Time unit to use:",
				new String[][] { { "Seconds", Activator.TIME_UNIT_SECONDS },
						{ "Minutes", Activator.TIME_UNIT_MINUTES },
						{ "Hours", Activator.TIME_UNIT_HOURS } },
				timeUnitsComposite);
		// Set the editor up to use this page
		timeUnitFieldEditor.setPage(this);
		timeUnitFieldEditor.setPreferenceStore(getPreferenceStore());
		timeUnitFieldEditor.load();

		// create listeners
		hookControls();

		// Initialize button enablement
		refresh();
		return composite;
	}

	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object parent) {
			return ((List<EventConfiguration>) parent)
					.toArray(new EventConfiguration[0]);
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			String returnValue = null;
			switch (index) {
			case 0:
				returnValue = ((EventConfiguration) obj).getName();
				break;
			case 1:
				returnValue = String.valueOf(((EventConfiguration) obj)
						.getStartKey());
				break;
			case 2:
				returnValue = String.valueOf(((EventConfiguration) obj)
						.getEndKey());
				break;
			default:
				break;
			}
			return returnValue;
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}

	private void hookControls() {

		newTypeButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				openDialog("Sample", "a", "s");
				updateEnablement();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		deleteButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection sel = (IStructuredSelection) tableViewer
						.getSelection();
				Object selectedElement = sel.getFirstElement();
				getEventConfigs().remove(selectedElement);
				tableViewer.setInput(getEventConfigs());
				updateEnablement();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void openDialog(String name, String startKey, String endKey) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		EventDialog eventDialog = new EventDialog(shell,
				"New Event Configuration");

		if (eventDialog.open() == Dialog.OK) {
			String eventName = eventDialog.getEventName();
			String eventStartKey = eventDialog.getStartEventKey();
			String eventEndKey = eventDialog.getEndEventKey();
			boolean punctual = eventDialog.isPunctual();

			EventConfiguration ec = null;
			if (!punctual) {
				ec = new EventConfiguration(eventName, eventStartKey.charAt(0),
						eventEndKey.charAt(0));
			} else {
				ec = new EventConfiguration(eventName, eventStartKey.charAt(0));
			}

			List<String> parametersThatAlreadyExist = parametersThatAlreadyExist(ec);
			if (parametersThatAlreadyExist != null
					&& parametersThatAlreadyExist.size() > 0) {
				String message = "One or more event parameter(s) already exist. This event configuration cannot be added. Change the following parameter(s):\n\n";
				for (String parameter : parametersThatAlreadyExist) {
					message += ("   -" + parameter + "\n");
				}
				MessageDialog.openInformation(shell, "Information", message);
				openDialog(eventName, eventStartKey, eventEndKey);
			} else if (areSameKeys(ec)) {
				MessageDialog.openInformation(shell, "Information",
						"Cannot use the same key to start and end an event");
				openDialog(eventName, eventStartKey, eventEndKey);
			} else {
				getEventConfigs().add(ec);
				tableViewer.setInput(getEventConfigs());
			}
		}
	}

	private List<String> parametersThatAlreadyExist(
			final EventConfiguration eventConfig) {
		List<String> result = new ArrayList<String>();

		List<String> names = new ArrayList<String>();
		List<Character> keys = new ArrayList<Character>();
		for (EventConfiguration ec : getEventConfigs()) {
			names.add(ec.getName());
			keys.add(ec.getStartKey());
			keys.add(ec.getEndKey());
		}

		if (names.contains(eventConfig.getName())
				|| keys.contains(eventConfig.getStartKey())
				|| keys.contains(eventConfig.getEndKey())) {
			if (names.contains(eventConfig.getName())) {
				result.add("Event Name: " + eventConfig.getName());
			}
			if (keys.contains(eventConfig.getStartKey())) {
				result.add("Start Key: " + eventConfig.getStartKey());
			}
			if (!eventConfig.isPunctual()) {
				if (keys.contains(eventConfig.getEndKey())) {
					result.add("End Key: " + eventConfig.getEndKey());
				}
			}
			return result;
		}
		return result;
	}

	private boolean areSameKeys(final EventConfiguration eventConfig) {
		if (!eventConfig.isPunctual()) {
			if (eventConfig.getStartKey() == eventConfig.getEndKey()) {
				return true;
			}
		}
		return false;
	}

	private void refresh() {
		tableViewer.refresh();
		updateEnablement();
	}

	private void updateEnablement() {
		if (getEventConfigs().size() < 1) {
			deleteButton.setEnabled(false);
		} else if (hasSelection()) {
			deleteButton.setEnabled(true);
		}
	}

	private boolean hasSelection() {
		return !tableViewer.getSelection().isEmpty();
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 *
	 * @param workbench
	 */
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		bioLogicPreferences.performDefaults();
		List<EventConfiguration> ecs = bioLogicPreferences
				.loadPersistedValueVariables();
		setEventConfigs(ecs);
		tableViewer.setInput(ecs);
		timeUnitFieldEditor.loadDefault();
	}

	/**
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		bioLogicPreferences.storeValueVariables(getEventConfigs());
		timeUnitFieldEditor.store();
		return true;
	}

	public List<EventConfiguration> getEventConfigs() {
		return eventConfigs;
	}

	public void setEventConfigs(List<EventConfiguration> eventConfigs) {
		this.eventConfigs = eventConfigs;
	}

}
