package com.zdh.crisom;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdh.crisom.lazylist.ImageLoader;
import com.zdh.crisom.model.DocumentObject;
import com.zdh.crisom.model.OptionObject;
import com.zdh.crisom.model.Product;
import com.zdh.crisom.utility.Constants;
import com.zdh.crisom.utility.JsonParser;
import com.zdh.crisom.utility.SharedPreferencesUtil;

public class ProductDetailActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact,lnTitle;
	
	private ImageView ivCategory,ivAvatar;	
	private Button btnLogin,btnVerify,btnAddToCart;
	private TextView tvTitle,tvTitle1,tvShortDes,tvDes,tvMainSite,tvPrice,tvDownload,tvFAQ,tvVideo,tvCost,tvCostSmall
	,tvPercentProfit,tvCostProfit;
	private EditText edtQuatity;
	static int currentProduct = 0;
	private ProgressDialog pDialog;
	private Product product = new Product();
	public ImageLoader imageLoader; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		currentProduct = getIntent().getExtras().getInt(Constants.KEY_PRODUCTID);
		
		init();
		initDataWebservice();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ChangeTextButtonLogin();
		
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
	
		ivCategory.setImageResource(R.drawable.ico_category_active);
		
		tvTitle1 = (TextView)findViewById(R.id.product_tvTitle1);
		tvShortDes = (TextView)findViewById(R.id.product_tvTitle2);
		tvDes = (TextView)findViewById(R.id.product_tvDescription);
		tvMainSite = (TextView)findViewById(R.id.product_tvMainSite);
		lnTitle = (LinearLayout)findViewById(R.id.product_lnTitle);
		tvPrice = (TextView)findViewById(R.id.product_tvPrice);
		tvDownload = (TextView)findViewById(R.id.product_tvDownload);
		tvFAQ = (TextView)findViewById(R.id.product_tvFAQ);
		tvVideo = (TextView)findViewById(R.id.product_tvVideo);
		tvCost = (TextView)findViewById(R.id.product_tvCost);
		tvCostSmall = (TextView)findViewById(R.id.product_tvCostSmall);
		tvPercentProfit = (TextView)findViewById(R.id.product_profit_tvPercent);
		tvCostProfit = (TextView)findViewById(R.id.product_profit_tvCost);
		
		btnVerify = (Button)findViewById(R.id.product_btnVerify);
		btnAddToCart = (Button)findViewById(R.id.product_btnAddToCart);
		
		edtQuatity = (EditText)findViewById(R.id.product_edtQuatity);
		ivAvatar = (ImageView)findViewById(R.id.product_ivAvatar);
		
		imageLoader = new ImageLoader(ProductDetailActivity.this);

		tvTitle.setText("CATEGORIES");
				
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		tvPrice.setOnClickListener(this);
		tvDownload.setOnClickListener(this);
		tvFAQ.setOnClickListener(this);
		tvVideo.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnVerify.setOnClickListener(this);
		btnAddToCart.setOnClickListener(this);
		tvMainSite.setOnClickListener(this);
				
	}
	
	private void initData() {
		
	}
	
	private void initDataWebservice(){
		new GetProductByIdAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(ProductDetailActivity.this), currentProduct).execute();
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		//----------------Click tab bottom--------------------
		//----------Home is clicked----------
		case R.id.include_footer_lnHome:
			Intent home = new Intent(ProductDetailActivity.this, HomeActivity.class);
			startActivity(home);
			break;		
		//----------Search is clicked----------
		case R.id.include_footer_lnSearch:
