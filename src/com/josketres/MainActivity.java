package com.josketres;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	static final String TAG = "DevicesCatalogClient";

	private static final String SERVER_HOST = "192.168.2.102:8000";

	private TextView text;
	private ProgressDialog loading;
	private Button registerButton;
	private Button borrowButton;
	private Button returnButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		text = (TextView) findViewById(R.id.text);
		registerButton = (Button) findViewById(R.id.registerButton);
		borrowButton = (Button) findViewById(R.id.borrowButton);
		returnButton = (Button) findViewById(R.id.returnButton);

		loading = new ProgressDialog(this);
		loading.setMessage("Loading...");
		loading.setCancelable(false);

		final String deviceId = Secure.getString(getContentResolver(),
				Secure.ANDROID_ID);

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loading.show();
				new RegisterDeviceTask(MainActivity.this).execute(SERVER_HOST,
						deviceId);
			}
		});

		borrowButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loading.show();
				new BorrowDeviceTask(MainActivity.this).execute(SERVER_HOST,
						deviceId);
			}
		});

		returnButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loading.show();
				new ReturnDeviceTask(MainActivity.this).execute(SERVER_HOST,
						deviceId);
			}
		});
		
		checkDeviceStatus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void updateDeviceStatus(DeviceStatus result) {
		loading.dismiss();

		switch (result) {
		case NOT_REGISTERED:
			text.setText("Device not registered yet.");
			registerButton.setVisibility(View.VISIBLE);
			returnButton.setVisibility(View.INVISIBLE);
			borrowButton.setVisibility(View.INVISIBLE);
			break;

		case AVAILABLE:
			text.setText("Device is available for borrow");
			borrowButton.setVisibility(View.VISIBLE);
			registerButton.setVisibility(View.INVISIBLE);
			returnButton.setVisibility(View.INVISIBLE);
			break;

		case BORROWED:
			text.setText("Device is borrowed");
			returnButton.setVisibility(View.VISIBLE);
			borrowButton.setVisibility(View.INVISIBLE);
			registerButton.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}

	}

	public void checkDeviceStatus() {

		loading.show();
		String deviceId = Secure.getString(getContentResolver(),
				Secure.ANDROID_ID);
		new CheckDeviceStatusTask(this).execute(SERVER_HOST, deviceId);
	}
}
