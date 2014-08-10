package com.zdh.crimson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zdh.crimson.adapter.CheckoutAdapter;
import com.zdh.crimson.model.CountryObject;
import com.zdh.crimson.model.StateObject;
import com.zdh.crimson.utility.CommonUtil;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.ExpandableHeightListView;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class CheckoutActivity extends BaseActivity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnContact;
	private ImageView ivCart;	
	private Button btnProceedCheckout,btnContinueShopping,btnClearShopping,btnUpdateShopping,btnProceedMutilAddress
	,btnApplyCoupon,btnCancelCoupon,btnQuote;
	private TextView tvTitle,tvSubTotal,tvTax,tvGrandTotal,tvShippingHandling,tvCoupon;
	private ExpandableHeightListView listview;	
	private EditText edtCoupon,edtState;
	private Spinner spnCountry,spnState;

	ArrayAdapter<String> countriesAdapter;
	ArrayAdapter<String> statesAdapter;

	CheckoutAdapter adapter;
	int positionState = 0 ;
	int positionCountry = 0 ;

	String sSubtotal = "";
	String sShipping = "";
	String sTax = "";
	String sGrandTotal = "";


	private ProgressDialog pDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		init();
	}

	private void init(){
		initView();
		handleOtherAction();
		initData();
	}	



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!SharedPreferencesUtil.getFlagLogin(getApplicationContext())){
			this.finish();
		}else{
			initDataWebservice();
		}
	}

	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lnContact);		

		ivCart = (ImageView)findViewById(R.id.include_footer_ivCart);	

		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		btnBack = (Button)findViewById(R.id.include_header_btnBack);

		btnProceedCheckout = (Button)findViewById(R.id.checkout_btnProceedCheckout);
		btnContinueShopping = (Button)findViewById(R.id.checkout_btnContinueShopping);
		btnClearShopping = (Button)findViewById(R.id.checkout_btnClearShopping);
		btnUpdateShopping = (Button)findViewById(R.id.checkout_btnUpdateShopping);
		btnProceedMutilAddress = (Button)findViewById(R.id.checkout_btnProceedMutilAddress);
		btnApplyCoupon = (Button)findViewById(R.id.checkout_btnApplyCoupon);
		btnCancelCoupon = (Button)findViewById(R.id.checkout_btnCancelCoupon);
		tvCoupon = (TextView)findViewById(R.id.checkout_tvCoupon);
		btnQuote = (Button)findViewById(R.id.checkout_btnQuote);

		listview = (ExpandableHeightListView)findViewById(R.id.checkout_lv);

		tvSubTotal = (TextView)findViewById(R.id.checkout_tvSubTotal);
		tvTax = (TextView)findViewById(R.id.checkout_tvTax);
		tvShippingHandling = (TextView)findViewById(R.id.checkout_tvShippingHandling);
		tvGrandTotal = (TextView)findViewById(R.id.checkout_tvGrandTotal);		
		edtCoupon = (EditText)findViewById(R.id.checkout_edtCoupon);
		spnState = (Spinner)findViewById(R.id.checkout_spnState);
		edtState = (EditText)findViewById(R.id.checkout_edtState);
		spnCountry = (Spinner)findViewById(R.id.checkout_spnCountry);

		ivCart.setImageResource(R.drawable.ico_cart_active);

		tvTitle.setText("CHECK OUT");
		btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		btnBack.setVisibility(View.VISIBLE);

		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnProceedCheckout.setOnClickListener(this);
		btnContinueShopping.setOnClickListener(this);
		btnClearShopping.setOnClickListener(this);
		btnUpdateShopping.setOnClickListener(this);
		btnProceedMutilAddress.setOnClickListener(this);
		btnApplyCoupon.setOnClickListener(this);
		btnQuote.setOnClickListener(this);
		btnCancelCoupon.setOnClickListener(this);

		pDialog = new ProgressDialog(CheckoutActivity.this);

	}

	private void initData() {		
		Collections.sort(FileUtil.countries);  
		Collections.sort(FileUtil.states); 

		countriesAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, FileUtil.countries);
		spnCountry.setAdapter(countriesAdapter);

		statesAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, FileUtil.states);
		spnState.setAdapter(statesAdapter);

		adapter = new CheckoutAdapter(CheckoutActivity.this, FileUtil.listRecent);
		listview.setAdapter(adapter);
		listview.setExpanded(true);
		listview.setFocusable(false);
	}

	private void initDataWebservice(){
		new GetCountryAsyncTask().execute();
	}

	private void handleOtherAction(){
		spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 	
				if (FileUtil.listCountry.size() > 0) {
					new GetStateAsyncTask(FileUtil.listCountry.get(i).getCode()).execute();
					positionCountry = i;
				}
				
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 

		spnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
				positionState = i;
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {		

			} 
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {

		//----------Button is clicked----------
		case R.id.checkout_btnProceedCheckout:
			Intent checkoutdetail = new Intent(CheckoutActivity.this, CheckoutDetailActivity.class);
			startActivity(checkoutdetail);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_CHECKOUTDETAIL;
			break;	


		case R.id.checkout_btnContinueShopping:
			Intent continueShopping = new Intent(CheckoutActivity.this, HomeActivity.class);
			startActivity(continueShopping);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_HOME;
			break;	

		case R.id.checkout_btnClearShopping:
			new ClearShoppingCartAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutActivity.this)).execute();
			break;	
		case R.id.checkout_btnUpdateShopping:
			new UpdateShoppingCartAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutActivity.this)).execute();
			break;	
		case R.id.checkout_btnProceedMutilAddress:
			Intent checkoutdetail2 = new Intent(CheckoutActivity.this, CheckoutDetailActivity.class);
			startActivity(checkoutdetail2);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_CHECKOUTDETAIL;
			break;	
		case R.id.checkout_btnApplyCoupon:
			String coupon = edtCoupon.getText().toString().trim();
			if (coupon.equals("")) {
				Toast.makeText(CheckoutActivity.this, "Please input coupon!", Toast.LENGTH_SHORT).show();
			}else{
				new ApplyCouponAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutActivity.this), coupon).execute();
			}

			break;

		case R.id.checkout_btnCancelCoupon:
			new CancelCouponAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutActivity.this)).execute();

			break;
		case R.id.checkout_btnQuote:
			//			if (CommonUtil.checkNumbericAndGreaterZero(CheckoutActivity.this,edtZipCode.getText().toString().trim(), "Postal Code")) {
			//				if (FileUtil.listState.size() <= 0) {
			//					new GetAQuoteAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutActivity.this), FileUtil.listCountry.get(positionCountry).getCode(), edtState.getText().toString().trim(), edtZipCode.getText().toString().trim()).execute();
			//				}else
			//				{
			//					new GetAQuoteAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutActivity.this), FileUtil.listCountry.get(positionCountry).getCode(), FileUtil.listState.get(positionState).getCode(), edtZipCode.getText().toString().trim()).execute();
			//				}				
			//			} 

			break;	

		default:
			break;
		}

	}


	//--------------------ClearShoppingCart----------------------------------------
	public class ClearShoppingCartAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		public ClearShoppingCartAsyncTask(int idCustomer){
			this.idCustomer = idCustomer;
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
				json = JsonParser.makeHttpRequest(
						Constants.URL_CLEARCART, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONObject jsonObject = new JSONObject(json);
					boolean s = jsonObject.getBoolean("info");
					if (s) {
						return "true";
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String s) {	
			if (s == null) {
				Toast.makeText(CheckoutActivity.this, "Can not clear your cart. Please try again!", Toast.LENGTH_SHORT).show();
			}
			else{
				FileUtil.listRecent.clear();
				Toast.makeText(CheckoutActivity.this, "Clear your cart success!", Toast.LENGTH_SHORT).show();
				adapter.notifyDataSetChanged();
				finish();
				overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			}
			pDialog.dismiss();	
		}
	}

	//--------------------GetState----------------------------------------
	public class GetStateAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		String idCountry;
		public GetStateAsyncTask(String idCountry){
			this.idCountry = idCountry;
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("country_id", String.valueOf(idCountry)));
				json = JsonParser.makeHttpRequest(
						Constants.URL_GETSTATE, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					FileUtil.listState.clear();
					for (int j = 0; j < array.length(); j++) {
						StateObject temp = new StateObject();
						temp.setName(array.getJSONObject(j).getString("name"));  
						temp.setCode(array.getJSONObject(j).getString("code"));   
						FileUtil.listState.add(temp);
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {	 
			if (FileUtil.listState.size() > 0 ) {
				spnState.setVisibility(View.VISIBLE);
				edtState.setVisibility(View.GONE);
			}else{
				spnState.setVisibility(View.GONE);
				edtState.setVisibility(View.VISIBLE);
			}

			updateListState();
			statesAdapter.notifyDataSetChanged();
			pDialog.dismiss();	
		}
	}

	//--------------------GetCountry----------------------------------------
	public class GetCountryAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		public GetCountryAsyncTask(){
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				json = JsonParser.makeHttpRequest(
						Constants.URL_GETCOUNTRY, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					FileUtil.listState.clear();
					for (int j = 0; j < array.length(); j++) {
						CountryObject temp = new CountryObject();
						temp.setName(array.getJSONObject(j).getString("name"));  
						temp.setCode(array.getJSONObject(j).getString("country_id"));   
						FileUtil.listCountry.add(temp);
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {	 
			updateListCountry();
			countriesAdapter.notifyDataSetChanged();
			pDialog.dismiss();	
			new GetCartCodeAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutActivity.this)).execute();
		}
	}

	//--------------------UpdateShoppingCart----------------------------------------
	public class UpdateShoppingCartAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		public UpdateShoppingCartAsyncTask(int idCustomer){
			this.idCustomer = idCustomer;
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();

				String ids = "";

				String[] arrIds = new String[FileUtil.listCartChange.size()];
				int i = 0;
				for (int key : FileUtil.listCartChange.keySet()) {
					arrIds[i] = String.valueOf(key);
					i++;
				}
				ids = CommonUtil.combineString(arrIds, ",");

				String qty = "";
				String[] arrQty = new String[FileUtil.listCartChange.size()];
				int j = 0;
				for (String value : FileUtil.listCartChange.values()) {
					arrQty[j] = value;
					j++;
				}
				qty = CommonUtil.combineString(arrQty, ",");

				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
				paramsUrl.add(new BasicNameValuePair("ids", ids));
				paramsUrl.add(new BasicNameValuePair("qty", qty));


				json = JsonParser.makeHttpRequest(
						Constants.URL_UPDATECART, "GET", paramsUrl);     
				if ((json != null) || (!json.equals(""))) {               
					JSONObject jsonObject = new JSONObject(json);
					boolean s = jsonObject.getBoolean("info");
					if (s) {
						return "true";
					}
				}                
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String s) {	  	    	
			pDialog.dismiss();	
			if (s == null) {				
				Toast.makeText(CheckoutActivity.this, "Update shopping cart failed!", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(CheckoutActivity.this, "Update shopping cart success!", Toast.LENGTH_SHORT).show();
				new GetCartCodeAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutActivity.this)).execute();
			}

		}
	}

	//--------------------ApplyCoupon----------------------------------------
	public class ApplyCouponAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		String code;
		public ApplyCouponAsyncTask(int idCustomer, String code){
			this.idCustomer = idCustomer;
			this.code = code;
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
				paramsUrl.add(new BasicNameValuePair("code", code));
				json = JsonParser.makeHttpRequest(
						Constants.URL_APPLYCOUPON, "GET", paramsUrl);

				if ((json != null) || (!json.equals(""))) {               
					JSONObject jsonObject = new JSONObject(json);
					boolean s = jsonObject.getBoolean("info");
					Log.d("json", json);
					if (s) {
						return "true";
					}
				}      

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return "false";
		}

		protected void onPostExecute(String s) {	     
			if (s.equals("true")) {
				tvCoupon.setVisibility(View.VISIBLE);
				btnCancelCoupon.setVisibility(View.VISIBLE);
				Toast.makeText(CheckoutActivity.this, "Apply coupon success!", Toast.LENGTH_SHORT).show();				
				pDialog.dismiss();	
				new GetCartCodeAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutActivity.this)).execute();
				
			}else{
				Toast.makeText(CheckoutActivity.this, "Apply coupon fail!", Toast.LENGTH_SHORT).show();
				pDialog.dismiss();	
			}
			
		}
	}

	//--------------------CancelCoupon----------------------------------------
	public class CancelCouponAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		public CancelCouponAsyncTask(int idCustomer){
			this.idCustomer = idCustomer;
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
				json = JsonParser.makeHttpRequest(
						Constants.URL_CANCELCOUPON, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONObject jsonObject = new JSONObject(json);
					boolean s = jsonObject.getBoolean("info");
					if (s) {
						return "true";
					}
				} 
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return "false";
		}

		protected void onPostExecute(String s) {	   
			if (s.equals("true")) {
				Toast.makeText(CheckoutActivity.this, "Cancel coupon success!", Toast.LENGTH_SHORT).show();
				tvCoupon.setVisibility(View.GONE);
				btnCancelCoupon.setVisibility(View.GONE);
				edtCoupon.setText("");
				pDialog.dismiss();	
				new GetCartCodeAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutActivity.this)).execute();
			}else{
				Toast.makeText(CheckoutActivity.this, "Cancel coupon fail!", Toast.LENGTH_SHORT).show();
				pDialog.dismiss();	
				new GetCartCodeAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutActivity.this)).execute();
			}
			
		}
	}

	//--------------------GetAQuote----------------------------------------
	

	//--------------------GetCartCode----------------------------------------
	public class GetCartCodeAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		public GetCartCodeAsyncTask(int idCustomer){
			this.idCustomer = idCustomer;
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
				json = JsonParser.makeHttpRequest(
						Constants.URL_GETCARTCOST, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONObject jsonObject = new JSONObject(json);
					JSONObject jsonObjectSubtotal = jsonObject.getJSONObject("subtotal");
					JSONObject jsonObjectShipping = jsonObject.getJSONObject("shipping");
					JSONObject jsonObjectTax = jsonObject.getJSONObject("tax");
					JSONObject jsonObjectGrandTotal = jsonObject.getJSONObject("grandtotal");

					sSubtotal = jsonObjectSubtotal.getString("cost");
					sShipping = jsonObjectShipping.getString("cost");
					sTax = jsonObjectTax.getString("cost");
					sGrandTotal = jsonObjectGrandTotal.getString("cost");

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tvSubTotal.setText(sSubtotal);
							tvShippingHandling.setText(sShipping);
							tvTax.setText(sTax);
							tvGrandTotal.setText(sGrandTotal);
						}
					});

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {	      
			pDialog.dismiss();	
		}
	}

	private void updateListCountry(){
		if (FileUtil.listCountry.size() > 0) {
			FileUtil.countries.clear();
			for (int i = 0; i < FileUtil.listCountry.size(); i++) {
				FileUtil.countries.add(FileUtil.listCountry.get(i).getName());
			}
		}		
	}

	private void updateListState(){
		if (FileUtil.listState.size() > 0) {
			FileUtil.states.clear();
			for (int i = 0; i < FileUtil.listState.size(); i++) {
				FileUtil.states.add(FileUtil.listState.get(i).getName());
			}
		}
		
	}

}
