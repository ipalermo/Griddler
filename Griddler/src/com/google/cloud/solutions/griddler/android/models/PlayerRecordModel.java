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
 * classes for a player's record and the application
 */
@SuppressWarnings("serial")
public class PlayerRecordModel implements Serializable {

  public static final String TAG = PlayerRecordModel.class.getSimpleName();

  private String playerName;
  private Integer gamesWon;
  private Integer gamesPlayed;

  /**
   * Constructor
   *
   * @param playerName The player's name/nickname
   * @param gamesWon The number of multiplayer games won
   * @param gamesPlayed The number of multiplayer games played
   */
  public PlayerRecordModel(String playerName, Integer gamesWon, Integer gamesPlayed) {
    this.playerName = playerName;
    this.gamesWon = gamesWon;
    this.gamesPlayed = gamesPlayed;
  }

  /**
   * Get the player's name/nickname
   *
   * @return {@link String}
   */
  public String getPlayerName() {
    return this.playerName;
  }

  /**
   * Get the number of multiplayer games won
   *
   * @return {@link Integer}
   */
  public Integer getGamesWon() {
    return this.gamesWon;
  }

  /**
   * Get the number of multiplayer games played
   *
   * @return {@link Integer}
   */
  public Integer getGamesPlayed() {
    return this.gamesPlayed;
  }

  /**
   * Provides the ability to increment the players number of games played and games won within the
   * application. In memory only
   *
   * @param won {@link Boolean}
   */
  public void playedGame(Boolean won) {
    if (won != null) {

      if (gamesPlayed == null) {
        gamesPlayed = 0;
      }

      if (gamesWon == null) {
        gamesWon = 0;
      }

      this.gamesPlayed++;

      if (won) {
        gamesWon++;
      }
    }
  }
}
