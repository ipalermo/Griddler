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
package com.google.cloud.solutions.griddler.android.ui.opponentslist;

import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.google.cloud.solutions.griddler.android.R;
import com.google.cloud.solutions.griddler.android.models.InvitationModel;
import com.google.cloud.solutions.griddler.android.models.PlayerModel;
import com.google.cloud.solutions.griddler.android.providers.OnCancelInvitationCompleted;
import com.google.cloud.solutions.griddler.android.providers.OnGetOpponentResponseToInvitationCompleted;
import com.google.cloud.solutions.griddler.android.providers.OnGetPlayerListCompleted;
import com.google.cloud.solutions.griddler.android.providers.OnSendInvitationCompleted;
import com.google.cloud.solutions.griddler.android.social.GooglePlusListener;
import com.google.cloud.solutions.griddler.android.social.GooglePlusPeopleLoadedListener;
import com.google.cloud.solutions.griddler.android.ui.ApplicationDialogs;
import com.google.cloud.solutions.griddler.android.ui.BaseActivity;
import com.google.cloud.solutions.griddler.android.ui.GoogleServicesFragment;
import com.google.cloud.solutions.griddler.android.ui.IntentExtras;
import com.google.cloud.solutions.griddler.android.ui.RequestCodes;
import com.google.cloud.solutions.griddler.android.ui.WaitOverlayFragment;
import com.google.cloud.solutions.griddler.android.ui.lobby.LobbyActivity;
import com.google.cloud.solutions.griddler.android.ui.opponentslist.OpponentListFragment.OpponentListFragmentInteractionListener;
import com.google.cloud.solutions.griddler.android.ui.opponentslist.models.OpponentItem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The purpose of this activity is to support the display of a list of either Google+ opponents or a
 * general list of opponents registered in the App Engine as well as the ability to send an
 * invitation to an opponent
 */
