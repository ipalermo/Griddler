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
package com.google.cloud.solutions.griddler.android.ui.summary;

import android.annotation.SuppressLint;

/**
 * This class serves as a model for a multiplayer game's line item detailing each word and whether
 * or not the players answered the question correctly
 */
@SuppressLint("DefaultLocale")
public class WordGuessResult {

  /**
   * The word/answer
   */
  public String word;

  /**
   * <code>true</code> if the current player guessed correctly
   */
  public Boolean player1GuessedCorrect = false;

  /**
   * <code>true</code> if the opponent guessed correctly
   */
  public Boolean player2GuessedCorrect = false;

  /**
   * Constructor
   */
  public WordGuessResult() {}

  /**
   * Constructor
   *
   * @param word The word
   * @param player1Guessed <code>true</code> if the current player guessed correctly
   * @param player2Guessed <code>true</code> if the opponent guessed correctly
   */
  public WordGuessResult(String word, Boolean player1Guessed, Boolean player2Guessed) {
    this.word = word.toUpperCase();
    this.player1GuessedCorrect = player1Guessed;
    this.player2GuessedCorrect = player2Guessed;
  }
}
