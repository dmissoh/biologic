package com.dmissoh.biologic.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.dmissoh.biologic.ICommandIds;
import com.dmissoh.biologic.editor.SequenceEditor;
import com.dmissoh.biologic.internal.PluginService;
import com.dmissoh.biologic.models.ISequence;

public class SaveAsAction extends Action {

	private final IWorkbenchWindow window;

	public SaveAsAction(String text, IWorkbenchWindow window) {
		super(text);
		this.window = window;
		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_OPEN_MESSAGE);
		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
		setImageDescriptor(PluginService.getInstance().getImageDescriptor(
				"icons/saveas.png"));
	}

	public void run() {
		IWorkbenchPart part = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		if (part instanceof SequenceEditor) {
			SequenceEditor editor = (SequenceEditor) part;
			Object inputObj = editor.getInputAsSequence();
			if (inputObj instanceof ISequence) {
				ISequence sequence = (ISequence) inputObj;
				if (sequence != null && sequence.getLogEntries() != null
						&& !sequence.getLogEntries().isEmpty()) {
					FileDialog dialog = new FileDialog(window.getShell(),
							SWT.SAVE);
					dialog.setFileName("backup.log");
					String[] extentions = new String[] { "*.log", "*.*" };
					dialog.setFilterExtensions(extentions);
					String selectedFile = dialog.open();
					if (selectedFile != null) {
						boolean ok = true;
						if ((new File(selectedFile)).exists()) {
							ok = MessageDialog
									.openQuestion(window.getShell(),
											"Overwrite",
											"The given file already exist, do you really want to overwrite it?");
						}
						if(ok){							
							editor.save(selectedFile);
						}
					}
				} else {
					MessageDialog.openInformation(window.getShell(),
							"Information", "There is nothing to save");
				}
			}

		} else {
			MessageDialog.openInformation(window.getShell(), "Information",
					"First activate the sequence view to save");
		}
	}
}