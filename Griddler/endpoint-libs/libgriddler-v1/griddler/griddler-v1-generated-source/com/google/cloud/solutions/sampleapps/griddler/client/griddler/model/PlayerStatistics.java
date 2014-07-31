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
 * Model definition for PlayerStatistics.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the griddler. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class PlayerStatistics extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer numberOfGames;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer numberOfWins;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getNumberOfGames() {
    return numberOfGames;
  }

  /**
   * @param numberOfGames numberOfGames or {@code null} for none
   */
  public PlayerStatistics setNumberOfGames(java.lang.Integer numberOfGames) {
    this.numberOfGames = numberOfGames;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getNumberOfWins() {
    return numberOfWins;
  }

  /**
   * @param numberOfWins numberOfWins or {@code null} for none
   */
  public PlayerStatistics setNumberOfWins(java.lang.Integer numberOfWins) {
    this.numberOfWins = numberOfWins;
    return this;
  }

  @Override
  public PlayerStatistics set(String fieldName, Object value) {
    return (PlayerStatistics) super.set(fieldName, value);
  }

  @Override
  public PlayerStatistics clone() {
    return (PlayerStatistics) super.clone();
  }

}
