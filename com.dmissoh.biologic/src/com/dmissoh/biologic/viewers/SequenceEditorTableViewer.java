package com.dmissoh.biologic.viewers;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.dmissoh.biologic.models.Entry;
import com.dmissoh.biologic.models.Sequence;
import com.dmissoh.biologic.time.Timer;

public class SequenceEditorTableViewer {

	private Timer timer;
	private Table table;
	private Thread timerThread;
	private Label startTimeLabelTextField;
	private TableViewer tableViewer;

	private Sequence sequence;
	private final String OWNER_COLUMN = "owner";
	private final String PERCENT_COLUMN = "percent";
	private final String COMPLETED_COLUMN = "completed";
	private final String DESCRIPTION_COLUMN = "description";

	// Set column names
	private String[] columnNames = new String[] { COMPLETED_COLUMN,
			DESCRIPTION_COLUMN, OWNER_COLUMN, PERCENT_COLUMN };

	public SequenceEditorTableViewer(Composite parent, Sequence input) {
		this.sequence = input;
		this.addChildControls(parent);
	}

	public void dispose() {

		// Tell the label provider to release its ressources
		tableViewer.getLabelProvider().dispose();
	}

	private void addChildControls(Composite composite) {

		// Create a composite to hold the children
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.FILL_BOTH);
		composite.setLayoutData(gridData);

		// Set numColumns to 3 for the buttons
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 4;
		composite.setLayout(layout);

		// Create the table
		createTable(composite);

		// Create and setup the TableViewer
		createTableViewer();
		tableViewer.setContentProvider(new LogContentProvider());
		tableViewer.setLabelProvider(new LogLabelProvider(getInput().isLive()));
		// The input for the table viewer is the instance of ExampleTaskList
		tableViewer.setInput(getInput());

		// Add the buttons
		createTimeComponents(composite);
	}

	private void createTable(Composite parent) {
		int style = SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		table = new Table(parent, style);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn column = new TableColumn(table, SWT.CENTER, 0);
		column.setText("!");
		column.setWidth(20);

		// 2nd column with task Description
		column = new TableColumn(table, SWT.LEFT, 1);
		if (getInput().isLive()) {
			column.setText("Time stamp");
		} else {
			column.setText("Time elapsed");
		}
		column.setWidth(100);
		column.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				tableViewer.setSorter(new LogTaskSorter(
						LogTaskSorter.DESCRIPTION));
			}
		});

		column = new TableColumn(table, SWT.LEFT, 2);
		column.setText("Name");
		column.setWidth(200);
		column.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				tableViewer.setSorter(new LogTaskSorter(LogTaskSorter.OWNER));
			}
		});

		column = new TableColumn(table, SWT.CENTER, 3);
		column.setText("Type");
		column.setWidth(80);
		column.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				tableViewer.setSorter(new LogTaskSorter(
						LogTaskSorter.PERCENT_COMPLETE));
			}
		});
	}

	private void createTableViewer() {

		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);

		tableViewer.setColumnProperties(columnNames);
		tableViewer.setSorter(new LogTaskSorter(LogTaskSorter.DESCRIPTION));
	}

	public void close() {
		Shell shell = table.getShell();

		if (shell != null && !shell.isDisposed())
			shell.dispose();
	}

	class LogContentProvider implements IStructuredContentProvider,
			IEventListListener {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if (newInput != null)
				((Sequence) newInput).addChangeListener(this);
			if (oldInput != null)
				((Sequence) oldInput).removeChangeListener(this);
		}

		public void dispose() {
			sequence.removeChangeListener(this);
		}

		public Object[] getElements(Object parent) {
			return sequence.getLogEntries().toArray();
		}

		public void addTask(Entry task) {
			tableViewer.add(task);
		}

		public void removeTask(Entry task) {
			tableViewer.remove(task);
		}

		public void updateTask(Entry task) {
			tableViewer.update(task, null);
		}
	}

	private void createTimeComponents(Composite parent) {

		Composite timeComposite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, true);
		timeComposite.setLayout(gridLayout);

		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.grabExcessHorizontalSpace = true;
		gridData.minimumWidth = 100;

		Label startTimeLabel = new Label(timeComposite, SWT.NONE);
		startTimeLabel.setText("Start time: ");
		startTimeLabel.setEnabled(isLive());
		startTimeLabelTextField = new Label(timeComposite, SWT.NONE);
		startTimeLabelTextField.setLayoutData(gridData);
		startTimeLabelTextField.setEnabled(isLive());
		startTimeLabelTextField.setEnabled(isLive());
		if (!isLive()) {
			startTimeLabelTextField.setText("not available in recorded mode");
		}

		Label currentTimeLabel = new Label(timeComposite, SWT.NONE);
		currentTimeLabel.setText("Current time: ");
		currentTimeLabel.setEnabled(isLive());

		final Label currentTimeLabelTextField = new Label(timeComposite,
				SWT.NONE);
		currentTimeLabelTextField.setLayoutData(gridData);
		currentTimeLabelTextField.setEnabled(isLive());
		if (!isLive()) {
			currentTimeLabelTextField.setText("not available in recorded mode");
		}

		Label timeElapsedLabelText = new Label(timeComposite, SWT.NONE);
		timeElapsedLabelText.setText("Elapsed time: ");
		final Label timeElapsedLabel = new Label(timeComposite, SWT.NONE);
		timeElapsedLabel.setLayoutData(gridData);

		timer = new Timer(currentTimeLabelTextField, timeElapsedLabel,
				getControl().getShell(), isLive());
		timerThread = new Thread(timer);
	}

	public void setStartTime(String startTime) {
		if (isLive()) {
			startTimeLabelTextField.setText(startTime);
		}
	}

	public void startTimer() {
		timerThread.start();
	}

	public void stopTimer() {
		timer.stop();
	}

	public void pauseTimer() {
		timer.pause();
	}

	public void resumeTimer() {
		timer.resume();
	}

	public ISelection getSelection() {
		return tableViewer.getSelection();
	}

	public Sequence getInput() {
		return sequence;
	}

	public Control getControl() {
		return table.getParent();
	}

	public void setSelection(ISelection selection) {
		tableViewer.setSelection(selection, true);
	}

	/**
	 * Set the focus to the label to avoid to activate the start button of the
	 * editor when the user hits the [SPACE] key.
	 */
	public void setFocus() {
		this.startTimeLabelTextField.forceFocus();
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	private boolean isLive() {
		return getInput().isLive();
	}

}