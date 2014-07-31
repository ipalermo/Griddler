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
package com.google.cloud.solutions.griddler.android.ui.lobby;

import com.google.cloud.solutions.griddler.android.R;
import com.google.cloud.solutions.griddler.android.models.GameModel;
import com.google.cloud.solutions.griddler.android.providers.OnGetGameCompleted;
import com.google.cloud.solutions.griddler.android.providers.OnStartNewSinglePlayerGameCompleted;
import com.google.cloud.solutions.griddler.android.ui.BaseActivity;
import com.google.cloud.solutions.griddler.android.ui.IntentExtras;
import com.google.cloud.solutions.griddler.android.ui.RequestCodes;
import com.google.cloud.solutions.griddler.android.ui.game.GameActivity;
import com.google.cloud.solutions.griddler.android.ui.splash.SplashAnimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * The purpose of this activity is to provide a "lobby" for the player to wait while the game board
 * is being generated
 */
public class LobbyActivity extends BaseActivity
    implements OnStartNewSinglePlayerGameCompleted, OnGetGameCompleted {

  private GameModel model;
  private Long gameId;

  private TextView countDownTextView, countdownMessageTextView, countdownNameTextView;
  private SplashAnimation countdownAnimation;

  /**
   * Initialize the views and start building the game board based upon single/multiplayer
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lobby);

    startBuildingGame();
    countDownTextView = ((TextView) findViewById(R.id.countDownText));
    countdownAnimation = ((SplashAnimation) findViewById(R.id.countdownAnimation));
    countdownMessageTextView = (TextView) findViewById(R.id.countDownMessage);
    countdownNameTextView = (TextView) findViewById(R.id.nameText);
  }

  /**
   * Pause the logo animation if it is running
   */
  @Override
  protected void onPause() {
    super.onPause();
    countdownAnimation.stop();
  }

  /**
   * Start the logo animation if it is not running
   */
  @Override
  protected void onResume() {
    super.onResume();
    countdownAnimation.start();
  }

  /**
   * Inflate the menu; this adds items to the action bar if it is present.
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.lobby, menu);
    return true;
  }

  private void startBuildingGame() {
    int gameType = getIntent().getIntExtra(IntentExtras.LOBBY_GAME_TYPE, 0);
    gameId = getIntent().getLongExtra(IntentExtras.LOBBY_GAME_ID, 0);

    switch (gameType) {
      case IntentExtras.LOBBY_SINGLE_PLAYER:
        mApplication.getDataProvider().startNewSinglePlayerGame(this);
        break;
      case IntentExtras.LOBBY_MULTI_PLAYER:
        mApplication.getDataProvider().getGame(gameId, this);
        break;
    }
  }

  private void gameBoardReady(GameModel model) {
    this.model = model;
    String nickName = model.getOpponent() == null ? null : model.getOpponent().getNickName();

    if (nickName != null) {
      countdownNameTextView.setText(String.format(getString(R.string.countdownOpponent), nickName));
      countdownMessageTextView.setText(R.string.countdownGetReadyToPlayOpponent);
    } else {
      countdownNameTextView.setText("");
      countdownMessageTextView.setText(getString(R.string.countdownGetReadyToPlay));
    }

    startCountdown();
  }

  private void startCountdown() {

    new CountDownTimer(4000, 1000) {
      @SuppressLint("DefaultLocale")
      @Override
      public void onTick(long millisUntilFinished) {
        countdownAnimation.stop();
        countdownAnimation.setVisibility(View.GONE);
        countDownTextView.setVisibility(View.VISIBLE);
        countDownTextView.setText(
            String.format("%d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
      }

      @Override
      public void onFinish() {
        Intent intent = new Intent(LobbyActivity.this, GameActivity.class);
        intent.putExtra(GameModel.TAG, model);
        startActivityForResult(intent, RequestCodes.REQUEST_GAME_RESULT);
      }
    }.start();
  }

  /**
   * Display the countdown timer
   */
  @Override
  public void onGetGameCompleted(GameModel result) {
    // Model can be null if there was no board for that level
    if (result != null) {
      gameBoardReady(result);
    }
  }

  /**
   * Display the countdown timer
   */
  @Override
  public void onStartNewSinglePlayerGameCompleted(GameModel result) {
    gameBoardReady(result);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case RequestCodes.REQUEST_GAME_RESULT:
        setResult(resultCode, data);
        finish();
        break;
    }
  }
}
