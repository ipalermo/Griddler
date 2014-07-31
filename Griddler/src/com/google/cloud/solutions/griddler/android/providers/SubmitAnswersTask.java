package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.model.GamePlayStatus;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * The purpose of this class is to make an asynchronous call to submit answers for a specific game
 *
 */
public class SubmitAnswersTask extends ServiceTask<Void> {

  private OnSubmitAnswersCompleted listener;
  private Long gameId;
  private GamePlayStatus answers;

  public SubmitAnswersTask(
      Griddler service, Long gameId, GamePlayStatus answers, OnSubmitAnswersCompleted listener) {
    super(service);
    this.listener = listener;
    this.gameId = gameId;
    this.answers = answers;
  }

  @Override
  protected void onPostExecute(Void result) {
    listener.onSubmitAnswersCompleted(true);
  }

  @Override
  protected Void executeEndpointCall() throws SocketTimeoutException, IOException {
    service.gameEndpoint().submitAnswers(gameId, answers).execute();
    return null;
  }
}
