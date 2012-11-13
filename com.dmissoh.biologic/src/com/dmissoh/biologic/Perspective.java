package com.dmissoh.biologic;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.dmissoh.biologic.view.BackUpLogView;
import com.dmissoh.biologic.view.RunningEventView;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		IFolderLayout folderNavigator = layout.createFolder("navigator",
				IPageLayout.LEFT, 0.3f, editorArea);
		folderNavigator.addPlaceholder(RunningEventView.ID + ":*");
		folderNavigator.addView(RunningEventView.ID);
		folderNavigator.addView(BackUpLogView.ID);

		layout.getViewLayout(RunningEventView.ID).setCloseable(false);
		layout.getViewLayout(BackUpLogView.ID).setCloseable(false);
	}
}
