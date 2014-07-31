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

import com.google.cloud.solutions.griddler.android.helpers.BoundedGrid;
import com.google.cloud.solutions.griddler.android.helpers.Location;
import com.google.cloud.solutions.griddler.android.models.GameModel;
import com.google.cloud.solutions.griddler.android.models.QuestionModel;
import com.google.cloud.solutions.griddler.android.ui.game.models.BoardRenderModel;
import com.google.cloud.solutions.griddler.android.ui.game.models.GridLayoutRenderModel;
import com.google.cloud.solutions.griddler.android.ui.game.models.LetterRenderModel;
import com.google.cloud.solutions.griddler.android.ui.game.models.QuestionRenderModel;

import java.util.List;

/**
 * Represents an adapter to be used to transform a GameModel into Render models for the GameActivity
 */
public class GameModelAdapter {

  /**
   * Project the {@link GameModel} data into a {@link BoardRenderModel}
   *
   * @param gameModel The game model
   * @return {@link BoardRenderModel}
   */
  public static BoardRenderModel adapt(GameModel gameModel) {
    BoardRenderModel result = new BoardRenderModel();

    result.setTotalTime(gameModel.getBoard().getAllottedTime());

    for (QuestionModel question : gameModel.getBoard().getQuestions()) {
      QuestionRenderModel wrm = new QuestionRenderModel();
      wrm.setQuestion(question.getQuestion());
      wrm.setWord(question.getWord().toString());
      result.getWords().add(wrm);
    }

    GridLayoutRenderModel layoutModel = new GridLayoutRenderModel();
    result.setGridLayout(layoutModel);

    List<List<Character>> boardDefinition = gameModel.getBoard().getBoardDefinition();
    BoundedGrid<LetterRenderModel> boardRenderModel =
        new BoundedGrid<LetterRenderModel>(boardDefinition.size(), boardDefinition.size());
    layoutModel.setBoard(boardRenderModel);

    for (int r = 0; r <= boardDefinition.size() - 1; r++) {
      for (int c = 0; c <= boardDefinition.size() - 1; c++) {
        Location loc = new Location(r, c);
        LetterRenderModel letterRenderModel = new LetterRenderModel();
        letterRenderModel.setGlyph(boardDefinition.get(r).get(c));
        boardRenderModel.put(loc, letterRenderModel);
      }
    }

    return result;
  }
}
