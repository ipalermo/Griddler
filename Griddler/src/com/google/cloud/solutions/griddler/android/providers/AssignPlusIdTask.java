package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.griddler.android.ApplicationSettings;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * The purpose of this class is to make an asynchronous call to assign the players Google+ Id
 *
 */
public class AssignPlusIdTask extends ServiceTask<Boolean> {

  private String plusId;
  private ApplicationSettings settings = null;
  private OnAssignGooglePlusIdCompleted listener;

  public AssignPlusIdTask(Griddler service, ApplicationSettings settings, String plusId,
      OnAssignGooglePlusIdCompleted listener) {
    super(service);
    this.settings = settings;
    this.listener = listener;
    this.plusId = plusId;
  }

  @Override
  protected void onPostExecute(Boolean success) {
    if (success == null) {
      // Assignign PlusId thrown an exception
      success = false;
    }

    settings.setHasPlusIdBeenSet(success);

    listener.onAssignGooglePlusId(success);
  }

  @Override // Exceptions are handled in the base class.
  protected Boolean executeEndpointCall() throws SocketTimeoutException, IOException {
    service.playerEndpoint().assignPlayerPlusId(plusId).execute();
    return true;
  }
}
