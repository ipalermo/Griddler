package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * The purpose of this class is to make an asynchronous call to accept an invitation
 *
 */
public class AcceptInvitationTask extends ServiceTask<Boolean> {

  private OnAcceptInvitationCompleted listener;
  private Long invitationId;
  private Long gameId;

  public AcceptInvitationTask(
      Griddler service, Long gameId, Long invitationId, OnAcceptInvitationCompleted listener) {
    super(service);
    this.listener = listener;
    this.invitationId = invitationId;
    this.gameId = gameId;
  }

  @Override
  protected void onPostExecute(Boolean result) {
    if (result == null) {
      // Accepting invitation thrown an exception
      result = false;
    }
    listener.onAcceptInvitationCompleted(result, gameId, invitationId);
  }

  @Override // Exceptions are handled in the base class.
  protected Boolean executeEndpointCall() throws SocketTimeoutException, IOException {
    service.gameEndpoint().acceptInvitation(gameId, invitationId).execute();
    return true;
  }
}
