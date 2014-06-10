package com.zdh.crisom;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends Activity {

	//--------define variables---------
	LinearLayout lnHome,lnSearch,lnCart,lnAccount,lnMore;
	TextView tvHome,tvSearch,tvCart,tvAccount,tvMore,tvTitle;
	ImageView ivHome,ivSearch,ivCart,ivAccount,ivMore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
	}
	
	private void init(){
		initView();
	}
	
	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.home_lnhome);
	}
}
