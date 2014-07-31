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

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * The purpose of this class is to provide a wrapper around specific resource functionality such as
 * building a {@link ColorStateList} from a resource
 */
public class ResourceHelpers {

  private static final String LOG_TAG = ResourceHelpers.class.getSimpleName();

  /**
   * Build a list of color states
   *
   * @param context The context
   * @param resource The ID of the resource
   * @return {@link ColorStateList}
   */
  public static ColorStateList getColorStateList(Context context, int resource) {

    XmlResourceParser parser = context.getResources().getXml(resource);
    ColorStateList colors = null;
    try {
      colors = ColorStateList.createFromXml(context.getResources(), parser);
    } catch (XmlPullParserException e) {
      Log.e(LOG_TAG, "GetColorStateList", e);
      e.printStackTrace();
    } catch (IOException e) {
      Log.e(LOG_TAG, "GetColorStateList", e);
      e.printStackTrace();
    }
    return colors;
  }
}
