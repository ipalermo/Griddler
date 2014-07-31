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
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * The purpose of this class is to provide the functionality to download an image from the url
 * asynchronously
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

  private static final String LOG_TAG = DownloadImageTask.class.getSimpleName();

  private ImageView bmImage;

  private OnDownloadImageCompleted listener;

  /**
   * Constructor
   *
   * @param bmImage The {@link ImageView} to assign the image to
   * @param listener The listener who is called once the download has completed
   */
  public DownloadImageTask(ImageView bmImage, OnDownloadImageCompleted listener) {
    this.bmImage = bmImage;
    this.listener = listener;
  }

  /**
   * Constructor
   *
   * @param bmImage The {@link ImageView} to assign the image to
   */
  public DownloadImageTask(ImageView bmImage) {
    this.bmImage = bmImage;
  }

  @Override
  protected Bitmap doInBackground(String... urls) {
    String urldisplay = urls[0];
    Bitmap mIcon11 = null;

    InputStream in = null;
    try {
      in = new java.net.URL(urldisplay).openStream();
    } catch (MalformedURLException e) {
      Log.e(LOG_TAG, "doInBackground", e);
      e.printStackTrace();
    } catch (IOException e) {
      Log.e(LOG_TAG, "doInBackground", e);
      e.printStackTrace();
    }

    if (in != null) {
      mIcon11 = BitmapFactory.decodeStream(in);
    }

    return mIcon11;
  }

  @Override
  protected void onPostExecute(Bitmap result) {

    if (listener != null) {
      listener.onDownloadImage(result);
    }

    bmImage.setImageBitmap(result);
    bmImage.invalidate();
  }
}