//			Intent intent = new Intent(HomeActivity.this, );
//			startActivity(intent);
			break;
			
		//----------Category is clicked----------
		case R.id.include_footer_lnCategory:
			
			break;
			
		//----------Cart is clicked----------
		case R.id.include_footer_lnCart:
			Intent cart = new Intent(ProductDetailActivity.this, CartActivity.class);
			startActivity(cart);
			break;
			
		//----------Contact is clicked----------
		case R.id.include_footer_lnContact:
			Intent contact = new Intent(ProductDetailActivity.this, ContactActivity.class);
			startActivity(contact);
			break;	
		
			
		case R.id.include_header_btnLogin:
			if (btnLogin.getText().toString().trim().equals(Constants.TEXT_BUTTON_LOGIN)) {
				Intent login = new Intent(ProductDetailActivity.this, LoginActivity.class);
				startActivity(login);
			}else{
				showDialog(ProductDetailActivity.this);
			}
			break;	
		case R.id.product_btnVerify:
			
			break;
		case R.id.product_btnAddToCart:
					
			break;	
		case R.id.product_tvMainSite:
			
			break;	
		case R.id.product_tvPrice:
			lnTitle.setBackgroundResource(R.drawable.tab_a);
			break;	
		case R.id.product_tvDownload:
			lnTitle.setBackgroundResource(R.drawable.tab_b);
			break;	
		case R.id.product_tvFAQ:
			lnTitle.setBackgroundResource(R.drawable.tab_c);
			break;	
		case R.id.product_tvVideo:
			lnTitle.setBackgroundResource(R.drawable.tab_d);
			break;	
		
		default:
			break;
		}
		
	}
	
	//------------------------------------------------------------
	
		public class GetProductByIdAsyncTask extends AsyncTask<String, String, String> {

			private int cid;
			private int pid;
			private String json;
			
			public GetProductByIdAsyncTask(int cid, int pid){
				this.cid = cid;
				this.pid = pid;
			}
		   
		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        pDialog = new ProgressDialog(ProductDetailActivity.this);
		        pDialog.setMessage("Loading...");
		        pDialog.setIndeterminate(false);
		        pDialog.setCancelable(true);
		        pDialog.show();
		    }

		    protected String doInBackground(String... params) {
		    	
		    	try {
	                // Building Parameters
	                List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
	                paramsUrl.add(new BasicNameValuePair("pid", String.valueOf(pid)));
	                paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(cid)));

	                json = JsonParser.makeHttpRequest(
	                		Constants.URL_GETPRODUCTBYID, "GET", paramsUrl);
	                Log.d("URL_GETPRODUCTBYID", "pid"+pid+"cid"+cid);
	                Log.d("json", json);
	                if ((json != null) || (!json.equals(""))) { 
	                	JSONObject jsonObject = new JSONObject(json);	                	
	                	product.setId(jsonObject.getInt("entity_id"));
	                	product.setName(jsonObject.getString("name"));
	                	product.setDes(jsonObject.getString("description"));    
	                	product.setShortDes(jsonObject.getString("short_description"));    
	                	
	                	JSONObject jsonTemp= (JSONObject) new JSONTokener(json).nextValue();
	                	//-------get image------
	                	JSONArray jsonImage = jsonTemp.getJSONArray("image");
	                	if (jsonImage != null && jsonImage.length() != 0) {
	                		product.setImage(jsonImage.getString(0)); 
						}

	                	//------get all document for product-----
	                	JSONArray jsonArrayDocument = jsonTemp.getJSONArray("document");
	                	if (jsonArrayDocument != null && jsonArrayDocument.length() != 0) {
	                		for (int i = 0; i < jsonArrayDocument.length(); i++) {
								DocumentObject documentObject = new DocumentObject();
								documentObject.setDocType(jsonArrayDocument.getJSONObject(i).getString("doc_type"));
								documentObject.setFile(jsonArrayDocument.getJSONObject(i).getString("file"));
								product.getListDocument().add(documentObject);
							}
	                	}
	                	
	                	
	                	//------get all options for product-----
	                	JSONArray jsonArrayOptions = jsonTemp.getJSONArray("options");
	                	if (jsonArrayOptions != null && jsonArrayOptions.length() != 0) {
	                		for (int i = 0; i < jsonArrayOptions.length(); i++) {
								OptionObject optionObject = new OptionObject();
								optionObject.setSku(jsonArrayOptions.getJSONObject(i).getString("sku"));
								optionObject.setColor(jsonArrayOptions.getJSONObject(i).getString("color"));
								optionObject.setExtension(jsonArrayOptions.getJSONObject(i).getString("Extension"));
								optionObject.setWeight(jsonArrayOptions.getJSONObject(i).getDouble("weight"));
								optionObject.setPrice(jsonArrayOptions.getJSONObject(i).getDouble("price"));
								optionObject.setMsrp(jsonArrayOptions.getJSONObject(i).getInt("msrp"));
								optionObject.setOid(jsonArrayOptions.getJSONObject(i).getInt("oid"));
								optionObject.setValue(jsonArrayOptions.getJSONObject(i).getInt("value"));
								product.getListOption().add(optionObject);
							}
	                	}
	                	
	                	
	        			
	                }	
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }

		        return null;
		    }

		    protected void onPostExecute(String file_url) {
		    	tvTitle1.setText(product.getName());
		    	tvShortDes.setText(product.getShortDes());
		    	tvDes.setTag(product.getDes());
		    	Log.d("product.getImage()", "abc"+product.getImage());
		    	imageLoader.DisplayImage(product.getImage(), ivAvatar);
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
			if (SharedPreferencesUtil.getFlagLogin(ProductDetailActivity.this)) {
				btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
			} else {
				btnLogin.setText(Constants.TEXT_BUTTON_LOGIN);
			}
		}
	
}
