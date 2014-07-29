package com.zdh.crimson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class CategoryListActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact;
	private ListView lvCategory;
	private ImageView ivCategory;	
	private Button btnLogin,btnBack;
	private TextView tvTitle;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		init();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (SharedPreferencesUtil.getFlagLogin(CategoryListActivity.this)) {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		} else {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGIN);
		}
		
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
		
		lvCategory = (ListView)findViewById(R.id.productlist_lv);	
		
		ivCategory = (ImageView)findViewById(R.id.include_footer_ivcategory);	
		
		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		btnBack = (Button)findViewById(R.id.include_header_btnBack);
	
		ivCategory.setImageResource(R.drawable.ico_category_active);
		tvTitle.setText("CATEGORIES");
				
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		
		lvCategory.setOnItemClickListener(new OnItemClickListener() {
		   @Override
		   public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {		
			   
		   } 
		});
				
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
			Intent home = new Intent(CategoryListActivity.this, HomeActivity.class);
			startActivity(home);
			break;		
		//----------Search is clicked----------
		case R.id.include_footer_lnSearch:
			Intent intent = new Intent(CategoryListActivity.this, SearchActivity.class);
			startActivity(intent);
			break;
			
		//----------Category is clicked----------
		case R.id.include_footer_lnCategory:
			
			break;
			
		//----------Cart is clicked----------
		case R.id.include_footer_lnCart:
			Intent cart = new Intent(CategoryListActivity.this, CartActivity.class);
			startActivity(cart);
			break;
			
		//----------Contact is clicked----------
		case R.id.include_footer_lnContact:
			Intent contact = new Intent(CategoryListActivity.this, ContactActivity.class);
			startActivity(contact);
			break;	
		
			
		case R.id.include_header_btnLogin:
			Intent login = new Intent(CategoryListActivity.this, LoginActivity.class);
			startActivity(login);
			break;	
			
		case R.id.include_header_btnBack:
			
			break;		
			
		
		default:
			break;
		}
		
	}
	
	
	
	
	
	
	
}
