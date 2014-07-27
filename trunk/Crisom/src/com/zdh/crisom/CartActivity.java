package com.zdh.crisom;

import java.util.ArrayList;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zdh.crisom.adapter.RecentAdapter;
import com.zdh.crisom.model.RecentObject;
import com.zdh.crisom.utility.CommonUtil;
import com.zdh.crisom.utility.Constants;
import com.zdh.crisom.utility.FileUtil;
import com.zdh.crisom.utility.JsonParser;
import com.zdh.crisom.utility.SharedPreferencesUtil;

public class CartActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact;
	private ImageView ivCart;	
	private Button btnLogin,btnBack,btnCheckOut;
	private TextView tvTitle,tvCountItem,tvTotal,tvThereis,tvItem;
	private ListView listview;
	private ProgressDialog pDialog;
	
	
	RecentAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);
		init();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (SharedPreferencesUtil.getFlagLogin(CartActivity.this)) {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		} else {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGIN);
		}
		
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
		
		ivCart = (ImageView)findViewById(R.id.include_footer_ivCart);	
		
		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		btnBack = (Button)findViewById(R.id.include_header_btnBack);
		btnCheckOut = (Button)findViewById(R.id.cart_btnCheckout);
		
		tvCountItem = (TextView)findViewById(R.id.cart_tvCountItem);
		tvThereis = (TextView)findViewById(R.id.cart_tvThereIs);
		tvItem = (TextView)findViewById(R.id.cart_tvItem);
		tvTotal = (TextView)findViewById(R.id.cart_tvTotal);
		
		listview = (ListView)findViewById(R.id.cart_lvItem);
		
		ivCart.setImageResource(R.drawable.ico_cart_active);
		
		tvTitle.setText("MY CART");
		btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		btnBack.setVisibility(View.INVISIBLE);
		
		btnCheckOut.setOnClickListener(this);
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
		   @Override
		   public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {		
			   
			   Intent intent = new Intent(CartActivity.this,ProductDetailActivity.class);
			   intent.putExtra(Constants.KEY_PRODUCTID, FileUtil.listRecent.get(position).getIdEntity());
			   startActivity(intent);	
			   overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
		   } 
		});
				
	}
	
	private void initData() {
		adapter = new RecentAdapter(CartActivity.this, FileUtil.listRecent);
		listview.setAdapter(adapter);
	}
	
	private void initDataWebservice(){
		new GetRecentProductAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CartActivity.this)).execute();
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		//----------------Click tab bottom--------------------
		//----------Home is clicked----------
		case R.id.include_footer_lnHome:
			Intent home = new Intent(CartActivity.this, HomeActivity.class);
			startActivity(home);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;		
		//----------Search is clicked----------
		case R.id.include_footer_lnSearch:
			Intent search = new Intent(CartActivity.this, SearchActivity.class);
			startActivity(search);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;
			
		//----------Category is clicked----------
		case R.id.include_footer_lnCategory:
			Intent category = new Intent(CartActivity.this, CategoryActivity.class);
			startActivity(category);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;
			
		//----------Cart is clicked----------
		case R.id.include_footer_lnCart:
			
			break;
			
		//----------Contact is clicked----------
		case R.id.include_footer_lnContact:
			Intent contact = new Intent(CartActivity.this, ContactActivity.class);
			startActivity(contact);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			break;	
		
			
		case R.id.include_header_btnLogin:
			
			if (btnLogin.getText().toString().trim().equals(Constants.TEXT_BUTTON_LOGIN)) {
				Intent login = new Intent(CartActivity.this, LoginActivity.class);
				startActivity(login);
			}else{
				
			}
			
			
			break;	
			
		case R.id.cart_btnCheckout:
			Intent checkout = new Intent(CartActivity.this, CheckoutActivity.class);
			startActivity(checkout);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			break;	
			
		
		default:
			break;
		}
		
	}
	
	//--------------------GetModelAsyncTask----------------------------------------
	public class GetRecentProductAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int cid;
		public GetRecentProductAsyncTask(int cid){
			this.cid = cid;
		}
	   	    

		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(CartActivity.this);
	        pDialog.setMessage("Loading...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }

	    protected String doInBackground(String... params) {
	    	
	    	try {
                // Building Parameters
                List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
                paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(cid)));
                json = JsonParser.makeHttpRequest(
                		Constants.URL_GETCARTITEM, "GET", paramsUrl);
                Log.d("json", json);
                if ((json != null) || (!json.equals(""))) {               
                	JSONArray array = new JSONArray(json);
                	FileUtil.listRecent.clear();
        			for (int j = 0; j < array.length(); j++) {
        				RecentObject temp = new RecentObject();
        				temp.setIdEntity(array.getJSONObject(j).getInt("entity_id"));
        				temp.setIdItem(array.getJSONObject(j).getInt("item_id"));
        				temp.setName(array.getJSONObject(j).getString("name"));
        				temp.setDesc(array.getJSONObject(j).getString("desc"));
        				temp.setPrice(array.getJSONObject(j).getDouble("price")); 
        				temp.setQuantity(array.getJSONObject(j).getInt("qty"));   
        				temp.setImage(array.getJSONObject(j).getString("image"));   
        				FileUtil.listRecent.add(temp);
        			}
        			
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

	        return null;
	    }

	    protected void onPostExecute(String file_url) {	   
	    	tvCountItem.setText(String.valueOf(FileUtil.listRecent.size()));
	    	tvTotal.setText(CommonUtil.formatMoney(CommonUtil.getTotal()));
	    	changeTextThereIs();
	    	adapter.notifyDataSetChanged();
	    	pDialog.dismiss();	
	    }
	}
	
	
	
	
	private void changeTextThereIs(){
		if (FileUtil.listRecent.size() <= 1) {
			tvThereis.setText(Constants.TEXT_THEREIS);
			tvItem.setText(Constants.TEXT_ITEM);
		}else{
			tvThereis.setText(Constants.TEXT_THEREARE);
			tvItem.setText(Constants.TEXT_ITEMS);
		}
	}
	
	
}
