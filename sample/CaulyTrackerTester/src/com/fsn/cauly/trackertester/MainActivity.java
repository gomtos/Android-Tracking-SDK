package com.fsn.cauly.trackertester;

import org.json.JSONException;
import org.json.JSONObject;

import com.fsn.cauly.tracker.CaulyTracker;
import com.fsn.cauly.tracker.CaulyTrackerBuilder;
import com.fsn.cauly.tracker.CaulyTrackerEvent;
import com.fsn.cauly.tracker.Logger;
import com.fsn.cauly.tracker.Logger.LogLevel;
import com.fsn.cauly.tracker.exception.CaulyException;
import com.fsn.cauly.tracker.TrackerConst;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements OnClickListener {

	private Button btnInstall;
	private Button btnSessionStart;
	private Button btnSessionClose;
	private Button btnTrackEvent;

	private Button btnCustomEvent;
	private Button btnMoveEvent;
	private Button btnMoveWebview;

	CaulyTracker caulyTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Uri data = this.getIntent().getData();
		if (data != null && data.isHierarchical()) {
			String uri = this.getIntent().getDataString();
			Log.i("MyApp", "Deep link clicked " + uri);

			try {
				CaulyTrackerBuilder.getTrackerInstance().traceDeepLink(uri);
			} catch (CaulyException e) {
				e.printStackTrace();
			}

		}
		
		btnInstall = (Button) findViewById(R.id.btn_install_request);
		btnInstall.setOnClickListener(this);

		btnSessionStart = (Button) findViewById(R.id.btn_session_start);
		btnSessionStart.setOnClickListener(this);

		btnSessionClose = (Button) findViewById(R.id.btn_session_close);
		btnSessionClose.setOnClickListener(this);

		btnTrackEvent = (Button) findViewById(R.id.btn_track_event);
		btnTrackEvent.setOnClickListener(this);

		btnCustomEvent = (Button) findViewById(R.id.btn_custom_event);
		btnCustomEvent.setOnClickListener(this);

		btnMoveEvent = (Button) findViewById(R.id.btn_move_event);
		btnMoveEvent.setOnClickListener(this);

		btnMoveWebview = (Button) findViewById(R.id.btn_move_webview);
		btnMoveWebview.setOnClickListener(this);

		CaulyTrackerBuilder caulyTrackerBuilder = new CaulyTrackerBuilder(getApplicationContext());

		// Initailize tracker instance.
		// LogLevel should not be set on Product Build.
		caulyTracker = caulyTrackerBuilder.setUserId("customer_id_tester") // Optional
																			// :
																			// user
																			// id
				.setAge("0") // Optional : age
				.setGender(TrackerConst.FEMALE) // Optional : Gender
				.setLogLevel(LogLevel.Error).build();

		// Tracking Start
		caulyTracker.startSession();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent paramData) {
		super.onActivityResult(requestCode, resultCode, paramData);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			CaulyTrackerBuilder.getTrackerInstance().closeSession();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_install_request) {
			caulyTracker.sendInstallReferrer("test_referrer");
		}

		if (v.getId() == R.id.btn_session_start) {
			caulyTracker.startSession();

		}

		if (v.getId() == R.id.btn_session_close) {
			caulyTracker.closeSession();
		}

		if (v.getId() == R.id.btn_track_event) {

			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("who", "me");
				jsonObject.put("why", "fun");
				jsonObject.put("when", "20151111");
			} catch (JSONException e) {
				Logger.writeLog(LogLevel.Error, e.getMessage());
				e.printStackTrace();
			}

			caulyTracker.trackEvent("event1_nameonly");
			caulyTracker.trackEvent("event2_single_string", "test");

			CaulyTrackerEvent caulyTrackerEvent = new CaulyTrackerEvent();
			caulyTrackerEvent.setEtc(jsonObject);
			caulyTrackerEvent.setParam1("test1");
			caulyTrackerEvent.setParam2("test2");
			caulyTrackerEvent.setParam3("test3");
			caulyTrackerEvent.setParam4("test4");
			caulyTracker.trackEvent("event3_custom_event", caulyTrackerEvent);

		}

		if (v.getId() == R.id.btn_custom_event) {

			caulyTracker.trackEvent(((EditText) findViewById(R.id.edit_name)).getText().toString(),
					((EditText) findViewById(R.id.edit_param)).getText().toString());

		}

		if (v.getId() == R.id.btn_move_event) {
			Intent eventPageIntent = new Intent(this, EventActivity.class);
			startActivity(eventPageIntent);

		}

		if (v.getId() == R.id.btn_move_webview) {
			Intent eventPageIntent = new Intent(this, WebviewActivity.class);
			startActivity(eventPageIntent);

		}

	}
}
