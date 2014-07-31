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
package com.google.cloud.solutions.griddler.android.models;

import java.io.Serializable;


/**
 * The purpose of this class is to provide a layer of abstraction between the service generated
 * classes for a game and the application
 */
@SuppressWarnings("serial")
public class GameModel implements Serializable {

  public static final String TAG = GameModel.class.getName();

  private Long gameId;
  private BoardModel board;
  private GamePlayModel currentPlayer;
  private GamePlayModel opponent;
  private Boolean areBothPlayersFinished = false;

  /**
   * Constructor
   *
   * @param gameId The game ID
   * @param board The board definition
   * @param currentPlayer The current player
   * @param opponent The opponent
   * @param areBothPlayersFinished Flag to specify if both players are finished
   */
  public GameModel(Long gameId, BoardModel board, GamePlayModel currentPlayer,
      GamePlayModel opponent, Boolean areBothPlayersFinished) {
    this.gameId = gameId;
    this.board = board;

    this.opponent = opponent;
    this.currentPlayer = currentPlayer;
    this.areBothPlayersFinished = areBothPlayersFinished;
  }

  /**
   * Get the game ID
   *
   * @return {@link Long}
   */
  public Long getGameId() {
    return gameId;
  }

  /**
   * Get the board model
   *
   * @return {@link BoardModel}
   */
  public BoardModel getBoard() {
    return board;
  }

  /**
   * Get the current player
   *
   * @return {@link GamePlayModel}
   */
  public GamePlayModel getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * Get the opponent
   *
   * @return {@link GamePlayModel}
   */
  public GamePlayModel getOpponent() {
    return opponent;
  }

  /**
   * Get if both players are finished
   *
   * @return {@link Boolean}
   */
  public Boolean getAreBothPlayersFinished() {
    return areBothPlayersFinished;
  }
}
