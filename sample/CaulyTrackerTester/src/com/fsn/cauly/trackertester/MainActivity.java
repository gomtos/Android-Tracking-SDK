package com.fsn.cauly.trackertester;

import org.json.JSONException;
import org.json.JSONObject;

import com.fsn.cauly.tracker.CaulyTrackerBuilder;
import com.fsn.cauly.tracker.Logger;
import com.fsn.cauly.tracker.Logger.LogLevel;
import com.fsn.cauly.tracker.TrackerConst;
import com.fsn.cauly.tracker.exception.CaulyException;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
		// Tracking Start
		try {
			CaulyTrackerBuilder.getTrackerInstance().startSession();
		} catch (CaulyException e) {
			e.printStackTrace();
		}

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

			CaulyTrackerBuilder.getTrackerInstance().setGender(TrackerConst.MALE);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		try {
			if (v.getId() == R.id.btn_install_request) {
				CaulyTrackerBuilder.getTrackerInstance().sendInstallReferrer("test_referrer");
			}

			if (v.getId() == R.id.btn_session_start) {
				CaulyTrackerBuilder.getTrackerInstance().startSession();

			}

			if (v.getId() == R.id.btn_session_close) {
				CaulyTrackerBuilder.getTrackerInstance().closeSession();
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

				CaulyTrackerBuilder.getTrackerInstance().trackEvent("event1_nameonly");
				CaulyTrackerBuilder.getTrackerInstance().trackEvent("event2_single_string", "test");

			}

			if (v.getId() == R.id.btn_custom_event) {

				CaulyTrackerBuilder.getTrackerInstance().trackEvent(
						((EditText) findViewById(R.id.edit_name)).getText().toString(),
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
		} catch (CaulyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
