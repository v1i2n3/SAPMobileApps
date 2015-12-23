package com.example.webview;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private WebView myWebView;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		myWebView.addJavascriptInterface(new MyJavaScriptInterface(this),
				"Android");

		String htmlData = getHTMLData();
		htmlData = htmlData.substring(0, htmlData.indexOf("</body>"));
		htmlData = htmlData
				+ "<input type=\"button\" value=\"Show Toast\" onClick=\"showAndroidToast('Toast to JavaScript interface')\" /><br><input type=\"button\" value=\"Second Activity\" onClick=\"closeActivity()\" /></body><script type=\"text/javascript\"> function showAndroidToast(toast) { Android.showToast(toast); } function closeActivity() {Android.closeActivity();}</script></html>";
		System.out.println("After manipulating the data " + htmlData);
		myWebView.loadData(htmlData, "text/html", "UTF-8");
	}

	/**
	 * @return - Static HTML data
	 */
	private String getHTMLData() {
		StringBuilder html = new StringBuilder();
		try {
			AssetManager assetManager = getAssets();

			InputStream input = assetManager.open("javascriptexample.html");
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			String line;
			while ((line = br.readLine()) != null) {
				html.append(line);
			}
			br.close();
		} catch (Exception e) {
			// Handle the exception here
		}

		return html.toString();
	}

	public class MyJavaScriptInterface {
		Activity activity;

		MyJavaScriptInterface(Activity activity) {
			this.activity = activity;
		}

		@JavascriptInterface
		public void showToast(String toast) {
			Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show();
		}

		@JavascriptInterface
		public void closeActivity() {
			//activity.finish();
			Intent mIntent = new Intent(MainActivity.this,SecondClass.class);
			startActivity(mIntent);
		}
	}
}
