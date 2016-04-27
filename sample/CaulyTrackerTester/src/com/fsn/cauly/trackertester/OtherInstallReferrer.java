package com.fsn.cauly.trackertester;

import com.fsn.cauly.tracker.InstallReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OtherInstallReferrer extends BroadcastReceiver {

	public OtherInstallReferrer() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals("com.android.vending.INSTALL_REFERRER")) {
			String referrerStr = intent.getStringExtra("referrer");
			if (referrerStr != null) {
				try {

					// Cauly Install check
					// 간단한 설치 정보만 전송
					InstallReceiver installReceiver = new InstallReceiver();
					installReceiver.onReceive(context, intent);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
