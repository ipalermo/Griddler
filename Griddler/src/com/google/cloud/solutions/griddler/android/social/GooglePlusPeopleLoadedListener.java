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

import com.google.android.gms.plus.model.people.PersonBuffer;

/**
 * The purpose of this interface is to be implemented by a class that serves as a listener to be
 * notified when the retrieval of a player's Google+ friends have been loaded. Used in conjunction
 * with {@link GooglePlusService}
 */
public interface GooglePlusPeopleLoadedListener {
  /**
   * Called when the retrieval of a player's Google+ friends has completed
   *
   * @param buffer The list of friends
   */
  void onGooglePlusPeopleLoaded(PersonBuffer buffer);
}
