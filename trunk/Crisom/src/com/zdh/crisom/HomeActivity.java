package com.zdh.crisom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	LinearLayout lnHome,lnSearch,lnCart,lnAccount,lnMore;
	TextView tvHome,tvSearch,tvCart,tvAccount,tvMore,tvTitle;
	ImageView ivHome,ivSearch,ivCart,ivAccount,ivMore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		init();
	}
	
	private void init(){
		initView();
	}
	
	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.home_lnhome);
		lnSearch = (LinearLayout)findViewById(R.id.home_lnsearch);
		lnCart = (LinearLayout)findViewById(R.id.home_lncart);
		lnAccount = (LinearLayout)findViewById(R.id.home_lnaccount);
		lnMore = (LinearLayout)findViewById(R.id.home_lnmore);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		//----------Home is clicked----------
		case R.id.home_lnhome:
			break;
		
		//----------Search is clicked----------
		case R.id.home_lnsearch:
			
			break;
		//----------Cart is clicked----------
		case R.id.home_lncart:
			
			break;
		//----------Account is clicked----------
		case R.id.home_lnaccount:
			
			break;
		//----------More is clicked----------
		case R.id.home_lnmore:
			
			break;	

		default:
			break;
		}
		
	}
	
	
}
