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
 * on 2014-05-05 at 04:21:55 UTC 
 * Modify at your own risk.
 */

package com.google.cloud.solutions.sampleapps.griddler.client.griddler.model;

/**
 * Model definition for GamePlay.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the griddler. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class GamePlay extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.Integer> correctAnswers;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean finished;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean isWinner;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Player player;

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
  public GamePlay setCorrectAnswers(java.util.List<java.lang.Integer> correctAnswers) {
    this.correctAnswers = correctAnswers;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getFinished() {
    return finished;
  }

  /**
   * @param finished finished or {@code null} for none
   */
  public GamePlay setFinished(java.lang.Boolean finished) {
    this.finished = finished;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getIsWinner() {
    return isWinner;
  }

  /**
   * @param isWinner isWinner or {@code null} for none
   */
  public GamePlay setIsWinner(java.lang.Boolean isWinner) {
    this.isWinner = isWinner;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * @param player player or {@code null} for none
   */
  public GamePlay setPlayer(Player player) {
    this.player = player;
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
  public GamePlay setTimeLeft(java.lang.Long timeLeft) {
    this.timeLeft = timeLeft;
    return this;
  }

  @Override
  public GamePlay set(String fieldName, Object value) {
    return (GamePlay) super.set(fieldName, value);
  }

  @Override
  public GamePlay clone() {
    return (GamePlay) super.clone();
  }

}