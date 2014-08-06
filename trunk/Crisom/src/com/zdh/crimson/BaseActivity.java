package com.zdh.crimson;

import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.SharedPreferencesUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BaseActivity extends Activity implements OnClickListener{

	protected Button btnLogin, btnBack;
	private int requestCodeLogin = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
					break;
					
				//----------Category is clicked----------
				case R.id.include_footer_lnCategory:
					Intent category = new Intent(getApplicationContext(), CategoryActivity.class);
					startActivity(category);
					if (FileUtil.POSITION_ACTIVITY > Constants.POSITION_ACTIVITY_CATEGORY) {
						overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
					}else{
						overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
					}	
					FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_CATEGORY;
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
					}
					break;
					
				//----------Contact is clicked----------
				case R.id.include_footer_lnContact:
					Intent contact = new Intent(getApplicationContext(), ContactActivity.class);
					startActivity(contact);
					FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_CONTACT;
					overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
					break;	
				
					
				case R.id.include_header_btnLogin:
					
					if (!SharedPreferencesUtil.getFlagLogin(getApplicationContext())) {
						Intent login = new Intent(getApplicationContext(), LoginActivity.class);
						startActivity(login);
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
		
		if (requestCode == requestCodeLogin)
			ChangeTextButtonLogin();
	}
	

	private void showDialog(final Context mContext, String title, String msg){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(msg);

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.delete);

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						if (SharedPreferencesUtil.getFlagLogin(getApplicationContext())) {
							SharedPreferencesUtil.saveFlagLogin(false, 0,mContext);
							ChangeTextButtonLogin();
						} else {
							Intent login = new Intent(getApplicationContext(),LoginActivity.class);
							startActivityForResult(login, requestCodeLogin);
							overridePendingTransition(R.anim.fly_in_from_top, R.anim.stay);	
						}
						dialog.dismiss();
					}
				});
		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int which) {
						
						dialog.dismiss();
						
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}
	
	protected void ChangeTextButtonLogin(){
		if (SharedPreferencesUtil.getFlagLogin(getApplicationContext())) {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		} else {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGIN);
		}
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		actionBackButton();
	}
	
	private void actionBackButton(){
		
		finish();
		overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
		if (FileUtil.POSITION_ACTIVITY != 0 || FileUtil.POSITION_ACTIVITY != Constants.POSITION_ACTIVITY_CATEGORY) {
			FileUtil.POSITION_ACTIVITY = FileUtil.POSITION_ACTIVITY - 1;
		}	
		
	}
	
	
}
