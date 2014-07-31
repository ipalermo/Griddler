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

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Provides the user with feedback that processing is occurring with a spinner while the game board
 * is loading
 */
public class BoardLoadingView extends LinearLayout implements IGameView {

  /**
   * Constructor
   *
   * @param context The context
   */
  public BoardLoadingView(Context context) {
    super(context);
    this.setWillNotDraw(false);
    init(context);
  }

  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   */
  public BoardLoadingView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.setWillNotDraw(false);
    init(context);
  }

  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   * @param defStyle
   */
  public BoardLoadingView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.setWillNotDraw(false);
    init(context);
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.fragment_game_boardloading, this);
  }

  @Override
  public void setGameManager(IGameManager gameManager) {}

  @Override
  public void setFragmentManager(FragmentManager fragmentManager) {}

  /**
   * Sets the visibility to INVISIBLE when the game state changes to PLAYING
   */
  @SuppressWarnings("incomplete-switch")
  @Override
  public void onGameManagerStateChange(GameStateType gameState) {
    switch (gameState) {
      case PLAYING:
        this.setVisibility(View.INVISIBLE);
        break;
    }

  }

  @Override
  public void onGameModelLoaded(BoardRenderModel boardRenderModel) {}

  @Override
  public void onTick() {}

  @Override
  public void onTouchMove(int x, int y) {}

  @Override
  public void onQuestionAnswered() {}

  @Override
  public void onQuestionSkipped() {}

  @Override
  public void updateUI() {}

  @Override
  public void onLetterAdded() {}

  @Override
  public void onLetterReset() {}
}
