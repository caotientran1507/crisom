package com.zdh.crisom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zdh.crisom.adapter.CheckoutAdapter;
import com.zdh.crisom.model.CountryObject;
import com.zdh.crisom.model.StateObject;
import com.zdh.crisom.utility.CommonUtil;
import com.zdh.crisom.utility.Constants;
import com.zdh.crisom.utility.FileUtil;
import com.zdh.crisom.utility.JsonParser;

public class CheckoutActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact;
	private ImageView ivCart;	
	private Button btnLogin,btnBack,btnProceedCheckout,btnContinueShopping,btnClearShopping,btnUpdateShopping,btnProceedMutilAddress
	,btnApplyCoupon,btnQuote;
	private TextView tvTitle,tvSubTotal,tvTax,tvGrandTotal;
	private ListView listview;	
	private EditText edtCoupon,edtZipCode;
	private Spinner spnCountry,spnState;
	
	ArrayAdapter<String> countriesAdapter;
	
	CheckoutAdapter adapter;
	
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
		initDataWebservice();
	}	

	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lnCart);
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
		btnQuote = (Button)findViewById(R.id.checkout_btnQuote);
		
		listview = (ListView)findViewById(R.id.checkout_lv);
		tvSubTotal = (TextView)findViewById(R.id.checkout_tvSubTotal);
		tvTax = (TextView)findViewById(R.id.checkout_tvTax);
		tvGrandTotal = (TextView)findViewById(R.id.checkout_tvGrandTotal);
		edtCoupon = (EditText)findViewById(R.id.checkout_edtCoupon);
		spnState = (Spinner)findViewById(R.id.checkout_spnState);
		edtZipCode = (EditText)findViewById(R.id.checkout_edtZipCode);
		spnCountry = (Spinner)findViewById(R.id.checkout_spnCountry);
		
		ivCart.setImageResource(R.drawable.ico_cart_active);
		
		tvTitle.setText("CHECK OUT");
		btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		btnBack.setVisibility(View.VISIBLE);
		
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
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
				
	}
	
	private void initData() {		
        Collections.sort(FileUtil.countries);  
		countriesAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, FileUtil.countries);
		spnCountry.setAdapter(countriesAdapter);
		
		adapter = new CheckoutAdapter(CheckoutActivity.this, FileUtil.listRecent);
		listview.setAdapter(adapter);
		
		tvSubTotal.setText(CommonUtil.formatMoney(CommonUtil.getTotal()));
	}
	
	private void initDataWebservice(){
		if (FileUtil.listCountry.size() <= 0 ) {
			new GetCountryAsyncTask().execute();
		}
	}
	
	private void handleOtherAction(){
		spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
		    	new GetStateAsyncTask(FileUtil.listCountry.get(i).getCode()).execute();
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
		
		switch (v.getId()) {
		//----------------Click tab bottom--------------------
		//----------Home is clicked----------
		case R.id.include_footer_lnHome:
			Intent home = new Intent(CheckoutActivity.this, HomeActivity.class);
			startActivity(home);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;		
		//----------Search is clicked----------
		case R.id.include_footer_lnSearch:
			Intent search = new Intent(CheckoutActivity.this, SearchActivity.class);
			startActivity(search);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;
			
		//----------Category is clicked----------
		case R.id.include_footer_lnCategory:
			Intent category = new Intent(CheckoutActivity.this, CategoryActivity.class);
			startActivity(category);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;
			
		//----------Cart is clicked----------
		case R.id.include_footer_lnCart:
			
			break;
			
		//----------Contact is clicked----------
		case R.id.include_footer_lnContact:
			Intent contact = new Intent(CheckoutActivity.this, ContactActivity.class);
			startActivity(contact);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			break;	
		
			
		case R.id.include_header_btnLogin:
			Intent login = new Intent(CheckoutActivity.this, LoginActivity.class);
			startActivity(login);
			break;	
			
		case R.id.include_header_btnBack:
			finish();
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;		
				
			
		//----------Button is clicked----------
		case R.id.checkout_btnProceedCheckout:
			Intent checkoutdetail = new Intent(CheckoutActivity.this, CheckoutDetailActivity.class);
			startActivity(checkoutdetail);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			break;	
			
			
		case R.id.checkout_btnContinueShopping:
			Intent continueShopping = new Intent(CheckoutActivity.this, HomeActivity.class);
			startActivity(continueShopping);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;	
			
		case R.id.checkout_btnClearShopping:
			
			break;	
		case R.id.checkout_btnUpdateShopping:
			
			break;	
		case R.id.checkout_btnProceedMutilAddress:
			
			break;	
		case R.id.checkout_btnApplyCoupon:
			String coupon = edtCoupon.getText().toString().trim();
			if (coupon.equals("")) {
				Toast.makeText(CheckoutActivity.this, "Please input coupon!", Toast.LENGTH_SHORT).show();
			}else{
				
			}
			
			break;
		case R.id.checkout_btnQuote:
			
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
	        pDialog = new ProgressDialog(CheckoutActivity.this);
	        pDialog.setMessage("Loading...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }

	    protected String doInBackground(String... params) {
	    	
	    	try {
                // Building Parameters
                List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
                paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
                json = JsonParser.makeHttpRequest(
                		Constants.URL_GETMODEL, "GET", paramsUrl);
                Log.d("json", json);
                if ((json != null) || (!json.equals(""))) {               
                	JSONArray array = new JSONArray(json);
        			for (int j = 0; j < array.length(); j++) {
        				String name = array.getString(j);      						
        				
        			}
        			
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
	        pDialog = new ProgressDialog(CheckoutActivity.this);
	        pDialog.setMessage("Loading...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
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
	        pDialog = new ProgressDialog(CheckoutActivity.this);
	        pDialog.setMessage("Loading...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }

	    protected String doInBackground(String... params) {
	    	
	    	try {
                // Building Parameters
                List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
                json = JsonParser.makeHttpRequest(
                		Constants.URL_GETSTATE, "GET", paramsUrl);
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
	        pDialog = new ProgressDialog(CheckoutActivity.this);
	        pDialog.setMessage("Loading...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }

	    protected String doInBackground(String... params) {
	    	
	    	try {
                // Building Parameters
                List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
                paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
                json = JsonParser.makeHttpRequest(
                		Constants.URL_GETMODEL, "GET", paramsUrl);
                Log.d("json", json);
                if ((json != null) || (!json.equals(""))) {               
                	JSONArray array = new JSONArray(json);
        			for (int j = 0; j < array.length(); j++) {
        				String name = array.getString(j);      						
        				
        			}
        			
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
	
	//--------------------ApplyCoupon----------------------------------------
	public class ApplyCouponAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		public ApplyCouponAsyncTask(int idCustomer){
			this.idCustomer = idCustomer;
		}
	   	    

		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(CheckoutActivity.this);
	        pDialog.setMessage("Loading...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }

	    protected String doInBackground(String... params) {
	    	
	    	try {
                // Building Parameters
                List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
                paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
                json = JsonParser.makeHttpRequest(
                		Constants.URL_GETMODEL, "GET", paramsUrl);
                if ((json != null) || (!json.equals(""))) {               
                	JSONArray array = new JSONArray(json);
        			for (int j = 0; j < array.length(); j++) {
        				String name = array.getString(j);      						
        				
        			}
        			
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
	
	//--------------------GetAQuote----------------------------------------
	public class GetAQuoteAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		String country;
		String state;
		String postalCode;
		public GetAQuoteAsyncTask(int idCustomer,String country,String state,String postalCode){
			this.idCustomer = idCustomer;
			this.country = country;
			this.state = state;
			this.postalCode = postalCode;
		}
	   	    

		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(CheckoutActivity.this);
	        pDialog.setMessage("Loading...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }

	    protected String doInBackground(String... params) {
	    	
	    	try {
                // Building Parameters
                List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
                paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
                paramsUrl.add(new BasicNameValuePair("country", country));
                paramsUrl.add(new BasicNameValuePair("state", state));
                paramsUrl.add(new BasicNameValuePair("postalCode", postalCode));
                json = JsonParser.makeHttpRequest(
                		Constants.URL_GETMODEL, "GET", paramsUrl);
                Log.d("json", json);
                if ((json != null) || (!json.equals(""))) {               
                	JSONArray array = new JSONArray(json);
        			for (int j = 0; j < array.length(); j++) {
        				String name = array.getString(j);      						
        				
        			}
        			
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
		FileUtil.countries.clear();
		for (int i = 0; i < FileUtil.listCountry.size(); i++) {
			FileUtil.countries.add(FileUtil.listCountry.get(i).getName());
		}
	}
	
	
}
