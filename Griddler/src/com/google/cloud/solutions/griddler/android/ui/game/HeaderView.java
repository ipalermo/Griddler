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

import com.google.cloud.solutions.griddler.android.R;
import com.google.cloud.solutions.griddler.android.ui.game.models.BoardRenderModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * The purpose of this class is to display the pagination, time remaining text and the time
 * remaining view
 */
public class HeaderView extends LinearLayout implements IGameView {

  IGameManager gameManager;
  TextView textViewPagination;
  TextView textViewRemainingTextTime;
  CountdownView countdownView;

  /**
   * Constructor
   *
   * @param context The context
   */
  public HeaderView(Context context) {
    super(context);
    this.setWillNotDraw(false);
    this.init(context);
  }

  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   */
  public HeaderView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.setWillNotDraw(false);
    this.init(context);
  }

  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   * @param defStyle
   */
  public HeaderView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.setWillNotDraw(false);
    this.init(context);
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.fragment_game_header, this);
    countdownView = (CountdownView) this.findViewById(R.id.countdownView);
    textViewPagination = (TextView) this.findViewById(R.id.textPagination);
    textViewRemainingTextTime = (TextView) this.findViewById(R.id.textCountdown);

    if (gameManager != null) {
      countdownView.setGameManager(gameManager);
    }
  }

  /**
   * Sets the game manager
   */
  @Override
  public void setGameManager(IGameManager gameManager) {
    this.gameManager = gameManager;
    if (countdownView != null) {
      countdownView.setGameManager(gameManager);
    }
  }

  /**
   * Reacts to the game manager state change by updating the visibility of the pagination view
   */
  @SuppressWarnings("incomplete-switch")
  @Override
  public void onGameManagerStateChange(GameStateType gameState) {
    switch (gameState) {
      case PLAYING:
        if (textViewPagination != null) {
          textViewPagination.setVisibility(View.VISIBLE);
        }
        setPagination();
        break;
      case FINISHED:
        if (textViewPagination != null) {
          textViewPagination.setVisibility(View.INVISIBLE);
        }
        break;
      case GENERATING:
        if (textViewPagination != null) {
          textViewPagination.setVisibility(View.INVISIBLE);
        }
        break;
      case PAUSED:
        break;
    }

    this.invalidate();
  }

  @Override
  public void onGameModelLoaded(BoardRenderModel boardRenderModel) {}

  /**
   * Sets the remaining time
   */
  @Override
  public void onTick() {
    setRemainingTime();
  }

  @Override
  public void setFragmentManager(FragmentManager fragmentManager) {}

  @Override
  public void onTouchMove(int x, int y) {}

  /**
   * Updates the pagination
   */
  @Override
  public void onQuestionAnswered() {
    setPagination();
  }

  /**
   * Updates the pagination
   */
  @Override
  public void onQuestionSkipped() {
    setPagination();
  }

  private void setPagination() {

    if (gameManager != null && gameManager.getBoardRenderModel() != null
        && textViewPagination != null) {

      int questionNumber = gameManager.getBoardRenderModel().getCurrentQuestionIndex() + 1;
      int questionCount = gameManager.getBoardRenderModel().getQuestionCount();

      textViewPagination.setText(String.format("%d of %d", questionNumber, questionCount));
    }

  }

  @SuppressLint("DefaultLocale")
  private void setRemainingTime() {

    if (gameManager != null && gameManager.getBoardRenderModel() != null
        && textViewRemainingTextTime != null) {

      long milliseconds = gameManager.getBoardRenderModel().getRemainingTimeMilliseconds();

      long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
      long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
          - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));

      String minutesText = String.valueOf(minutes);
      String secondsText = String.valueOf(seconds);

      if (seconds < 10) {
        secondsText = String.format("0%d", seconds);
      }


      String timeRemaining = String.format("%s:%s", minutesText, secondsText);

      textViewRemainingTextTime.setText(timeRemaining);
      textViewRemainingTextTime.invalidate();
    }
  }

  @Override
  public void updateUI() {}

  @Override
  public void onLetterAdded() {}

  @Override
  public void onLetterReset() {}
}
