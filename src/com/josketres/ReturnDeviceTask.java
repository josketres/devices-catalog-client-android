package com.josketres;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class ReturnDeviceTask extends AsyncTask<String, Void, Void> {

	private final MainActivity gui;
	private AndroidHttpClient httpClient = AndroidHttpClient.newInstance("");

	public ReturnDeviceTask(MainActivity gui) {
		this.gui = gui;
	}

	@Override
	protected Void doInBackground(String... params) {
		String host = params[0];
		String deviceId = params[1];
		String url = String.format("http://%s/api/device/%s/borrower", host,
				deviceId);
		Log.i(MainActivity.TAG, "DELETE: " + url);
		HttpDelete request = new HttpDelete(url);
		try {
			httpClient.execute(request);
		} catch (ClientProtocolException e) {
			Log.e(MainActivity.TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(MainActivity.TAG, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		gui.checkDeviceStatus();
	}

}
