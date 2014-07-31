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
package com.google.cloud.solutions.griddler.android.ui;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.plus.model.people.Person;
import com.google.cloud.solutions.griddler.android.GameApplication;
import com.google.cloud.solutions.griddler.android.GameBackendSettings;
import com.google.cloud.solutions.griddler.android.R;
import com.google.cloud.solutions.griddler.android.providers.OnAssignGooglePlusIdCompleted;
import com.google.cloud.solutions.griddler.android.social.GooglePlusListener;
import com.google.cloud.solutions.griddler.android.social.GooglePlusPeopleLoadedListener;
import com.google.cloud.solutions.griddler.android.social.GooglePlusService;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * The purpose of this fragment is to encapsulate Google+ functionality as well as manage GCM
 * registration
 *
 */
public class GoogleServicesFragment extends Fragment implements OnAssignGooglePlusIdCompleted {

  private static final String TAG = GoogleServicesFragment.class.getSimpleName();

  private final GooglePlusBroadcastReceiver handleGooglePlusMessageReceiver =
      new GooglePlusBroadcastReceiver();

  /**
   * Name of fragment
   */
  public static final String FRAGMENT_NAME = GoogleServicesFragment.class.getName();

  /**
   * Bundle field to notifiy the fragment to connect to google+
   */
  public static final String FORCE_CONNECT_TO_GOOGLE_PLUS = "forceConnectToGooglePlus";

  private GooglePlusService googlePlusService = null;
  private GooglePlusListener listener = null;
  private boolean isServiceBound = false;
  private GameApplication application = null;
  private Activity activity = null;
  private ServiceConnection serviceConnection = null;
  private boolean forceConnectToGooglePlus = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    activity = getActivity();
    application = (GameApplication) activity.getApplication();

    Bundle arguments = getArguments();
    if (arguments != null && arguments.containsKey(FORCE_CONNECT_TO_GOOGLE_PLUS)) {
      forceConnectToGooglePlus = arguments.getBoolean(FORCE_CONNECT_TO_GOOGLE_PLUS);
    }

    /*
     * If the activity hosting this fragment implements the GooglePlusListener interface then set
     * the listener value.
     */
    if (GooglePlusListener.class.isInstance(activity)) {
      listener = (GooglePlusListener) activity;
    }

    /*
     * Create the new service connection
     */
    serviceConnection = new ServiceConnection() {

      @Override
      public void onServiceConnected(ComponentName className, IBinder service) {
        googlePlusService = ((GooglePlusService.LocalBinder) service).getService();

        /*
         * Connect to Google+ if the user is using the service
         */
        if (forceConnectToGooglePlus || application.getApplicationSettings().getUsingGooglePlus()) {
          googlePlusService.connect();
        }
      }

      @Override
      public void onServiceDisconnected(ComponentName className) {
        googlePlusService = null;
      }
    };

