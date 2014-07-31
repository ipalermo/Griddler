/*
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.solutions.griddler.android.social;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.google.cloud.solutions.griddler.android.GameApplication;
import com.google.cloud.solutions.griddler.android.ui.RequestCodes;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.GriddlerScopes;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

/**
 * The purpose of this class is to encapsulate the Google+ Client {@link PlusClient}
 */
public class GooglePlusService extends Service
    implements ConnectionCallbacks, OnConnectionFailedListener {

  /**
   * Broadcast message action
   */
  public static final String GOOGLE_PLUS_SERVICE_BROADCAST_MESSAGE_ACTION =
      "googlePlusServiceBroadcastAction";

  /**
   * Defines the type of message being broadcast
   */
  public static final String GOOGLE_PLUS_SERVICE_MESSAGE_TYPE_EXTRA =
      "googlePlusServiceMessageTypeExtra";

  /**
   * Defines the extra for the {@link ConnectionResult} extra
   */
  public static final String GOOGLE_PLUS_CONNECTION_FAILED_RESOLUTION_EXTRA =
      "googlePlusConnectionFailedResolutionExtra";

  /**
   * The error code extra for the {@link ConnectionResult} extra
   */
  public static final String GOOGLE_PLUS_CONNECTION_FAILED_ERROR_CODE_EXTRA =
      "googlePlusConnectionFailedErrorCodeExtra";

  /**
   * Google+ connected message type
   */
  public static final int GOOGLE_PLUS_CONNECTED_MESSAGE = 1;

  /**
   * Google+ Person not public message type
   */
  public static final int GOOGLE_PLUS_PERSON_NOT_PUBLIC = 2;

  /**
   * Google+ Connection Failed message type
   */
  public static final int GOOGLE_PLUS_CONNECTION_FAILED = 3;

  private PlusClient plus = null;
  private PlusClient.Builder builder = null;
  private GameApplication application = null;

  private final IBinder binder = new LocalBinder();

  @Override
  public void onCreate() {
    application = (GameApplication) getApplication();

    builder = new PlusClient.Builder(getApplicationContext(), this, this);
    builder.setScopes(Scopes.PLUS_LOGIN, GriddlerScopes.USERINFO_EMAIL);
    builder.setAccountName(application.getSelectedAccountName());
    plus = builder.build();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return Service.START_STICKY;
  }

  @Override
  public void onDisconnected() {
    this.disconnect();
  }

  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  /**
   * Get the current Google+ person
   *
   * @return {@link Person}
   */
  public Person getCurrentPerson() {
    return plus.getCurrentPerson();
  }

  /**
   * Query Google+ for a list of the current person's friends
   *
   * @param listener The listener to be invoked when the query is completed
   */
  public void loadPeople(final GooglePlusPeopleLoadedListener listener) {

    if (!plus.isConnected()) {
      plus.connect();
      listener.onGooglePlusPeopleLoaded(null);
    } else {
      plus.loadVisiblePeople(new OnPeopleLoadedListener() {

        @Override
        public void onPeopleLoaded(
            ConnectionResult status, PersonBuffer personBuffer, String nextPageToken) {

          switch (status.getErrorCode()) {
            case ConnectionResult.SUCCESS:
              listener.onGooglePlusPeopleLoaded(personBuffer);
              break;
            case ConnectionResult.SIGN_IN_REQUIRED:
              try {
                if (status.hasResolution()) {
                  status.startResolutionForResult((Activity) getApplicationContext(),
                      RequestCodes.REQUEST_GOOGLE_PLUS_AUTHORIZATION);
                } else {
                  listener.onGooglePlusPeopleLoaded(null);
                  renewConnection();
                }
              } catch (SendIntentException e) {
                listener.onGooglePlusPeopleLoaded(null);
                renewConnection();
              }
              break;
            default:
              listener.onGooglePlusPeopleLoaded(null);
              break;
          }
        }
      },  null);/*Person.Collection.VISIBLE);*/
    }
  }

  /**
   * Connect to Google+
   */
  public void connect() {

    if (!plus.isConnected()) {
      plus.connect();
    } else {
      broadcastGooglePlusConnected();
    }
  }

  /**
   * Disconnect from Google+
   */
  public void disconnect() {
    if (plus != null) {
      plus.disconnect();
    }
  }

  /**
   * Renew the connection by disconnecting and connecting to Google+
   */
  public void renewConnection() {
    disconnect();
    connect();
  }

  /**
   * Renew the connection with a new account name by disconnecting and connecting to Google+
   *
   * @param accountName
   */
  public void renewConnection(String accountName) {
    disconnect();

    builder.setAccountName(accountName);
    plus = builder.build();

    connect();
  }

  @Override
  public void onConnectionFailed(ConnectionResult result) {
    broadcastGooglePlusConnectionFailed(result);
  }

  @Override
  public void onConnected(Bundle bundle) {

    if (getCurrentPerson() == null) {
      application.getApplicationSettings().setUsingGooglePlus(false);
      application.getApplicationSettings().setHasPlusIdBeenSet(false);
      disconnect();

      broadcastGooglePlusPersonNotPublic();
    } else {

      application.getApplicationSettings().setUsingGooglePlus(true);

      broadcastGooglePlusConnected();
    }
  }

  /**
   * Class for clients to access.
   */
  public class LocalBinder extends Binder {
    public GooglePlusService getService() {
      return GooglePlusService.this;
    }
  }

  private void broadcastGooglePlusConnected() {
    Intent intent = new Intent(GOOGLE_PLUS_SERVICE_BROADCAST_MESSAGE_ACTION);
    intent.putExtra(GOOGLE_PLUS_SERVICE_MESSAGE_TYPE_EXTRA, GOOGLE_PLUS_CONNECTED_MESSAGE);
    sendBroadcast(intent);
  }

  private void broadcastGooglePlusPersonNotPublic() {
    Intent intent = new Intent(GOOGLE_PLUS_SERVICE_BROADCAST_MESSAGE_ACTION);
    intent.putExtra(GOOGLE_PLUS_SERVICE_MESSAGE_TYPE_EXTRA, GOOGLE_PLUS_PERSON_NOT_PUBLIC);
    sendBroadcast(intent);
  }

  private void broadcastGooglePlusConnectionFailed(ConnectionResult result) {

    if (result.hasResolution()) {

      Intent intent = new Intent(GOOGLE_PLUS_SERVICE_BROADCAST_MESSAGE_ACTION);
      intent.putExtra(GOOGLE_PLUS_SERVICE_MESSAGE_TYPE_EXTRA, GOOGLE_PLUS_CONNECTION_FAILED);
      intent.putExtra(GOOGLE_PLUS_CONNECTION_FAILED_RESOLUTION_EXTRA, result.getResolution());
      intent.putExtra(GOOGLE_PLUS_CONNECTION_FAILED_ERROR_CODE_EXTRA, result.getErrorCode());
      sendBroadcast(intent);
    }
  }
}
