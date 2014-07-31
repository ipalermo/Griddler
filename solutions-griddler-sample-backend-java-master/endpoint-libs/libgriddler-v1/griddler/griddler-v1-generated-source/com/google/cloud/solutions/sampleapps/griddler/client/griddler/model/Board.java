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
 * Model definition for Board.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the griddler. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Board extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long allottedTime;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.String> answers;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.String> gridDefinition;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.String> riddles;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getAllottedTime() {
    return allottedTime;
  }

  /**
   * @param allottedTime allottedTime or {@code null} for none
   */
  public Board setAllottedTime(java.lang.Long allottedTime) {
    this.allottedTime = allottedTime;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.String> getAnswers() {
    return answers;
  }

  /**
   * @param answers answers or {@code null} for none
   */
  public Board setAnswers(java.util.List<java.lang.String> answers) {
    this.answers = answers;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.String> getGridDefinition() {
    return gridDefinition;
  }

  /**
   * @param gridDefinition gridDefinition or {@code null} for none
   */
  public Board setGridDefinition(java.util.List<java.lang.String> gridDefinition) {
    this.gridDefinition = gridDefinition;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.String> getRiddles() {
    return riddles;
  }

  /**
   * @param riddles riddles or {@code null} for none
   */
  public Board setRiddles(java.util.List<java.lang.String> riddles) {
    this.riddles = riddles;
    return this;
  }

  @Override
  public Board set(String fieldName, Object value) {
    return (Board) super.set(fieldName, value);
  }

  @Override
  public Board clone() {
    return (Board) super.clone();
  }

}
