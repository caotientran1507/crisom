package com.zdh.crisom;

import com.zdh.crisom.utility.Constants;
import com.zdh.crisom.utility.FileUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact;
	private ImageView ivContact;	
	private Button btnLogin;
	private TextView tvTitle;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		init();
	}
	
	private void init(){
		initView();
		initData();
		initDataWebservice();
	}	

	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnhome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnsearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lncategory);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lncart);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lncontact);		
		
		ivContact = (ImageView)findViewById(R.id.include_footer_ivcategory);	
		
		tvTitle = (TextView)findViewById(R.id.include_header_tvtitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
	
		ivContact.setImageResource(R.drawable.ico_category_active);
		tvTitle.setText("CONTACT");
				
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
				
	}
	
	private void initData() {
		if (FileUtil.flagLogin) {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		} else {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGIN);
		}
	}
	
	private void initDataWebservice(){
	
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		//----------------Click tab bottom--------------------
		//----------Home is clicked----------
		case R.id.include_footer_lnhome:
			Intent home = new Intent(ContactActivity.this, HomeActivity.class);
			startActivity(home);
			break;		
		//----------Search is clicked----------
		case R.id.include_footer_lnsearch:
//			Intent intent = new Intent(HomeActivity.this, );
//			startActivity(intent);
			break;
			
		//----------Category is clicked----------
		case R.id.include_footer_lncategory:
			Intent category = new Intent(ContactActivity.this, CategoryActivity.class);
			startActivity(category);
			break;
			
		//----------Cart is clicked----------
		case R.id.include_footer_lncart:
			Intent cart = new Intent(ContactActivity.this, LoginActivity.class);
			startActivity(cart);
			break;
			
		//----------Contact is clicked----------
		case R.id.include_footer_lncontact:
			break;	
		
			
		case R.id.include_header_btnLogin:
			Intent login = new Intent(ContactActivity.this, LoginActivity.class);
			startActivity(login);
			break;	
			
		
		default:
			break;
		}
		
	}
	
	
	
	
	
	
	
}
