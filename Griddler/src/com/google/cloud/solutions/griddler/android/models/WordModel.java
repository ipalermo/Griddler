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
 * Defines an answer for a question in the game
 */
@SuppressWarnings("serial")
public class WordModel implements Serializable {
  private List<LetterModel> letters;

  /**
   * Constructor
   *
   * @param letters The list of letters that make up the word
   */
  public WordModel(List<LetterModel> letters) {
    this.letters = letters;
  }

  /**
   * Get the list of letters
   *
   * @return {@link List}&lt;{@link LetterModel}&gt;
   */
  public List<LetterModel> getLetters() {
    return this.letters;
  }

  /**
   * Get the {@link String} representation of the word/answer
   *
   * @return {@link String}
   */
  @Override
  public String toString() {

    StringBuilder wordString = new StringBuilder();

    if (letters != null) {
      for (LetterModel letter : letters) {
        wordString.append(letter.getLetter());
      }
    }

    return wordString.toString();
  }
}
