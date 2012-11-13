/*
 * Created on 04.12.2004
 *
 */
package com.dmissoh.biologic.internal;

import com.dmissoh.biologic.Activator;

public class PluginService extends BasePluginService {

	static PluginService m_instance;

	private PluginService() {
		super(Activator.getDefault());
	}

	public static synchronized PluginService getInstance() {
		if (m_instance == null) {
			m_instance = new PluginService();
		}
		return m_instance;
	}
}
