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

package com.google.cloud.solutions.griddler.android.ui;

import com.google.cloud.solutions.griddler.android.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * The purpose of this fragment is to display a spinner on the activity to inform the user that
 * processing is occurring
 */
public class WaitOverlayFragment extends DialogFragment {

  private Boolean showWaitSpinner = true;

  public static final String WAIT_OVERLAY_TEXT = "waitOverlayText";

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View theView = inflater.inflate(R.layout.fragment_wait_overlay, container, false);

    return theView;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {

    Bundle arguments = getArguments();
    if (arguments != null && arguments.containsKey(WAIT_OVERLAY_TEXT)) {
      setText(arguments.getString(WAIT_OVERLAY_TEXT), view);
    }

    super.onViewCreated(view, savedInstanceState);
  }

  /**
   * Set the text to display under the "spinner" progress bar
   */
  public void setText(String text) {
    setText(text, getView());
  }

  private void setText(String text, View view) {
    TextView textView = (TextView) view.findViewById(R.id.bottomWaitingOverlayTextView);
    textView.setText(text);
    textView.invalidate();
  }

  /**
   * Get the current value indicating if the spinner is showing
   */
  public Boolean getShowWaitSpinner() {
    return showWaitSpinner;
  }

  /**
   * Specify if the spinner should show or hide
   *
   * @param showSpinner Show or hide the spinner
   */
  public void setShowWaitSpinner(Boolean showSpinner) {
    showWaitSpinner = showSpinner;
    ProgressBar pb = ((ProgressBar) getActivity().findViewById(R.id.waitSpinner));
    if (!showWaitSpinner) {
      pb.setVisibility(View.GONE);
    } else {
      pb.setVisibility(View.VISIBLE);
    }
  }
}
