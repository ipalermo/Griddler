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
package com.google.cloud.solutions.griddler.android.ui.splash;

import com.google.cloud.solutions.griddler.android.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * The purpose of this class is to support a logo splash animation
 */
public class SplashAnimation extends LinearLayout {
  private ImageView movie;
  private AnimationDrawable animation = null;

  int currentIndex = 0;

  /**
   * Constructor
   *
   * @param context The context
   */
  public SplashAnimation(Context context) {
    super(context);
    init(context);
  }

  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   */
  public SplashAnimation(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   * @param defStyle
   */
  public SplashAnimation(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  /**
   * Start the animation
   */
  public void start() {
    if (!animation.isRunning()) {
      animation.start();
    }
  }

  /**
   * Stop the animation
   */
  public void stop() {
    if (animation.isRunning()) {
      animation.stop();
    }
  }

  private void init(Context context) {

    LayoutInflater.from(context).inflate(R.layout.fragment_splashscreen_animation, this);

    this.setWillNotDraw(false);

    movie = (ImageView) findViewById(R.id.splash_movie);

    animation = new AnimationDrawable();
    animation.addFrame(getResources().getDrawable(R.drawable.splash_0), 120);
    animation.addFrame(getResources().getDrawable(R.drawable.splash_1), 120);
    animation.addFrame(getResources().getDrawable(R.drawable.splash_2), 120);
    animation.addFrame(getResources().getDrawable(R.drawable.splash_3), 120);
    animation.addFrame(getResources().getDrawable(R.drawable.splash_4), 120);
    animation.addFrame(getResources().getDrawable(R.drawable.splash_5), 120);
    animation.addFrame(getResources().getDrawable(R.drawable.splash_6), 120);
    animation.addFrame(getResources().getDrawable(R.drawable.splash_7), 120);
    animation.addFrame(getResources().getDrawable(R.drawable.splash_8), 120);
    animation.addFrame(getResources().getDrawable(R.drawable.splash_0), 120);
    animation.addFrame(getResources().getDrawable(R.drawable.splash_0), 120);
    animation.setOneShot(false);

    movie.setBackground(animation);
  }
}
