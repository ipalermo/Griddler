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

import com.google.cloud.solutions.griddler.android.R;
import com.google.cloud.solutions.griddler.android.models.GameModel;
import com.google.cloud.solutions.griddler.android.models.QuestionModel;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * The purpose of this fragment is to manage the overall layout of the activity such as the header
 * and the list of answers
 */
public class SummaryListFragment extends ListFragment {

  private ListHeaderView listViewHeader;
  private GameSummaryItemAdapter listViewAdapter;
  private GameModel gameModel;
  private OnSummaryListFragmentInteractionListener mListener;


  /**
   * Constructor
   */
  public SummaryListFragment() {}

  /**
   * Hydrate the fragment with the game model
   *
   * @param model An instance of {@link GameModel}
   */
  public void hydrateState(GameModel model) {
    gameModel = model;
    this.buildLayout();
  }

  /**
   * Inflate the summary list
   */
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_summary_list, container, false);

    return view;
  }

  private void buildLayout() {

    ListView listView = (ListView) getActivity().findViewById(android.R.id.list);

    listViewHeader = new ListHeaderView(SummaryListFragment.this.getActivity(), null);

    listView.addHeaderView(listViewHeader);

    Button homeButton = (Button) listViewHeader.findViewById(R.id.homeButton);
    homeButton.setOnClickListener(homeOnClickListener);

    TextView gameResult = (TextView) listViewHeader.findViewById(R.id.gameResultText);
    TextView gameOverText = (TextView) listViewHeader.findViewById(R.id.gameOverText);

    String playerName = getString(R.string.summary_header_you);
    int playerCorrectGuesses = gameModel.getCurrentPlayer().getAnswerIndexes().size();

    List<QuestionModel> questions = gameModel.getBoard().getQuestions();
    List<WordGuessResult> listItems = new ArrayList<WordGuessResult>();

    for (QuestionModel question : questions) {
      listItems.add(new WordGuessResult(question.getWord().toString(), false, false));
    }

    List<Integer> playerCorrectIndexes = gameModel.getCurrentPlayer().getAnswerIndexes();

    for (Integer index : playerCorrectIndexes) {
      listItems.get(index).player1GuessedCorrect = true;
    }

    if (playerCorrectIndexes.size() == questions.size()) {
      gameOverText.setText(getString(R.string.summary_perfect));
    }

    int adapterLayout = R.layout.list_item_game_summary_single;

    gameResult.setText(getString(
        R.string.num_questions_correct, Integer.toString(playerCorrectIndexes.size()),
        Integer.toString(questions.size())));

    if (gameModel.getOpponent() != null) {

      List<Integer> opponentCorrectIndexes = gameModel.getOpponent().getAnswerIndexes();

      String opponentName = gameModel.getOpponent().getNickName();
      int opponentCorrectGuesses = gameModel.getOpponent().getAnswerIndexes().size();

      for (Integer index : opponentCorrectIndexes) {
        listItems.get(index).player2GuessedCorrect = true;
      }

      if (gameModel.getCurrentPlayer().getIsWinner()) {

        if (playerCorrectGuesses == opponentCorrectGuesses) {
          gameResult.setText(getString(R.string.summary_you_finished_before, opponentName));
          gameOverText.setText(getString(R.string.you_won));
        } else {
          gameResult.setText(getString(R.string.someone_beat_someone, playerName, opponentName,
              Integer.toString(playerCorrectGuesses), Integer.toString(opponentCorrectGuesses)));
        }
      } else if (gameModel.getOpponent().getIsWinner()) {

        if (playerCorrectGuesses == opponentCorrectGuesses) {
          gameResult.setText(getString(R.string.summary_finished_before_you, opponentName));
          gameOverText.setText(getString(R.string.you_lost));
        } else {
          gameResult.setText(getString(R.string.someone_beat_someone, opponentName, playerName,
              Integer.toString(opponentCorrectGuesses), Integer.toString(playerCorrectGuesses)));
        }
      } else {
        // tie
        gameResult.setText(getString(R.string.tie));
      }

      listViewHeader.setLeftHeaderText(playerName);
      listViewHeader.setRightHeaderText(opponentName);
      adapterLayout = R.layout.list_item_game_summary_double;
    }

    WordGuessResult[] itemArray = listItems.toArray(new WordGuessResult[] {});
    listViewAdapter = new GameSummaryItemAdapter(
        SummaryListFragment.this.getActivity(), adapterLayout, itemArray);

    setListAdapter(listViewAdapter);
  }

  private OnClickListener homeOnClickListener = new OnClickListener() {
    @Override
    public void onClick(View v) {
      SummaryListFragment.this.getActivity().finish();
    }
  };

  /**
   * Assign the listener to the activity
   */
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnSummaryListFragmentInteractionListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(
          activity.toString() + " must implement OnSummaryListFragmentInteractionListener");
    }
  }

  /**
   * Assign null to the listener
   */
  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  /**
   * Notify the listener of fragment interaction
   */
  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);

    if (null != mListener) {
      // Notify the active callbacks
      mListener.onFragmentInteraction();
    }
  }


  /**
   * Interface used to inform the activity of any fragment interaction
   */
  public interface OnSummaryListFragmentInteractionListener {
    public void onFragmentInteraction();
  }
}
