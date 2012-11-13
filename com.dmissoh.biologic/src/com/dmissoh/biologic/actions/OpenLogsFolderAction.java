package com.dmissoh.biologic.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.dmissoh.biologic.utils.ExportUtils;

public class OpenLogsFolderAction implements IViewActionDelegate {

	@SuppressWarnings("unused")
	private IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		openFolder();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
	}

	/**
	 * Opens the folder that contains the log files.
	 * This works only on window platforms.
	 */
	public void openFolder() {
		try {
			String logFolderPath = ExportUtils.getInstance().getLogFolderPath();
			Runtime.getRuntime().exec(
					"rundll32 SHELL32.DLL,ShellExec_RunDLL \"" + logFolderPath
							+ "\"");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
