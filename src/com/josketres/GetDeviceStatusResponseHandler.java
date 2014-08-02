package com.josketres;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class GetDeviceStatusResponseHandler implements
		ResponseHandler<DeviceStatus> {

	@Override
	public DeviceStatus handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		Log.i("DevicesCatalogClient", "status:"
				+ response.getStatusLine().getStatusCode());

		if (response.getStatusLine().getStatusCode() == 404) {
			return DeviceStatus.NOT_REGISTERED;
		} else if (response.getStatusLine().getStatusCode() == 200) {
			String json = new BasicResponseHandler().handleResponse(response);
			try {
				JSONObject object = (JSONObject) new JSONTokener(json)
						.nextValue();
				String status = object.getString("status");
				switch (status) {
				case "available":
					return DeviceStatus.AVAILABLE;
				case "borrowed":
					return DeviceStatus.BORROWED;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return DeviceStatus.NOT_REGISTERED;
	}

}
