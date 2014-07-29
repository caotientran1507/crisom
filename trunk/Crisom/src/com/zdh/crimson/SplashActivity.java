package com.zdh.crimson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.stay,R.anim.right_to_center);

					}
				});
			}
		}.start();
	}
}
