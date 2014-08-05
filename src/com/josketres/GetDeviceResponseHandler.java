package com.josketres;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class GetDeviceResponseHandler implements ResponseHandler<Device> {

	@Override
	public Device handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		if (response.getStatusLine().getStatusCode() == 404) {
			return null;
		} else if (response.getStatusLine().getStatusCode() == 200) {
			return parseDevice(response);
		}
		return null;
	}

	private Device parseDevice(HttpResponse response)
			throws HttpResponseException, IOException {
		String json = new BasicResponseHandler().handleResponse(response);
		try {
			JSONObject object = (JSONObject) new JSONTokener(json).nextValue();

			DeviceStatus status = parseStatus(object);
			String name = object.getString("name");
			String id = object.getString("id");

			String borrowerName = parseBorrowerName(object);

			return new Device(id, name, status, borrowerName);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String parseBorrowerName(JSONObject object) {

		try {
			JSONObject borrower = object.getJSONObject("borrower");
			return borrower.getString("name");
		} catch (JSONException e) {
			return null;
		}
	}

	private DeviceStatus parseStatus(JSONObject object) throws JSONException {

		switch (object.getString("status")) {
		case "available":
			return DeviceStatus.AVAILABLE;
		case "borrowed":
			return DeviceStatus.BORROWED;
		}
		return DeviceStatus.NOT_REGISTERED;
	}
}
