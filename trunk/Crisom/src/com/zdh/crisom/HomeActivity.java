package com.zdh.crisom;

import com.zdh.crisom.utility.Constants;
import com.zdh.crisom.utility.FileUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class HomeActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	LinearLayout lnHome,lnSearch,lnCategory,lnAccount,lnMore;
	TextView tvHome,tvSearch,tvCart,tvAccount,tvMore,tvTitle;
	ImageView ivHome,ivSearch,ivCart,ivAccount,ivMore;
	
	ViewFlipper viewFlipper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		init();
	}
	
	private void init(){
		initView();
		initDataWebservice();
	}
	
	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.home_lnhome);
		lnSearch = (LinearLayout)findViewById(R.id.home_lnsearch);
		lnCategory = (LinearLayout)findViewById(R.id.home_lncategory);
		lnAccount = (LinearLayout)findViewById(R.id.home_lnaccount);
		lnMore = (LinearLayout)findViewById(R.id.home_lnmore);
		
		ivHome = (ImageView)findViewById(R.id.home_ivhome);
		ivSearch = (ImageView)findViewById(R.id.home_ivsearch);
		ivCart = (ImageView)findViewById(R.id.home_ivcart);
		ivAccount = (ImageView)findViewById(R.id.home_ivaccount);
		ivMore = (ImageView)findViewById(R.id.home_ivmore);
		
		tvHome = (TextView)findViewById(R.id.home_tvhome);
		tvSearch = (TextView)findViewById(R.id.home_tvsearch);
		tvCart = (TextView)findViewById(R.id.home_tvcategory);
		tvAccount = (TextView)findViewById(R.id.home_tvaccount);
		tvMore = (TextView)findViewById(R.id.home_tvmore);
		tvTitle = (TextView)findViewById(R.id.home_tvtitle);
		
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnAccount.setOnClickListener(this);
		lnMore.setOnClickListener(this);
		
	}
	
	private void initDataWebservice(){
		FileUtil.listImageSlide.add(FileUtil.imageSlide1);
		FileUtil.listImageSlide.add(FileUtil.imageSlide2);
		FileUtil.listImageSlide.add(FileUtil.imageSlide3);
		FileUtil.listImageSlide.add(FileUtil.imageSlide4);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		//----------Home is clicked----------
		case R.id.home_lnhome:
			if(FileUtil.currentButton != Constants.BUTTON_HOME){
				tvHome.setTextColor(getResources().getColor(R.color.crisom_red));
				ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ico_home_active));
				tvTitle.setText("HOME");
			}
			
			
			
			
			switch (FileUtil.currentButton) {
			case Constants.BUTTON_HOME:
				 
				break;
			case Constants.BUTTON_SEARCH:

				tvSearch.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.ico_search));
				
				break;
			case Constants.BUTTON_CATEGORY:
				tvCart.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivCart.setImageDrawable(getResources().getDrawable(R.drawable.ico_cart));
				
				break;
				
			case Constants.BUTTON_ACCOUNT:
				tvAccount.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.ico_account));
				
				break;
			case Constants.BUTTON_MORE:
				tvMore.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivMore.setImageDrawable(getResources().getDrawable(R.drawable.ico_more));
				
				break;
				
			default:
				break;
			}
			
			FileUtil.currentButton = Constants.BUTTON_HOME;
			break;
		
		//----------Search is clicked----------
		case R.id.home_lnsearch:
			if(FileUtil.currentButton != Constants.BUTTON_SEARCH){
				tvSearch.setTextColor(getResources().getColor(R.color.crisom_red));
				ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.ico_search_active));
				tvTitle.setText("SEARCH");
			}
			
			
			switch (FileUtil.currentButton) {
			case Constants.BUTTON_HOME:
				
				tvHome.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ico_home));
				break;
			case Constants.BUTTON_SEARCH:
				
				break;
			case Constants.BUTTON_CATEGORY:
				tvCart.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivCart.setImageDrawable(getResources().getDrawable(R.drawable.ico_cart));
				
				break;
				
			case Constants.BUTTON_ACCOUNT:
				tvAccount.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.ico_account));
				
				break;
			case Constants.BUTTON_MORE:
				tvMore.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivMore.setImageDrawable(getResources().getDrawable(R.drawable.ico_more));
				
				break;
				
			default:
				break;
			}
			
			FileUtil.currentButton = Constants.BUTTON_SEARCH;
			break;
			
		//----------Category is clicked----------
		case R.id.home_lncategory:
			if(FileUtil.currentButton != Constants.BUTTON_CATEGORY){
				tvCart.setTextColor(getResources().getColor(R.color.crisom_red));
				ivCart.setImageDrawable(getResources().getDrawable(R.drawable.ico_cart_active));
				tvTitle.setText("CATEGORY");
			}
			
			switch (FileUtil.currentButton) {
			case Constants.BUTTON_HOME:
				
				tvHome.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ico_home));
				break;
			case Constants.BUTTON_SEARCH:
				
				tvSearch.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.ico_search));
				
				break;
			case Constants.BUTTON_CATEGORY:
				
				
				break;
				
			case Constants.BUTTON_ACCOUNT:
				tvAccount.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.ico_account));
				
				break;
			case Constants.BUTTON_MORE:
				tvMore.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivMore.setImageDrawable(getResources().getDrawable(R.drawable.ico_more));
				
				break;
				
			default:
				break;
			}
			FileUtil.currentButton = Constants.BUTTON_CATEGORY;
			break;
			
		//----------Account is clicked----------
		case R.id.home_lnaccount:
			if(FileUtil.currentButton != Constants.BUTTON_ACCOUNT){
				tvAccount.setTextColor(getResources().getColor(R.color.crisom_red));
				ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.ico_account_active));
				tvTitle.setText("ACCOUNT");
			}
			
			switch (FileUtil.currentButton) {
			case Constants.BUTTON_HOME:
				
				tvHome.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ico_home));
				break;
			case Constants.BUTTON_SEARCH:
				
				tvSearch.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.ico_search));
				
				break;
			case Constants.BUTTON_CATEGORY:
				
				tvCart.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivCart.setImageDrawable(getResources().getDrawable(R.drawable.ico_cart));
				
				break;
				
			case Constants.BUTTON_ACCOUNT:
				
				
				break;
			case Constants.BUTTON_MORE:
				tvMore.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivMore.setImageDrawable(getResources().getDrawable(R.drawable.ico_more));
				
				break;
				
			default:
				break;
			}
			FileUtil.currentButton = Constants.BUTTON_ACCOUNT;
			break;
			
		//----------More is clicked----------
		case R.id.home_lnmore:
			
			if(FileUtil.currentButton != Constants.BUTTON_MORE){
				tvMore.setTextColor(getResources().getColor(R.color.crisom_red));
				ivMore.setImageDrawable(getResources().getDrawable(R.drawable.ico_more_active));
				tvTitle.setText("MORE");
			}
			
			switch (FileUtil.currentButton) {
			case Constants.BUTTON_HOME:
				
				tvHome.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ico_home));
				break;
			case Constants.BUTTON_SEARCH:
				
				tvSearch.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivSearch.setImageDrawable(getResources().getDrawable(R.drawable.ico_search));
				
				break;
			case Constants.BUTTON_CATEGORY:
				
				tvCart.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivCart.setImageDrawable(getResources().getDrawable(R.drawable.ico_cart));
				
				break;
				
			case Constants.BUTTON_ACCOUNT:
				
				tvAccount.setTextColor(getResources().getColor(R.color.crisom_grayitem));
				ivAccount.setImageDrawable(getResources().getDrawable(R.drawable.ico_account));
				
				break;
			case Constants.BUTTON_MORE:
				
				break;
				
			default:
				break;
			}
			
			FileUtil.currentButton = Constants.BUTTON_MORE;
			break;	

		default:
			break;
		}
		
	}
	
	
	
	private void setFlipperImage(int res) {	   
	    ImageView image = new ImageView(getApplicationContext());
	    image.setBackgroundResource(res);
	    viewFlipper.addView(image);
	}
}
