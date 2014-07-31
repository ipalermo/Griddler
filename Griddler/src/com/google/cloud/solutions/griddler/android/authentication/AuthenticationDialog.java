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

package com.google.cloud.solutions.griddler.android.authentication;

/**
 * The purpose of this enum is for categorizing the type of authentication dialog
 */
public enum AuthenticationDialog {
  AUTHORIZATION("User authorization", 2), CHOOSE_ACCOUNT(
      "Choose an account", 1), GOOGLE_PLAY_SERVICES("Google Play services", 0);

  private int intValue;
  private String stringValue;

  private AuthenticationDialog(String toString, int value) {
    stringValue = toString;
    intValue = value;
  }

  /**
   * Returns the {@link String} value of the enum
   */
  @Override
  public String toString() {
    return stringValue;
  }

  /**
   * Returns the integer value of the enum
   *
   * @return The integer value of the enum
   */
  public int value() {
    return intValue;
  }
}
