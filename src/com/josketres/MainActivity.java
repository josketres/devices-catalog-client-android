package com.josketres;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	static final String TAG = "DevicesCatalogClient";

	private static final String SERVER_HOST = "devstation19.office.tipp24.de:8000";

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
				registerDevice(deviceId);
			}
		});

		borrowButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				borrowDevice(deviceId);
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

		getDeviceStatus();
	}

	private void registerDevice(final String deviceId) {

		final EditText input = new EditText(MainActivity.this);
		new AlertDialog.Builder(MainActivity.this)
				.setTitle("Register device")
				.setMessage("Enter device name:")
				.setView(input)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Editable deviceName = input.getText();
						loading.show();
						new RegisterDeviceTask(MainActivity.this).execute(
								SERVER_HOST, deviceId, deviceName.toString());
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Do nothing.
							}
						}).show();
	}

	private void borrowDevice(final String deviceId) {

		final EditText input = new EditText(MainActivity.this);
		new AlertDialog.Builder(MainActivity.this)
				.setTitle("Borrow device")
				.setMessage("Enter borrower's name:")
				.setView(input)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Editable borrowerName = input.getText();
						loading.show();
						new BorrowDeviceTask(MainActivity.this).execute(
								SERVER_HOST, deviceId, borrowerName.toString());
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Do nothing.
							}
						}).show();
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

	public void updateDeviceInfo(Device device) {
		loading.dismiss();

		DeviceStatus status = DeviceStatus.NOT_REGISTERED;
		if (device != null) {
			status = device.status;
		}

		switch (status) {
		case NOT_REGISTERED:
			text.setText("Device not registered yet.");
			registerButton.setVisibility(View.VISIBLE);
			returnButton.setVisibility(View.INVISIBLE);
			borrowButton.setVisibility(View.INVISIBLE);
			break;

		case AVAILABLE:
			text.setText(String.format("Device %s is available for borrow",
					device.name));
			borrowButton.setVisibility(View.VISIBLE);
			registerButton.setVisibility(View.INVISIBLE);
			returnButton.setVisibility(View.INVISIBLE);
			break;

		case BORROWED:
			text.setText(String.format("Device %s is borrowed to %s",
					device.name, device.borrowerName));
			returnButton.setVisibility(View.VISIBLE);
			borrowButton.setVisibility(View.INVISIBLE);
			registerButton.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}

	}

	public void getDeviceStatus() {

		loading.show();
		String deviceId = Secure.getString(getContentResolver(),
				Secure.ANDROID_ID);
		new GetDeviceTask(this).execute(SERVER_HOST, deviceId);
	}
}
