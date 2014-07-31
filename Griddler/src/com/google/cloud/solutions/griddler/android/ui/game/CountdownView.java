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

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * Represents a view that renders the gameboard countdown-timer spinner
 */
public class CountdownView extends RelativeLayout {
  Paint progressPaint;
  Paint circlePaint;
  Rect mViewRect;

  float circleDiameter = 60.0f;

  IGameManager gameManager;

  /**
   * Constructor
   *
   * @param context The context
   */
  public CountdownView(Context context) {
    super(context);
    this.setWillNotDraw(false);
    this.init(context);
  }

  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   */
  public CountdownView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.setWillNotDraw(false);
    this.init(context);
  }

  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   * @param defStyle
   */
  public CountdownView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.setWillNotDraw(false);
    this.init(context);
  }

  /**
   * Sets the game manager
   *
   * @param gameManager {@link IGameManager}
   */
  public void setGameManager(IGameManager gameManager) {
    this.gameManager = gameManager;
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.fragment_game_countdown, this);

    progressPaint = new Paint();
    progressPaint.setAntiAlias(true);
    progressPaint.setColor(getResources().getColor(R.color.red));
    progressPaint.setStrokeWidth(15f);
    progressPaint.setStyle(Paint.Style.STROKE);

    circlePaint = new Paint();
    circlePaint.setAntiAlias(true);
    circlePaint.setColor(getResources().getColor(R.color.white));
    circlePaint.setStrokeWidth(15f);
    circlePaint.setStyle(Paint.Style.STROKE);
  }

  private RectF getCurrentBounds() {
    float left = this.getWidth() / 2 - circleDiameter / 2;
    float top = 27f;
    return new RectF(left, top, left + circleDiameter, top + circleDiameter);
  }

  @Override
  protected void onDraw(android.graphics.Canvas canvas) {
    super.onDraw(canvas);

    if (gameManager != null) {

      RectF targetBounds = getCurrentBounds();

      canvas.drawCircle(targetBounds.left + (targetBounds.right - targetBounds.left) / 2,
          targetBounds.top + (targetBounds.bottom - targetBounds.top) / 2, targetBounds.width() / 2,
          circlePaint);

      float targetAngle = (360 * (gameManager.getBoardRenderModel().getTotalTime()
          - gameManager.getBoardRenderModel().getRemainingTimeMilliseconds()))
          / gameManager.getBoardRenderModel().getTotalTime();

      canvas.drawArc(getCurrentBounds(), 270, targetAngle, false, progressPaint);

      this.invalidate();
    }
  }
}
