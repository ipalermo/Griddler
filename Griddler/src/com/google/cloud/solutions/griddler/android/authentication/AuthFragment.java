package com.google.cloud.solutions.griddler.android.authentication;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.cloud.solutions.griddler.android.ApplicationSettings;
import com.google.cloud.solutions.griddler.android.GameApplication;
import com.google.cloud.solutions.griddler.android.R;
import com.google.cloud.solutions.griddler.android.ui.RequestCodes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;

import java.io.IOException;
import java.util.UUID;

/**
 * A fragment that can be added to an activity to perform account authentication. attach it using
 * getAuthFragment() in Activity's onCreate() method, call authenticateTask() to start the process
 * and forward calls in onActivityResult to handleOnActivityResult().
 */
public final class AuthFragment extends Fragment {

  /**
   * The purpose of this class is to handle asynchronous authentication
   */
  private class AuthenticationTask extends AsyncTask<Void, Void, Void> {

    private UUID mId = UUID.randomUUID();

    /**
     * Constructor
     */
    public AuthenticationTask() {
      Log.d(TAG, String.format("Auth task %s -> initializing", mId));
    }

    /**
     * Performs the authentication
     */
    @Override
    protected Void doInBackground(Void... params) {
      Log.d(TAG, String.format("Auth task %s -> doInBackground", mId));
      authenticate();
      return null;
    }

    /**
     * Required for implementation. Logs event
     */
    @Override
    protected void onPostExecute(Void result) {
      super.onPostExecute(result);
      Log.d(TAG, String.format("Auth task %s -> onPostExecute", mId));
    }

