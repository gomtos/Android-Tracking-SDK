package com.fsn.cauly.trackertester;

import com.fsn.cauly.tracker.CaulyTrackerBuilder;
import com.fsn.cauly.tracker.Logger.LogLevel;
import com.fsn.cauly.tracker.TrackerConst;

import android.app.Application;

public class TestApplication extends Application {

	public TestApplication() {
	}

	@Override
	public void onCreate() {
		super.onCreate();

		CaulyTrackerBuilder caulyTrackerBuilder = new CaulyTrackerBuilder(getApplicationContext());

		// Initailize tracker instance.
		// LogLevel should not be set on Product Build.
		caulyTrackerBuilder.setUserId("customer_id_tester") // Optional
															// :
															// user
															// id
				.setAge("0") // Optional : age
				.setGender(TrackerConst.FEMALE) // Optional : Gender
				.setLogLevel(LogLevel.Debug).build();
	}

}
