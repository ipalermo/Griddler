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
package com.google.cloud.solutions.griddler.android.ui.game;

import com.google.cloud.solutions.griddler.android.R;
import com.google.cloud.solutions.griddler.android.helpers.Location;
import com.google.cloud.solutions.griddler.android.ui.game.models.LetterRenderModel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Represents a selected letter that is displayed while spelling a word
 */
public class QuestionAnswerLetter extends LinearLayout {
  TextView letterTextView;
  IGameManager gameManager;

  /**
   * Constructor
   *
   * @param context The context
   */
  public QuestionAnswerLetter(Context context) {
    super(context);
    this.setWillNotDraw(false);
    init(context);
  }

  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   */
  public QuestionAnswerLetter(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.setWillNotDraw(false);
    init(context);
  }

  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   * @param defStyle
   */
  public QuestionAnswerLetter(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.setWillNotDraw(false);
    init(context);
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.fragment_game_questionanswer_letter, this);
    this.letterTextView =
        (TextView) this.findViewById(R.id.gameactivity_fragment_questionanswer_letter);
  }

  /**
   * Set the game manager
   *
   * @param gameManager An instance of {@link IGameManager}
   */
  public void setGameManager(IGameManager gameManager) {
    this.gameManager = gameManager;
  }

  /**
   * Sets the letter
   *
   * @param letterLocation The location of the letter
   */
  public void setLocation(Location letterLocation) {
    LetterRenderModel letter =
        this.gameManager.getBoardRenderModel().getGridLayout().getBoard().get(letterLocation);
    this.letterTextView.setText(letter.getGlyph().toString().toUpperCase());
  }
}
