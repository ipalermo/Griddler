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
package com.google.cloud.solutions.griddler.android.ui;

import com.google.cloud.solutions.griddler.android.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Icon button.
 */
public class IconButton extends RelativeLayout {

  private TextView textView;
  private ImageView imageView;
  private Drawable imageSource;

  public IconButton(Context context, AttributeSet attrs) {
    this(context, attrs, 0);

  }

  public IconButton(Context context) {
    this(context, null);
  }

  public IconButton(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    LayoutInflater layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.icon_button, this, true);
    this.textView = (TextView) findViewById(R.id.textView);
    this.imageView = (ImageView) findViewById(R.id.imageView);


    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconButton);
    String s = a.getString(R.styleable.IconButton_buttonText);
    Drawable i = a.getDrawable(R.styleable.IconButton_imageDrawable);
    if (s != null) {
      this.setButtonText(s);
    }
    if (i != null) {
      this.setImageDrawable(i);
    }

    a.recycle();
  }

  public void setButtonText(String buttonText) {
    this.textView.setText(buttonText);
  }

  public String getButtonText() {
    return (String) this.textView.getText();
  }

  public Drawable getImageDrawable() {
    return imageSource;
  }

  public void setImageDrawable(Drawable imageSource) {
    this.imageSource = imageSource;
    this.imageView.setImageDrawable(imageSource);
  }

}
