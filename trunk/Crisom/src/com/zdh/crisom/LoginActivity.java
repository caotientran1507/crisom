package com.zdh.crisom;

import android.app.Activity;
import android.os.Bundle;

import com.zdh.crisom.utility.TextViewEx;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_about);
		
		 TextViewEx txtViewEx = (TextViewEx) findViewById(R.id.tvAbout);		 
		 txtViewEx.setText(txtViewEx.getText().toString(), true);

	}
}