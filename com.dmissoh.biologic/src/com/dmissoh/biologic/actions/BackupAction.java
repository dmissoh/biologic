package com.dmissoh.biologic.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import com.dmissoh.biologic.utils.ExportUtils;
import com.dmissoh.biologic.utils.TimeUtils;

public class BackupAction implements IWorkbenchWindowActionDelegate {

	public static void zipFolder(String folderToZip, String outputFile) {

		try {

			// create ZipOutputStream object
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					outputFile));

			// path to the folder to be zipped
			File zipFolder = new File(folderToZip);

			// get path prefix so that the zip file does not contain the whole
			// path
			int len = zipFolder.getAbsolutePath().lastIndexOf(File.separator);
			String baseName = zipFolder.getAbsolutePath().substring(0, len + 1);

			addFolderToZip(zipFolder, out, baseName);

			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addFolderToZip(File folder, ZipOutputStream zip,
			String baseName) throws IOException {
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				addFolderToZip(file, zip, baseName);
			} else {
				String name = file.getAbsolutePath().substring(
						baseName.length());
				ZipEntry zipEntry = new ZipEntry(name);
				zip.putNextEntry(zipEntry);
				IOUtils.copy(new FileInputStream(file), zip);
				zip.closeEntry();
			}
		}
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {
		IPath path = ExportUtils.getInstance().getBundleLocation();
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		String dateSuffix = TimeUtils.formatToDateTime(System
				.currentTimeMillis());
		dialog.setFileName("backup_" + dateSuffix + ".zip");
		String[] extentions = new String[] { "*.zip" };
		dialog.setFilterExtensions(extentions);
		String selectedFile = dialog.open();
		if (selectedFile != null) {
			boolean ok = true;
			if ((new File(selectedFile)).exists()) {
				ok = MessageDialog
						.openQuestion(shell, "Overwrite",
								"The given file already exist, do you really want to overwrite it?");
			}
			if (ok) {
				zipFolder(path.toFile().getAbsolutePath(), selectedFile);
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
	}
}
