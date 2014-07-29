package com.zdh.crimson;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPassActivity extends Activity implements View.OnClickListener{
	EditText edtEmail;
	Button btnSubmit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgotpass);
		
		init();
	}
	
	private void init(){
    	initView();
    	initData();
    }
    
	private void initView() {
		edtEmail = (EditText)findViewById(R.id.forgotpass_edt_email);
		btnSubmit = (Button)findViewById(R.id.forgotpass_btn_submit);
	}

	private void initData(){
    
    
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forgotpass_btn_submit:
			
			break;

		default:
			break;
		}
		
	}
}
