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
package com.google.cloud.solutions.griddler.android.ui.settings;

import com.google.cloud.solutions.griddler.android.R;
import com.google.cloud.solutions.griddler.android.authentication.AuthEventListener;
import com.google.cloud.solutions.griddler.android.authentication.AuthFragment;
import com.google.cloud.solutions.griddler.android.models.PlayerRecordModel;
import com.google.cloud.solutions.griddler.android.providers.OnGetPlayerRecordCompleted;
import com.google.cloud.solutions.griddler.android.providers.OnRegisterPlayerCompleted;
import com.google.cloud.solutions.griddler.android.social.GooglePlusListener;
import com.google.cloud.solutions.griddler.android.ui.BaseActivity;
import com.google.cloud.solutions.griddler.android.ui.GoogleServicesFragment;
import com.google.cloud.solutions.griddler.android.ui.IntentExtras;
import com.google.cloud.solutions.griddler.android.ui.WaitOverlayFragment;
import com.google.cloud.solutions.griddler.android.ui.settings.SettingsFragment.SettingsFragmentInteractionListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

/**
 * The purpose of this activity is to allow the user to select a different account and to enable
 * their account to use Google+
 */
public class SettingsActivity extends BaseActivity
    implements
    AuthEventListener,
    OnRegisterPlayerCompleted,
    OnGetPlayerRecordCompleted,
    GooglePlusListener,
    SettingsFragmentInteractionListener {

  private SettingsFragment settingsFragment;

  private AuthFragment authFragment;

  private WaitOverlayFragment waitFragment;

  private String originalAccountName = null;

  private GoogleServicesFragment googleServicesFragment = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    settingsFragment = new SettingsFragment();

    getFragmentManager()
        .beginTransaction().replace(android.R.id.content, settingsFragment).commit();

    waitFragment = new WaitOverlayFragment();
    getFragmentManager()
        .beginTransaction()
        .add(android.R.id.content, waitFragment)
        .hide(waitFragment)
        .commit();

    authFragment = AuthFragment.getAuthFragment(this);

    originalAccountName = mApplication.getSelectedAccountName();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return false;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    authFragment.handleOnActivityResult(requestCode, resultCode, data);
  }

  private void displayMessageAndAuthenticate(int message) {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setNeutralButton(R.string.dialogButtonOK, new OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        mApplication.setSelectedAccountName(null); // basically a log-off
        authFragment.authenticateAsync();
      }
    });

    builder.setMessage(message);
    builder.setTitle(getString(R.string.app_name));
    AlertDialog dialog = builder.create();

    dialog.show();

    styleDialogButtons(dialog);
  }

  /**
   * If registration was successful get the player's record
   */
  @Override
  public void onRegisterPlayerCompleted(Boolean result) {

    if (result != null && result) {
      mApplication.getDataProvider().getPlayerRecord(this);
    } else {
      getFragmentManager().beginTransaction().hide(waitFragment).commit();
      displayMessageAndAuthenticate(R.string.failedToRegister);
    }
  }

  @Override
  public void onAuthCompleted() {

    getFragmentManager().beginTransaction().show(waitFragment).commit();
    registerPlayer();
  }

  private void registerPlayer() {
    mApplication.getDataProvider().registerPlayer(this);
  }

  /**
   * Set the application back to the prior account
   */
  @Override
  public void onAuthFailed() {
    mApplication.setSelectedAccountName(originalAccountName);
    mApplication.setPreviousAccount(null);
    mApplication.initializeGoogleServices();
  }

  /**
   * Navigate to the landing page
   */
  @Override
  public void onGetPlayerRecordCompleted(PlayerRecordModel model) {

    getFragmentManager().beginTransaction().hide(waitFragment).commit();

    Intent intent = new Intent();
    intent.putExtra(IntentExtras.SETTINGS_SWITCHED_ACCOUNT, model);
    setResult(RESULT_OK, intent);

    finish();
  }

  @Override
  public void onGooglePlusConnected() {

    getFragmentManager().beginTransaction().hide(waitFragment).commit();

    settingsFragment.hideEnableGooglePlusPreference();

    Intent intent = new Intent();
    intent.putExtra(IntentExtras.SETTINGS_ENABLED_GOOGLE_PLUS, true);
    setResult(RESULT_OK, intent);
  }

  @Override
  public void onGooglePlusPersonNotPublic() {
    getFragmentManager().beginTransaction().hide(waitFragment).commit();
    settingsFragment.showEnableGooglePlusPreference();
  }

  @Override
  public void onEnableGooglePlus() {
    connectToGooglePlus();
  }

  @Override
  public void onSwitchAccount() {
    mApplication.setPreviousAccount(mApplication.getSelectedAccountName());
    mApplication.setSelectedAccountName(null); // basically a log-off
    authFragment.authenticateAsync();
  }

  private void connectToGooglePlus() {

    getFragmentManager().beginTransaction().show(waitFragment).commit();

    if (googleServicesFragment == null) {

      /*
       * Create a bundle to notify the fragment that it should attempt to connect regardless of the
       * user's Google+ preferences
       */
      Bundle bundle = new Bundle();
      bundle.putBoolean(GoogleServicesFragment.FORCE_CONNECT_TO_GOOGLE_PLUS, true);

      googleServicesFragment = new GoogleServicesFragment();
      googleServicesFragment.setArguments(bundle);
      getFragmentManager().beginTransaction()
          .add(googleServicesFragment, GoogleServicesFragment.FRAGMENT_NAME).commit();
    } else {
      googleServicesFragment.renewConnection();
    }
  }
}
