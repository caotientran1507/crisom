package com.zdh.crimson;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zdh.crimson.model.Customer;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

	EditText edtEmail,edtPass;
	Button btnLogin,btnCancel,btnLoginTop;
	private ProgressDialog pDialog;
	
	String email;
	String pass;
	
	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnContact;		
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
		lnContact.setOnClickListener(this);
		pDialog = new ProgressDialog(LoginActivity.this);
		
	}

	private void initData(){
    
		
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		
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
		private boolean status = false;
		
		public LoginAsyncTask(String email, String pass){
			this.email = email;
			this.pass = pass;
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
                paramsUrl.add(new BasicNameValuePair("email", email));
                paramsUrl.add(new BasicNameValuePair("pass", pass));

                json = JsonParser.makeHttpRequest(
                		Constants.URL_LOGIN, "GET", paramsUrl);
                
                
                if ((json != null) || (!json.equals(""))) {  
                	JSONObject jsonObject = new JSONObject(json);
                	status = jsonObject.getBoolean("status");
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
	        if(status){
	        	setResult(RESULT_OK);
	        	Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
	        }else{
	        	setResult(RESULT_CANCELED);
	        	Toast.makeText(LoginActivity.this, "Login fail", Toast.LENGTH_SHORT).show();
	        }
	        LoginActivity.this.finish();
	    }
	}
}
