package com.zdh.crimson.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zdh.crimson.R;
import com.zdh.crimson.SearchActivity;
import com.zdh.crimson.model.OptionObject;
import com.zdh.crimson.utility.CommonUtil;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class ChildSearchActivityAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater = null;
	private ArrayList<OptionObject> listOption = new ArrayList<OptionObject>();
	private ProgressDialog pDialog;
	int idProduct;
	boolean update_layout_SearchActivity = false;


	public ChildSearchActivityAdapter(Context context,ArrayList<OptionObject> listOption,int idProduct) {
		this.listOption = listOption;	
		this.idProduct = idProduct;
		this.context = context;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
	}
	
	

	public void setUpdate_layout_SearchActivity(boolean update_layout_SearchActivity) {
		this.update_layout_SearchActivity = update_layout_SearchActivity;
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;
		final ChildHolder holder ;
		if (convertView == null) {
			view = inflater.inflate(R.layout.row_child, null);
			holder = new ChildHolder();

			holder.tvModel = (TextView) view.findViewById(R.id.row_child_tvModel);
			holder.tvColor = (TextView) view.findViewById(R.id.row_child_tvColor);
			holder.tvWeight = (TextView) view.findViewById(R.id.row_child_tvShipWeight);
			holder.tvOtherField = (TextView) view.findViewById(R.id.row_child_tvOtherField);
			holder.tvPrice = (TextView) view.findViewById(R.id.row_child_tvPrice);
			holder.tvMSRP = (TextView) view.findViewById(R.id.row_child_tvMSRP);
			holder.tvIncart = (TextView) view.findViewById(R.id.row_child_tvInCart);
			holder.edtQuantity = (EditText) view.findViewById(R.id.row_child_edtQuantityAddtocart);
			holder.btnAddtoCart = (LinearLayout) view.findViewById(R.id.row_child_btnAddtoCart);			
			holder.lnAddtoCart = (LinearLayout) view.findViewById(R.id.row_product_lnAddtocart);


			view.setTag(holder);
		} else {
			holder = (ChildHolder) view.getTag();			
		}

		holder.btnAddtoCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String quantity =  holder.edtQuantity.getText().toString().trim();
				if (quantity.equals("")) {
					Toast.makeText(context, "Please input quantity!", Toast.LENGTH_SHORT).show();
				}else {
					int quantityNumber = 0;
					try {
						quantityNumber = Integer.parseInt(quantity);
					} catch (Exception e) {
						Toast.makeText(context, "Invalid quantity!", Toast.LENGTH_SHORT).show();
					}
					if (quantityNumber < 1) {
						Toast.makeText(context, "Quantity must be greater 0!", Toast.LENGTH_SHORT).show();
					}else{
						new AddtoCartAsyncTask(context,idProduct,listOption.get(position).getOid(),listOption.get(position).getValue(),quantityNumber,SharedPreferencesUtil.getIdCustomerLogin(context), holder.tvIncart).execute();
					}
				}
			}
		});


		if (SharedPreferencesUtil.getFlagLogin(context)) {
			holder.lnAddtoCart.setVisibility(View.VISIBLE);
		} else {
			holder.lnAddtoCart.setVisibility(View.INVISIBLE);
		}

		holder.tvModel.setText(listOption.get(position).getSku());
		holder.tvColor.setText(listOption.get(position).getColor());
		holder.tvWeight.setText(String.valueOf(listOption.get(position).getWeight()));
		holder.tvMSRP.setText(listOption.get(position).getMsrp());
		holder.tvIncart.setText(String.valueOf(listOption.get(position).getInCart()));
		holder.tvPrice.setText(listOption.get(position).getPrice());
		
		if (listOption.get(position).getOtherFieldTitle().equals("") || listOption.get(position).getOtherFieldTitle().equals("null")) {
			holder.tvOtherField.setVisibility(View.GONE);
		}else{
			holder.tvOtherField.setVisibility(View.VISIBLE);
			String s = "<strong><font color=\"#2f3a76\">"+listOption.get(position).getOtherFieldTitle()+": </font></strong>"+ listOption.get(position).getOtherFieldValue();
			holder.tvOtherField.setText(Html.fromHtml(s));
		}

		return view;
	}

	private class ChildHolder {
		TextView tvModel;	
		TextView tvColor;
		TextView tvWeight;
		TextView tvOtherField;
		TextView tvPrice;
		TextView tvMSRP;
		TextView tvIncart;
		EditText edtQuantity;
		LinearLayout btnAddtoCart;
		LinearLayout lnAddtoCart;
	}

	//-------------------------AddtoCart--------------------------------------------------------
	public class AddtoCartAsyncTask extends AsyncTask<String, String, String> {

		private int pid;
		private int oid;
		private int ovalue;
		private int qty;
		private int cid;
		private String json;
		private Context mContext;
		private TextView mtvIncart;
		public AddtoCartAsyncTask(Context context, int pid,int oid,int ovalue,int qty,int cid,TextView tvIncart){

			this.mContext = context;
			this.pid = pid;
			this.oid = oid;
			this.ovalue = ovalue;
			this.qty = qty;
			this.cid = cid;
			this.mtvIncart = tvIncart;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Processing...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			pDialog.setContentView(R.layout.dialog_process);
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("pid", String.valueOf(pid)));
				paramsUrl.add(new BasicNameValuePair("oid", String.valueOf(oid)));
				paramsUrl.add(new BasicNameValuePair("ovalue", String.valueOf(ovalue)));
				paramsUrl.add(new BasicNameValuePair("qty", String.valueOf(qty)));
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(cid)));                

				json = JsonParser.makeHttpRequest(Constants.URL_ADDTOCART, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {  
					JSONObject jsonObject = new JSONObject(json);
					String status = jsonObject.getString("status");
					return status;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String status) { 
			if (status == null) {
				Toast.makeText(context, "Can not add product to your cart. Please try again!", Toast.LENGTH_SHORT).show();
			}else{
				if(update_layout_SearchActivity)
					((SearchActivity)mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mtvIncart.setText(String.valueOf(Integer.parseInt(mtvIncart.getText().toString().trim())+qty));
						}
					});
			}
			pDialog.dismiss();       
			CommonUtil.hideSoftKeyboard(mContext);
		}

	}
}