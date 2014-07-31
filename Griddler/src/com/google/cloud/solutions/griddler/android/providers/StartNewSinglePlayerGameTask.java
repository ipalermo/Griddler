package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.griddler.android.ApplicationSettings;
import com.google.cloud.solutions.griddler.android.models.GameModel;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.model.Game;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * The purpose of this class is to make an asynchronous call to get a game and board model to start
 * a new single player game
 *
 */
public class StartNewSinglePlayerGameTask extends ServiceTask<Game> {

  private ApplicationSettings settings = null;
  private OnStartNewSinglePlayerGameCompleted listener;

  public StartNewSinglePlayerGameTask(Griddler service, ApplicationSettings settings,
      OnStartNewSinglePlayerGameCompleted listener) {

    super(service);
    this.settings = settings;
    this.listener = listener;
  }

  @Override
  protected Game executeEndpointCall() throws IOException, SocketTimeoutException {
    try {
      Game game = service.gameEndpoint().startSinglePlayerGame(1).execute();
      return game;
    } catch (IOException e) {
      return null;
    }
  }

  @Override
  protected void onPostExecute(Game game) {

    GameModel model = null;

    if (game != null && game.getId() != null) {
      model = GameBuilder.buildGameModel(settings, game);
    }

    listener.onStartNewSinglePlayerGameCompleted(model);
  }
}
