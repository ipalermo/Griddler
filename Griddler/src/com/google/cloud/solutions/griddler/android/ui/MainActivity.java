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

import com.google.android.gms.plus.model.people.Person;
import com.google.cloud.solutions.griddler.android.R;
import com.google.cloud.solutions.griddler.android.helpers.DownloadImageTask;
import com.google.cloud.solutions.griddler.android.helpers.OnDownloadImageCompleted;
import com.google.cloud.solutions.griddler.android.models.PlayerRecordModel;
import com.google.cloud.solutions.griddler.android.providers.OnAcceptInvitationCompleted;
import com.google.cloud.solutions.griddler.android.providers.OnDeclineInvitationCompleted;
import com.google.cloud.solutions.griddler.android.social.GooglePlusListener;
import com.google.cloud.solutions.griddler.android.ui.lobby.LobbyActivity;
import com.google.cloud.solutions.griddler.android.ui.opponentslist.OpponentListActivity;
import com.google.cloud.solutions.griddler.android.ui.settings.SettingsActivity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The purpose of this activity is for providing the player with a main landing screen to display
 * their record and allow them to start a new game. This activity is also responsible for accepting
 * and declining multiplayer invites
 */
public class MainActivity extends BaseActivity
    implements
    OnDownloadImageCompleted,
    OnAcceptInvitationCompleted,
    OnDeclineInvitationCompleted,
    GooglePlusListener {

  private static final String TAG = MainActivity.class.getSimpleName();

  private static PlayerRecordModel playerRecordModel;
  private static Bitmap userProfileImage;
  private static String userDisplayName;

  private AlertDialog acceptingChallengeDialog;
  private GoogleServicesFragment googleServicesFragment = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate");

    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    loadData();

    handleNotificationInvitationIntent(getIntent());

    googleServicesFragment = new GoogleServicesFragment();
    getFragmentManager().beginTransaction()
        .add(googleServicesFragment, GoogleServicesFragment.FRAGMENT_NAME).commit();
  }

  /**
   * Register receivers and handle the new intent
   */
  @Override
  protected void onNewIntent(Intent intent) {

    registerReceiver();
    handleNotificationInvitationIntent(intent);
    super.onNewIntent(intent);
  }

  /**
   * load the data from the intent and display the player's multiplayer record
   */
  protected void loadData() {

    // if the intent exists
    if (getIntent().hasExtra(PlayerRecordModel.TAG)) {
      playerRecordModel =
          (PlayerRecordModel) getIntent().getSerializableExtra(PlayerRecordModel.TAG);
    }

    displayPlayerRecord();
  }

  /**
   * React to activity results such as the ending of a game, switching accounts, and Google+
   * authorization
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    /*
     * Pass the activity results to the google services fragment for inspection
     */
    googleServicesFragment.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case RequestCodes.REQUEST_GAME_RESULT:
        if (data != null && data.getExtras().containsKey(IntentExtras.SUMMARY_WON_GAME)) {
          Boolean wonGame = data.getBooleanExtra(IntentExtras.SUMMARY_WON_GAME, false);
          refreshRecord(wonGame);
        } else if (data != null
            && data.getExtras().containsKey(IntentExtras.SUMMARY_FAILED_TO_SUBMIT_ANSWERS)) {
          ApplicationDialogs.displayAlert(this, getString(R.string.failedToSubmitAnswers));
        }
        break;
      case RequestCodes.REQUEST_SWITCHED_ACCOUNTS:
        if (data != null && data.getExtras().containsKey(IntentExtras.SETTINGS_SWITCHED_ACCOUNT)) {
          userProfileImage = null;
          playerRecordModel =
              (PlayerRecordModel) data.getSerializableExtra(IntentExtras.SETTINGS_SWITCHED_ACCOUNT);
        }
        displayPlayerRecord();
        break;
      case RequestCodes.REQUEST_GOOGLE_PLUS_AUTHORIZATION:
        if (resultCode == RESULT_CANCELED) {
          displayPlayerRecord();
        }
        break;
    }
  }

  private void refreshRecord(Boolean wonGame) {
    if (wonGame != null) {
      if (playerRecordModel != null) {
        playerRecordModel.playedGame(wonGame);
        displayPlayerRecord();
      }
    }
  }

  /**
   * Occurs when the user clicks the button to start a single player game
   *
   * @param v The View
   */
  public void onSinglePlayerButtonClick(View v) {
    Intent intent = new Intent(this, LobbyActivity.class);
    intent.putExtra(IntentExtras.LOBBY_GAME_TYPE, IntentExtras.LOBBY_SINGLE_PLAYER);
    startActivityForResult(intent, RequestCodes.REQUEST_GAME_RESULT);
  }

  /**
   * Occurs when the user clicks the button to challenge an opponent
   *
   * @param v The View
   */
  public void onChallengeOpponentButtonClick(View v) {
    startActivityForResult(
        new Intent(this, OpponentListActivity.class), RequestCodes.REQUEST_GAME_RESULT);
  }

  /**
   * Display the user's record
   */
  private void displayPlayerRecord() {

    if (playerRecordModel == null) {
      return;
    }

    ImageView profileImage = (ImageView) findViewById(R.id.user_image);
    TextView text = (TextView) findViewById(R.id.userNameText);

    if (!mApplication.getApplicationSettings().getUsingGooglePlus()) {
      text.setText(mApplication.getSelectedAccountName());
      profileImage.setImageResource(R.drawable.avatar_large);
      profileImage.invalidate();
    } else {
      if (userProfileImage != null) {
        profileImage.setImageBitmap(userProfileImage);
        profileImage.invalidate();
      } else {
        profileImage.setImageResource(R.drawable.avatar_large);
        profileImage.invalidate();
      }

      if (userDisplayName != null) {
        text.setText(userDisplayName);
      } else {
        text.setText(mApplication.getSelectedAccountName());
      }
    }

    TextView stats = (TextView) findViewById(R.id.userStatsText);

    if (playerRecordModel.getGamesPlayed() > 0) {

      String statsText = String.format(
          getString(R.string.userStatsText), playerRecordModel.getGamesWon(),
          playerRecordModel.getGamesPlayed());
      stats.setText(statsText);
    } else {
      stats.setText(getString(R.string.noGamesPlayedMessage));
    }
  }

  /**
   * Display the users name and profile picture since they are a Google+ user
   */
  @Override
  public void onGooglePlusConnected() {

    Person person = googleServicesFragment.getCurrentPerson();

    TextView text = (TextView) findViewById(R.id.userNameText);
    ImageView profileImage = (ImageView) findViewById(R.id.user_image);

    if (person == null) {
      text.setText(mApplication.getSelectedAccountName());
      profileImage.setImageResource(R.drawable.avatar_large);
      profileImage.invalidate();
    } else {

      String displayName = person.getDisplayName();

      if (displayName == null || displayName.trim().length() == 0) {
        text.setText(mApplication.getSelectedAccountName());
      } else {
        userDisplayName = person.getDisplayName();
        text.setText(userDisplayName);
      }

      String url = person.getImage().getUrl();

      if (url == null || url.trim().length() == 0) {
        profileImage.setImageResource(R.drawable.avatar_large);
        profileImage.invalidate();
      } else {

        // if the size was specified in the url just replace it
        if (url.indexOf("sz=") > 0) {
          // change the default size to 96
          url = url.replaceFirst("([?&]sz)=[^?&]+", "$1=144");
        } else {
          // if the size wasn't specified just append it
          if (url.indexOf("?") > 0) {
            url = url + "&sz=144";
          } else {
            url = url + "?sz=144";
          }
        }

        new DownloadImageTask(profileImage, this).execute(url);
      }
    }
  }

  /**
   * Assign a local variable to the user's profile image from Google+ for caching purposes
   */
  @Override
  public void onDownloadImage(Bitmap image) {
    userProfileImage = image;
  }

  /**
   * Process the intent by checking if it is a notification based intent and either accepting,
   * declining, or display a dialog to the user prompting them with an accept/decline dialog.
   *
   * @param intent The {@link Intent}
   */
  protected void handleNotificationInvitationIntent(Intent intent) {

    if (intent != null && intent.getExtras() != null
        && intent.getExtras().containsKey(ApplicationNotifications.INVITATION_ID_EXTRA_ID)) {

      Long playerId = intent.getLongExtra(ApplicationNotifications.PLAYER_ID_EXTRA_ID, 0);

      if (mApplication.getCurrentPlayerId().compareTo(playerId) == 0) {

        NotificationManager notificationManager =
            (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);


        Long invitationId = intent.getLongExtra(ApplicationNotifications.INVITATION_ID_EXTRA_ID, 0);
        Long gameId = intent.getLongExtra(ApplicationNotifications.GAME_ID_EXTRA_ID, 0);

        int playAction = intent.getIntExtra(ApplicationNotifications.INVITATION_ACTION_EXTRA_ID, 0);

        switch (playAction) {
          case ApplicationNotifications.ACCEPT_INVITATION_ACTION:
            showAcceptingChallengeDialog();
            mApplication.getDataProvider().acceptInvitation(gameId, invitationId, this);
            break;
          case ApplicationNotifications.DECLINE_INVITATION_ACTION:
            mApplication.getDataProvider().declineInvitation(gameId, invitationId, this);
            break;
          case ApplicationNotifications.PROMPT_INVITATION_ACTION:
            String message =
                intent.getStringExtra(ApplicationNotifications.INVITATION_MESSAGE_EXTRA_ID);
            showInvitationDialog(message, invitationId, gameId);
            break;
        }
      } else {
        String nickName = intent.getStringExtra(ApplicationNotifications.NICK_NAME_EXTRA_ID);
        ApplicationDialogs.displayAlert(
            this, String.format(getString(R.string.invitationIntendedForAnotherUser), nickName));
      }
    }
  }

  /**
   * Dismiss the accepting challenge dialog because we're about to navigate to the lobby
   */
  private void hideAcceptingChallengeDialog() {
    if (acceptingChallengeDialog != null) {
      acceptingChallengeDialog.dismiss();
    }
  }

  /**
   * Display a dialog to the user while the application is accepting the challenge.
   */
  private void showAcceptingChallengeDialog() {

    if (acceptingChallengeDialog == null) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);

      builder.setMessage(R.string.acceptingChallenge);
      builder.setTitle(R.string.app_name);
      builder.setCancelable(false);
      acceptingChallengeDialog = builder.create();
    }

    acceptingChallengeDialog.show();

    styleDialogButtons(acceptingChallengeDialog);
  }

  /**
   * Display a dialog to the user prompting them to accept/decline the invitation
   *
   * @param message The message to display
   * @param invitationId The invitation ID
   * @param gameId The game ID
   */
  private void showInvitationDialog(String message, final Long invitationId, final Long gameId) {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setNegativeButton(R.string.declineInvitation, new OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        mApplication.getDataProvider().declineInvitation(gameId, invitationId, MainActivity.this);
      }
    });

    builder.setPositiveButton(R.string.acceptInvitation, new OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        showAcceptingChallengeDialog();
        mApplication.getDataProvider().acceptInvitation(gameId, invitationId, MainActivity.this);

      }
    });

    builder.setMessage(message);
    builder.setTitle(R.string.app_name);
    builder.setCancelable(false);
    AlertDialog inviteDialog = builder.create();

    inviteDialog.show();

    styleDialogButtons(inviteDialog);
  }

  /**
   * If the declining of an invitation fails, display an alert to the user
   */
  @Override
  public void onDeclineInvitationCompleted(Boolean result) {
    if (!result) {
      ApplicationDialogs.displayAlert(this, getString(R.string.unableToDeclineInvitation));
    }
  }

  /**
   * Navigate to the lobby activity if accepting the invitation was successful else display a
   * message to the user that the sender must have canceled
   */
  @Override
  public void onAcceptInvitationCompleted(Boolean result, Long gameId, Long invitationId) {

    hideAcceptingChallengeDialog();

    // evaluate the result...if false, the challenger must have cancelled
    if (result) {
      Intent intent = new Intent(this, LobbyActivity.class);
      intent.putExtra(IntentExtras.LOBBY_GAME_TYPE, IntentExtras.LOBBY_MULTI_PLAYER);
      intent.putExtra(IntentExtras.LOBBY_GAME_ID, gameId);
      startActivityForResult(intent, RequestCodes.REQUEST_GAME_RESULT);
    } else {
      ApplicationDialogs.displayAlert(this, this.getString(R.string.challengerCancelledCantAccept));
    }
  }

  /**
   * If the options menu matches settings then navigate to the settings screen
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int itemId = item.getItemId();
	if (itemId == R.id.action_settings) {
		startActivityForResult(
            new Intent(this, SettingsActivity.class), RequestCodes.REQUEST_SWITCHED_ACCOUNTS);
	} else {
	}

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onGooglePlusPersonNotPublic() {
    displayPlayerRecord();
  }
}
