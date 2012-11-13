package com.dmissoh.biologic.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.dmissoh.biologic.view.BackUpLogView;

public class RefreshViewAction implements IViewActionDelegate {

	private IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		if (view instanceof BackUpLogView) {
			BackUpLogView backUpLogView = (BackUpLogView) view;
			backUpLogView.handleSave();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
	}

}
