package com.zdh.crimson;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.SharedPreferencesUtil;
import com.zdh.crimson.utility.StackActivity;

public class BaseActivity extends Activity implements OnClickListener{

	protected Button btnLogin, btnBack;
	protected int requestCodeLogin = 100;
	protected boolean oldStatusLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Push intent to stack manager
		StackActivity.getInstance().push(this);
		super.onCreate(savedInstanceState);
		
	}
	

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		//----------------Click tab bottom--------------------
				//----------Home is clicked----------
				case R.id.include_footer_lnHome:
					Intent home = new Intent(getApplicationContext(), HomeActivity.class);
					startActivity(home);
					FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_HOME;
					overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);	
					
					// Finish all activity that was opened
					StackActivity.getInstance().finishAll();
					break;
				//----------Search is clicked----------
				case R.id.include_footer_lnSearch:
					Intent search = new Intent(getApplicationContext(), SearchActivity.class);
					startActivity(search);
					if (FileUtil.POSITION_ACTIVITY > Constants.POSITION_ACTIVITY_SEARCH) {
						overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
					}else{
						overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
					}		
					FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_SEARCH;
					
					// Finish all activity that was opened
					StackActivity.getInstance().finishAll();
					break;
					
				//----------Category is clicked----------
				case R.id.include_footer_lnCategory:
					FileUtil.listCategory.clear();
					Intent category = new Intent(getApplicationContext(), CategoryActivity.class);
					startActivity(category);
					if (FileUtil.POSITION_ACTIVITY > Constants.POSITION_ACTIVITY_CATEGORY) {
						overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
					}else{
						overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
					}	
					FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_CATEGORY;
					// Finish all activity that was opened
					StackActivity.getInstance().finishAll();
					break;
					
				//----------Cart is clicked----------
				case R.id.include_footer_lnCart:
					if (!SharedPreferencesUtil.getFlagLogin(getApplicationContext())) {
						showDialog(this,
								   Constants.WARNING_LOGIN_TITLE,
								   Constants.WARNING_LOGIN_MESSAGE);
					} else {
						Intent cart = new Intent(getApplicationContext(),CartActivity.class);
						startActivity(cart);
						if (FileUtil.POSITION_ACTIVITY > Constants.POSITION_ACTIVITY_CART) {
							overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
						}else{
							overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
						}
						FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_CART;
						// Finish all activity that was opened
						StackActivity.getInstance().finishAll();
					}
					
					break;
					
				//----------Contact is clicked----------
				case R.id.include_footer_lnContact:
					Intent contact = new Intent(getApplicationContext(), ContactActivity.class);
					startActivity(contact);
					FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_CONTACT;
					overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
					
					// Finish all activity that was opened
					StackActivity.getInstance().finishAll();
					break;	
				
					
				case R.id.include_header_btnLogin:
					
					if (!SharedPreferencesUtil.getFlagLogin(getApplicationContext())) {
						Intent login = new Intent(getApplicationContext(), LoginActivity.class);
						startActivityForResult(login, requestCodeLogin);
						overridePendingTransition(R.anim.fly_in_from_top, R.anim.stay);	
					}else{
						showDialog(this,Constants.CONFIRM_LOGOUT_TITLE, Constants.CONFIRM_LOGOUT_MESSAGE);
					}
					
					break;
				
				case R.id.include_header_btnBack:
					actionBackButton();
					break;	  
		}
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == requestCodeLogin){
			ChangeTextButtonLogin();
		}			
	}
	
	private void backToHome(){
		ChangeTextButtonLogin();
		if (FileUtil.POSITION_ACTIVITY == Constants.POSITION_ACTIVITY_CHECKOUT 
				|| FileUtil.POSITION_ACTIVITY == Constants.POSITION_ACTIVITY_CHECKOUTDETAIL
				|| FileUtil.POSITION_ACTIVITY == Constants.POSITION_ACTIVITY_CART ) {
			Intent home = new Intent(getApplicationContext(), HomeActivity.class);
			startActivity(home);
			FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_HOME;
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			// Finish all activity that was opened
			StackActivity.getInstance().finishAll();
		}
	}
	
	private void showDialog(final Context mContext, String title, String msg){
		
		final Dialog alert = new Dialog(mContext,R.style.CustomAlertDialog);
		alert.setContentView(R.layout.dialog_alert_login);
		
		//set value to title & message
		TextView alertTitle = (TextView) alert.findViewById(R.id.alert_title);
		alertTitle.setText(title);
		
		TextView alertMessage = (TextView) alert.findViewById(R.id.alert_message);
		alertMessage.setText(msg);
		
		// progress button yes & no
		Button alertYes = (Button) alert.findViewById(R.id.alert_button_yes);
		alertYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (SharedPreferencesUtil.getFlagLogin(getApplicationContext())) {
					SharedPreferencesUtil.saveFlagLogin(false, 0,mContext);
					backToHome();
					logout();
				} else {
					Intent login = new Intent(getApplicationContext(),LoginActivity.class);
					startActivityForResult(login, requestCodeLogin);
					overridePendingTransition(R.anim.fly_in_from_top, R.anim.stay);	
				}
				alert.dismiss();
			}
		});
		
		Button alertNo = (Button) alert.findViewById(R.id.alert_button_no);
		alertNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alert.dismiss();
			}
		});
		
		alert.show();
	}
	
	protected void ChangeTextButtonLogin(){
		if (SharedPreferencesUtil.getFlagLogin(getApplicationContext())) {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
			oldStatusLogin = true;
		} else {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGIN);
			oldStatusLogin = false;
		}
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		actionBackButton();
	}
	
	protected void actionBackButton(){
		
		finish();
		overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
		if (FileUtil.POSITION_ACTIVITY != 0 || FileUtil.POSITION_ACTIVITY != Constants.POSITION_ACTIVITY_CATEGORY) {
			FileUtil.POSITION_ACTIVITY = FileUtil.POSITION_ACTIVITY - 1;
		}	
		
	}
	
	public void logout(){				
	}
}
