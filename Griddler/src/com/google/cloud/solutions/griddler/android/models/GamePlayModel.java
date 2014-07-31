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
import java.util.ArrayList;
import java.util.List;

/**
 * The purpose of this class is to provide a layer of abstraction between the service generated
 * classes for a game's player and the application
 */
@SuppressWarnings("serial")
public class GamePlayModel implements Serializable {
  private Long playerId;
  private String nickName;
  private Long timeLeft;
  private List<Integer> answerIndexes;
  private Boolean isWinner;

  /**
   * Constructor
   *
   * @param playerId The player's ID
   * @param nickName The players Nickname
   * @param timeLeft The amount of time left in milliseconds when the player completed the game
   * @param answerIndexes A list of {@link Integer} indexes specifiying what answers the player had
   * @param isWinner The flag to determine if this player was the winner
   */
  public GamePlayModel(Long playerId, String nickName, Long timeLeft, List<Integer> answerIndexes,
      Boolean isWinner) {
    this.playerId = playerId;
    this.nickName = nickName;
    this.timeLeft = timeLeft;
    this.answerIndexes = answerIndexes == null ? new ArrayList<Integer>() : answerIndexes;
    this.isWinner = isWinner;
  }

  /**
   * Get the player's ID
   *
   * @return {@link Long}
   */
  public Long getPlayerId() {
    return playerId;
  }

  /**
   * Get the player's nickname
   *
   * @return {@link String}
   */
  public String getNickName() {
    return nickName;
  }

  /**
   * Get whether or not the player is the winner
   *
   * @return {@link Boolean}
   */
  public Boolean getIsWinner() {
    return isWinner;
  }

  /**
   * Get the amount of time left in milliseconds when the player completed the game
   *
   * @return {@link Long}
   */
  public Long getTimeLeft() {
    return timeLeft;
  }

  /**
   * Set the time left in milliseconds
   *
   * @param timeLeft Time in milliseconds
   */
  public void setTimeLeft(Long timeLeft) {
    this.timeLeft = timeLeft;
  }

  /**
   * Get the list of indexes that the player answered
   *
   * @return {@link List}&lt;{@link Integer}&gt;
   */
  public List<Integer> getAnswerIndexes() {
    return answerIndexes;
  }

  /**
   * Add the index to the list of answers. If it already exists it isn't added.
   *
   * @param index The index to add
   */
  public void addAnswerIndex(Integer index) {

    if (answerIndexes == null) {
      answerIndexes = new ArrayList<Integer>();
    }

    if (!answerIndexes.contains(index)) {
      answerIndexes.add(index);
    }
  }
}
