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
import java.util.List;

/**
 * The purpose of this class is to provide a layer of abstraction between the service generated
 * classes for a game board and the application
 */
@SuppressWarnings("serial")
public class BoardModel implements Serializable {
  private List<List<Character>> boardDefinition;
  private List<QuestionModel> questions;
  private Long allottedTime;

  /**
   * Get the list of questions
   *
   * @return {@link List}&lt;{@link QuestionModel}&gt;
   */
  public List<QuestionModel> getQuestions() {
    return questions;
  }

  /**
   * Get the allotted time the player has to solve the entire game board
   *
   * @return {@link Long}
   */
  public Long getAllottedTime() {
    return allottedTime;
  }

  /**
   * Get the board definition
   *
   * @return {@link List}&lt;{@link List}&lt;{@link Character}&gt;&gt;
   */
  public List<List<Character>> getBoardDefinition() {
    return boardDefinition;
  }

  /**
   * Constructor
   *
   * @param boardDefinition The board definition
   * @param questions The questions
   * @param allottedTime The allotted time to finish the game
   */
  public BoardModel(
      List<List<Character>> boardDefinition, List<QuestionModel> questions, Long allottedTime) {
    this.questions = questions;
    this.allottedTime = allottedTime;
    this.boardDefinition = boardDefinition;
  }
}
