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
import com.google.cloud.solutions.griddler.android.ui.game.models.QuestionRenderModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Represents a string of letters that the user has selected to spell a word
 */
public class QuestionAnswerView extends Fragment {
  private static final String LOG_TAG = QuestionAnswerView.class.getSimpleName();
  private QuestionRenderModel wordRenderModel;
  TextView textViewSkip;

  IGameManager gameManager;

  /**
   * Initialize the answer layout and animations as well as setting up the skip button click event
   */
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d(LOG_TAG, "onCreateView");

    final ViewGroup rootView =
        (ViewGroup) inflater.inflate(R.layout.fragment_game_questionanswer, container, false);

    textViewSkip = (TextView) rootView.findViewById(R.id.textViewSkip);

    textViewSkip.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        gameManager.onQuestionSkipped();
        rootView.invalidate();
      }
    });

    if (wordRenderModel != null) {
      TextView questionText = (TextView) rootView.findViewById(R.id.questionText);
      questionText.setText(wordRenderModel.getQuestion());
    }

    return rootView;
  }

  /**
   * Get the word model
   *
   */
  public QuestionRenderModel getWordRenderModel() {
    return wordRenderModel;
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
   * Set the word model
   *
   * @param wordRenderModel
   */
  public void setWordRenderModel(QuestionRenderModel wordRenderModel) {
    this.wordRenderModel = wordRenderModel;
  }
 }
