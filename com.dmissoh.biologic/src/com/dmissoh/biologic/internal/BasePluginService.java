package com.dmissoh.biologic.internal;

import java.util.Properties;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class BasePluginService {

	protected AbstractUIPlugin plugin;
	private Properties initProperties;

	protected BasePluginService(AbstractUIPlugin plugin) {
		this.plugin = plugin;
	}

	public void log(Throwable t) {
		ILog log = Platform.getLog(plugin.getBundle());
		Status status = new Status(Status.ERROR, plugin.getBundle()
				.getSymbolicName(), Status.ERROR, t.getLocalizedMessage(), t); //$NON-NLS-1$
		log.log(status);
	}

	public void handleException(String message) {
		handleException(message, null);
	}

	public void handleException(Throwable e) {
		handleException(null, e);
	}

	public void handleException(String message, Throwable e) {
		if (message == null) {
			if (e.getMessage() == null) {
				message = e.toString();
			} else {
				message = e.getMessage();
			}
		}
		ILog log = Platform.getLog(plugin.getBundle());
		Status status = new Status(Status.ERROR, plugin.getBundle()
				.getSymbolicName(), Status.ERROR, message, e);
		log.log(status);
		if (plugin.getWorkbench() != null
				&& plugin.getWorkbench().getActiveWorkbenchWindow() != null
				&& plugin.getWorkbench().getActiveWorkbenchWindow().getShell() != null) {
			Shell shell = plugin.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
			ErrorDialog.openError(shell, "Error", message, status); //$NON-NLS-1$
		}
	}

	public synchronized Image getImage(String file) {

		Image image = plugin.getImageRegistry().get(file);
		if (image == null) {
			image = getImageDescriptor(file).createImage();
			plugin.getImageRegistry().put(file, image);
		}
		return image;
	}

	public synchronized ImageDescriptor getImageDescriptor(String file) {

		ImageDescriptor id = null;
		try {
			id = ImageDescriptor.createFromURL(plugin.getBundle()
					.getEntry(file));
		} catch (Exception e) {
			id = ImageDescriptor.getMissingImageDescriptor();
		}
		return id;
	}

	public Properties getInitProperties() {
		return initProperties;
	}

	public AbstractUIPlugin getPlugin() {
		return plugin;
	}

	public void setHelp(IAction action, String contextId) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(action,
				getPlugin().getBundle().getSymbolicName() + "." + contextId);
	}

	public void setHelp(Control control, String contextId) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(control,
				getPlugin().getBundle().getSymbolicName() + "." + contextId);
	}

	public void setHelp(Menu menu, String contextId) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(menu,
				getPlugin().getBundle().getSymbolicName() + "." + contextId);
	}

	public void setHelp(MenuItem item, String contextId) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(item,
				getPlugin().getBundle().getSymbolicName() + "." + contextId);
	}
}
