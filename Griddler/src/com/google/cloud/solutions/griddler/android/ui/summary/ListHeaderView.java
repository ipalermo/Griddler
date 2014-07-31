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
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * The purpose of this view is to format the display of the header on the summary activity
 */
public class ListHeaderView extends RelativeLayout {

  private TextView leftHeader;
  private TextView rightHeader;
  private String leftHeaderText;
  private String rightHeaderText;


  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   * @param defStyle
   */
  public ListHeaderView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   */
  public ListHeaderView(Context context, AttributeSet attrs) {
    super(context, attrs);
    LayoutInflater layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.list_header_game_summary, this, true);

    leftHeader = (TextView) findViewById(R.id.resultsTextView1);
    rightHeader = (TextView) findViewById(R.id.resultsTextView2);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListHeaderView);
    String l = a.getString(R.styleable.ListHeaderView_leftHeaderText);
    String r = a.getString(R.styleable.ListHeaderView_rightHeaderText);
    if (l != null) {
      this.setLeftHeaderText(l);
    }
    if (r != null) {
      this.setRightHeaderText(r);
    }

    a.recycle();
  }

  /**
   * Constructor
   *
   * @param context The context
   */
  public ListHeaderView(Context context) {
    super(context);
  }

  /**
   * Get the left header text
   *
   * @return {@link String}
   */
  public String getLeftHeaderText() {
    return leftHeaderText;
  }

  /**
   * Set the left header text
   *
   * @param leftHeaderText The text
   */
  public void setLeftHeaderText(String leftHeaderText) {
    this.leftHeaderText = leftHeaderText;
    leftHeader.setText(leftHeaderText);
  }

  /**
   * Get the right header text
   *
   * @return {@link String}
   */
  public String getRightHeaderText() {
    return rightHeaderText;
  }

  /**
   * Set the right header text
   *
   * @param rightHeaderText The text
   */
  public void setRightHeaderText(String rightHeaderText) {
    this.rightHeaderText = rightHeaderText;
    rightHeader.setText(rightHeaderText);
  }

}
