/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.tabris.passepartout;


/**
 * <p>
 * A {@link QueryListener} can be attached to a {@link FluidGridLayout} to get notified when a specific
 * {@link Query} was activated or deactivated. This is often used to exchange UI elements when the screen size changes.
 * </p>
 *
 * @see FluidGridLayout
 *
 * @since 0.9
 */
public interface QueryListener {

  /**
   * <p>
   * Will be called when a {@link Query} becomes active.
   * </p>
   */
  void activated( Query query );

  /**
   * <p>
   * Will be called when a {@link Query} becomes deactive.
   * </p>
   */
  void deactivated( Query query );
}
