/*
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.solutions.griddler.android.ui.game;

import com.google.cloud.solutions.griddler.android.GameApplication;
import com.google.cloud.solutions.griddler.android.R;
import com.google.cloud.solutions.griddler.android.helpers.CountDownTimerPausable;
import com.google.cloud.solutions.griddler.android.helpers.Location;
import com.google.cloud.solutions.griddler.android.models.GameModel;
import com.google.cloud.solutions.griddler.android.ui.RequestCodes;
import com.google.cloud.solutions.griddler.android.ui.game.models.BoardRenderModel;
import com.google.cloud.solutions.griddler.android.ui.summary.GameSummaryActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Game Workflow and View Controllers. The game transitions between a GameStateType
 * status during playback/feedback.
 */
public class GameActivity extends FragmentActivity implements IGameManager {

  BoardRenderModel board;
  GameStateType gameState = GameStateType.GENERATING;
  CountDownTimerPausable countdownTimer;
  GameModel gameModel;
  long currentTimeLeft = 0;

  // Subviews of the game
  List<IGameView> views;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_game);

    // Register all IGameViews
    views = new ArrayList<IGameView>();
    views.add((HeaderView) this.findViewById(R.id.gameactivity_fragment_header));
    views.add((BoardView) this.findViewById(R.id.gameactivity_fragment_boardview));
    views.add((QuestionAnswerPagerView) this.findViewById(
        R.id.gameactivity_fragment_questionanswerpager));
    views.add((BoardLoadingView) this.findViewById(R.id.gameactivity_fragment_boardloadingview));

    // Register game manager with views
    for (IGameView view : views) {
      view.setGameManager(this);
      view.setFragmentManager(this.getSupportFragmentManager());
    }

    goToGameState(GameStateType.GENERATING);

    gameModel = (GameModel) getIntent().getSerializableExtra(GameModel.TAG);
    board = GameModelAdapter.adapt(gameModel);

    bindSimulationEvents();

    goToGameState(GameStateType.PLAYING);
    countdownTimer.start();

    for (IGameView view : views) {
      view.onGameModelLoaded(board);
    }
  }

  private void bindSimulationEvents() {
    countdownTimer = new CountDownTimerPausable(board.getTotalTime(), 1000) {
      @SuppressLint("DefaultLocale")
      @Override
      public void onTick(long millisUntilFinished) {

        currentTimeLeft = millisUntilFinished;
        board.setRemainingTime(millisUntilFinished);

        runOnUiThread(new Runnable() {

          @Override
          public void run() {
            // Register game manager with views
            for (IGameView view : views) {
              view.onTick();
            }
          }
        });
      }

      @Override
      public void onFinish() {
        runOnUiThread(new Runnable() {

          @Override
          public void run() {
            // since the time ran out assign the time left to zero
            currentTimeLeft = 0;
            // change the game state to finished
            goToGameState(GameStateType.FINISHED);
          }
        });
      }
    };
  }

  /**
   * Got to the game state specified
   */
  @Override
  public void goToGameState(GameStateType stateType) {
    switch (stateType) {
      case PLAYING:
        if (countdownTimer.isPaused()) {
          countdownTimer.start();
        }
        break;
      case FINISHED:
        navigateToSummary();
        break;
      case GENERATING:
        break;
      case QUESTION_TRANSITIONING:
        countdownTimer.pause();
        break;
      case PAUSED:
        break;
    }

    gameState = stateType;

    for (IGameView gameView : this.views) {
      gameView.onGameManagerStateChange(this.gameState);
    }
  }

  private void navigateToSummary() {

    // cancel the timer
    countdownTimer.cancel();

    // assign the time left
    gameModel.getCurrentPlayer().setTimeLeft(currentTimeLeft);

    // navigate to the summary
    Intent intent = new Intent(GameActivity.this, GameSummaryActivity.class);
    intent.putExtra(GameModel.TAG, gameModel);
    startActivityForResult(intent, RequestCodes.REQUEST_GAME_RESULT);
  }

  /**
   * Gets the current game state
   */
  @Override
  public GameStateType getState() {
    return this.gameState;
  }

  /**
   * Get the board renderer model
   */
  @Override
  public BoardRenderModel getBoardRenderModel() {
    return board;
  }

  /**
   * Returns true if the board renderer model has loaded
   */
  @Override
  public Boolean isBoardRenderModelLoaded() {
    return board != null;
  }

  /**
   * Occurs when a question is skipped. Updates UI appropriately such as the current question etc.
   */
  @Override
  public void onQuestionSkipped() {

    if (board.getAllQuestionsCompleted()) {
      this.goToGameState(GameStateType.FINISHED);
    } else {
      board.setCurrentWordIndex(board.getIndexOfNextUnansweredQuestion());
    }

    board.getGridLayout().clearSelectedLetters();

    for (IGameView view : views) {
      view.onQuestionSkipped();
    }
  }

  /**
   * Occurs when a letter is selected. Updates the UI based upon the connectedness of the letter
   * selected
   */
  @Override
  public void onLetterSelected(Location letterLocation) {

    if (!board.getGridLayout().validateConnectedness()) {
      board.getGridLayout().clearSelectedLetters();
      for (IGameView view : views) {
        view.updateUI();
        view.onLetterReset();
      }

    }

    if (!board.getGridLayout().getBoard().get(letterLocation).getIsSelected()) {
      board.getGridLayout().toggleLetterSelection(letterLocation);

      for (IGameView view : views) {
        view.updateUI();
        view.onLetterAdded();
      }
    }
  }

  /**
   * Updates the UI after checking if the selected letters have correctly spelled the word
   */
  @Override
  public void simulate() {
    // check for completion
    if (board.isCurrentQuestionAnsweredBySelection()) {
      onQuestionAnswered();
    } else {
      board.getGridLayout().clearSelectedLetters();
      for (IGameView view : views) {
        view.updateUI();
        view.onLetterReset();
      }
    }
  }

  /**
   * Occurs when the game surface has been touched
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (this.gameState == GameStateType.PLAYING) {
      float x = event.getRawX();
      float y = event.getRawY();
      switch (event.getAction()) {
        case MotionEvent.ACTION_MOVE:
          for (IGameView gameView : this.views) {
            gameView.onTouchMove((int) x, (int) y);
          }
          break;
        case MotionEvent.ACTION_UP:
          simulate();
          break;
      }
    }
    return false;
  }

  /**
   * Updates the UI appropriately when a question has been answered correctly
   */
  @Override
  public void onQuestionAnswered() {
    board.getWords().get(board.getCurrentQuestionIndex()).setCompleted(true);
    gameModel.getCurrentPlayer().addAnswerIndex(board.getCurrentQuestionIndex());

    // advance to next available question
    if (board.getAllQuestionsCompleted()) {
      goToGameState(GameStateType.FINISHED);
    } else {
      goToGameState(GameStateType.QUESTION_TRANSITIONING);

      // clear letters
      board.getGridLayout().clearSelectedLetters();

      board.setCurrentWordIndex(board.getIndexOfNextUnansweredQuestion());
      for (IGameView view : views) {
        view.onQuestionAnswered();
      }

      Handler h = new Handler();
      h.postDelayed(new Runnable() {
        @Override
        public void run() {
          for (IGameView view : views) {
            view.updateUI();
          }
          goToGameState(GameStateType.PLAYING);
        }
      }, 1000);
    }
  }

  /**
   * Cancel the timer
   */
  @Override
  public void onBackPressed() {

    if (countdownTimer != null) {
      countdownTimer.cancel();
    }

    super.onBackPressed();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case RequestCodes.REQUEST_GAME_RESULT:
        setResult(resultCode, data);
        finish();
        break;
    }
  }

  /**
   * Marks the application as not playing a game
   */
  @Override
  protected void onPause() {
    super.onPause();
    ((GameApplication) getApplication()).setIsCurrentlyPlayingGame(false);
  }

  /**
   * Marks the application as currently playing a game
   */
  @Override
  protected void onResume() {
    super.onResume();
    ((GameApplication) getApplication()).setIsCurrentlyPlayingGame(true);
  }
}
