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

import com.google.cloud.solutions.griddler.android.ui.game.models.BoardRenderModel;

import android.support.v4.app.FragmentManager;

/**
 * Represents a Game view that receives game manager updates
 */
public interface IGameView {

  /**
   * Inject reference of the owner Game Manager
   *
   * @param gameManager
   */
  void setGameManager(IGameManager gameManager);

  /**
   * Used to communicate the game activity fragment context into sub-views
   *
   * @param fragmentManager
   */
  void setFragmentManager(FragmentManager fragmentManager);

  /**
   * Called when the GameManager GameState changes
   *
   * @param gameState
   */
  void onGameManagerStateChange(GameStateType gameState);

  /**
   * Called after a persisted game model is loaded and initialized in the game architecture
   *
   * @param boardRenderModel
   */
  void onGameModelLoaded(BoardRenderModel boardRenderModel);

  /**
   * Called when a question is answered
   */
  void onQuestionAnswered();

  /**
   * Called with a question is skipped
   */
  void onQuestionSkipped();

  /**
   * Called when the game engine completes one cycle of simulation
   */
  void onTick();

  /**
   * Called when a letter is either classified or unclassified
   */
  void onLetterAdded();

  /**
   * Called when the letters are reset on the gameboard
   */
  void onLetterReset();

  /**
   * Called when the game engine is ready to notify of a touch move event by the user
   *
   * @param x
   * @param y
   */
  void onTouchMove(int x, int y);

  /**
   * Called by the game engine after a similuation cycle is ready for rendition
   */
  void updateUI();
}
