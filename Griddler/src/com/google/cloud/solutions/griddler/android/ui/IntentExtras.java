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

package com.google.cloud.solutions.griddler.android.ui;

/**
 * The purpose of this class is to manage all intent extras
 */
public class IntentExtras {

  /**
   * Intent extra that is sent when the user has won the game
   */
  public static final String SUMMARY_WON_GAME = "wonGame";

  /**
   * Intent extra that is sent when the application failed to submit the answers
   */
  public static final String SUMMARY_FAILED_TO_SUBMIT_ANSWERS = "failedToSubmitAnswers";

  /**
   * Intent extra name indicating that the user switched accounts
   */
  public static final String SETTINGS_SWITCHED_ACCOUNT = "switchedAccount";

  /**
   * Intent extra name indicating that the user enabled Google+
   */
  public static final String SETTINGS_ENABLED_GOOGLE_PLUS = "enableGooglePlus";

  /**
   * Single Player Game Type
   */
  public static final int LOBBY_SINGLE_PLAYER = 1;

  /**
   * Multiplayer Game Type
   */
  public static final int LOBBY_MULTI_PLAYER = 2;

  /**
   * Game type intent extra
   */
  public static final String LOBBY_GAME_TYPE = "GameType";

  /**
   * Game ID intent extra
   */
  public static final String LOBBY_GAME_ID = "gameId";
}
