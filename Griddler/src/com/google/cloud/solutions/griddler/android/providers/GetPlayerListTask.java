package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.griddler.android.models.PlayerModel;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.model.Player;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.model.PlayerCollection;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * The purpose of this class is to make an asynchronous call to get a list of active players
 *
 */
public class GetPlayerListTask extends ServiceTask<PlayerCollection> {

  OnGetPlayerListCompleted listener;

  public GetPlayerListTask(Griddler service, OnGetPlayerListCompleted listener) {
    super(service);
    this.listener = listener;
  }

  @Override
  protected void onPostExecute(PlayerCollection result) {

    List<PlayerModel> players = new ArrayList<PlayerModel>();


    if (result != null && result.getItems() != null) {
      for (Player player : result.getItems()) {
        players.add(new PlayerModel(player.getId(), player.getNickname()));
      }
    }

    listener.onGetPlayerListCompleted(players);
  }

  @Override
  protected PlayerCollection executeEndpointCall() throws SocketTimeoutException, IOException {
    return service.playerEndpoint().list().execute();
  }
}
