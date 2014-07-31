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
 * Represents a letter on the game board
 */
public class LetterRenderModel {
  private Character glyph;
  private Boolean isSelected = false;

  /**
   * Gets the letter
   *
   * @return {@link Character}
   */
  public Character getGlyph() {
    return glyph;
  }

  /**
   * Set the letter
   *
   * @param glyph The letter
   */
  public void setGlyph(Character glyph) {
    this.glyph = glyph;
  }

  /**
   * Returns <code>true</code> if the letter is selected
   *
   * @return {@link Boolean}
   */
  public Boolean getIsSelected() {
    return isSelected;
  }

  /**
   * Sets the letter's selection
   *
   * @param isSelected <code>true</code> if the letter should be selected
   */
  public void setIsSelected(Boolean isSelected) {
    this.isSelected = isSelected;
  }
}
