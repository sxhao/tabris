/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.ui;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.Serializable;

import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.tabris.internal.ui.ActionDescriptor;
import com.eclipsesource.tabris.internal.ui.TestAction;
import com.eclipsesource.tabris.internal.ui.UITestUtil;
import com.eclipsesource.tabris.test.RWTRunner;


@RunWith( RWTRunner.class )
public class ActionConfigurationTest {

  @Before
  public void setUp() {
    new Display();
  }

  @Test
  public void testIsSerializable() {
    assertTrue( Serializable.class.isAssignableFrom( ActionConfiguration.class ) );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullId() {
    new ActionConfiguration( null, TestAction.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithEmptyId() {
    new ActionConfiguration( "", TestAction.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testFailsWithNullType() {
    new ActionConfiguration( "foo", null );
  }

  @Test
  public void testCanCreateDescriptor() {
    ActionConfiguration configuration = new ActionConfiguration( "foo", TestAction.class );

    ActionDescriptor descriptor = configuration.getAdapter( ActionDescriptor.class );

    assertNotNull( descriptor );
  }

  @Test
  public void testSetsDefaultAttributes() {
    ActionConfiguration configuration = new ActionConfiguration( "foo", TestAction.class );

    ActionDescriptor descriptor = configuration.getAdapter( ActionDescriptor.class );

    assertEquals( "foo", descriptor.getId() );
    assertTrue( descriptor.getAction() instanceof TestAction );
    assertNull( descriptor.getImage() );
    assertEquals( "", descriptor.getTitle() );
    assertTrue( descriptor.isEnabled() );
    assertTrue( descriptor.isVisible() );
  }

  @Test
  public void testSetsTitle() {
    ActionConfiguration configuration = new ActionConfiguration( "foo", TestAction.class ).setTitle( "bar" );

    ActionDescriptor descriptor = configuration.getAdapter( ActionDescriptor.class );

    assertEquals( "bar", descriptor.getTitle() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetTitleFailsWithNull() {
    new ActionConfiguration( "foo", TestAction.class ).setTitle( null );
  }

  @Test
  public void testSetsVisible() {
    ActionConfiguration configuration = new ActionConfiguration( "foo", TestAction.class ).setVisible( false );

    ActionDescriptor descriptor = configuration.getAdapter( ActionDescriptor.class );

    assertFalse( descriptor.isVisible() );
  }

  @Test
  public void testSetsEnabled() {
    ActionConfiguration configuration = new ActionConfiguration( "foo", TestAction.class ).setEnabled( false );

    ActionDescriptor descriptor = configuration.getAdapter( ActionDescriptor.class );

    assertFalse( descriptor.isEnabled() );
  }

  @Test
  public void testSetsImage() {
    InputStream image = UITestUtil.class.getResourceAsStream( "testImage.png" );
    ActionConfiguration configuration = new ActionConfiguration( "foo", TestAction.class ).setImage( image );

    ActionDescriptor descriptor = configuration.getAdapter( ActionDescriptor.class );

    assertArrayEquals( UITestUtil.getImageBytes(), descriptor.getImage() );
  }

  @Test
  public void testSetsPlacementPriority() {
    ActionConfiguration configuration = new ActionConfiguration( "foo", TestAction.class );

    configuration.setPlacementPriority( PlacementPriority.HIGH );

    ActionDescriptor descriptor = configuration.getAdapter( ActionDescriptor.class );
    assertSame( PlacementPriority.HIGH, descriptor.getPlacementPriority() );
  }

  @Test
  public void testSetPlacementPriorityReturnsConfiguration() {
    ActionConfiguration configuration = new ActionConfiguration( "foo", TestAction.class );

    ActionConfiguration actualConfiguration = configuration.setPlacementPriority( PlacementPriority.HIGH );

    assertSame( configuration, actualConfiguration );
  }

  @Test( expected = IllegalArgumentException.class )
  public void testSetImageFailsWithNull() {
    new ActionConfiguration( "foo", TestAction.class ).setImage( null );
  }

}
