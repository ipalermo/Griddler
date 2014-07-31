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

package com.google.cloud.solutions.sampleapps.griddler.backend.api;

/**
 * Griddler API and services configuration.
 */
public class Configuration {

  /**
   * API name.
   */
  static final String NAME = "griddler";

  /**
   * API version.
   */
  static final String VERSION = "v1";

  /**
   * API description.
   */
  static final String DESCRIPTION = "Griddler API";

  /**
   * Settings for @ApiNamespace.
   */
  static final String NAMESPACE_OWNER_DOMAIN = "google.com";
  static final String NAMESPACE_OWNER_NAME = "google.com";
  static final String NAMESPACE_PACKAGE_PATH = "cloud.solutions.sampleapps.griddler.client";

  /**
   * Web Client ID.
   */

  static final String WEB_CLIENT_ID = "376291138618-tentqfga6khq7d7r9jk64uluuiiu13f4.apps.googleusercontent.com";
  /**
   * Android Client ID.
   */

  static final String ANDROID_CLIENT_ID = "376291138618-qig0fu4akd56ge0eeprb92u9te0uds6a.apps.googleusercontent.com";

  /**
   * iOS Client ID.
   */

  static final String IOS_CLIENT_ID = "376291138618-k0qbfitb9h79ivppj6373u8bq0ld4a5k.apps.googleusercontent.com";

  /**
   * GCM API Key.
   */

  public static final String CLOUD_MESSAGING_API_KEY = "AIzaSyDZlxZo_8EZYvP_RyRf5Gx8djO1Lm18PIk";

  /**
   * Audience ID.
   */
  static final String AUDIENCE = WEB_CLIENT_ID;

  /**
   * API Scope.
   */
  static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";

  /**
   * APNS certificate file. The certificate file needs to be deployed with other binaries. Eclipse
   * does it automatically if the file is placed under the src folder.
   */
  public static final String APNS_CERTIFICATE_FILE = "YouriOSPushNoficicationCertificate.p12";

  /**
   * APNS certificate password.
   */
  public static final String APNS_CERTIFICATE_PASSWORD =
      "YouriOSPushNoficicationCertificatePassword";
}
