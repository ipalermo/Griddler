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
package com.google.cloud.solutions.griddler.android.ui.splash;

import com.google.cloud.solutions.griddler.android.GameApplication;
import com.google.cloud.solutions.griddler.android.R;
import com.google.cloud.solutions.griddler.android.authentication.AuthEventListener;
import com.google.cloud.solutions.griddler.android.authentication.AuthFragment;
import com.google.cloud.solutions.griddler.android.models.PlayerRecordModel;
import com.google.cloud.solutions.griddler.android.providers.OnGetPlayerRecordCompleted;
import com.google.cloud.solutions.griddler.android.providers.OnRegisterPlayerCompleted;
import com.google.cloud.solutions.griddler.android.ui.ApplicationDialogs;
import com.google.cloud.solutions.griddler.android.ui.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * The purpose of this activity is to display a splash screen
 */
public class SplashScreenActivity extends Activity
    implements OnGetPlayerRecordCompleted, OnRegisterPlayerCompleted, AuthEventListener {

  private SplashAnimation mLogoAnimation;

  private AuthFragment mAuthFragment;

  private GameApplication mApplication = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_splash_screen);

    mLogoAnimation = (SplashAnimation) findViewById(R.id.theAnimation);

    mApplication = (GameApplication) SplashScreenActivity.this.getApplication();

    mAuthFragment = AuthFragment.getAuthFragment(this);

    doInit();
  }

  /**
   * Show/Hide the animation
   */
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    if (hasFocus) {
      mLogoAnimation.start();
    } else {
      mLogoAnimation.stop();
    }
  }

  private void attemptToLoadPlayerData() {
    if (mApplication.isUserSignedIn()) {
      // if a user is singed in then in most cases a record for this user exists on the backend.
      // However the previous call to registerPlayer might have failed, so let's call it again.
      mApplication.getDataProvider().registerPlayer(this);
      mApplication.getDataProvider().getPlayerRecord(this);
    } else {
      mAuthFragment.authenticateAsync(); // flow continue in onAuthCompleted()
    }
  }

  protected void doInit() {

    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        try {
          attemptToLoadPlayerData();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }, 3000);
  }

  /**
   * Navigate to the main activity
   */
  @Override
  public void onGetPlayerRecordCompleted(PlayerRecordModel model) {
    Intent intent = new Intent(this, MainActivity.class);
    intent.putExtra(PlayerRecordModel.TAG, model);
    this.startActivity(intent);
    this.finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    mAuthFragment.handleOnActivityResult(requestCode, resultCode, data);
  }

  /**
   * Register the player
   */
  @Override
  public void onAuthCompleted() {
    registerPlayer();
  }

  private void registerPlayer() {
    mApplication.getDataProvider().registerPlayer(this);
  }

  /**
   * Close the application
   */
  @Override
  public void onAuthFailed() {
    this.finish();
  }

  private void displayMessageAndAuthenticate(int message) {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setNeutralButton(R.string.dialogButtonOK, new OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        mApplication.setSelectedAccountName(null); // basically a log-off
        mAuthFragment.authenticateAsync();
      }
    });

    builder.setMessage(message);
    builder.setTitle(getString(R.string.app_name));
    AlertDialog dialog = builder.create();

    dialog.show();

    ApplicationDialogs.styleDialogButtons(this, dialog);
  }

  /**
   * Get the player record if the registration was successful
   */
  @Override
  public void onRegisterPlayerCompleted(Boolean result) {

    if (result != null && result) {
      mApplication.getDataProvider().getPlayerRecord(SplashScreenActivity.this);
    } else {
      displayMessageAndAuthenticate(R.string.failedToRegister);
    }
  }
}
