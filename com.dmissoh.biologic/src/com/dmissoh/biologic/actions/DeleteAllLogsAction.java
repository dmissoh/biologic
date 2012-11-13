package com.dmissoh.biologic.actions;

import java.io.File;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.dmissoh.biologic.utils.ExportUtils;
import com.dmissoh.biologic.view.BackUpLogView;

public class DeleteAllLogsAction implements IViewActionDelegate {

	@SuppressWarnings("unused")
	private IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		if (MessageDialog.openConfirm(shell, "Log files deletion",
				"Do you really want to delete all stored log files?")) {
			String logFolderPath = ExportUtils.getInstance().getLogFolderPath();
			File logsFolder = new File(logFolderPath);
			if (logsFolder.exists() && logsFolder.isDirectory()) {
				File[] logFiles = logsFolder.listFiles();
				for (File logFile : logFiles) {
					logFile.getName().endsWith(".log");
					logFile.delete();
				}
			}
			if (view instanceof BackUpLogView) {
				BackUpLogView backUpLogView = (BackUpLogView) view;
				backUpLogView.handleSave();
			}
		}

	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
