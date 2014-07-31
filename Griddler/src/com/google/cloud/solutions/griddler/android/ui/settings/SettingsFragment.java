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
package com.google.cloud.solutions.griddler.android.ui.settings;

import com.google.cloud.solutions.griddler.android.GameApplication;
import com.google.cloud.solutions.griddler.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * The purpose of this fragment is to display the user's preferences
 */
public class SettingsFragment extends PreferenceFragment {

  private static final String SWITCHACCOUNT_PREFERENCE = "switchAccountPreference";
  private static final String ACCOUNT_PREFERENCE = "accountPreference";
  private static final String ABOUT_PREFERENCE = "aboutPreference";
  private static final String JOIN_GOOGLE_PLUS_PREFERENCE = "joinGooglePlusPreference";

  private static final String TAG = SettingsFragment.class.getSimpleName();

  private GameApplication application;

  private SettingsFragmentInteractionListener listener = null;

  /**
   * Constructor
   */
  public SettingsFragment() {

  }

  /**
   * Define interface for listener to react to the "enable google plus" and switch account button
   * clicked
   */
  public interface SettingsFragmentInteractionListener {
    void onEnableGooglePlus();

    void onSwitchAccount();
  }

  private Preference getSwitchAccountPreference() {
    return findPreference(SWITCHACCOUNT_PREFERENCE);
  }

  private Preference getAccountPreference() {
    return findPreference(ACCOUNT_PREFERENCE);
  }

  private Preference getAboutPreference() {
    return findPreference(ABOUT_PREFERENCE);
  }

  private Preference getGooglePlusPreference() {
    return findPreference(JOIN_GOOGLE_PLUS_PREFERENCE);
  }

  private void refreshPreferences() {

    // account preference
    String account = application.getSelectedAccountName();

    Preference preference;

    if (account != null) {
      preference = getAccountPreference();
      preference.setTitle(account);
      preference.setSummary("Logged in");
    }
  }

  /**
   * Hide the enable Google+ button
   */
  public void hideEnableGooglePlusPreference() {
    getPreferenceScreen().removePreference(getGooglePlusPreference());
  }

  /**
   * Show the enable Google+ button
   */
  public void showEnableGooglePlusPreference() {
    getPreferenceScreen().addPreference(getGooglePlusPreference());
  }

  /**
   * Initialize the view based upon the user's Google+ settings and wire-up any events
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate");
    super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.xml.settings);

    Activity activity = getActivity();

    application = (GameApplication) activity.getApplication();


    if (SettingsFragmentInteractionListener.class.isInstance(activity)) {
      listener = (SettingsFragmentInteractionListener) activity;
    }

    if (application.getApplicationSettings().getUsingGooglePlus()) {
      hideEnableGooglePlusPreference();
    } else {
      getGooglePlusPreference().setOnPreferenceClickListener(new OnPreferenceClickListener() {

        @Override
        public boolean onPreferenceClick(Preference preference) {

          if (listener != null) {
            listener.onEnableGooglePlus();
          }

          return false;
        }
      });
    }

    getAboutPreference().setTitle(application.getAppNameAndVersion());

    getSwitchAccountPreference().setOnPreferenceClickListener(new OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference preference) {

        if (listener != null) {
          listener.onSwitchAccount();
        }

        return false;
      }
    });
  }

  /**
   * Refresh the user's preferences
   */
  @Override
  public void onResume() {
    Log.d(TAG, "onResume");
    super.onResume();
    refreshPreferences();
  }
}
