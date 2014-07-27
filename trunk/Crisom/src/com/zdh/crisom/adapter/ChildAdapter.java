package com.zdh.crisom.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdh.crisom.R;
import com.zdh.crisom.model.OptionObject;
import com.zdh.crisom.model.Product;
import com.zdh.crisom.utility.Constants;
import com.zdh.crisom.utility.FileUtil;
import com.zdh.crisom.utility.JsonParser;
import com.zdh.crisom.utility.SharedPreferencesUtil;

public class ChildAdapter extends BaseAdapter {

	int currentPosition;	
	CategoryHolder holder = null;
	private Context context;
	private LayoutInflater inflater = null;
	private ArrayList<OptionObject> listOption = new ArrayList<OptionObject>();
	private ProgressDialog pDialog;


	public ChildAdapter(Context context,ArrayList<OptionObject> listOption) {
		this.listOption = listOption;		
		this.context = context;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
	}

	public int getCount() {
		return listOption.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {

		TextView tvModel;	
		TextView tvColor;
		TextView tvWeight;
		TextView tvExtension;
		TextView tvPrice;
		TextView tvMSRP;
		TextView tvIncart;
		EditText edtQuantity;
		LinearLayout btnAddtoCart;
		LinearLayout lnAddtoCart;
		View view = convertView;
		currentPosition = position;
		if (convertView == null) {
			view = inflater.inflate(R.layout.row_child, null);
			holder = new CategoryHolder();
			
			tvModel = (TextView) view.findViewById(R.id.row_child_tvModel);
			tvColor = (TextView) view.findViewById(R.id.row_child_tvColor);
			tvWeight = (TextView) view.findViewById(R.id.row_child_tvShipWeight);
			tvExtension = (TextView) view.findViewById(R.id.row_child_tvExtension);
			tvPrice = (TextView) view.findViewById(R.id.row_child_tvPrice);
			tvMSRP = (TextView) view.findViewById(R.id.row_child_tvMSRP);
			tvIncart = (TextView) view.findViewById(R.id.row_child_tvInCart);
			edtQuantity = (EditText) view.findViewById(R.id.row_child_edtQuantityAddtocart);
			btnAddtoCart = (LinearLayout) view.findViewById(R.id.row_child_btnAddtoCart);			
			lnAddtoCart = (LinearLayout) view.findViewById(R.id.row_product_lnAddtocart);
			
			holder.tvModel = tvModel;
			holder.tvColor = tvColor;
			holder.tvWeight = tvWeight;
			holder.tvMSRP = tvMSRP;			
			holder.tvIncart = tvIncart;
			holder.tvPrice = tvPrice;
			holder.tvExtension = tvExtension;
			holder.edtQuantity = edtQuantity;
			holder.lnAddtoCart = lnAddtoCart;			
			holder.btnAddtoCart = btnAddtoCart;

			view.setTag(holder);
		} else {
			holder = (CategoryHolder) view.getTag();			
			tvModel = holder.tvModel;
			tvColor = holder.tvColor;
			tvWeight = holder.tvWeight;
			tvMSRP = holder.tvMSRP;
			tvIncart = holder.tvIncart;
			tvPrice = holder.tvPrice;
			tvExtension = holder.tvExtension;
			edtQuantity = holder.edtQuantity;
			lnAddtoCart = holder.lnAddtoCart;
			btnAddtoCart = holder.btnAddtoCart;
		}
		
		btnAddtoCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		
		if (SharedPreferencesUtil.getFlagLogin(context)) {
			lnAddtoCart.setVisibility(View.VISIBLE);
		} else {
			lnAddtoCart.setVisibility(View.INVISIBLE);
		}
		
		// ------load data--------
		holder.tvModel = tvModel;
		holder.tvColor = tvColor;
		holder.tvWeight = tvWeight;
		holder.tvMSRP = tvMSRP;			
		holder.tvIncart = tvIncart;
		holder.tvPrice = tvPrice;
		holder.tvExtension = tvExtension;
		
		tvModel.setText(listOption.get(currentPosition).getSku());
		tvColor.setText(listOption.get(currentPosition).getColor());
		tvWeight.setText(String.valueOf(listOption.get(currentPosition).getWeight()));
		tvMSRP.setText(String.valueOf(listOption.get(currentPosition).getMsrp()));
		tvIncart.setText("");
		tvPrice.setText(String.valueOf(listOption.get(currentPosition).getPrice()));
		tvExtension.setText(listOption.get(currentPosition).getExtension());
		return view;
	}

	private class CategoryHolder {
		TextView tvModel;	
		TextView tvColor;
		TextView tvWeight;
		TextView tvExtension;
		TextView tvPrice;
		TextView tvMSRP;
		TextView tvIncart;
		EditText edtQuantity;
		LinearLayout btnAddtoCart;
		LinearLayout lnAddtoCart;
	}
	
	//-------------------------AddtoCart--------------------------------------------------------
	public class AddtoCartAsyncTask extends AsyncTask<String, String, String> {

		private int cat_id;
		private String json;
		
		public AddtoCartAsyncTask(int cat_id){
			this.cat_id = cat_id;
		}
	   
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(context);
	        pDialog.setMessage("Processing...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }

	    protected String doInBackground(String... params) {
	    	
	    	try {
                // Building Parameters
                List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
                paramsUrl.add(new BasicNameValuePair("pid", String.valueOf(cat_id)));
                paramsUrl.add(new BasicNameValuePair("oid", String.valueOf(cat_id)));
                paramsUrl.add(new BasicNameValuePair("ovalue", String.valueOf(cat_id)));
                paramsUrl.add(new BasicNameValuePair("qty", String.valueOf(cat_id)));
                paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(cat_id)));

                json = JsonParser.makeHttpRequest(Constants.URL_ADDTOCART, "GET", paramsUrl);
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
	        pDialog.dismiss();	      
	    }
	}
}