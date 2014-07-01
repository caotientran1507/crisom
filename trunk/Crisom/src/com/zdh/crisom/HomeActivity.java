package com.zdh.crisom;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.zdh.crisom.utility.Constants;
import com.zdh.crisom.utility.FileUtil;

public class HomeActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact;
	private ImageView ivHome;	
	private Button btnLogin;
	private TextView tvTitle, tvMainSite;
	
	private Uri uriUrl = Uri.parse(Constants.URL);
	private Animation slideLeftIn, slideLeftOut;
	private ViewFlipper mViewFlipper;	
	private ArrayList<ImageView> listImage = new ArrayList<ImageView>();
	private ImageView img1, img2, img3, img4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		init();
	}
	
	private void init(){
		initView();
		initData();
		initDataWebservice();
	}
	
	private void initData() {
		//------------------------------------------------
		if (FileUtil.flagLogin) {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		} else {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGIN);
		}
		//-------------------------set Animation--------------------------------
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.left_out);
		mViewFlipper.setInAnimation(slideLeftIn);
		mViewFlipper.setOutAnimation(slideLeftOut);
		mViewFlipper.setFlipInterval(3000);
		mViewFlipper.startFlipping();
		
		slideLeftOut.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				int currentViewIndex = mViewFlipper.getDisplayedChild();
				for (int j = 0; j < listImage.size(); j++) {
					if (j != currentViewIndex) {
						listImage.get(j).setImageResource(R.drawable.viewpager_nomal);
					}
				}
				listImage.get(currentViewIndex).setImageResource(R.drawable.viewpager_active);
								
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				
			}
			
		});
		//--------------------------------------------------------------------
	}

	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnhome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnsearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lncategory);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lncart);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lncontact);		
		
		ivHome = (ImageView)findViewById(R.id.include_footer_ivhome);	
		
		tvTitle = (TextView)findViewById(R.id.include_header_tvtitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		
		tvMainSite = (TextView)findViewById(R.id.home_tv_mainsite);
		
		mViewFlipper = (ViewFlipper)findViewById(R.id.home_vf);		
		
		ivHome.setImageResource(R.drawable.ico_home_active);
		tvTitle.setText("HOME");
				
		img1 = (ImageView) findViewById(R.id.home_mark1);
		img2 = (ImageView) findViewById(R.id.home_mark2);
		img3 = (ImageView) findViewById(R.id.home_mark3);
		img4 = (ImageView) findViewById(R.id.home_mark4);
		
		listImage.add(img1);
		listImage.add(img2);
		listImage.add(img3);
		listImage.add(img4);
		
		tvMainSite.setOnClickListener(this);
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		img1.setOnClickListener(this);
		img2.setOnClickListener(this);
		img3.setOnClickListener(this);
		img4.setOnClickListener(this);
		tvTitle.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
				
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
		//----------------Click tab bottom--------------------
		//----------Home is clicked----------
		case R.id.include_footer_lnhome:
			
			break;		
		//----------Search is clicked----------
		case R.id.include_footer_lnsearch:
//			Intent intent = new Intent(HomeActivity.this, );
//			startActivity(intent);
			break;
			
		//----------Category is clicked----------
		case R.id.include_footer_lncategory:
			Intent category = new Intent(HomeActivity.this, LoginActivity.class);
			startActivity(category);
			break;
			
		//----------Cart is clicked----------
		case R.id.include_footer_lncart:
			Intent cart = new Intent(HomeActivity.this, LoginActivity.class);
			startActivity(cart);
			break;
			
		//----------Contact is clicked----------
		case R.id.include_footer_lncontact:
			Intent contact = new Intent(HomeActivity.this, LoginActivity.class);
			startActivity(contact);
			break;	
		
		//----------------Click other--------------------
		case R.id.home_tv_mainsite:
			Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
			startActivity(launchBrowser);
			break;	
			
		case R.id.include_header_btnLogin:
			Intent login = new Intent(HomeActivity.this, LoginActivity.class);
			startActivity(login);
			break;	
			
		//-----------------------------------------------------
		//---------click marker slide image------------
		case R.id.home_mark1:
			for (int j = 0; j < listImage.size(); j++) {
				if (j != 0) {
					listImage.get(j).setImageResource(R.drawable.viewpager_nomal);
				}
			}
			listImage.get(0).setImageResource(R.drawable.viewpager_active);
			mViewFlipper.setDisplayedChild(0);
			break;	
			
		case R.id.home_mark2:
			for (int j = 1; j < listImage.size(); j++) {
				if (j != 1) {
					listImage.get(j).setImageResource(R.drawable.viewpager_nomal);
				}
			}
			listImage.get(1).setImageResource(R.drawable.viewpager_active);
			mViewFlipper.setDisplayedChild(1);		
			break;
			
		case R.id.home_mark3:
			for (int j = 2; j < listImage.size(); j++) {
				if (j != 2) {
					listImage.get(j).setImageResource(R.drawable.viewpager_nomal);
				}
			}
			listImage.get(2).setImageResource(R.drawable.viewpager_active);
			mViewFlipper.setDisplayedChild(2);
			break;
			
		case R.id.home_mark4:
			for (int j = 3; j < listImage.size(); j++) {
				if (j != 3) {
					listImage.get(j).setImageResource(R.drawable.viewpager_nomal);
				}
			}
			listImage.get(3).setImageResource(R.drawable.viewpager_active);
			mViewFlipper.setDisplayedChild(3);
			break;
			
			
			

		default:
			break;
		}
		
	}
	
	
	
	
	
	
	
}
