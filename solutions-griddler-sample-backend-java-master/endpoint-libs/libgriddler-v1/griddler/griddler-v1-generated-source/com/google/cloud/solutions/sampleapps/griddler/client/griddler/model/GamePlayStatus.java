/*
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
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2014-04-15 19:10:39 UTC)
 * on 2014-04-27 at 22:33:40 UTC 
 * Modify at your own risk.
 */

package com.google.cloud.solutions.sampleapps.griddler.client.griddler.model;

/**
 * Model definition for GamePlayStatus.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the griddler. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class GamePlayStatus extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.Integer> correctAnswers;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long timeLeft;

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.Integer> getCorrectAnswers() {
    return correctAnswers;
  }

  /**
   * @param correctAnswers correctAnswers or {@code null} for none
   */
  public GamePlayStatus setCorrectAnswers(java.util.List<java.lang.Integer> correctAnswers) {
    this.correctAnswers = correctAnswers;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getTimeLeft() {
    return timeLeft;
  }

  /**
   * @param timeLeft timeLeft or {@code null} for none
   */
  public GamePlayStatus setTimeLeft(java.lang.Long timeLeft) {
    this.timeLeft = timeLeft;
    return this;
  }

  @Override
  public GamePlayStatus set(String fieldName, Object value) {
    return (GamePlayStatus) super.set(fieldName, value);
  }

  @Override
  public GamePlayStatus clone() {
    return (GamePlayStatus) super.clone();
  }

}