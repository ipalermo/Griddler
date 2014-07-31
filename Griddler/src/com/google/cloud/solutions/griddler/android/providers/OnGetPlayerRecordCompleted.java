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
package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.griddler.android.models.PlayerRecordModel;

/**
 * The purpose of this interface is to be implemented by a class that serves as a listener to be
 * notified when the retrieval of a player's record has completed. Used in conjunction with
 * {@link DataProvider}
 */
public interface OnGetPlayerRecordCompleted {
  /**
   * Called when the player's record has been retrieved
   *
   * @param model The player's record model
   */
  void onGetPlayerRecordCompleted(PlayerRecordModel model);
}
