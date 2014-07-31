package com.google.cloud.solutions.griddler.android.providers;

import com.google.cloud.solutions.sampleapps.griddler.client.griddler.Griddler;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * The purpose of this class is to make an asynchronous call to register the device
 *
 */
public class RegisterDeviceTask extends ServiceTask<Boolean> {
  private OnRegisterDeviceCompleted listener;
  private String deviceId = null;

  public RegisterDeviceTask(Griddler service, String deviceId, OnRegisterDeviceCompleted listener) {
    super(service);
    this.deviceId = deviceId;
    this.listener = listener;
  }

  @Override
  protected void onPostExecute(Boolean success) {
    if (success == null) {
      // Calling registerDevice thrown an exception
      success = false;
    }
    listener.onRegisterDeviceCompleted(success);
  }

  @Override
  protected Boolean executeEndpointCall() throws SocketTimeoutException, IOException {
    service.playerEndpoint().registerDevice(deviceId, 1).execute();
    return true;
  }
}
