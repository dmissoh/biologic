/*
 * Copyright (c) 1999 - 2005 by empolis 
 * 
 * Created on 25.10.2006
 */
package com.dmissoh.biologic.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
  private static final String BUNDLE_NAME = "com.empolis.ecls.client.content.ui.preferences.application.messages"; //$NON-NLS-1$

  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {}

  //public static String ContentApplicationPreferencePage_Viewer;
}
