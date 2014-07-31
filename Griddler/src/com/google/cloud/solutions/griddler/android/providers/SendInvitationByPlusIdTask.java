package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.griddler.android.models.InvitationModel;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.model.Invitation;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * The purpose of this class is to make an asynchronous call to send an invitation to an opponent
 * using the opponent's Google+ Id
 *
 */
public class SendInvitationByPlusIdTask extends ServiceTask<InvitationModel> {

  private OnSendInvitationCompleted listener;
  private String playerId;

  public SendInvitationByPlusIdTask(
      Griddler service, String playerId, OnSendInvitationCompleted listener) {
    super(service);
    this.listener = listener;
    this.playerId = playerId;
  }

  @Override
  protected void onPostExecute(InvitationModel result) {
    listener.onSendInvitationCompleted(result);
  }

  @Override
  protected InvitationModel executeEndpointCall() throws SocketTimeoutException, IOException {
    Invitation invitation =
        service.gameEndpoint().startMultiplayerGameByGooglePlusId(1, playerId).execute();
    return new InvitationModel(invitation.getGameId(), invitation.getInvitationId());
  }
}
