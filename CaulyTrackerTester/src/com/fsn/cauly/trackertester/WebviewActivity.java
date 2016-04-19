package com.fsn.cauly.trackertester;

import com.fsn.cauly.tracker.CaulyJsInterface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class WebviewActivity extends AppCompatActivity {

	private WebView testWebview;
	private Button btnClearCache;

	private final String TAG = "WEB";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		testWebview = (WebView) findViewById(R.id.testWebview);

		testWebview.getSettings().setJavaScriptEnabled(true);

		testWebview.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(final WebView view, final String url, final String message, JsResult result) {
				Log.d(TAG, "onJsAlert(!" + view + ", " + url + ", " + message + ", " + result + ")");
				Toast.makeText(getApplicationContext(), message, 3000).show();
				result.confirm();
				return true; // I handled it
			}

		});
		testWebview.setWebViewClient(new WebViewClient());
		testWebview.clearCache(true);

		testWebview.loadUrl(
				"http://image.cauly.co.kr/richad/test/changju/tracker/initadid/CaulyTrackingSample.html?t=" + System.currentTimeMillis());

		testWebview.addJavascriptInterface(new CaulyJsInterface(testWebview), CaulyJsInterface.CAULY_JS_INTERFACE_NAME);

		btnClearCache = (Button) findViewById(R.id.btnClearCache);
		btnClearCache.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (testWebview != null) {
					testWebview.clearCache(true);
					CookieManager.getInstance().removeAllCookie();

				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.webview, menu);
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

}