public class OpponentListActivity extends BaseActivity
    implements
    OpponentListFragmentInteractionListener,
    OnGetPlayerListCompleted,
    OnSendInvitationCompleted,
    OnClickListener,
    OnCancelInvitationCompleted,
    GooglePlusListener {

  private WaitOverlayFragment waitFragment;

  private Timer timer = new Timer();

  private AlertDialog inviteDialog;

  private Long currentInvitationId;

  private Long currentGameId;

  private OpponentItem selectedOpponentItem;

  private boolean attemptingToCancel = false;

  private TabButton yourCirclesButton;

  private TabButton allOpponentsButton;

  private Map<String, OpponentItem> opponents = new LinkedHashMap<String, OpponentItem>();

  private GoogleServicesFragment googleServicesFragment = null;

  /**
   * Display the "spinny" fragment and begin the loading of either Google+ opponents or general game
   * opponents
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    currentInvitationId = null;
    currentGameId = null;

    setContentView(R.layout.activity_opponent_list);

    waitFragment = new WaitOverlayFragment();

    getFragmentManager().beginTransaction().add(android.R.id.content, waitFragment).commit();

    boolean usingGooglePlus = mApplication.getApplicationSettings().getUsingGooglePlus();

    yourCirclesButton = (TabButton) findViewById(R.id.yourCirclesButton);
    allOpponentsButton = (TabButton) findViewById(R.id.allOpponentsButton);
    LinearLayout tabLayout = (LinearLayout) findViewById(R.id.tabButtonLayout);

    this.yourCirclesButton.setSelected(true);
    if (!usingGooglePlus) {
      this.yourCirclesButton.setSelected(false);
      this.allOpponentsButton.setSelected(true);
      tabLayout.setVisibility(View.GONE);
      loadPlayerList();
    } else {

      googleServicesFragment = new GoogleServicesFragment();
      getFragmentManager().beginTransaction()
          .add(googleServicesFragment, GoogleServicesFragment.FRAGMENT_NAME).commit();

    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return false;
  }

  /**
   * Loads the list of Google+ friends
   *
   * @param v The View
   */
  public void onYourCirclesButtonClicked(View v) {
    if (!this.yourCirclesButton.isSelected()) {
      this.yourCirclesButton.setSelected(true);
      this.allOpponentsButton.setSelected(false);
      getFragmentManager().beginTransaction().show(waitFragment).commit();
      loadGooglePlusFriends();
    }
  }

  /**
   * Loads the list of general opponents
   *
   * @param v The View
   */
  public void onAllOpponentsButtonClicked(View v) {
    if (!this.allOpponentsButton.isSelected()) {
      this.yourCirclesButton.setSelected(false);
      this.allOpponentsButton.setSelected(true);
      getFragmentManager().beginTransaction().show(waitFragment).commit();
      loadPlayerList();
    }
  }

  /**
   * Occurs when an opponent is selected in the list. Sends an invitation to the opponent
   */
  @Override
  public void onFragmentInteraction(String id) {

    if (!attemptingToCancel) {
      selectedOpponentItem = opponents.get(id);

      showInviteDialog();

      if (selectedOpponentItem.isPlusId) {
        mApplication.getDataProvider().sendInvitation(selectedOpponentItem.id, this);
      } else {
        mApplication.getDataProvider().sendInvitation(Long.valueOf(selectedOpponentItem.id), this);
      }

    } else {
      ApplicationDialogs.displayAlert(this, getString(R.string.attemptingToCancelinvitation));
    }

  }

  /**
   * Loads the opponents into the list fragment
   */
  @Override
  public void onGetPlayerListCompleted(List<PlayerModel> result) {

    opponents.clear();

    if (result != null) {

      for (PlayerModel model : result) {
        opponents.put(model.getPlayerId().toString(),
            new OpponentItem(model.getPlayerId().toString(), model.getNickName()));
      }
    }

    OpponentListFragment fragment =
        (OpponentListFragment) getFragmentManager().findFragmentById(R.id.opponent_list_fragment);


    if (fragment != null && fragment.getView() != null) {
      fragment.setList(opponents);
      getFragmentManager().beginTransaction().hide(waitFragment).commit();
    }

    if (!opponents.isEmpty()) {
      fragment.setShowNoOpponents(false);
    } else {
      fragment.setShowNoOpponents(true);
    }

  }

  /**
   * Handles the result of sending an invitation
   */
  @Override
  public void onSendInvitationCompleted(InvitationModel result) {

    if (result != null) {

      if (result.getStatus() == 1) {
        currentInvitationId = null;
        currentGameId = null;
        inviteDialog.dismiss();
        ApplicationDialogs.displayAlert(this, getString(R.string.playerNotRegisteredInGriddler));
      } else {

        currentInvitationId = result.getInvitationId();
        currentGameId = result.getGameId();

        if (attemptingToCancel) {
          mApplication.getDataProvider().cancelInvitation(currentGameId, currentInvitationId, this);
        } else {
          timer.schedule(new CheckOpponentInvitationResponse(), 0);
        }
      }

    } else {
      currentInvitationId = null;
      currentGameId = null;
      inviteDialog.dismiss();
      ApplicationDialogs.displayAlert(this, getString(R.string.unableToSendInvitation));
    }
  }

  /**
   * The purpose of this class is to check the status of a sent invitation
   */
  private class CheckOpponentInvitationResponse extends TimerTask
      implements OnGetOpponentResponseToInvitationCompleted {

    @Override
    public void run() {
      if (currentGameId != null && currentInvitationId != null) {
        mApplication.getDataProvider()
            .getOpponentResponseToInvitation(currentGameId, currentInvitationId, this);
      }
    }

    @Override
    public void onGetOpponentResponseToInvitationCompleted(String invitationStatus) {

      if (currentInvitationId == null || currentGameId == null || attemptingToCancel) {
        return;
      }

      if (invitationStatus != null && invitationStatus.equalsIgnoreCase("SENT")) {
        // run again in 1 second
        timer.schedule(new CheckOpponentInvitationResponse(), 1000);
      } else {

        if (inviteDialog != null) {
          inviteDialog.dismiss();
        }

        if (invitationStatus != null && invitationStatus.equalsIgnoreCase("ACCEPTED")) {

          // send off to lobby
          Intent intent = new Intent(OpponentListActivity.this, LobbyActivity.class);
          intent.putExtra(IntentExtras.LOBBY_GAME_TYPE, IntentExtras.LOBBY_MULTI_PLAYER);
          intent.putExtra(IntentExtras.LOBBY_GAME_ID, currentGameId);
          startActivityForResult(intent, RequestCodes.REQUEST_GAME_RESULT);

        } else {
          ApplicationDialogs.displayAlert(OpponentListActivity.this, selectedOpponentItem.content,
              OpponentListActivity.this.getString(R.string.challengeDeclined));
        }
      }
    }
  }

  private void showInviteDialog() {

    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setNegativeButton(R.string.cancelChallenge, this);

    builder.setMessage(R.string.sendingChallenge);
    builder.setTitle(selectedOpponentItem.content);
    builder.setCancelable(false);
    inviteDialog = builder.create();

    inviteDialog.show();

    styleDialogButtons(inviteDialog);
  }

  /**
   * Handles the click events from the send invitation dialog
   */
  @Override
  public void onClick(DialogInterface dialog, int which) {
    switch (which) {
      case DialogInterface.BUTTON_NEGATIVE:
        attemptingToCancel = true;

        // can't cancel because there isn't a current invitation
        if (currentGameId != null && currentInvitationId != null) {
          mApplication.getDataProvider().cancelInvitation(currentGameId, currentInvitationId, this);
        }
        break;
    }
  }

  /**
   * Handles the result of a canceled invitation
   */
  @Override
  public void onCancelInvitationCompleted(Boolean result) {

    if (!result) {
      attemptingToCancel = false;
      if (inviteDialog.getContext() != null) {
        inviteDialog.setMessage(this.getString(R.string.opponentAcceptedCantCancel));
        inviteDialog.show();
      }
    } else {
      // cancel the timer so polling stops
      currentInvitationId = null;
      currentGameId = null;
      attemptingToCancel = false;
    }
  }

  /**
   * Set the result and finish the activity if the request code is a finished game
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case RequestCodes.REQUEST_GAME_RESULT:
        setResult(resultCode, data);
        finish();
        break;
    }
  }


  private void loadGooglePlusFriends() {
    googleServicesFragment.loadPeople(new GooglePlusPeopleLoadedListener() {

      @Override
      public void onGooglePlusPeopleLoaded(PersonBuffer buffer) {

        try {

          opponents.clear();

          if (buffer == null) {
            return;
          }

          if (buffer != null) {

            for (int index = 0; index < buffer.getCount(); index++) {
              Person person = buffer.get(index).freeze();
              opponents.put(person.getId().toString(), new OpponentItem(
                  person.getId(), person.getDisplayName(), person.getImage().getUrl()));
            }
          }

          OpponentListFragment fragment = (OpponentListFragment) getFragmentManager()
              .findFragmentById(R.id.opponent_list_fragment);

          if (fragment != null && fragment.getView() != null) {
            fragment.setList(opponents);
          }

          if (!opponents.isEmpty()) {
            fragment.setShowNoOpponents(false);
          } else {
            fragment.setShowNoOpponents(true);
          }
        } finally {
          getFragmentManager().beginTransaction().hide(waitFragment).commit();

          if (buffer != null) {
            buffer.close();
          }
        }
      }
    });
  }

  private void loadPlayerList() {
    mApplication.getDataProvider().getPlayerList(this);
  }


  @Override
  public void onGooglePlusConnected() {
    loadGooglePlusFriends();
  }

  @Override
  public void onGooglePlusPersonNotPublic() {}

}
