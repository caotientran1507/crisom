package com.zdh.crimson;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactActivity extends BaseActivity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart;
	private ImageView ivContact;	
	private Button btnBack;
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
	}	

	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lnCart);
		
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
		btnLogin.setOnClickListener(this);
				
	}
	
}