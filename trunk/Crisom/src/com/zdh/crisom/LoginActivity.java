package com.zdh.crisom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements View.OnClickListener{

	EditText edtEmail,edtPass;
	Button btnLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		init();
	}
	
	private void init(){
    	initView();
    	initData();
    }
    
	private void initView() {
		edtEmail = (EditText)findViewById(R.id.login_edt_email);
		edtPass = (EditText)findViewById(R.id.login_edt_pass);
		btnLogin = (Button)findViewById(R.id.login_btn_login);
	}

	private void initData(){
    
    
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn_login:
			
			break;

		default:
			break;
		}
		
	}
}
