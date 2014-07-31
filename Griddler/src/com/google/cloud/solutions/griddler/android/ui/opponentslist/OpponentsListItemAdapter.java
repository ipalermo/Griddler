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
package com.google.cloud.solutions.griddler.android.ui.opponentslist;

import com.google.cloud.solutions.griddler.android.R;
import com.google.cloud.solutions.griddler.android.helpers.DownloadImageTask;
import com.google.cloud.solutions.griddler.android.ui.opponentslist.models.OpponentItem;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter used for managing the list of opponents
 */
public class OpponentsListItemAdapter extends ArrayAdapter<OpponentItem> {
  private final Context context;
  private int layoutResourceId;
  private final OpponentItem[] values;

  /**
   * Constructor
   *
   * @param context The context
   * @param layoutResourceId The layout resource ID
   * @param values The list of opponents
   */
  public OpponentsListItemAdapter(Context context, int layoutResourceId, OpponentItem[] values) {
    super(context, layoutResourceId, values);
    this.layoutResourceId = layoutResourceId;
    this.context = context;
    this.values = values;
  }

  /**
   * Builds the opponent item for the view
   */
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(layoutResourceId, parent, false);

    TextView textView = (TextView) rowView.findViewById(R.id.opponentItemText);
    ImageView imageView = (ImageView) rowView.findViewById(R.id.opponentIcon);

    OpponentItem item = values[position];

    textView.setText(item.content);

    if (item.imageUrl != null) {
      new DownloadImageTask(imageView).executeOnExecutor(
          AsyncTask.THREAD_POOL_EXECUTOR, item.imageUrl);
    } else {
      imageView.setImageResource(R.drawable.avatar_small);
    }

    return rowView;
  }


}