    /**
     * Required for implementation. Logs event
     */
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      Log.d(TAG, String.format("Auth task %s -> onPreExecute", mId));
    }
  }

  private static final String TAG = AuthFragment.class.getSimpleName();

  /**
   * Attach an authorization managing fragment to you activity.
   *
   * @param activity The activity where the fragment is located
   * @return {@link AuthFragment}
   */
  public static AuthFragment getAuthFragment(Activity activity) {
    return getAuthFragment(activity, (AuthEventListener) activity);
  }

  /**
   * Attach an authorization managing fragment to you activity.
   *
   * @param activity The activity where the fragment is located
   * @param listener The listener that is used for callbacks
   * @return {@link AuthFragment}
   */
  public static AuthFragment getAuthFragment(Activity activity, AuthEventListener listener) {

    // Check if the fragment is already attached.
    FragmentManager fragmentManager = activity.getFragmentManager();
    Fragment fragment = fragmentManager.findFragmentByTag(TAG);
    if (fragment instanceof AuthFragment) {
      // The fragment is attached. return it.
      return (AuthFragment) fragment;
    }

    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    // If a fragment was already attached, remove it to clean up.
    if (fragment != null) {
      fragmentTransaction.remove(fragment);
    }

    // Create a new fragment and attach it to the fragment manager.
    AuthFragment authFragment = new AuthFragment();
    authFragment.setListener(listener);
    fragmentTransaction.add(authFragment, TAG);
    fragmentTransaction.commit();

    return authFragment;
  }

  private void setListener(AuthEventListener listener) {
    mListener = listener;
  }

  private GameApplication mApplication;

  private GoogleAccountCredential mCredential;

  private AuthEventListener mListener;

  /**
   * Constructor
   */
  public AuthFragment() {
    Log.d(TAG, "ctor");
  }

  /**
   * Start the authentication process asynchronously
   */
  public void authenticateAsync() {
    new AuthenticationTask().execute();
  }

  private void authenticate() {

    // not checking for mApplication.isUserSignedIn()
    // since this method can be used to fix invalid/expired authentication

    // verify Google Play services
    if (!checkGoogleServicesAvailability()) {
      // dialog result will be handle in handleOnActivityResult()
      return;
    }

    // verify account selection
    if (mCredential.getSelectedAccountName() == null) {
      Log.d(TAG, "No account selected, launching account chooser dialog");
      getActivity().startActivityForResult(mApplication.getCredential().newChooseAccountIntent(),
          RequestCodes.REQUEST_ACCOUNT_PICKER);

      // dialog result will be handle in handleOnActivityResult()
      return;
    }

    try {
      // verifies authentication by getting a valid authentication token
      String token = mCredential.getToken();
      String name = mCredential.getSelectedAccountName();
      Log.d(TAG, String.format("Token for %s: %s", name, token));
      mApplication.setSelectedAccountName(name);
      mApplication.setPreviousAccount(null);
      mApplication.initializeGoogleServices();

      // re-set activity somehow, fragment's activity may had been killed
      if (mListener != null) {
        mListener.onAuthCompleted();
      }

    } catch (final GooglePlayServicesAvailabilityIOException e) {
      showGoogleServicesAvailabilityDialog(e.getConnectionStatusCode());
    } catch (UserRecoverableAuthIOException e) {
      getActivity().startActivityForResult(e.getIntent(), RequestCodes.REQUEST_AUTHORIZATION);
    } catch (GoogleAuthIOException e) {
      GoogleAuthException ex = e.getCause();
      ex.printStackTrace();

    } catch (GoogleAuthException e) {
      Log.e(TAG, "authenticate", e);
      e.printStackTrace();
      mListener.onAuthFailed();
    } catch (IOException e) {
      Log.e(TAG, "authenticate", e);
      e.printStackTrace();
      mListener.onAuthFailed();
    }
  }

  /***
   * Verifies that Google Play services is installed and enabled on this device, and that the
   * version installed on this device is no older than the one required by this client.
   */
  private boolean checkGoogleServicesAvailability() {

    final int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

    if (statusCode == ConnectionResult.SUCCESS) {
      return true;
    }

    showGoogleServicesAvailabilityDialog(statusCode);

    return false;

  }

  private void showGoogleServicesAvailabilityDialog(final int statusCode) {

    if (GooglePlayServicesUtil.isUserRecoverableError(statusCode)) {
      getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          GooglePlayServicesUtil.getErrorDialog(
              statusCode, getActivity(), RequestCodes.REQUEST_GOOGLE_PLAY_SERVICES).show();
        }
      });
    }

  }

  private void handleGooglePlayServicesResult(int resultCode, Intent data) {

    if (resultCode == Activity.RESULT_OK) {
      Log.d(TAG, "Google Play services installed/updated.");
      authenticateAsync();
    } else {
      // user cancel or something went wrong
      onAuthDialogCanceled(AuthenticationDialog.GOOGLE_PLAY_SERVICES);
    }

  }

  private void handleChooseAccountResult(int resultCode, Intent data) {

    if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {

      String accountName = data.getExtras().getString(ApplicationSettings.PREF_ACCOUNT_NAME);
      if (accountName != null) {
        Log.d(TAG, String.format("Google account %s selected.", accountName));
        mCredential.setSelectedAccountName(accountName);
        authenticateAsync();
      }
    } else {
      // user cancel or something went wrong
      onAuthDialogCanceled(AuthenticationDialog.CHOOSE_ACCOUNT);
    }

  }

  private void handleAuthorizationResult(int resultCode, Intent data) {

    if (resultCode == Activity.RESULT_OK) {
      Log.d(TAG, "App authorization done.");
      authenticateAsync();
    } else {
      // user cancel or something went wrong
      onAuthDialogCanceled(AuthenticationDialog.AUTHORIZATION);
    }

  }

  /**
   * Handles the activity results for Google Play Services, account picker, and authorization
   * dialogs
   *
   * @param requestCode The request code
   * @param resultCode The result code
   * @param data The intent
   * @return Returns <code>true</> when completed
   */
  public boolean handleOnActivityResult(int requestCode, int resultCode, Intent data) {

    switch (requestCode) {
      case RequestCodes.REQUEST_GOOGLE_PLAY_SERVICES:
        handleGooglePlayServicesResult(resultCode, data);
        break;
      case RequestCodes.REQUEST_ACCOUNT_PICKER:
        handleChooseAccountResult(resultCode, data);

        break;
      case RequestCodes.REQUEST_AUTHORIZATION:
        handleAuthorizationResult(resultCode, data);
        break;
    }

    return true;

  }

  private void onAuthDialogCanceled(AuthenticationDialog dialogType) {

    Log.d(TAG, String.format("%s dialog was canceled", dialogType));

    if (mApplication.getPreviousAccount() != null) {
      // no need to show up the warning dialog when switching accounts,
      mListener.onAuthFailed();
      return;
    }

    // using same theme as the Choose-an-account dialog
    AlertDialog.Builder builder = new AlertDialog.Builder(
        new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Dialog));

    builder.setTitle(R.string.auth_dialog_title).setMessage(R.string.auth_dialog_message);

    builder.setPositiveButton(R.string.auth_dialog_retry, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int id) {
        authenticateAsync();
      }
    });

    builder.setNegativeButton(R.string.auth_dialog_exit, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mListener.onAuthFailed();
      }
    });

    builder.create().show();

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Retain instance to avoid reconnecting on rotate.
    // This means that onDestroy and onCreate will not be called on
    // configuration changes.
    setRetainInstance(true);

    Activity activity = getActivity();

    mApplication = (GameApplication) activity.getApplication();

    mCredential = mApplication.getCredential();
  }

}
