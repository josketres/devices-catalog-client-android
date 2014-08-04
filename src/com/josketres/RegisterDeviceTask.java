package com.josketres;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class RegisterDeviceTask extends AsyncTask<String, Void, Void> {

	private final MainActivity gui;

	public RegisterDeviceTask(MainActivity gui) {
		this.gui = gui;
	}

	@Override
	protected Void doInBackground(String... params) {
		String host = params[0];
		String deviceId = params[1];
		String deviceName = params[2];
		
		String json = String.format("{ \"id\" : \"%s\", \"name\" : \"%s\" }", deviceId, deviceName);
		String url = "http://" + host + "/api/device";
		Log.i(MainActivity.TAG, "POST: " + url);
		Log.i(MainActivity.TAG, "POST-BODY: " + json);
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("");
		try {
			HttpPost request = new HttpPost(url);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setEntity(new StringEntity(json));
			httpClient.execute(request);
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
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		gui.checkDeviceStatus();
	}

}
