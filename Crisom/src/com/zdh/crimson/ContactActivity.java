package com.zdh.crimson;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class ContactActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact;
	private ImageView ivContact;	
	private Button btnLogin,btnBack;
	private TextView tvTitle;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		init();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ChangeTextButtonLogin();
		
	}
	
	
	
	private void init(){
		initView();
		initData();
		initDataWebservice();
	}	

	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lnCart);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lnContact);		
		
		ivContact = (ImageView)findViewById(R.id.include_footer_ivcontact);	
		
		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		btnBack = (Button)findViewById(R.id.include_header_btnBack);
		ivContact.setImageResource(R.drawable.ico_category_active);
		
		tvTitle.setText("CONTACT");
		btnBack.setVisibility(View.INVISIBLE);
		ivContact.setImageResource(R.drawable.ico_contact_active);
		
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
				
	}
	
	private void initData() {
		
	}
	
	private void initDataWebservice(){
	
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		//----------------Click tab bottom--------------------
		//----------Home is clicked----------
		case R.id.include_footer_lnHome:
			Intent home = new Intent(ContactActivity.this, HomeActivity.class);
			startActivity(home);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;		
		//----------Search is clicked----------
		case R.id.include_footer_lnSearch:
			Intent intent = new Intent(ContactActivity.this, SearchActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;
			
		//----------Category is clicked----------
		case R.id.include_footer_lnCategory:
			Intent category = new Intent(ContactActivity.this, CategoryActivity.class);
			startActivity(category);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;
			
		//----------Cart is clicked----------
		case R.id.include_footer_lnCart:
			if (!SharedPreferencesUtil.getFlagLogin(ContactActivity.this)) {
				showDialog(ContactActivity.this,Constants.WARNING_LOGIN_TITLE, Constants.WARNING_LOGIN_MESSAGE);
			}else{
				Intent cart = new Intent(ContactActivity.this, CartActivity.class);
				startActivity(cart);
				overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			}
			
			break;
			
		//----------Contact is clicked----------
		case R.id.include_footer_lnContact:
			break;	
		
			
		case R.id.include_header_btnLogin:
			if (btnLogin.getText().toString().trim().equals(Constants.TEXT_BUTTON_LOGIN)) {
				Intent login = new Intent(ContactActivity.this, LoginActivity.class);
				startActivity(login);
			}else{
				showDialog(ContactActivity.this,Constants.CONFIRM_LOGOUT_TITLE, Constants.CONFIRM_LOGOUT_MESSAGE);				
			}
			break;	
			
		
		default:
			break;
		}
		
	}
	
	
	public void showDialog(final Context mContext, String title, String msg){
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
						SharedPreferencesUtil.saveFlagLogin(false, 0,mContext);
						ChangeTextButtonLogin();
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
	
	public void ChangeTextButtonLogin(){
		if (SharedPreferencesUtil.getFlagLogin(ContactActivity.this)) {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		} else {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGIN);
		}
	}

}