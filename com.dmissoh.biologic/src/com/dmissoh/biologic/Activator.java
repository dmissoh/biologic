package com.dmissoh.biologic;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.dmissoh.biologic";

	// The shared instance
	private static Activator plugin;

	// The identifiers for the preferences
	public static final String P_TIME_UNIT_PREFERENCE = "TIME_UNIT_PREFERENCE";

	public static final String TIME_UNIT_SECONDS = "SECONDS";
	public static final String TIME_UNIT_MINUTES = "MINUTES";
	public static final String TIME_UNIT_HOURS = "HOURS";

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public String getTimeUnitPreference() {
		return getPreferenceStore().getString(P_TIME_UNIT_PREFERENCE);
	}

	public String getDefaultTimeUnitPreference() {
		return getPreferenceStore().getDefaultString(P_TIME_UNIT_PREFERENCE);
	}

	@Override
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		store.setDefault(P_TIME_UNIT_PREFERENCE, TIME_UNIT_SECONDS);
	}

}
