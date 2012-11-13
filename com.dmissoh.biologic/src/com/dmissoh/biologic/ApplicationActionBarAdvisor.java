package com.dmissoh.biologic;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.dmissoh.biologic.actions.OpenSequenceEditorAction;
import com.dmissoh.biologic.actions.SaveAsAction;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private final IWorkbenchWindow window;

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.
	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction newWindowAction;
	private OpenSequenceEditorAction openSequenceViewAction;
	private Action saveAsAction;

	// private IWorkbenchAction nextEditorAction;
	// private IWorkbenchAction prevEditorAction;

	private IWorkbenchAction openPreferencesAction;
	private IWorkbenchAction showViewMenuAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
		window = configurer.getWindowConfigurer().getWindow();
	}

	private MenuManager createWindowMenu() {
		MenuManager menu = new MenuManager("Window",
				IWorkbenchActionConstants.M_WINDOW);

		menu.add(openPreferencesAction);

		menu.add(ContributionItemFactory.OPEN_WINDOWS.create(getWindow()));

		addPerspectiveActions(menu);

		return menu;
	}

	private void addPerspectiveActions(MenuManager menu) {
		{
			String openText = "Open perspective";
			MenuManager changePerspMenuMgr = new MenuManager(openText,
					"openPerspective"); //$NON-NLS-1$
			IContributionItem changePerspMenuItem = ContributionItemFactory.PERSPECTIVES_SHORTLIST
					.create(getWindow());
			changePerspMenuMgr.add(changePerspMenuItem);
			menu.add(changePerspMenuMgr);
		}
		{
			MenuManager showViewMenuMgr = new MenuManager(
					"Show View", "showView"); //$NON-NLS-1$
			IContributionItem showViewMenu = ContributionItemFactory.VIEWS_SHORTLIST
					.create(getWindow());
			showViewMenuMgr.add(showViewMenu);
			menu.add(showViewMenuMgr);
		}
		menu.add(new Separator());
		// menu.add(editActionSetAction);
		// menu.add(savePerspectiveAction);
		// menu.add(resetPerspectiveAction);
		// menu.add(closePerspAction);
		// menu.add(closeAllPerspsAction);
	}

	protected void makeActions(final IWorkbenchWindow window) {
		// Creates the actions and registers them.
		// Registering is needed to ensure that key bindings work.
		// The corresponding commands keybindings are defined in the plugin.xml
		// file.
		// Registering also provides automatic disposal of the actions when
		// the window is closed.

		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);

		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);

		newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
		register(newWindowAction);

		openSequenceViewAction = new OpenSequenceEditorAction(window,
				"New Sequence View");
		register(openSequenceViewAction);

		saveAsAction = new SaveAsAction("Save As...", window);
		register(saveAsAction);

		openPreferencesAction = ActionFactory.PREFERENCES.create(window);
		register(openPreferencesAction);

		showViewMenuAction = ActionFactory.SHOW_VIEW_MENU.create(window);
		register(showViewMenuAction);

	}

	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&File",
				IWorkbenchActionConstants.M_FILE);
		MenuManager helpMenu = new MenuManager("&Help",
				IWorkbenchActionConstants.M_HELP);

		menuBar.add(fileMenu);
		// Add a group marker indicating where action set menus will appear.
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(createWindowMenu());
		menuBar.add(helpMenu);

		// File
		fileMenu.add(newWindowAction);
		fileMenu.add(new Separator());
		fileMenu.add(saveAsAction);
		fileMenu.add(openSequenceViewAction);
		fileMenu.add(new Separator());
		fileMenu.add(exitAction);
		// fileMenu.add(nextEditorAction);
		// fileMenu.add(prevEditorAction);

		helpMenu.add(aboutAction);
	}

	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBar.add(new ToolBarContributionItem(toolbar, "main"));
		toolbar.add(openSequenceViewAction);
		toolbar.add(saveAsAction);
	}

	private IWorkbenchWindow getWindow() {
		return window;
	}
}
