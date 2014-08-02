package com.zdh.crimson;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zdh.crimson.adapter.CategoryAdapter;
import com.zdh.crimson.model.Category;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class CategoryActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact;	
	private ImageView ivCategory;	
	private Button btnLogin,btnBack;
	private TextView tvTitle;
	private ListView lvCategory;
	CategoryAdapter adapter;
	static int currentCategory = 2;
	private ProgressDialog pDialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		if (getIntent().getExtras() != null) {
			currentCategory = getIntent().getExtras().getInt(Constants.KEY_CATEGORYID);	
		
		}
		
		init();
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		ChangeTextButtonLogin();
		initDataWebservice();
		
	}
	
	private void init(){
		initView();
		initData();
	}
	
	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lnCart);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lnContact);	
		
		
		ivCategory = (ImageView)findViewById(R.id.include_footer_ivcategory);	
		
		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		btnBack = (Button)findViewById(R.id.include_header_btnBack);
		lvCategory = (ListView)findViewById(R.id.category_lv);	
	
		ivCategory.setImageResource(R.drawable.ico_category_active);
		tvTitle.setText("CATEGORIES");
		btnBack.setVisibility(View.INVISIBLE);		
				
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
			   
			   
			   if (FileUtil.listCategory.get(position).getSubcat()) {
				   currentCategory = FileUtil.listCategory.get(position).getId();
				   Log.d("currentCategory", ""+currentCategory);
				   new GetCategoriesByIdAsyncTask(currentCategory).execute();
			   }
			   else{
				   Intent intent = new Intent(CategoryActivity.this,ProductListActivity.class);
				   intent.putExtra(Constants.KEY_CATEGORYID, FileUtil.listCategory.get(position).getId());
				   startActivity(intent);
			   }
			   
		   } 
		});
		
		pDialog = new ProgressDialog(CategoryActivity.this);
				
	}
	
	private void initData() {
		
		
		adapter=new CategoryAdapter(CategoryActivity.this, FileUtil.listCategory);        
		lvCategory.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	private void initDataWebservice(){	
			new GetCategoriesByIdAsyncTask(currentCategory).execute();
		
	}


	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		//----------------Click tab bottom--------------------
		//----------Home is clicked----------
		case R.id.include_footer_lnHome:
			Intent home = new Intent(CategoryActivity.this, HomeActivity.class);
			startActivity(home);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;		
		//----------Search is clicked----------
		case R.id.include_footer_lnSearch:
			Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			break;
			
		//----------Category is clicked----------
		case R.id.include_footer_lnCategory:
			
			break;
			
		//----------Cart is clicked----------
		case R.id.include_footer_lnCart:
			if (!SharedPreferencesUtil.getFlagLogin(CategoryActivity.this)) {
				showDialog(CategoryActivity.this,Constants.WARNING_LOGIN_TITLE, Constants.WARNING_LOGIN_MESSAGE);
			}else{
				Intent cart = new Intent(CategoryActivity.this, CartActivity.class);
				startActivity(cart);
				overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			}
			
			break;
			
		//----------Contact is clicked----------
		case R.id.include_footer_lnContact:
			Intent contact = new Intent(CategoryActivity.this, ContactActivity.class);
			startActivity(contact);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			break;	
						
			
		//----------Click Login and Back---------
		case R.id.include_header_btnLogin:
			if (btnLogin.getText().toString().trim().equals(Constants.TEXT_BUTTON_LOGIN)) {
				Intent login = new Intent(CategoryActivity.this, LoginActivity.class);
				startActivity(login);
			}else{
				showDialog(CategoryActivity.this,Constants.CONFIRM_LOGOUT_TITLE, Constants.CONFIRM_LOGOUT_MESSAGE);
			}		
			break;
			
		case R.id.include_header_btnBack:					
			new GetCategoriesByIdAsyncTask(FileUtil.listCategory.get(0).getIdParent()).execute();			
			break;
			
		default:
			break;
		}
		
	}
	
	//------------------------------------------------------------
	
	public class GetCategoriesByIdAsyncTask extends AsyncTask<String, String, String> {

		private int cat_id;
		private String json;
		
		public GetCategoriesByIdAsyncTask(int cat_id){
			this.cat_id = cat_id;
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
                paramsUrl.add(new BasicNameValuePair("cat_id", String.valueOf(cat_id)));

                json = JsonParser.makeHttpRequest(
                		Constants.URL_GETCATEGORIESBYID, "GET", paramsUrl);
                
                if ((json != null) || (!json.equals(""))) {               
                	JSONArray array = new JSONArray(json);
                	FileUtil.listCategory.clear();
        			for (int j = 0; j < array.length(); j++) {
        				Category temp = new Category();
        				temp.setId(array.getJSONObject(j).getInt("id"));
        				temp.setName(array.getJSONObject(j).getString("name"));
        				temp.setSubcat(array.getJSONObject(j).getBoolean("subcat"));    
        				temp.setIdParent(array.getJSONObject(j).getInt("id_parent"));
        				FileUtil.listCategory.add(temp);
        				
        			}
        			
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

	        return null;
	    }

	    protected void onPostExecute(String file_url) {
	    	adapter.notifyDataSetChanged();
	    	if (FileUtil.listCategory.get(0).getIdParent() != 0) {
	    		btnBack.setVisibility(View.VISIBLE);	
			}else{
				btnBack.setVisibility(View.INVISIBLE);								
			}   
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
		if (SharedPreferencesUtil.getFlagLogin(CategoryActivity.this)) {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		} else {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGIN);
		}
	}
}
