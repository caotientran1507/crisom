package com.zdh.crimson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import com.zdh.crimson.adapter.CheckoutAdapter;
import com.zdh.crimson.model.CountryObject;
import com.zdh.crimson.model.StateObject;
import com.zdh.crimson.utility.CommonUtil;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class CheckoutActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact;
	private ImageView ivCart;	
	private Button btnLogin,btnBack,btnProceedCheckout,btnContinueShopping,btnClearShopping,btnUpdateShopping,btnProceedMutilAddress
	,btnApplyCoupon,btnCancelCoupon,btnQuote;
	private TextView tvTitle,tvSubTotal,tvTax,tvGrandTotal,tvCoupon;
	private ListView listview;	
	private EditText edtCoupon,edtZipCode,edtState;
	private Spinner spnCountry,spnState;
	
	ArrayAdapter<String> countriesAdapter;
	ArrayAdapter<String> statesAdapter;
	 	
	CheckoutAdapter adapter;
	int positionState = 0 ;
	int positionCountry = 0 ;
	
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
		btnCancelCoupon = (Button)findViewById(R.id.checkout_btnCancelCoupon);
		tvCoupon = (TextView)findViewById(R.id.checkout_tvCoupon);
		btnQuote = (Button)findViewById(R.id.checkout_btnQuote);
		
		listview = (ListView)findViewById(R.id.checkout_lv);
		tvSubTotal = (TextView)findViewById(R.id.checkout_tvSubTotal);
		tvTax = (TextView)findViewById(R.id.checkout_tvTax);
		tvGrandTotal = (TextView)findViewById(R.id.checkout_tvGrandTotal);
		edtCoupon = (EditText)findViewById(R.id.checkout_edtCoupon);
		spnState = (Spinner)findViewById(R.id.checkout_spnState);
		edtState = (EditText)findViewById(R.id.checkout_edtState);
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
		btnCancelCoupon.setOnClickListener(this);
				
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
		    	positionCountry = i;
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
			new ClearShoppingCartAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutActivity.this)).execute();
			break;	
		case R.id.checkout_btnUpdateShopping:
			new UpdateShoppingCartAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutActivity.this)).execute();
			break;	
		case R.id.checkout_btnProceedMutilAddress:
			Intent checkoutdetail2 = new Intent(CheckoutActivity.this, CheckoutDetailActivity.class);
			startActivity(checkoutdetail2);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
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
                String ids = "";
                int step = 0;
                for (int i : FileUtil.listCartChange.keySet()) {
                	if (step == 0) {
                		ids = ids + String.valueOf(i);
					}else{
						ids = "," + ids + String.valueOf(i);
					}
                	
				}
                step = 0;
                String qty = "";
                for (String s : FileUtil.listCartChange.values()) {
                	if (step == 0) {
                		qty = qty + s;
					}else{
						qty = "," + s;
					}
                	qty += s;
				}
                
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
                paramsUrl.add(new BasicNameValuePair("code", code));
                json = JsonParser.makeHttpRequest(
                		Constants.URL_APPLYCOUPON, "GET", paramsUrl);
                
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
	    		tvCoupon.setVisibility(View.VISIBLE);
	    		btnCancelCoupon.setVisibility(View.VISIBLE);
	    	}else{
	    		Toast.makeText(CheckoutActivity.this, "Apply coupn success!", Toast.LENGTH_SHORT).show();
	    	}
	    	pDialog.dismiss();	
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

	        return null;
	    }

	    protected void onPostExecute(String s) {	   
	    	if (s == null) {
	    		
	    	}else{
	    		Toast.makeText(CheckoutActivity.this, "Apply coupn success!", Toast.LENGTH_SHORT).show();
	    		tvCoupon.setVisibility(View.GONE);
	    		btnCancelCoupon.setVisibility(View.GONE);
	    	}
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
	
	private void updateListState(){
		FileUtil.states.clear();
		for (int i = 0; i < FileUtil.listState.size(); i++) {
			FileUtil.states.add(FileUtil.listState.get(i).getName());
		}
	}
	
	
}