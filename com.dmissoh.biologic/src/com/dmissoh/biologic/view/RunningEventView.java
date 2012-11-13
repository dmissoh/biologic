package com.dmissoh.biologic.view;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.dmissoh.biologic.editor.SequenceEditor;
import com.dmissoh.biologic.internal.PluginService;
import com.dmissoh.biologic.listeners.ILogEventListener;
import com.dmissoh.biologic.models.RunningEvent;

public class RunningEventView extends ViewPart implements ILogEventListener {

	public static final String ID = "BioLogic.navigationView";

	private Table table;
	private TableViewer viewer;
	private IPartListener partListener;

	public RunningEventView() {
		partListener = new IPartListener() {

			public void partActivated(IWorkbenchPart part) {
				if (part instanceof SequenceEditor) {
					SequenceEditor editor = (SequenceEditor) part;
					editor.addLogEntryListener(RunningEventView.this);
					viewer.setInput(editor.getRunningEvent());
				}
			}

			public void partBroughtToTop(IWorkbenchPart part) {
				// TODO Auto-generated method stub
			}

			public void partClosed(IWorkbenchPart part) {
				// if (part instanceof SequenceView) {
				// SequenceView view = (SequenceView) part;
				// view.removeLogEntryListener(RunningEventView.this);
				// }
			}

			public void partDeactivated(IWorkbenchPart part) {
				// if (part instanceof SequenceView) {
				// SequenceView view = (SequenceView) part;
				// view.removeLogEntryListener(RunningEventView.this);
				// }
			}

			public void partOpened(IWorkbenchPart part) {
				// if (part instanceof SequenceView) {
				// SequenceView view = (SequenceView) part;
				// view.addLogEntryListener(RunningEventView.this);
				// viewer.setInput(view.getRunningEvent());
				// }
			}
		};
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.addPartListener(partListener);
	}

	class RunningEventContentProvider implements IStructuredContentProvider {

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				List<RunningEvent> logEntries = (List<RunningEvent>) inputElement;
				return (logEntries.toArray(new RunningEvent[logEntries.size()]));
			}
			return new Object[0];
		}

		public void dispose() {
			// TODO Auto-generated method stub
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
		}

	}

	@Override
	public void dispose() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			page.removePartListener(partListener);
		}
		super.dispose();
	}

	class RunningEventLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			Image image = null;
			switch (columnIndex) {
			case 0:
				image = PluginService.getInstance().getImage(
						"icons/log_start.gif");
				break;
			default:
				break;
			}
			return image;
		}

		public String getColumnText(Object element, int columnIndex) {
			String text = "";
			if (element instanceof RunningEvent) {
				RunningEvent runningEvent = (RunningEvent) element;
				switch (columnIndex) {
				case 0:
					text = runningEvent.getEntry().getName();
					break;
				case 1:
					text = String.valueOf(runningEvent.getStartKey())
							.toUpperCase();
					break;
				case 2:
					text = String.valueOf(runningEvent.getEndKey())
							.toUpperCase();
					break;
				default:
					break;
				}
			}
			return text;
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		createTable(parent);

		viewer = new TableViewer(table);
		viewer.setUseHashlookup(true);

		viewer.setContentProvider(new RunningEventContentProvider());
		viewer.setLabelProvider(new RunningEventLabelProvider());
	}

	private void createTable(Composite parent) {
		int style = SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		table = new Table(parent, style);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn column = new TableColumn(table, SWT.CENTER, 0);
		column.setText("Event");
		column.setWidth(150);

		// 2nd column with task Description
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Start");

		column.setWidth(40);

		column = new TableColumn(table, SWT.LEFT, 2);
		column.setText("End");
		column.setWidth(40);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void handleRunningEventsChanges(List<RunningEvent> runningEntries) {
		viewer.setInput(runningEntries);
	}
}