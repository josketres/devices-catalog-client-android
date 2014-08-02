package com.josketres;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class CheckDeviceStatusTask extends
		AsyncTask<String, Void, DeviceStatus> {

	private final MainActivity gui;
	private AndroidHttpClient httpClient = AndroidHttpClient.newInstance("");

	public CheckDeviceStatusTask(MainActivity mainActivity) {
		gui = mainActivity;
	}

	@Override
	protected DeviceStatus doInBackground(String... params) {

		String host = params[0];
		String deviceId = params[1];
		String url = "http://" + host + "/api/device/" + deviceId;
		Log.i(MainActivity.TAG, "GET: " + url);
		HttpGet request = new HttpGet(url);
		try {
			return httpClient.execute(request, new GetDeviceStatusResponseHandler());
		} catch (ClientProtocolException e) {
			Log.e(MainActivity.TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(MainActivity.TAG, e.getMessage());
			e.printStackTrace();
		}
		return DeviceStatus.NOT_REGISTERED;
	}

	@Override
	protected void onPostExecute(DeviceStatus result) {
		super.onPostExecute(result);
		gui.updateDeviceStatus(result);
	}

}