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
import com.google.cloud.solutions.griddler.android.ui.game.models.BoardRenderModel;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;

/**
 * The purpose of this class is to allow paging for the list of questions/clues
 */
public class QuestionAnswerPagerView extends ViewPager implements IGameView {
  IGameManager gameManager;
  FragmentManager fragmentManager;
  Boolean enabled = false;

  int focusedPage = 0;

  QuestionAnswerPagerAdapter itemAdapter;

  /**
   * Constructor
   *
   * @param context The context
   */
  public QuestionAnswerPagerView(Context context) {
    super(context);
    this.init(context);
  }

  /**
   * Constructor
   *
   * @param context The context
   * @param attrs The attributes
   */
  public QuestionAnswerPagerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.init(context);
  }

  private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.fragment_game_questionanswer_pager, this);
  }

  /**
   * Set the game manager
   */
  @Override
  public void setGameManager(IGameManager gameManager) {
    this.gameManager = gameManager;
  }

  @Override
  public void onGameManagerStateChange(GameStateType gameState) {

  }

  /**
   * Initialize the list adapter and wire-up listeners
   */
  @Override
  public void onGameModelLoaded(BoardRenderModel boardRenderModel) {
    itemAdapter = new QuestionAnswerPagerAdapter(fragmentManager);
    this.setOnPageChangeListener(new QuestionAnswerPagerAdapterChangeListener());
    this.setAdapter(itemAdapter);
  }

  /**
   * Sets the current item in the list if it doesn't match the game manager's current question index
   */
  @Override
  public void onTick() {
    if (gameManager.getBoardRenderModel().getCurrentQuestionIndex() != focusedPage) {
      this.setCurrentItem(gameManager.getBoardRenderModel().getCurrentQuestionIndex());
    }
  }

  /**
   * Set the fragment manager
   */
  @Override
  public void setFragmentManager(FragmentManager fragmentManager) {
    this.fragmentManager = fragmentManager;
  }

  /**
   * Calls <code>return super.onTouchEvent(event);</code> if the paging is enabled. 
   * Otherwise it returns false.
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (this.enabled) {
      return super.onTouchEvent(event);
    }

    return false;
  }

  /**
   * Calls <code>return super.onInterceptTouchEvent(event);</code> if the paging is enabled. 
   * Otherwise it returns false.
   */
  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    if (this.enabled) {
      return super.onInterceptTouchEvent(event);
    }

    return false;
  }

  /**
   * Sets the value indicating if paging is enabled
   *
   * @param enabled <code>true</code> if enabled
   */
  public void setPagingEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  private class QuestionAnswerPagerAdapter extends FragmentStatePagerAdapter {
    QuestionAnswerView[] viewFragments;

    public QuestionAnswerPagerAdapter(FragmentManager fm) {
      super(fm);
      viewFragments = new QuestionAnswerView[this.getCount()];
    }

    @Override
    public Fragment getItem(int position) {
      viewFragments[position] = new QuestionAnswerView();
      viewFragments[position].setWordRenderModel(
          gameManager.getBoardRenderModel().getWords().get(position));
      viewFragments[position].setGameManager(gameManager);
      return viewFragments[position];
    }

    @Override
    public int getCount() {
      if (gameManager != null) {
        return gameManager.getBoardRenderModel().getWords().size();
      }
      return 0;
    }
  }

  private class QuestionAnswerPagerAdapterChangeListener
      extends ViewPager.SimpleOnPageChangeListener {
    @Override
    public void onPageSelected(int position) {
      focusedPage = position;
    }
  }

  @Override
  public void onTouchMove(int x, int y) {}

  @Override
  public void updateUI() {}

  @Override
  public void onQuestionAnswered() {}

  @Override
  public void onQuestionSkipped() {}

  @Override
  public void onLetterAdded() {}

  @Override
  public void onLetterReset() {}
}
