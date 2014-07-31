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
package com.google.cloud.solutions.griddler.android.helpers;

import android.graphics.Bitmap;

/**
 * Interface that is used as a listener with the {@link DownloadImageTask}
 */
public interface OnDownloadImageCompleted {

  /**
   * Provides the listener to know when the image is downloaded
   *
   * @param image The bitmap that was downloaded
   */
  void onDownloadImage(Bitmap image);
}
