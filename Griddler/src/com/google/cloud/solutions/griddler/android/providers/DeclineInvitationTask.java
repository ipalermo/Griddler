package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * The purpose of this class is to make an asynchronous call to decline an invitation
 *
 */
public class DeclineInvitationTask extends ServiceTask<Boolean> {

  private Long invitationId;
  private Long gameId;
  private OnDeclineInvitationCompleted listener;

  public DeclineInvitationTask(
      Griddler service, Long gameId, Long invitationId, OnDeclineInvitationCompleted listener) {
    super(service);
    this.listener = listener;
    this.invitationId = invitationId;
    this.gameId = gameId;
  }

  @Override
  protected void onPostExecute(Boolean result) {
    if (result == null) {
      // Declining the invitation thrown an exception.
      result = false;
    }
    listener.onDeclineInvitationCompleted(result);
  }

  @Override // Exceptions are handled in the base class.
  protected Boolean executeEndpointCall() throws SocketTimeoutException, IOException {
    service.gameEndpoint().declineInvitation(gameId, invitationId).execute();
    return true;
  }
}
