package com.google.cloud.solutions.griddler.android.providers;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * The purpose of this class is to provide a generic method of performing asynchronous calls to the
 * cloud endpoints
 *
 * @param <T>
 */
public abstract class ServiceTask<T> extends AsyncTask<Void, Void, T> {

  protected Griddler service = null;
  private static final int RETRY_MAX = 3;
  private static final String TAG = ServiceTask.class.getSimpleName();

  protected ServiceTask(Griddler service) {
    this.service = service;
  }

  @Override
  protected T doInBackground(Void... params) {
    int attemptNumber = 0;

    while (attemptNumber <= RETRY_MAX) {
      try {

        attemptNumber++;

        return executeEndpointCall();
      } catch (java.net.SocketTimeoutException e) {
        Log.e(TAG, "ServiceTask<T> doInBackground", e);
      } catch (GoogleJsonResponseException e) {
        Log.e(TAG, "ServiceTask<T> doInBackground", e);
        // Do not retry on HTTP 4xx errors
        if (e.getStatusCode() >= 400 && e.getStatusCode() <= 499) {
          return null;
        }
      } catch (IOException e) {
        Log.e(TAG, "ServiceTask<T> doInBackground", e);
      } catch (RuntimeException e) {
        Log.e(TAG, "ServiceTask<T> doInBackground", e);
      }
    }

    return null;
  }

  protected abstract T executeEndpointCall() throws SocketTimeoutException, IOException;
}
