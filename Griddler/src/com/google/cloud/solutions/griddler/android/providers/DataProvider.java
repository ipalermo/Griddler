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
package com.google.cloud.solutions.griddler.android.providers;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.cloud.solutions.griddler.android.ApplicationSettings;
import com.google.cloud.solutions.griddler.android.GameBackendSettings;
import com.google.cloud.solutions.griddler.android.R;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.model.GamePlayStatus;

import android.content.Context;

import java.util.List;

/**
 * The purpose of this class is to support making asynchronous calls to the app engine cloud
 * endpoints
 */
public class DataProvider {

  private Griddler service = null;

  private ApplicationSettings settings = null;

  /**
   * Constructor
   *
   * @param context The context
   */
  public DataProvider(Context context) {
    settings = new ApplicationSettings(context);

    GoogleAccountCredential credential =
        GoogleAccountCredential.usingAudience(context, GameBackendSettings.AUDIENCE_ID);
    credential.setSelectedAccountName(settings.getSelectedAccountName());

    Griddler.Builder builder =
        new Griddler.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential);
    builder.setApplicationName(context.getString(R.string.app_name));
    builder.setRootUrl(GameBackendSettings.DEFAULT_ROOT_URL);
    service = builder.build();
  }

  /**
   * Assign the players Google+ ID
   *
   * @param plusId The plus ID
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void assignPlusId(String plusId, OnAssignGooglePlusIdCompleted listener) {
    AssignPlusIdTask task = new AssignPlusIdTask(service, settings, plusId, listener);
    task.execute();
  }

  /**
   * Register a player
   *
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void registerPlayer(OnRegisterPlayerCompleted listener) {
    RegisterPlayerTask task = new RegisterPlayerTask(service, settings, listener);
    task.execute();
  }

  /**
   * Unregister the device
   *
   * @param deviceId The device ID
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void unRegisterDevice(String deviceId, OnUnregisterDeviceCompleted listener) {
    UnregisterDeviceTask task = new UnregisterDeviceTask(service, deviceId, listener);
    task.execute();
  }

  /**
   * Register a device
   *
   * @param deviceId The device ID
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void registerDevice(String deviceId, OnRegisterDeviceCompleted listener) {
    RegisterDeviceTask task = new RegisterDeviceTask(service, deviceId, listener);
    task.execute();
  }

  /**
   * Send an invitation to a multiplayer game
   *
   * @param playerId The player ID to invite
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void sendInvitation(Long playerId, OnSendInvitationCompleted listener) {
    SendInvitationTask task = new SendInvitationTask(service, playerId, listener);
    task.execute();
  }

  /**
   * Send an invitation to a multiplayer game
   *
   * @param plusId The player's Google+ ID
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void sendInvitation(String plusId, OnSendInvitationCompleted listener) {
    SendInvitationByPlusIdTask task = new SendInvitationByPlusIdTask(service, plusId, listener);
    task.execute();
  }

  /**
   * Accept an invitation
   *
   * @param gameId The game ID
   * @param invitationId The invitation ID
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void acceptInvitation(
      Long gameId, Long invitationId, OnAcceptInvitationCompleted listener) {
    AcceptInvitationTask task = new AcceptInvitationTask(service, gameId, invitationId, listener);
    task.execute();
  }

  /**
   * Decline an invitation
   *
   * @param gameId The game ID
   * @param invitationId The invitation ID
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void declineInvitation(
      Long gameId, Long invitationId, OnDeclineInvitationCompleted listener) {
    DeclineInvitationTask task = new DeclineInvitationTask(service, gameId, invitationId, listener);
    task.execute();
  }

  /**
   * Get the opponents response to an invitation
   *
   * @param gameId The game ID
   * @param invitationId The invitation ID
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void getOpponentResponseToInvitation(
      Long gameId, Long invitationId, OnGetOpponentResponseToInvitationCompleted listener) {
    GetOpponentResponseToInvitationTask task =
        new GetOpponentResponseToInvitationTask(service, gameId, invitationId, listener);
    task.execute();
  }

  /**
   * Get the list of active registered players.
   *
   *  This method was intentionally simplified for a small number of opponents. If large lists are
   * expected to be returned paging should be implemented.
   *
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void getPlayerList(OnGetPlayerListCompleted listener) {
    GetPlayerListTask task = new GetPlayerListTask(service, listener);
    task.execute();
  }

  /**
   * Get a game
   *
   * @param gameId The game ID
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void getGame(Long gameId, OnGetGameCompleted listener) {
    GetGameTask task = new GetGameTask(service, settings, gameId, listener);
    task.execute();
  }

  /**
   * Start a new single player game
   *
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void startNewSinglePlayerGame(OnStartNewSinglePlayerGameCompleted listener) {
    StartNewSinglePlayerGameTask task =
        new StartNewSinglePlayerGameTask(service, settings, listener);
    task.execute();
  }

  /**
   * Cancel an invitation
   *
   * @param gameId The game ID
   * @param invitationId The invitation ID
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void cancelInvitation(
      Long gameId, Long invitationId, OnCancelInvitationCompleted listener) {
    CancelInvitationTask task = new CancelInvitationTask(service, gameId, invitationId, listener);
    task.execute();
  }

  /**
   * Get the current player's record
   *
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void getPlayerRecord(OnGetPlayerRecordCompleted listener) {
    GetPlayerRecordTask task = new GetPlayerRecordTask(service, listener);
    task.execute();
  }

  /**
   * Submit the current player's answers for a game
   *
   * @param gameId The game ID
   * @param answers The list of indexes of the questions answered
   * @param timeLeft The amount of time left on the game board in milliseconds
   * @param listener The listener that is executed when the asynchronous call is complete
   */
  public void submitAnswers(
      Long gameId, List<Integer> answers, Long timeLeft, OnSubmitAnswersCompleted listener) {
    GamePlayStatus playerAnswers = new GamePlayStatus();
    playerAnswers.setCorrectAnswers(answers);
    playerAnswers.setTimeLeft(timeLeft);

    SubmitAnswersTask task = new SubmitAnswersTask(service, gameId, playerAnswers, listener);
    task.execute();
  }
}
