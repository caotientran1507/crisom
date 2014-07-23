package com.zdh.crisom;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zdh.crisom.model.Customer;
import com.zdh.crisom.utility.Constants;
import com.zdh.crisom.utility.JsonParser;
import com.zdh.crisom.utility.SharedPreferencesUtil;

public class LoginActivity extends Activity implements View.OnClickListener{

	EditText edtEmail,edtPass;
	Button btnLogin,btnCancel,btnLoginTop;
	private ProgressDialog pDialog;
	
	String email;
	String pass;
	
	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact;		
	private TextView tvTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		init();
	}
	
	private void init(){
    	initView();
    	initData();
    }
    
	private void initView() {
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lnCart);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lnContact);		
		
		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);		
	
		tvTitle.setText("LOG IN");
				
		
		edtEmail = (EditText)findViewById(R.id.login_edt_email);
		edtPass = (EditText)findViewById(R.id.login_edt_pass);
		btnLogin = (Button)findViewById(R.id.login_btn_login);
		btnCancel = (Button)findViewById(R.id.login_btn_cancel);
		btnLoginTop = (Button)findViewById(R.id.include_header_btnLogin);
		
		btnLoginTop.setVisibility(View.INVISIBLE);
		
		btnLogin.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		
	}

	private void initData(){
    
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//----------------Click tab bottom--------------------
		//----------Home is clicked----------
		case R.id.include_footer_lnHome:
			Intent home = new Intent(LoginActivity.this, HomeActivity.class);
			startActivity(home);
			overridePendingTransition(R.anim.slide_in_down,
				    R.anim.slide_out_down);
			break;		
		//----------Search is clicked----------
		case R.id.include_footer_lnSearch:
			Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fly_out_to_bottom, R.anim.stay);
			break;
			
		//----------Category is clicked----------
		case R.id.include_footer_lnCategory:
			Intent category = new Intent(LoginActivity.this, CategoryActivity.class);
			startActivity(category);
			overridePendingTransition(R.anim.fly_out_to_bottom, R.anim.stay);
			break;
			
		//----------Cart is clicked----------
		case R.id.include_footer_lnCart:
			Intent cart = new Intent(LoginActivity.this, LoginActivity.class);
			startActivity(cart);
			overridePendingTransition(R.anim.fly_out_to_bottom, R.anim.stay);
			break;
			
		//----------Contact is clicked----------
		case R.id.include_footer_lnContact:
			Intent contact = new Intent(LoginActivity.this, ContactActivity.class);
			startActivity(contact);
			overridePendingTransition(R.anim.fly_out_to_bottom, R.anim.stay);
			break;	
				
				
		
		
		case R.id.login_btn_login:
			if (checkValidInput()) {
				new LoginAsyncTask(email, pass).execute();				
			}
			break;
		case R.id.login_btn_cancel:
			finish();
			overridePendingTransition(R.anim.fly_out_to_bottom, R.anim.stay);
			break;

		default:
			break;
		}
		
	}
	
	private boolean checkValidInput(){
		
		email = edtEmail.getText().toString().trim();
		if (email.equals("")) {
			Toast.makeText(LoginActivity.this, "Please input email!", Toast.LENGTH_LONG).show();
			return false;
		}
		pass = edtPass.getText().toString().trim();
		if (pass.equals("")) {
			Toast.makeText(LoginActivity.this, "Please input password!", Toast.LENGTH_LONG).show();
			return false;
		}
		
		return true;
	}
	
	//------------------------------------------------------------
	
	public class LoginAsyncTask extends AsyncTask<String, String, String> {

		private String email;
		private String pass;
		private String json;
		
		public LoginAsyncTask(String email, String pass){
			this.email = email;
			this.pass = pass;
		}
	   
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(LoginActivity.this);
	        pDialog.setMessage("Checking...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }

	    protected String doInBackground(String... params) {
	    	
	    	try {
                // Building Parameters
                List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
                paramsUrl.add(new BasicNameValuePair("email", email));
                paramsUrl.add(new BasicNameValuePair("pass", pass));

                json = JsonParser.makeHttpRequest(
                		Constants.URL_LOGIN, "GET", paramsUrl);
                
                
                if ((json != null) || (!json.equals(""))) {  
                	JSONObject jsonObject = new JSONObject(json);
                	boolean status = jsonObject.getBoolean("status");
                	if (status) {
                		Customer customer = new Customer();
                		 JSONObject jsonTemp= (JSONObject) new JSONTokener(json).nextValue();
                	     JSONObject jsonInfo = jsonTemp.getJSONObject("info");
                	     customer.setEmail(jsonInfo.getString("email"));	                	     
                	     customer.setId(jsonInfo.getInt("entity_id"));
                	     SharedPreferencesUtil.saveFlagLogin(true, customer.getId(), LoginActivity.this);
                	    
					}
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

	        return null;
	    }

	    protected void onPostExecute(String file_url) {		  	    	
	        pDialog.dismiss();	
	        Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_LONG).show();
	        LoginActivity.this.finish();
	    }
	}
}
