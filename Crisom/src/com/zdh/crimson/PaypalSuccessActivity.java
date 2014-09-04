package com.zdh.crimson;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;
import com.zdh.crimson.utility.StackActivity;

public class PaypalSuccessActivity extends BaseActivity implements View.OnClickListener{

	TextView tvOrder;
	Button btnContinue;
	String url = "";
	TextView tvTitle;	
	private ImageView ivCart;
	private ProgressDialog pDialog;
	String idOrder = "";
	private LinearLayout lnHome,lnSearch,lnCategory,lnContact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paypal_success);	
		
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lnContact);
		
		tvOrder = (TextView)findViewById(R.id.paypal_success_tvOrder);
		btnContinue = (Button)findViewById(R.id.paypal_success_btnContinue);

		ivCart = (ImageView)findViewById(R.id.include_footer_ivCart);	
		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnBack = (Button)findViewById(R.id.include_header_btnBack);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		ivCart.setImageResource(R.drawable.ico_category_active);
		
		btnBack.setVisibility(View.GONE);
		btnLogin.setVisibility(View.GONE);
		tvTitle.setText("PAYMENT");
		
		
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnContact.setOnClickListener(this);		
		btnBack.setOnClickListener(this);
		
		pDialog = new ProgressDialog(PaypalSuccessActivity.this);
		Log.d("idOrder", "idOrder");
		if (getIntent().getExtras() != null) {
			idOrder = getIntent().getExtras().getString(Constants.KEY_ORDERID);
			Log.d("idOrder", "idOrder"+idOrder);
			tvOrder.setText(idOrder);	
		}else{
			new GetLastOrderNumberAsyncTask(String.valueOf(SharedPreferencesUtil.getIdCustomerLogin(PaypalSuccessActivity.this))).execute();
		}
		
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

	//--------------------GetAll----------------------------------------
	public class GetLastOrderNumberAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		String idOrder;
		public GetLastOrderNumberAsyncTask(String idOrder){
			this.idOrder = idOrder;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idOrder)));
				json = JsonParser.makeHttpRequest(Constants.URL_GETLASTORDERNUMBER, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {  
					
					JSONObject jsonObject = new JSONObject(json);
					idOrder = jsonObject.getString("id");
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tvOrder.setText(idOrder);							
						}
					});

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {	
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}			
		}
	}

}
