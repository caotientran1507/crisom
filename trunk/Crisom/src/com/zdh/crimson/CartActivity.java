package com.zdh.crimson;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

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
import android.widget.Toast;

import com.zdh.crimson.adapter.CartAdapter;
import com.zdh.crimson.model.CartObject;
import com.zdh.crimson.utility.CommonUtil;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class CartActivity extends BaseActivity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnContact;
	private ImageView ivCart;	
	private Button btnBack,btnCheckOut;
	private TextView tvTitle,tvCountItem,tvTotal,tvThereis,tvItem;
	private ListView listview;
	private ProgressDialog pDialog;
	CartAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);
		init();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(!SharedPreferencesUtil.getFlagLogin(getApplicationContext())){
			this.finish();
		}else{
			updateText();
			adapter.notifyDataSetChanged();
			ChangeTextButtonLogin();
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
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
		   @Override
		   public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {		
			   
			   Intent intent = new Intent(CartActivity.this,ProductDetailActivity.class);
			   intent.putExtra(Constants.KEY_PRODUCTID, FileUtil.listCart.get(position).getIdEntity());
			   startActivity(intent);
			   overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			   
			   overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			   FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_PRODUCTDETAIL;
		   } 
		});
		pDialog = new ProgressDialog(CartActivity.this);
				
	}
	
	private void initData() {
		adapter = new CartAdapter(CartActivity.this, FileUtil.listCart);
		listview.setAdapter(adapter);
	}
	
	private void initDataWebservice(){
		new GetCartAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CartActivity.this)).execute();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
			
		case R.id.cart_btnCheckout:
			if (FileUtil.listCart.size() > 0) {
				Intent checkout = new Intent(CartActivity.this, CheckoutActivity.class);
				startActivity(checkout);
				overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
				FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_CHECKOUT;
			}
			else{
				Toast.makeText(CartActivity.this, "No item in your cart!", Toast.LENGTH_SHORT).show();
			}
			break;	
			
		
		default:
			break;
		}
		
	}
	
	//--------------------GetModelAsyncTask----------------------------------------
	public class GetCartAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int cid;
		public GetCartAsyncTask(int cid){
			this.cid = cid;
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
                paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(cid)));
                json = JsonParser.makeHttpRequest(
                		Constants.URL_GETCARTITEM, "GET", paramsUrl);
                Log.d("json", json);
                if ((json != null) || (!json.equals(""))) {               
                	JSONArray array = new JSONArray(json);
                	FileUtil.listCart.clear();
        			for (int j = 0; j < array.length(); j++) {
        				CartObject temp = new CartObject();
        				temp.setIdEntity(array.getJSONObject(j).getInt("entity_id"));
        				temp.setIdItem(array.getJSONObject(j).getInt("item_id"));
        				temp.setName(array.getJSONObject(j).getString("name"));
        				temp.setDesc(array.getJSONObject(j).getString("desc"));
        				temp.setPrice(array.getJSONObject(j).getDouble("price")); 
        				temp.setQuantity(array.getJSONObject(j).getInt("qty"));   
        				temp.setImage(array.getJSONObject(j).getString("image"));   
        				temp.setColor(array.getJSONObject(j).getString("color"));
        				temp.setOptionName(array.getJSONObject(j).getString("option_name"));
        				FileUtil.listCart.add(temp);
        			}
        			
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

	        return null;
	    }

	    protected void onPostExecute(String file_url) {
	    	updateText();
	    	adapter.notifyDataSetChanged();
	    	pDialog.dismiss();	
	    }
	}
	
	
	private void updateText(){
		if (FileUtil.listCart.size() > 0) {
			tvCountItem.setText(String.valueOf(getQuantityAll()));
		} else {
			tvCountItem.setText("0");
		}		
    	tvTotal.setText(CommonUtil.formatMoney(CommonUtil.getTotal()));
    	changeTextThereIs();
	}
	
	private int getQuantityAll(){
		int i = 0;
		for (int j = 0; j < FileUtil.listCart.size(); j++) {
			i = i + FileUtil.listCart.get(j).getQuantity();
		}		
		return i;		
	}
	
	
	private void changeTextThereIs(){
		if (FileUtil.listCart.size() <= 1) {
			tvThereis.setText(Constants.TEXT_THEREIS);
			tvItem.setText(Constants.TEXT_ITEM);
		}else{
			tvThereis.setText(Constants.TEXT_THEREARE);
			tvItem.setText(Constants.TEXT_ITEMS);
		}
	}
	
}
