package com.zdh.crisom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zdh.crisom.utility.Constants;
import com.zdh.crisom.utility.FileUtil;

public class ProductListActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact;
	private ListView listCategory;
	private ImageView ivCategory;	
	private Button btnLogin;
	private TextView tvTitle;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_list);
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
		
		listCategory = (ListView)findViewById(R.id.categoty_list_lv);	
		
		ivCategory = (ImageView)findViewById(R.id.include_footer_ivcategory);	
		
		tvTitle = (TextView)findViewById(R.id.include_header_tvtitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
	
		ivCategory.setImageResource(R.drawable.ico_category_active);
		tvTitle.setText("CATEGORIES");
				
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
			Intent home = new Intent(ProductListActivity.this, HomeActivity.class);
			startActivity(home);
			break;		
		//----------Search is clicked----------
		case R.id.include_footer_lnsearch:
//			Intent intent = new Intent(HomeActivity.this, );
//			startActivity(intent);
			break;
			
		//----------Category is clicked----------
		case R.id.include_footer_lncategory:
			
			break;
			
		//----------Cart is clicked----------
		case R.id.include_footer_lncart:
			Intent cart = new Intent(ProductListActivity.this, LoginActivity.class);
			startActivity(cart);
			break;
			
		//----------Contact is clicked----------
		case R.id.include_footer_lncontact:
			Intent contact = new Intent(ProductListActivity.this, ContactActivity.class);
			startActivity(contact);
			break;	
		
			
		case R.id.include_header_btnLogin:
			Intent login = new Intent(ProductListActivity.this, LoginActivity.class);
			startActivity(login);
			break;	
		
		default:
			break;
		}
		
	}
	
	
	
	
	
	
	
}
