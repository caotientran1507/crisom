package com.zdh.crisom;

import com.zdh.crisom.utility.Constants;
import com.zdh.crisom.utility.FileUtil;

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
		
		ivHome = (ImageView)findViewById(R.id.home_ivhome);
		ivSearch = (ImageView)findViewById(R.id.home_ivsearch);
		ivCart = (ImageView)findViewById(R.id.home_ivcart);
		ivAccount = (ImageView)findViewById(R.id.home_ivaccount);
		ivMore = (ImageView)findViewById(R.id.home_ivmore);
		
		tvHome = (TextView)findViewById(R.id.home_tvhome);
		tvSearch = (TextView)findViewById(R.id.home_tvsearch);
		tvCart = (TextView)findViewById(R.id.home_tvcart);
		tvAccount = (TextView)findViewById(R.id.home_tvaccount);
		tvMore = (TextView)findViewById(R.id.home_tvmore);
		tvTitle = (TextView)findViewById(R.id.home_tvtitle);
		
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnAccount.setOnClickListener(this);
		lnMore.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		//----------Home is clicked----------
		case R.id.home_lnhome:
			if (FileUtil.currentButton != Constants.BUTTON_HOME) {
				tvHome.setTextColor(getResources().getColor(R.color.crisom_red));
				ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ico_home_active));
			}
			
			FileUtil.currentButton = Constants.BUTTON_HOME;
			break;
		
		//----------Search is clicked----------
		case R.id.home_lnsearch:
			
			FileUtil.currentButton = Constants.BUTTON_SEARCH;
			break;
		//----------Cart is clicked----------
		case R.id.home_lncart:
			
			FileUtil.currentButton = Constants.BUTTON_CART;
			break;
		//----------Account is clicked----------
		case R.id.home_lnaccount:
			
			FileUtil.currentButton = Constants.BUTTON_ACCOUNT;
			break;
		//----------More is clicked----------
		case R.id.home_lnmore:
			
			FileUtil.currentButton = Constants.BUTTON_MORE;
			break;	

		default:
			break;
		}
		
	}
	
	
}
