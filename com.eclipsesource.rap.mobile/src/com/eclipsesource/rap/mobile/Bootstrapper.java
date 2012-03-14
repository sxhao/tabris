/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.rap.mobile;

import org.eclipse.rwt.application.ApplicationConfiguration;
import org.eclipse.rwt.application.ApplicationConfigurator;
import org.eclipse.rwt.internal.application.ApplicationConfigurationImpl;

import com.eclipsesource.rap.mobile.internal.bootstrap.ConfigurationWrapper;
import com.eclipsesource.rap.mobile.internal.bootstrap.ProxyApplicationConfigurator;
import com.eclipsesource.rap.mobile.internal.bootstrap.ThemePhaseListener;


@SuppressWarnings("restriction")
public class Bootstrapper {
  
  public static final String THEME_ID_IOS = "com.eclipsesource.rap.mobile.theme.ios";
  public static final String THEME_ID_ANDROID = "com.eclipsesource.rap.mobile.theme.android";
  private static final String THEME_PATH_IOS = "theme/ios.css";
  private static final String THEME_PATH_ANDROID = "theme/theme-android-holo.css";
  
  public static void bootstrap( ApplicationConfiguration configuration ) {
    ApplicationConfigurationImpl config = chooseConfiguration( configuration );
    config.addPhaseListener( new ThemePhaseListener() );
    registerMobileThemes( config );
  }
  
  private static ApplicationConfigurationImpl chooseConfiguration( ApplicationConfiguration configuration ) {
    ApplicationConfigurationImpl config = ( ApplicationConfigurationImpl )configuration;
    ApplicationConfigurator configurator = config.getAdapter( ApplicationConfigurator.class );
    if( !( configurator instanceof ProxyApplicationConfigurator ) ) {
      ProxyApplicationConfigurator proxy = new ProxyApplicationConfigurator( configurator );
      config = new ConfigurationWrapper( config, proxy );
    }
    return config;
  }
  
  private static void registerMobileThemes( ApplicationConfiguration configuration ) {
    configuration.addStyleSheet( THEME_ID_ANDROID, THEME_PATH_ANDROID );
    configuration.addStyleSheet( THEME_ID_IOS, THEME_PATH_IOS );
  }

  private Bootstrapper() {
    // prevent instantiation
  }
}
