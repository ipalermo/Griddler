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
package com.google.cloud.solutions.griddler.android.social;

/**
 * The purpose of this interface is to allow the implementor to react to Google+ events such as
 * connected, connection failed, and when a Google+ profile isn't available publicly
 *
 */
public interface GooglePlusListener {
  /**
   * Called when Google+ client has been initialized
   */
  void onGooglePlusConnected();

  /**
   * Called when the retrieval of a player has completed, but their profile isn't publicly available
   */
  void onGooglePlusPersonNotPublic();
}
