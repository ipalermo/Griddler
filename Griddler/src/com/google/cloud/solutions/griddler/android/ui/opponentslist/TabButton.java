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

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Represents a tab button
 */
public class TabButton extends RelativeLayout {

  private String tabText;
  private TextView tabTextView;


  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   * @param defStyle
   */
  public TabButton(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    LayoutInflater layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.tab_button, this, true);
    this.tabTextView = (TextView) findViewById(R.id.tabTextView);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabButton);
    String s = a.getString(R.styleable.TabButton_tabText);
    if (s != null) {
      this.setTabText(s);
    }

    a.recycle();
  }

  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   */
  public TabButton(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  /**
   * Constructor
   *
   * @param context The context
   */
  public TabButton(Context context) {
    this(context, null);
  }

  /**
   * Returns the tab's text
   *
   * @return {@link String}
   */
  public String getTabText() {
    return tabText;
  }

  /**
   * Sets the tab's text
   *
   * @param tabString The text to display
   */
  public void setTabText(String tabString) {
    this.tabText = tabString;
    this.tabTextView.setText(tabString);
  }
}
