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

import com.google.cloud.solutions.griddler.android.helpers.Location;
import com.google.cloud.solutions.griddler.android.ui.game.models.BoardRenderModel;

import android.view.MotionEvent;

/**
 * Represents an architectural api for the game engine
 */
public interface IGameManager {

  /**
   * The current state of the game engine
   */
  GameStateType getState();

  /**
   * The render model for the game engines
   */
  BoardRenderModel getBoardRenderModel();

  /**
   * Returns true if the render model has been initialized in the engine Generally represents if the
   * game is ready for playback
   */
  Boolean isBoardRenderModelLoaded();

  /**
   * Api for transitioning the game engine into a particular GameState
   *
   * @param gameState
   */
  void goToGameState(GameStateType gameState);

  /**
   * Called when a question is skipped by the user
   */
  void onQuestionSkipped();

  /**
   * Called when a letter is selected by the user
   *
   * @param letterRenderModel
   */
  void onLetterSelected(Location letterRenderModel);

  /**
   * Called when a question is answered by the user
   */
  void onQuestionAnswered();

  /**
   * Notifies the game engine that a touch event has occurred
   *
   * @param event
   */
  boolean onTouchEvent(MotionEvent event);

  /**
   * Forced the game engine to simulate on the current render model This typically implies that a
   * client needs the engine to update based on the current timestamp
   */
  void simulate();
}