    doBindService();
  }

  private void doBindService() {

    activity.bindService(
        new Intent(activity, GooglePlusService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    isServiceBound = true;

  }

  private void doUnbindService() {
    if (isServiceBound) {
      activity.unbindService(serviceConnection);
      isServiceBound = false;
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    doUnbindService();
    GCMRegistrar.onDestroy(activity.getApplicationContext());
  }

  @Override
  public void onStart() {
    registerReceiver();
    super.onStart();
  }

  @Override
  public void onStop() {
    unRegisterReceiver();
    super.onStop();
  }

  /**
   * Register receiver to listen for messages from Google+ Service
   */
  private void registerReceiver() {
    activity.registerReceiver(handleGooglePlusMessageReceiver,
        new IntentFilter(GooglePlusService.GOOGLE_PLUS_SERVICE_BROADCAST_MESSAGE_ACTION));
  }

  /**
   * Unregister receiver for Google+ messages
   */
  private void unRegisterReceiver() {
    activity.unregisterReceiver(handleGooglePlusMessageReceiver);
  }

  /**
   * Occurs when a Google+ connection failed. More than likely due to the person not being a Google+
   * user or they haven't granted the application privileges.
   */
  private void onGooglePlusConnectionFailed(int statusCode, PendingIntent intent) {

    ConnectionResult result = new ConnectionResult(statusCode, intent);

    if (result.hasResolution()) {
      try {
        result.startResolutionForResult(activity, RequestCodes.REQUEST_GOOGLE_PLUS_AUTHORIZATION);
      } catch (SendIntentException e) {
        googlePlusService.connect();
      }
    }
  }

  /**
   * Occurs when a connection to Google+ succeeds, but the profile isn't available
   */
  private void onGooglePlusPersonNotPublic() {
    initializeGCM();
    ApplicationDialogs.displayAlert(
        activity, this.getString(R.string.googlePlusProfileNotAvailable));
  }

  /**
   * Occurs when the connection to Google+ succeeds
   */
  private void onGooglePlusConnected() {

    if (!application.getApplicationSettings().getHasPlusIdBeenSet()) {
      application.getDataProvider()
          .assignPlusId(googlePlusService.getCurrentPerson().getId(), this);
    }

    if (listener != null) {
      listener.onGooglePlusConnected();
    }
  }

  /**
   * Occurs when the Google+ Id has been assigned
   */
  @Override
  public void onAssignGooglePlusId(Boolean result) {
    initializeGCM();
  }

  /**
   * Load people from your circles
   *
   * @param listener
   */
  public void loadPeople(GooglePlusPeopleLoadedListener listener) {
    googlePlusService.loadPeople(listener);
  }

  /**
   * Get the current Google+ person
   */
  public Person getCurrentPerson() {
    return googlePlusService.getCurrentPerson();
  }

  /**
   * Renew the Google+ connection
   */
  public void renewConnection() {
    googlePlusService.renewConnection();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case RequestCodes.REQUEST_GOOGLE_PLUS_AUTHORIZATION:

        if (resultCode == Activity.RESULT_CANCELED) {
          application.getApplicationSettings().setUsingGooglePlus(false);
          application.getApplicationSettings().setHasPlusIdBeenSet(false);
          setGCMRegistration(false);
          initializeGCM();
        } else {
          application.getApplicationSettings().setUsingGooglePlus(true);
          application.getApplicationSettings().setHasPlusIdBeenSet(false);
          setGCMRegistration(false);
          googlePlusService.renewConnection();
        }
        break;
      case RequestCodes.REQUEST_SWITCHED_ACCOUNTS:

        if (data != null) {
          if (data.getExtras().containsKey(IntentExtras.SETTINGS_SWITCHED_ACCOUNT)) {
            application.getApplicationSettings().setHasPlusIdBeenSet(false);
            application.getApplicationSettings().setUsingGooglePlus(true);
            setGCMRegistration(false);

            googlePlusService.renewConnection(application.getSelectedAccountName());

          } else if (data.getExtras().containsKey(IntentExtras.SETTINGS_ENABLED_GOOGLE_PLUS)) {
            googlePlusService.renewConnection();
          }
        }
    }
  }

  /**
   * Initializes Google Cloud Messaging
   */
  private void initializeGCM() {

    try {

      Context context = activity.getApplicationContext();

      GCMRegistrar.checkDevice(context);
      GCMRegistrar.checkManifest(context);

      String regId = GCMRegistrar.getRegistrationId(context);

      if (regId == null || regId.trim().length() == 0 || !GCMRegistrar.isRegistered(context)
          || !GCMRegistrar.isRegisteredOnServer(context)) {
        GCMRegistrar.register(context, GameBackendSettings.GCM_SENDER_ID);
      }
    } catch (RuntimeException e) {
      Log.e(TAG, "initializeGCM", e);
    }
  }

  /**
   * Set the Google Cloud Messaging registration on the server
   *
   * @param registeredOnServer
   */
  private void setGCMRegistration(boolean registeredOnServer) {
    try {
      GCMRegistrar.setRegisteredOnServer(activity.getApplicationContext(), registeredOnServer);
    } catch (RuntimeException e) {
      Log.e(TAG, "setGCMRegistration", e);
    }
  }

  /**
   * The purpose of this class is to act as a BroadcastReceiver for Google+ related messages
   */
  private class GooglePlusBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

      int messageType =
          intent.getExtras().getInt(GooglePlusService.GOOGLE_PLUS_SERVICE_MESSAGE_TYPE_EXTRA);

      switch (messageType) {
        case GooglePlusService.GOOGLE_PLUS_CONNECTED_MESSAGE:
          onGooglePlusConnected();
          break;
        case GooglePlusService.GOOGLE_PLUS_PERSON_NOT_PUBLIC:
          onGooglePlusPersonNotPublic();
          break;
        case GooglePlusService.GOOGLE_PLUS_CONNECTION_FAILED:
          PendingIntent pIntent = (PendingIntent) intent.getExtras()
              .getParcelable(GooglePlusService.GOOGLE_PLUS_CONNECTION_FAILED_RESOLUTION_EXTRA);
          int statusCode = intent.getExtras()
              .getInt(GooglePlusService.GOOGLE_PLUS_CONNECTION_FAILED_ERROR_CODE_EXTRA);
          onGooglePlusConnectionFailed(statusCode, pIntent);
          break;
      }

    }

  }
}
