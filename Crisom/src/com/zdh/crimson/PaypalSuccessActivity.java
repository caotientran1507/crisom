package com.zdh.crimson;

import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.StackActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PaypalSuccessActivity extends BaseActivity implements View.OnClickListener{
	
	TextView tvOrder;
	Button btnContinue;
	String url = "";
	TextView tvTitle;	
	private ImageView ivCart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paypal_success);		
		tvOrder = (TextView)findViewById(R.id.paypal_success_tvOrder);
		btnContinue = (Button)findViewById(R.id.paypal_success_btnContinue);
		
		ivCart = (ImageView)findViewById(R.id.include_footer_ivCart);	
		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnBack = (Button)findViewById(R.id.include_header_btnBack);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		ivCart.setImageResource(R.drawable.ico_category_active);

		tvTitle.setText("PAYPAL");
		btnBack.setVisibility(View.INVISIBLE);
		btnLogin.setVisibility(View.INVISIBLE);
				
		tvOrder.setText(FileUtil.orderID);
		btnContinue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent home = new Intent(getApplicationContext(), HomeActivity.class);
				startActivity(home);
				FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_HOME;
				overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);	
				
				StackActivity.getInstance().finishAll();
				
			}
		});

	}
	
}
