/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.eclipse.rap.rwt.client.service.BrowserNavigation;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.tabris.test.RWTRunner;


@RunWith( RWTRunner.class )
public class TabrisSWTClientTest {

  @Test
  public void testHasBrowserNavigationService() {
    TabrisSWTClient client = new TabrisSWTClient();

    BrowserNavigation service = client.getService( BrowserNavigation.class );

    assertNotNull( service );
  }

  @Test
  public void testBrowserNavigationServiceIsSessionSingleton() {
    TabrisSWTClient client = new TabrisSWTClient();

    BrowserNavigation service = client.getService( BrowserNavigation.class );
    BrowserNavigation service2 = client.getService( BrowserNavigation.class );

    assertSame( service, service2 );
  }

  @Test
  public void testHasTableItemHeightService() {
    TabrisSWTClient client = new TabrisSWTClient();

    TableItemHeightService service = client.getService( TableItemHeightService.class );

    assertNotNull( service );
  }

  @Test
  public void testTableItemHeightServiceIsSessionSingleton() {
    TabrisSWTClient client = new TabrisSWTClient();

    TableItemHeightService service = client.getService( TableItemHeightService.class );
    TableItemHeightService service2 = client.getService( TableItemHeightService.class );

    assertSame( service, service2 );
  }
}
