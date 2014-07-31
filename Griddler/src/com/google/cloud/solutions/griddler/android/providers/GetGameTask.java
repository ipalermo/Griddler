package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.griddler.android.ApplicationSettings;
import com.google.cloud.solutions.griddler.android.models.GameModel;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.model.Game;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * The purpose of this class is to make an asynchronous call to get a game
 *
 */
public class GetGameTask extends ServiceTask<Game> {

  private ApplicationSettings settings = null;
  private OnGetGameCompleted listener;
  private Long gameId;

  public GetGameTask(
      Griddler service, ApplicationSettings settings, Long gameId, OnGetGameCompleted listener) {
    super(service);
    this.settings = settings;
    this.listener = listener;
    this.gameId = gameId;
  }

  @Override
  protected void onPostExecute(Game game) {

    GameModel model = null;

    if (game != null && game.getId() != null) {
      model = GameBuilder.buildGameModel(settings, game);
    }

    listener.onGetGameCompleted(model);

  }

  @Override
  protected Game executeEndpointCall() throws SocketTimeoutException, IOException {
    return service.gameEndpoint().getGame(gameId).execute();
  }


}
