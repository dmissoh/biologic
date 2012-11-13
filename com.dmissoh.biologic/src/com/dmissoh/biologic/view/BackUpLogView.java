package com.dmissoh.biologic.view;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.dmissoh.biologic.editor.SequenceEditor;
import com.dmissoh.biologic.editor.SequenceEditorInput;
import com.dmissoh.biologic.internal.PluginService;
import com.dmissoh.biologic.listeners.ISaveListener;
import com.dmissoh.biologic.models.Sequence;
import com.dmissoh.biologic.models.SequenceStoreObject;
import com.dmissoh.biologic.utils.ExportUtils;
import com.dmissoh.biologic.utils.TimeUtils;

public class BackUpLogView extends ViewPart implements ISaveListener {

	public static final String ID = BackUpLogView.class.getName();
	private TableViewer viewer;
	private IPartListener partListener;

	public BackUpLogView() {
		ExportUtils.getInstance().addSaveListener(this);
		partListener = new IPartListener() {

			public void partActivated(IWorkbenchPart part) {
				synchronizeSelection(part);
			}

			public void partBroughtToTop(IWorkbenchPart part) {
				// TODO Auto-generated method stub
			}

			public void partClosed(IWorkbenchPart part) {
				// TODO Auto-generated method stub
			}

			public void partDeactivated(IWorkbenchPart part) {
				// TODO Auto-generated method stub
			}

			public void partOpened(IWorkbenchPart part) {
				synchronizeSelection(part);
			}
		};
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.addPartListener(partListener);
	}

	private void synchronizeSelection(IWorkbenchPart part) {
		if (part instanceof SequenceEditor) {
			SequenceEditor editor = (SequenceEditor) part;
			Sequence sequence = editor.getInputAsSequence();
			long startTime = sequence.getStartTime();
			String logName = "log_" + startTime + ".log";
			viewer.setSelection(new StructuredSelection(logName));
		}
	}

	class ViewContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof String[]) {
				return (Object[]) inputElement;
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
		ExportUtils.getInstance().removeSaveListener(this);
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			page.removePartListener(partListener);
		}
		super.dispose();
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return PluginService.getInstance().getImage("icons/backuplog.gif");
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof String) {
				String logName = element.toString();

				Sequence sequence = restoreSequenceFromLog(getLogPathForName(logName));

				if(sequence != null){
					return sequence.getDescription();
				}

				int indexOfPrefix = logName.indexOf("log_") + 4;
				int indexOfSuffix = logName.indexOf(".log");
				String timeStr = logName
						.substring(indexOfPrefix, indexOfSuffix);
				long time = Long.valueOf(timeStr);
				return TimeUtils.formatToDateTime(time);
			}
			return "";
		}
	}

	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getInput());

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				String logFolderPath = ExportUtils.getInstance()
						.getLogFolderPath();
				String log = ((IStructuredSelection) event.getSelection())
						.getFirstElement().toString();
				String logPath = logFolderPath + File.separator + log;
				try {
					Sequence sequence = restoreSequenceFromLog(logPath);
					SequenceEditorInput input = new SequenceEditorInput(true,
							sequence.isLive());
					input.setSequence(sequence);
					PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().openEditor(input,
									SequenceEditor.class.getName());
				} catch (PartInitException e) {
					PluginService.getInstance().handleException(e);
				}
			}

		});
	}

	private String getLogPathForName(String logName) {
		String logFolderPath = ExportUtils.getInstance().getLogFolderPath();
		return logFolderPath + File.separator + logName;
	}

	private Sequence restoreSequenceFromLog(String logPath) {
		File logFile = new File(logPath);
		Sequence sequence = null;
		if (logFile.exists()) {
			SequenceStoreObject sto = ExportUtils.getInstance()
					.restore(logPath);
			sequence = ExportUtils.getInstance().convertStoredToBusiness(sto);
		}
		return sequence;
	}

	private String[] getInput() {
		String logFolderPath = ExportUtils.getInstance().getLogFolderPath();
		File logFolder = new File(logFolderPath);
		if (logFolder.exists()) {
			String[] logs = logFolder.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.indexOf(".log") > -1) {
						return true;
					}
					return false;
				}
			});
			List<String> list = Arrays.asList(logs);
			Collections.reverse(list);
			return list.toArray(new String[list.size()]);
		}
		return null;
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void handleSave() {
		viewer.setInput(getInput());
	}
}