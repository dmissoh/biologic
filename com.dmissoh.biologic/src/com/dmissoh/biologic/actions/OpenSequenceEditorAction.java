package com.dmissoh.biologic.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.dmissoh.biologic.ICommandIds;
import com.dmissoh.biologic.dialog.StartSequenceDialog;
import com.dmissoh.biologic.editor.SequenceEditor;
import com.dmissoh.biologic.editor.SequenceEditorInput;
import com.dmissoh.biologic.internal.PluginService;
import com.dmissoh.biologic.utils.ModelUtils;

public class OpenSequenceEditorAction extends Action {

	private final IWorkbenchWindow window;

	public OpenSequenceEditorAction(IWorkbenchWindow window, String label) {
		this.window = window;
		setText(label);
		// The id is used to refer to the action in a menu or tool bar
		setId(ICommandIds.CMD_OPEN);
		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_OPEN);
		setImageDescriptor(PluginService.getInstance().getImageDescriptor(
				"icons/sequence.png"));
	}

	private boolean hasConfig() {
		return (ModelUtils.getFamiliesFromPreferences().size() > 0);
	}

	public void run() {
		if (window != null) {
			if (hasConfig()) {
				try {
					StartSequenceDialog startSequenceDialog = new StartSequenceDialog(
							window.getShell(), "Sequence Description");

					if (startSequenceDialog.open() == Dialog.OK) {
						boolean isLive = startSequenceDialog.isLive();

						String description = startSequenceDialog
								.getDescription();

						long now = System.currentTimeMillis();
						SequenceEditorInput input = new SequenceEditorInput(
								false, isLive);
						input.getSequence().setDescription(description);
						input.getSequence().setStartTime(now);
						window.getActivePage().openEditor(input,
								SequenceEditor.class.getName());
					}

				} catch (PartInitException e) {
					MessageDialog.openError(window.getShell(), "Error",
							"Error opening view:" + e.getMessage());
				}
			} else {
				MessageDialog
						.openWarning(
								window.getShell(),
								"Missing Shortkeys Configuration",
								"No events have been configured.\nUse the preference page to add at least one configuration.");
			}
		}
	}
}
