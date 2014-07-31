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

/**
 * The purpose of this class is to provide a layer of abstraction between the service generated
 * classes for a player and the application
 */
public class PlayerModel {
  private Long playerId;
  private String nickName;

  /**
   * Constructor
   *
   * @param playerId The ID of the player
   * @param nickName The nickname of the player
   */
  public PlayerModel(Long playerId, String nickName) {
    this.playerId = playerId;
    this.nickName = nickName;
  }

  /**
   * Get the player ID
   *
   * @return {@link Long}
   */
  public Long getPlayerId() {
    return playerId;
  }

  /**
   * Get the nickname
   *
   * @return {@link String}
   */
  public String getNickName() {
    return nickName;
  }
}
