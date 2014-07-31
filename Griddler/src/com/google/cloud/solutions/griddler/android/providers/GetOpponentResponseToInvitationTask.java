package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.model.Invitation;

import java.io.IOException;
import java.net.SocketTimeoutException;


/**
 * The purpose of this class is to make an asynchronous call to determine if the opponent responded
 * to the invitation
 *
 */
public class GetOpponentResponseToInvitationTask extends ServiceTask<String> {
  private Long invitationId;
  private Long gameId;
  private OnGetOpponentResponseToInvitationCompleted listener;

  public GetOpponentResponseToInvitationTask(Griddler service, Long gameId, Long invitationId,
      OnGetOpponentResponseToInvitationCompleted listener) {
    super(service);
    this.listener = listener;
    this.invitationId = invitationId;
    this.gameId = gameId;
  }

  @Override
  protected void onPostExecute(String result) {
    listener.onGetOpponentResponseToInvitationCompleted(result);
  }

  @Override // Exceptions are handled in the base class
  protected String executeEndpointCall() throws SocketTimeoutException, IOException {
    Invitation invitation =
        service.gameEndpoint().getInvitationStatus(gameId, invitationId).execute();
    return invitation.getStatus();
  }
}
