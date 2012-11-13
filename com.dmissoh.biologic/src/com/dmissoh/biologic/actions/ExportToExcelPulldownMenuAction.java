package com.dmissoh.biologic.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;
import org.eclipse.ui.PlatformUI;

import com.dmissoh.biologic.editor.SequenceEditor;
import com.dmissoh.biologic.internal.PluginService;
import com.dmissoh.biologic.utils.ExportUtils;

/**
 * @author misso01
 *
 */
public class ExportToExcelPulldownMenuAction implements
		IWorkbenchWindowPulldownDelegate {

	private Menu exportToExcelPulldownMenu;

	public Menu getMenu(Control parent) {
		if (exportToExcelPulldownMenu == null) {
			// Build the menu
			exportToExcelPulldownMenu = createExportMenu(parent,
					exportToExcelPulldownMenu);
		}
		// Determine active perspective id
		IWorkbench workbench = PlatformUI.getWorkbench();
		IPerspectiveDescriptor perspective = workbench
				.getActiveWorkbenchWindow().getActivePage().getPerspective();
		String id = null;
		if (perspective != null) {
			id = perspective.getId();
		}
		MenuItem[] items = exportToExcelPulldownMenu.getItems();
		for (MenuItem item : items) {
			if (id == null) {
				// No perspective is active
				item.setEnabled(true);
			} else {
				// Check and disable the menuItem for the active perspective
				boolean equals = item.getData().equals(id);
				item.setEnabled(!equals);
			}
		}
		return exportToExcelPulldownMenu;
	}

	private Menu createExportMenu(Control parent, Menu menu) {
		if (menu == null) {
			menu = new Menu(parent);

			final MenuItem exportLogEntriesMenuItem = new MenuItem(menu,
					SWT.PUSH);
			exportLogEntriesMenuItem.setText("Export log entries");
			exportLogEntriesMenuItem.setImage(PluginService.getInstance()
					.getImage("icons/export_xsl.png"));
			exportLogEntriesMenuItem.setData("exportLogEntriesMenuItem");

			// Handle selection
			exportLogEntriesMenuItem
					.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							exportLogEntries();
						}
					});

			final MenuItem exportGroupedEventMenuItem = new MenuItem(menu,
					SWT.PUSH);
			exportGroupedEventMenuItem.setText("Export grouped events");
			exportGroupedEventMenuItem.setImage(PluginService.getInstance()
					.getImage("icons/export_xsl.png"));
			exportGroupedEventMenuItem.setData("exportGroupedEventMenuItem");

			// Handle selection
			exportGroupedEventMenuItem
					.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							exportGroupedEvents();
						}
					});

		} else {
			// Delete children
		}

		return menu;
	}

	private void exportLogEntries() {
		IWorkbenchPart part = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		if (part instanceof SequenceEditor) {
			SequenceEditor editor = (SequenceEditor) part;
			ExportUtils.getInstance().exportLogEntries(
					editor.getInputAsSequence());
		}
	}

	private void exportGroupedEvents() {
		IWorkbenchPart part = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActivePart();
		if (part instanceof SequenceEditor) {
			SequenceEditor editor = (SequenceEditor) part;
			ExportUtils.getInstance().exportGroupedEvents(
					editor.getInputAsSequence(), editor.getFamilies());
		}
	}

	public void dispose() {
		if (exportToExcelPulldownMenu != null) {
			exportToExcelPulldownMenu.dispose();
		}
	}

	public void init(IWorkbenchWindow window) {
	}

	public void run(IAction action) {
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
