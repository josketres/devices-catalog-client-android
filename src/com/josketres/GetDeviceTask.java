package com.josketres;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class GetDeviceTask extends AsyncTask<String, Void, Device> {

	private final MainActivity gui;

	public GetDeviceTask(MainActivity mainActivity) {
		gui = mainActivity;
	}

	@Override
	protected Device doInBackground(String... params) {

		String host = params[0];
		String deviceId = params[1];
		String url = "http://" + host + "/api/device/" + deviceId;
		Log.i(MainActivity.TAG, "GET: " + url);
		HttpGet request = new HttpGet(url);
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("");
		try {
			return httpClient.execute(request, new GetDeviceResponseHandler());
		} catch (ClientProtocolException e) {
			Log.e(MainActivity.TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(MainActivity.TAG, e.getMessage());
			e.printStackTrace();
		} finally {
			httpClient.close();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Device device) {
		super.onPostExecute(device);
		gui.updateDeviceInfo(device);
	}

}