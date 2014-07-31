package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.griddler.android.ApplicationSettings;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.model.Player;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * The purpose of this class is to make an asynchronous call to register the player
 *
 */
public class RegisterPlayerTask extends ServiceTask<Player> {

  private ApplicationSettings settings = null;
  private OnRegisterPlayerCompleted listener;

  public RegisterPlayerTask(
      Griddler service, ApplicationSettings settings, OnRegisterPlayerCompleted listener) {
    super(service);
    this.settings = settings;
    this.listener = listener;
  }

  @Override
  protected void onPostExecute(Player player) {
    if (player != null) {
      settings.setPlayerId(player.getId());
    }

    listener.onRegisterPlayerCompleted(player != null);
  }

  @Override
  protected Player executeEndpointCall() throws SocketTimeoutException, IOException {
    String googlePlusId = "";
    return service.playerEndpoint().insert(googlePlusId).execute();
  }
}
