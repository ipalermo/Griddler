package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.griddler.android.models.PlayerRecordModel;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.model.Player;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * The purpose of this class is to make an asynchronous call to retrieve a player's multiplayer
 * record
 *
 */
public class GetPlayerRecordTask extends ServiceTask<Player> {

  OnGetPlayerRecordCompleted listener;

  public GetPlayerRecordTask(Griddler service, OnGetPlayerRecordCompleted listener) {
    super(service);
    this.listener = listener;
  }

  @Override
  protected void onPostExecute(Player player) {

    PlayerRecordModel model = null;

    if (player != null) {
      model = new PlayerRecordModel(player.getNickname(), player.getStatistics().getNumberOfWins(),
          player.getStatistics().getNumberOfGames());
    }

    listener.onGetPlayerRecordCompleted(model);
  }

  @Override
  protected Player executeEndpointCall() throws SocketTimeoutException, IOException {
    return service.playerEndpoint().get().execute();
  }
}
