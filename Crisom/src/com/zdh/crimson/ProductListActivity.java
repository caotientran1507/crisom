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

import com.zdh.crimson.adapter.ProductListAdapter;
import com.zdh.crimson.model.Product;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class ProductListActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact,lnNarrowTitle,lnNarrowContent;
	private ListView lvProduct,lvCheckbox;
	private ImageView ivCategory,ivNarrowShow;	
	private Button btnLogin,btnBack;
	private TextView tvTitle;
	private ProgressDialog pDialog;
	ProductListAdapter adapter;
	static int currentCategory = 0;
	
	ArrayList<Boolean> listCheckbox = new ArrayList<Boolean>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		currentCategory = getIntent().getExtras().getInt(Constants.KEY_CATEGORYID);		
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
		initData();
		initDataWebservice();
	}
	
	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lnCart);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lnContact);	
		
		lnNarrowTitle = (LinearLayout)findViewById(R.id.productlist_Narrow_lnTitle);
		lnNarrowContent = (LinearLayout)findViewById(R.id.productlist_Narrow_lnContent);
		ivNarrowShow = (ImageView)findViewById(R.id.productlist_Narrow_img);	
		lvProduct = (ListView)findViewById(R.id.productlist_lv);
		lvCheckbox = (ListView)findViewById(R.id.productlist_lvCheckbox);
		
		ivCategory = (ImageView)findViewById(R.id.include_footer_ivcategory);	
		
		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		btnBack = (Button)findViewById(R.id.include_header_btnBack);
		
		ivCategory.setImageResource(R.drawable.ico_category_active);
		tvTitle.setText("CATEGORIES");
				
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		lnNarrowTitle.setOnClickListener(this);
		
		lvProduct.setOnItemClickListener(new OnItemClickListener() {
		   @Override
		   public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {	
			   
			   Intent intent = new Intent(ProductListActivity.this,ProductDetailActivity.class);
			   intent.putExtra(Constants.KEY_PRODUCTID, FileUtil.listProduct.get(position).getId());
			   startActivity(intent);
		   } 
		});
				
	}
	
	private void initData() {
		
		
		adapter = new ProductListAdapter(ProductListActivity.this, FileUtil.listProduct);
		lvProduct.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	private void initDataWebservice(){
		new GetProductsByCategoryIdAsyncTask(currentCategory).execute();

	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		//----------------Click tab bottom--------------------
		//----------Home is clicked----------
		case R.id.include_footer_lnHome:
			Intent home = new Intent(ProductListActivity.this, HomeActivity.class);
			startActivity(home);
			break;		
		//----------Search is clicked----------
		case R.id.include_footer_lnSearch:
			Intent intent = new Intent(ProductListActivity.this, SearchActivity.class);
			startActivity(intent);
			break;
			
		//----------Category is clicked----------
		case R.id.include_footer_lnCategory:
			
			break;
			
		//----------Cart is clicked----------
		case R.id.include_footer_lnCart:
			Intent cart = new Intent(ProductListActivity.this, LoginActivity.class);
			startActivity(cart);
			break;
			
		//----------Contact is clicked----------
		case R.id.include_footer_lnContact:
			Intent contact = new Intent(ProductListActivity.this, ContactActivity.class);
			startActivity(contact);
			break;	
		
			
		case R.id.include_header_btnLogin:
			if (btnLogin.getText().toString().trim().equals(Constants.TEXT_BUTTON_LOGIN)) {
				Intent login = new Intent(ProductListActivity.this, LoginActivity.class);
				startActivity(login);
			}else{
				showDialog(ProductListActivity.this);
			}
			break;	
			
		case R.id.include_header_btnBack:
			finish();
			break;	
			
			
		case R.id.productlist_Narrow_lnTitle:
			if (lnNarrowContent.getVisibility() == View.VISIBLE) {
				lnNarrowContent.setVisibility(View.GONE);
				ivNarrowShow.setImageResource(R.drawable.ico_down_white);
			}else{
				lnNarrowContent.setVisibility(View.VISIBLE);
				ivNarrowShow.setImageResource(R.drawable.ico_next_white);
			}
			
			break;		
		
		default:
			break;
		}
		
	}
	
	//------------------------------------------------------------
	
	public class GetProductsByCategoryIdAsyncTask extends AsyncTask<String, String, String> {

		private int cat_id;
		private String json;
		
		public GetProductsByCategoryIdAsyncTask(int cat_id){
			this.cat_id = cat_id;
		}
	   
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(ProductListActivity.this);
	        pDialog.setMessage("Loading...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }

	    protected String doInBackground(String... params) {
	    	
	    	try {
                // Building Parameters
                List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
                paramsUrl.add(new BasicNameValuePair("cat_id", String.valueOf(cat_id)));

                json = JsonParser.makeHttpRequest(
                		Constants.URL_GETPRODUCTBYCATEGORYID, "GET", paramsUrl);
                Log.d("json", "json"+json);
                if ((json != null) || (!json.equals(""))) {               
                	JSONArray array = new JSONArray(json);
                	FileUtil.listProduct.clear();
        			for (int j = 0; j < array.length(); j++) {
        				Product temp = new Product();
        				temp.setId(array.getJSONObject(j).getInt("entity_id"));
        				temp.setName(array.getJSONObject(j).getString("name"));
        				temp.setDes(array.getJSONObject(j).getString("description"));    
        				temp.setShortDes(array.getJSONObject(j).getString("short_description"));    
        				temp.setImage(array.getJSONObject(j).getString("image"));
        				FileUtil.listProduct.add(temp);					
        			}
        			
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

	        return null;
	    }

	    protected void onPostExecute(String file_url) {
	    	Log.d("FileUtil.listProduct", "FileUtil.listProduct"+FileUtil.listProduct.size());
	    	adapter.notifyDataSetChanged();
	        pDialog.dismiss();	      
	    }
	}
	
	
	public void showDialog(final Context mContext){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("Confirm Log out...");

		// Setting Dialog Message
		alertDialog.setMessage("Are you sure you want log out now?");

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.delete);

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						SharedPreferencesUtil.saveFlagLogin(false, 0,mContext);
						ChangeTextButtonLogin();
						adapter.notifyDataSetChanged();
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
		if (SharedPreferencesUtil.getFlagLogin(ProductListActivity.this)) {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		} else {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGIN);
		}		
	}
	
	
}
