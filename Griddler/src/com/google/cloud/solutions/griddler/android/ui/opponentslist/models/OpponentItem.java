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

package com.google.cloud.solutions.griddler.android.ui.opponentslist.models;

/**
 * The purpose of this class is to be used for the opponents list view
 */
public class OpponentItem {
  public String id;
  public String content;
  public String imageUrl;
  public boolean isPlusId = false;

  /**
   * Constructor used for Google+ users
   *
   * @param id The Google+ ID
   * @param content The name to display
   * @param imageUrl The url of their profile image
   */
  public OpponentItem(String id, String content, String imageUrl) {
    this(id, content);
    this.imageUrl = imageUrl;
    this.isPlusId = true;
  }

  /**
   * Constructor used for adding a general opponent
   *
   * @param id The user's ID
   * @param content The name to display
   */
  public OpponentItem(String id, String content) {
    this.id = id;
    this.content = content;
  }

  /**
   * Returns the display name
   */
  @Override
  public String toString() {
    return content;
  }
}
