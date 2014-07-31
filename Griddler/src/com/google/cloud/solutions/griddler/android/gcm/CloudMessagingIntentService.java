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
package com.google.cloud.solutions.griddler.android.gcm;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.google.cloud.solutions.griddler.android.ApplicationSettings;
import com.google.cloud.solutions.griddler.android.GameApplication;
import com.google.cloud.solutions.griddler.android.GameBackendSettings;
import com.google.cloud.solutions.griddler.android.providers.DataProvider;
import com.google.cloud.solutions.griddler.android.providers.OnRegisterDeviceCompleted;
import com.google.cloud.solutions.griddler.android.providers.OnUnregisterDeviceCompleted;
import com.google.cloud.solutions.griddler.android.ui.ApplicationDialogs;
import com.google.cloud.solutions.griddler.android.ui.ApplicationNotifications;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * The purpose of this class is to act as a GCM Intent Service. It is used for the registration and
 * unregistration of devices for push notifications.
 */
public class CloudMessagingIntentService extends GCMBaseIntentService
    implements OnRegisterDeviceCompleted, OnUnregisterDeviceCompleted {

  private ApplicationSettings applicationSettings = null;
  private DataProvider dataProvider = null;
  private static final String LOG_TAG = CloudMessagingIntentService.class.getSimpleName();

  /**
   * Constructor
   */
  public CloudMessagingIntentService() {
    super(GameBackendSettings.GCM_SENDER_ID);
  }

  /**
   * Initialize the data provider {@link DataProvider}
   */
  @Override
  public void onCreate() {
    dataProvider = new DataProvider(this);
    applicationSettings = new ApplicationSettings(this);
    super.onCreate();
  }

  /**
   * Occurs when a registration ID has been generated for the device
   */
  @Override
  protected void onRegistered(Context context, String regId) {
    try {
      Log.d(LOG_TAG, "onRegistered");
      dataProvider.registerDevice(regId, this);
    } catch (Exception e) {
      Log.e(LOG_TAG, "onRegistered", e);
      e.printStackTrace();
    }
  }

  /**
   * Occurs when there is an error with GCM
   */
  @Override
  protected void onError(Context context, String errorMessage) {

    /**
     * The display of the message below is only in place for development purposes.
     */
    String fullMessage = String.format(
        getString(com.google.cloud.solutions.griddler.android.R.string.gcmError), errorMessage);
    ApplicationDialogs.displayAlert(context, fullMessage);

    Log.e(LOG_TAG, "onError " + fullMessage);
    return;
  }

  /**
   * Occurs when the device has received a message
   */
  @Override
  protected void onMessage(Context context, Intent intent) {
    Log.d(LOG_TAG, "onMessage");
    String messageText = intent.getStringExtra("messageText");
    Long invitationId = Long.valueOf(intent.getStringExtra("invitationId"));
    Long gameId = Long.valueOf(intent.getStringExtra("gameId"));
    Long playerId = Long.valueOf(intent.getStringExtra("playerId"));
    String nickName = intent.getStringExtra("nickName");

    // Get an instance of the application to determine if the application is
    // running in the foreground and if a game is currently being played
    GameApplication app = (GameApplication) getApplication();

    boolean currentPlayerIsInvitee = playerId.compareTo(applicationSettings.getPlayerId()) == 0;

    // ensure that the currently logged in player is the player
    // that is intended to receive the invitation or that the application is in the background.
    if (!app.getIsCurrentlyPlayingGame()
        && (currentPlayerIsInvitee || !app.getIsApplicationActive())) {
      ApplicationNotifications.generateInvitationNotification(context,
          messageText,
          gameId,
          invitationId,
          playerId,
          nickName);
    }
  }

  /**
   * Occurs when the device is to be unregistered
   */
  @Override
  protected void onUnregistered(Context arg0, String arg1) {
    try {
      Log.d(LOG_TAG, "onRegistered");
      dataProvider.unRegisterDevice(arg1, this);
    } catch (RuntimeException e) {
      Log.e(LOG_TAG, "onRegistered", e);
      e.printStackTrace();
    }
  }

  /**
   * Occurs when the asynchronous call to register the device has completed
   */
  @Override
  public void onRegisterDeviceCompleted(Boolean result) {

    try {
      if (result != null && result) {
        GCMRegistrar.setRegisteredOnServer(this, true);
      } else {
        GCMRegistrar.setRegisteredOnServer(this, false);
      }
    } catch (RuntimeException e) {
      Log.e(LOG_TAG, "onRegisterDeviceCompleted", e);
      e.printStackTrace();
    }
  }

  /**
   * Occurs when the asynchronous call to unregister the device has completed
   */
  @Override
  public void onUnregisterDeviceCompleted(Boolean result) {

    try {
      if (result != null && result) {
        GCMRegistrar.setRegisteredOnServer(this, false);
      }
    } catch (RuntimeException e) {
      Log.e(LOG_TAG, "onUnregisterDeviceCompleted", e);
      e.printStackTrace();
    }
  }
}
