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
package com.google.cloud.solutions.griddler.android.ui.game.models;

/**
 * Represents the question/clue
 */
public class QuestionRenderModel {
  private String question;
  private String word;
  private boolean completed = false;

  /**
   * Returns the question/clue
   *
   * @return {@link String}
   */
  public String getQuestion() {
    return question;
  }

  /**
   * Set the question
   *
   * @param question The question
   */
  public void setQuestion(String question) {
    this.question = question;
  }

  /**
   * Returns <code>true</code> if the question has been completed
   */
  public boolean getCompleted() {
    return completed;
  }

  /**
   * Set the question as completed
   *
   * @param completed <code>true</code> if completed
   */
  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  /**
   * Returns the word/answer to the question
   *
   * @return {@link String}
   */
  public String getWord() {
    return word;
  }

  /**
   * Set the word/answer
   *
   * @param word The word
   */
  public void setWord(String word) {
    this.word = word;
  }
}
