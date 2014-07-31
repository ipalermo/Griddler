package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * The purpose of this class is to make an asynchronous call to unregister a device for a specific
 * player
 *
 */
public class UnregisterDeviceTask extends ServiceTask<Boolean> {
  private OnUnregisterDeviceCompleted listener;
  private String deviceId;

  public UnregisterDeviceTask(
      Griddler service, String deviceId, OnUnregisterDeviceCompleted listener) {
    super(service);
    this.listener = listener;
    this.deviceId = deviceId;
  }

  @Override
  protected void onPostExecute(Boolean success) {
    listener.onUnregisterDeviceCompleted(success);
  }

  @Override
  protected Boolean executeEndpointCall() throws SocketTimeoutException, IOException {
    try {
      service.playerEndpoint().unRegisterDevice(deviceId).execute();
      return true;
    } catch (IOException e) {
      return false;
    }
  }
}
