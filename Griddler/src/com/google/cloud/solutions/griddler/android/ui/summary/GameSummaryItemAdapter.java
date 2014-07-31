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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * the purpose of this adapter is to manage the list of words and format the view based upon whether
 * or not the player guessed them correctly
 */
public class GameSummaryItemAdapter extends ArrayAdapter<WordGuessResult> {
  private final Context context;
  private int layoutResourceId;
  private final WordGuessResult[] values;

  /**
   * Constructor
   *
   * @param context The context
   * @param layoutResourceId The resource ID
   * @param values The list of values
   */
  public GameSummaryItemAdapter(Context context, int layoutResourceId, WordGuessResult[] values) {
    super(context, layoutResourceId, values);
    this.layoutResourceId = layoutResourceId;
    this.context = context;
    this.values = values;
  }

  /**
   * Formats the view based upon the answer being evaluated
   */
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(layoutResourceId, parent, false);
    TextView textView = (TextView) rowView.findViewById(R.id.wordTextView);
    ImageView imageView1 = (ImageView) rowView.findViewById(R.id.imageView1);

    WordGuessResult item = values[position];

    textView.setText(item.word);
    if (item.player1GuessedCorrect) {
      imageView1.setVisibility(View.VISIBLE);
    } else {
      imageView1.setVisibility(View.GONE);
    }

    ImageView imageView2 = (ImageView) rowView.findViewById(R.id.imageView2);
    if (imageView2 != null) {
      if (item.player2GuessedCorrect) {
        imageView2.setVisibility(View.VISIBLE);
      } else {
        imageView2.setVisibility(View.GONE);
      }
    }

    return rowView;
  }
}
