package com.zdh.crimson;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.zdh.crimson.adapter.ChildAdapter;
import com.zdh.crimson.adapter.SearchAdapter;
import com.zdh.crimson.model.Product;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class SearchActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact,lnSearchImage;
	private ImageView ivSearch;	
	private Button btnLogin;
	private TextView tvTitle;
	private EditText edtSearch;
	private ListView listview;
	SearchAdapter adapter;
	private ProgressDialog pDialog;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		init();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ChangeTextButtonLogin();
		adapter.notifyDataSetChanged();
	}
	
	private void init(){
		initView();
		handleOtherAction();
		initData();
	}	

	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lnCart);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lnContact);		
		
		edtSearch = (EditText)findViewById(R.id.include_search_edt);
		lnSearchImage = (LinearLayout)findViewById(R.id.include_search_lnImage);
		
		ivSearch = (ImageView)findViewById(R.id.include_footer_ivsearch);	
		
		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		ivSearch.setImageResource(R.drawable.ico_category_active);
		
		listview = (ListView)findViewById(R.id.search_lv);
		
		tvTitle.setText("SEARCH");
		ivSearch.setImageResource(R.drawable.ico_search_active);
		
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		lnSearchImage.setOnClickListener(this);	
		
		listview.setOnItemClickListener(new OnItemClickListener() {
	       @Override
	       public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {			
	    	   Intent intent = new Intent(SearchActivity.this,ProductDetailActivity.class);
				intent.putExtra(Constants.KEY_PRODUCTID, FileUtil.listSearch.get(position).getId());
				startActivity(intent);
	       } 
		});
				
	}
	
	private void handleOtherAction(){
		edtSearch.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		        	new SearchAsyncTask(edtSearch.getText().toString().trim()).execute();
		        }
		        return handled;
		    }
		});
	}
	
	private void initData() {		
		adapter = new SearchAdapter(SearchActivity.this, FileUtil.listSearch);
		listview.setAdapter(adapter);
		if (getIntent().getExtras() != null) {
			String key = getIntent().getExtras().getString(Constants.KEY_SEARCH);
			new SearchAsyncTask(key).execute();
			edtSearch.setText(key);
		}
	}
	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		//----------------Click tab bottom--------------------
		//----------Home is clicked----------
		case R.id.include_footer_lnHome:
			Intent home = new Intent(SearchActivity.this, HomeActivity.class);
			startActivity(home);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;		
		//----------Search is clicked----------
		case R.id.include_footer_lnSearch:
			break;
			
		//----------Category is clicked----------
		case R.id.include_footer_lnCategory:
			Intent category = new Intent(SearchActivity.this, CategoryActivity.class);
			startActivity(category);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			break;
			
		//----------Cart is clicked----------
		case R.id.include_footer_lnCart:
			if (!SharedPreferencesUtil.getFlagLogin(SearchActivity.this)) {
				showDialog(SearchActivity.this,Constants.WARNING_LOGIN_TITLE, Constants.WARNING_LOGIN_MESSAGE);
			}else{
				Intent cart = new Intent(SearchActivity.this, CartActivity.class);
				startActivity(cart);
				overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			}
			break;
			
		//----------Contact is clicked----------
		case R.id.include_footer_lnContact:
			Intent contact = new Intent(SearchActivity.this, ContactActivity.class);
			startActivity(contact);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			break;	
		
			
		case R.id.include_header_btnLogin:
			if (btnLogin.getText().toString().trim().equals(Constants.TEXT_BUTTON_LOGIN)) {
				Intent login = new Intent(SearchActivity.this, LoginActivity.class);
				startActivity(login);
			}else{
				showDialog(SearchActivity.this,Constants.CONFIRM_LOGOUT_TITLE, Constants.CONFIRM_LOGOUT_MESSAGE);
			}
			break;	
			
		case R.id.include_search_lnImage:
			new SearchAsyncTask(edtSearch.getText().toString().trim()).execute();
			break;	
			
		
		default:
			break;
		}
		
	}
	
	
	//------------------------------------------------------------
	
	public class SearchAsyncTask extends AsyncTask<String, String, String> {

		private String key;
		private String json;
		
		public SearchAsyncTask(String key){
			this.key = key;
		}
	   
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(SearchActivity.this);
	        pDialog.setMessage("Loading...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }

	    protected String doInBackground(String... params) {
	    	
	    	try {
                // Building Parameters
                List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
                paramsUrl.add(new BasicNameValuePair("q", key));

                json = JsonParser.makeHttpRequest(
                		Constants.URL_SEARCH, "GET", paramsUrl);
                Log.d("json", "json"+json);
                if ((json != null) || (!json.equals(""))) {               
                	JSONArray array = new JSONArray(json);
                	FileUtil.listSearch.clear();
        			for (int j = 0; j < array.length(); j++) {
        				Product temp = new Product();
        				temp.setId(array.getJSONObject(j).getInt("entity_id"));
        				temp.setName(array.getJSONObject(j).getString("name"));
        				temp.setDes(array.getJSONObject(j).getString("description"));    
        				temp.setShortDes(array.getJSONObject(j).getString("short_description"));    
        				temp.setImage(array.getJSONObject(j).getString("image"));
        				FileUtil.listSearch.add(temp);					
        			}
        			
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

	        return null;
	    }

	    protected void onPostExecute(String file_url) {
	    	adapter.notifyDataSetChanged();
	        pDialog.dismiss();	      
	    }
	}
	

	public void showDialog(final Context mContext, String title, String msg){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(msg);

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.delete);

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						SharedPreferencesUtil.saveFlagLogin(false, 0,mContext);
						ChangeTextButtonLogin();
						dialog.dismiss();
					}
				});
		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int which) {
						
						dialog.dismiss();
						
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}
	
	public void ChangeTextButtonLogin(){
		if (SharedPreferencesUtil.getFlagLogin(SearchActivity.this)) {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		} else {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGIN);
		}
	}
	
	
	
	
}
