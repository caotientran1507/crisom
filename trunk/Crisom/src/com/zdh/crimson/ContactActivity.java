package com.zdh.crimson;

import com.zdh.crimson.utility.Constants;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactActivity extends BaseActivity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart;
	private ImageView ivContact;	
	private Button btnBack;
	private TextView tvTitle,toll_free_call,phone_local_call,customerservice,info_email,suport_email,orders_email1,orders_email2;


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

		toll_free_call = (TextView)findViewById(R.id.toll_free_call);
		phone_local_call = (TextView)findViewById(R.id.phone_local_call);
		customerservice = (TextView)findViewById(R.id.customerservice);
		info_email = (TextView)findViewById(R.id.info_email);
		orders_email1 = (TextView)findViewById(R.id.orders_email1);
		suport_email = (TextView)findViewById(R.id.suport_email);
		orders_email2 = (TextView)findViewById(R.id.orders_email2);

		toll_free_call.setOnClickListener(this);
		phone_local_call.setOnClickListener(this);
		customerservice.setOnClickListener(this);
		orders_email1.setOnClickListener(this);
		info_email.setOnClickListener(this);
		suport_email.setOnClickListener(this);
		orders_email2.setOnClickListener(this);

		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		btnLogin.setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.toll_free_call:
			callPhone(Constants.phone_free_call);
			break;
		case R.id.phone_local_call:
			callPhone(Constants.phone_local_call);
			break;		
		case R.id.customerservice:
			sendMail(Constants.email_customer, "Customer Service");
			break;
		case R.id.orders_email1:
			sendMail(Constants.email_order, "Order");
			break;
		case R.id.info_email:
			sendMail(Constants.email_info, "Infomation");
			break;
		case R.id.suport_email:
			sendMail(Constants.email_support, "Support");
		case R.id.orders_email2:
			sendMail(Constants.email_order, "Order");
			break;
		default:
			break;
		}
	}

	public void callPhone(String phoneNumber){
		if(phoneNumber.equals("")){
			Toast.makeText(ContactActivity.this, "Phone number is not available", Toast.LENGTH_SHORT).show();
		}else{
			if (!(((TelephonyManager) ContactActivity.this.getSystemService(Context.TELEPHONY_SERVICE)).getPhoneType()== TelephonyManager.PHONE_TYPE_NONE)){
				String number ="tel:"+ phoneNumber;
				Intent iCall = new Intent(Intent.ACTION_CALL, Uri.parse(number));
				startActivity(iCall);
			}else{
				Toast.makeText(ContactActivity.this, "Device does not support phone feature", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void sendMail(String to, String subject) {
		// TODO Auto-generated method stub
		String message = "";

		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
		email.putExtra(Intent.EXTRA_SUBJECT, subject);
		email.putExtra(Intent.EXTRA_TEXT, message);

		// need this to prompts email client only
		email.setType("message/rfc822");

		startActivity(Intent.createChooser(email, "Choose an Email"));
	}


}