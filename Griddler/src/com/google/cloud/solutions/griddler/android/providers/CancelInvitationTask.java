package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * The purpose of this class is to make an asynchronous call to cancel an invitation
 *
 */
public class CancelInvitationTask extends ServiceTask<Boolean> {
  private Long invitationId;
  private Long gameId;
  private OnCancelInvitationCompleted listener;

  public CancelInvitationTask(
      Griddler service, Long gameId, Long invitationId, OnCancelInvitationCompleted listener) {
    super(service);
    this.listener = listener;
    this.invitationId = invitationId;
    this.gameId = gameId;
  }

  @Override
  protected void onPostExecute(Boolean result) {
    if (result == null) {
      // Canceling the invitation thrown an exception.
      result = false;
    }
    listener.onCancelInvitationCompleted(result);
  }

  @Override // Exceptions are handled in the base class.
  protected Boolean executeEndpointCall() throws SocketTimeoutException, IOException {
    service.gameEndpoint().cancelInvitation(gameId, invitationId).execute();
    return true;
  }
}
