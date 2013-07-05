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
package com.eclipsesource.tabris.xcallbackurl;

import static com.eclipsesource.tabris.internal.Clauses.when;
import static com.eclipsesource.tabris.internal.Clauses.whenNull;
import static com.eclipsesource.tabris.internal.Constants.EVENT_CANCEL;
import static com.eclipsesource.tabris.internal.Constants.EVENT_ERROR;
import static com.eclipsesource.tabris.internal.Constants.EVENT_SUCCESS;
import static com.eclipsesource.tabris.internal.Constants.METHOD_CALL;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ACTION_PARAMETERS;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ERROR_CODE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_ERROR_MESSAGE;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_PARAMETERS;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TARGET_ACTION;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_TARGET_SCHEME;
import static com.eclipsesource.tabris.internal.Constants.PROPERTY_XSOURCE;
import static com.eclipsesource.tabris.internal.Constants.TYPE_XCALLBACK;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.Adaptable;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;


/**
 * see http://x-callback-url.com/specifications/
 * @since 1.1
 */
public class XCallback implements Serializable, Adaptable {

  private final RemoteObject remoteObject;
  private final List<XCallbackListener> listeners;
  private final XCallbackConfiguration configuration;
  private boolean destroyed;

  public XCallback( XCallbackConfiguration configuration ) {
    whenNull( configuration ).throwIllegalArgument( "Configuration must not be null" );
    this.configuration = configuration;
    this.listeners = new ArrayList<XCallbackListener>();
    this.remoteObject = RWT.getUISession().getConnection().createRemoteObject( TYPE_XCALLBACK );
    remoteObject.setHandler( createOperationHandler() );
  }

  private AbstractOperationHandler createOperationHandler() {
    return new AbstractOperationHandler() {

      @Override
      public void handleNotify( String event, JsonObject properties ) {
        if( event.equals( EVENT_SUCCESS ) ) {
          dispatchOnSuccess( getParameter( properties ) );
        } else if( event.equals( EVENT_ERROR ) ) {
          dispatchOnError( properties.get( PROPERTY_ERROR_CODE ).asString(),
                           properties.get( PROPERTY_ERROR_MESSAGE ).asString() );
        } else if( event.equals( EVENT_CANCEL ) ) {
          dispatchOnCancel();
        }
      }
    };
  }

  private Map<String, String> getParameter( JsonObject properties ) {
    Map<String, String> parameter = new HashMap<String, String>();
    if( properties != null && properties.get( PROPERTY_PARAMETERS ) != null ) {
      JsonObject object = properties.get( PROPERTY_PARAMETERS ).asObject();
      List<String> names = object.names();
      for( String name : names ) {
        parameter.put( name, object.get( name ).asString() );
      }
    }
    return parameter;
  }

  private void dispatchOnSuccess( Map<String, String> parameter ) {
    for( XCallbackListener listener : listeners ) {
      listener.onSuccess( parameter );
    }
  }

  private void dispatchOnError( String errorCode, String errorMessage ) {
    for( XCallbackListener listener : listeners ) {
      listener.onError( errorCode, errorMessage );
    }
  }

  private void dispatchOnCancel() {
    for( XCallbackListener listener : listeners ) {
      listener.onCancel();
    }
  }

  public void addXCallbackListener( XCallbackListener listener ) {
    whenNull( listener ).throwIllegalArgument( "Listener must not be null" );
    listeners.add( listener );
  }

  public void removeXCallbackListener( XCallbackListener listener ) {
    whenNull( listener ).throwIllegalArgument( "Listener must not be null" );
    listeners.remove( listener );
  }

  public void call() {
    when( destroyed ).throwIllegalState( "XCallback already disposed" );
    remoteObject.call( METHOD_CALL, createParameters( configuration ) );
  }

  private JsonObject createParameters( XCallbackConfiguration configuration ) {
    JsonObject parameters = new JsonObject();
    parameters.add( PROPERTY_TARGET_SCHEME, configuration.getTargetScheme() );
    parameters.add( PROPERTY_TARGET_ACTION, configuration.getTargetAction() );
    addOptionalParameter( configuration, parameters );
    return parameters;
  }

  private void addOptionalParameter( XCallbackConfiguration configuration, JsonObject parameters ) {
    addXSource( configuration, parameters );
    addActionParameter( configuration, parameters );
  }

  private void addXSource( XCallbackConfiguration configuration, JsonObject parameters ) {
    String xSource = configuration.getXSource();
    if( xSource != null ) {
      parameters.add( PROPERTY_XSOURCE, xSource );
    }
  }

  private void addActionParameter( XCallbackConfiguration configuration, JsonObject parameters ) {
    Map<String, String> actionParameters = configuration.getActionParameters();
    if( actionParameters != null && !actionParameters.isEmpty() ) {
      JsonObject xActionParameter = new JsonObject();
      for( Entry<String, String> actionParameter : actionParameters.entrySet() ) {
        xActionParameter.add( actionParameter.getKey(), actionParameter.getValue() );
      }
      parameters.add( PROPERTY_ACTION_PARAMETERS, xActionParameter );
    }
  }

  public void dispose() {
    when( destroyed ).throwIllegalState( "XCallback already disposed" );
    remoteObject.destroy();
    destroyed = true;
  }

  RemoteObject getRemoteObject() {
    return remoteObject;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getAdapter( Class<T> adapter ) {
    if( adapter == RemoteObject.class ) {
      return ( T )remoteObject;
    } else if( adapter == XCallbackConfiguration.class ) {
      return ( T )configuration;
    }
    return null;
  }

}