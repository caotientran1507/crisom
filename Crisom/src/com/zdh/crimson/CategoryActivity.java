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
import android.view.View.OnClickListener;
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

public class CategoryActivity extends BaseActivity implements View.OnClickListener {

	// --------define variables---------
	private LinearLayout lnHome, lnSearch, lnCart, lnContact;
	private ImageView ivCategory;
	private TextView tvTitle;
	private ListView lvCategory;
	CategoryAdapter adapter;
	private ProgressDialog pDialog;
	
	static private int currentLevel = 0; 
	static private int currentCategory = 0; 
	private int narrowSearchID = Constants.KEY_CATEGORY_OTHER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		if (getIntent().getExtras() != null) {
			currentCategory = getIntent().getExtras().getInt(Constants.KEY_CATEGORYID);
			if (narrowSearchID != Constants.CATEGORY_ROOT) {
				currentLevel ++;
				narrowSearchID = getIntent().getExtras().getInt(Constants.KEY_CATEGORY_NARROWSEARCH);			
			}			
		}
		currentCategory = (currentCategory == 0) ? Constants.CATEGORY_ROOT : currentCategory ;
		init();
		initDataWebservice();
		FileUtil.listCategory.clear();
	}

	@Override
	protected void onResume() { 
		super.onResume();
		ChangeTextButtonLogin();
	}

	private void init() {
		initView();
		initData();
	}

	private void initView() {
		lnHome = (LinearLayout) findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout) findViewById(R.id.include_footer_lnSearch);
		lnCart = (LinearLayout) findViewById(R.id.include_footer_lnCart);
		lnContact = (LinearLayout) findViewById(R.id.include_footer_lnContact);

		ivCategory = (ImageView) findViewById(R.id.include_footer_ivcategory);

		tvTitle = (TextView) findViewById(R.id.include_header_tvTitle);
		btnLogin = (Button) findViewById(R.id.include_header_btnLogin);
		btnBack = (Button) findViewById(R.id.include_header_btnBack);
		lvCategory = (ListView) findViewById(R.id.category_lv);

		ivCategory.setImageResource(R.drawable.ico_category_active);
		tvTitle.setText("CATEGORIES");
		btnBack.setVisibility(View.INVISIBLE);

		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actionBackButton();
			}
		});

		lvCategory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view,int position, long arg) {
				
				if (FileUtil.listCategory.size() > 0) {
					if (currentLevel == 0) {
						if (FileUtil.listCategory.get(position).getName().equalsIgnoreCase("TV SIZE")) {
							narrowSearchID = Constants.KEY_CATEGORY_TVSIZE;
						} else {
							narrowSearchID = Constants.KEY_CATEGORY_OTHER;
						}
						
					}
					Log.d("narrowSearchIDabc", ""+narrowSearchID);
					if (FileUtil.listCategory.get(position).getSubcat()) {
						currentCategory = FileUtil.listCategory.get(position).getId();
						new GetCategoriesByIdAsyncTask(currentCategory).execute();
						currentLevel++;
					} else {
						Intent intent = new Intent(CategoryActivity.this,ProductListActivity.class);
						intent.putExtra(Constants.KEY_CATEGORYID,FileUtil.listCategory.get(position).getId());
						intent.putExtra(Constants.KEY_CATEGORY_NARROWSEARCH,narrowSearchID);
						startActivity(intent);
						overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
						FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_PRODUCTLIST;
					}
				}
			}
		});

		pDialog = new ProgressDialog(CategoryActivity.this);

	}

	private void initData() {

		adapter = new CategoryAdapter(CategoryActivity.this, FileUtil.listCategory);
		lvCategory.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void initDataWebservice() {
		new GetCategoriesByIdAsyncTask(currentCategory).execute();
	}
	
	
	@Override
	public void onBackPressed() {
		actionBackButton();
	}

	@Override
	protected void actionBackButton() {
		if(currentLevel > 0){
			new GetCategoriesByIdAsyncTask(FileUtil.listCategory.get(0).getIdParent()).execute();
			currentLevel--;
		}else{
			finish();
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
		}
	}

	public class GetCategoriesByIdAsyncTask extends AsyncTask<String, String, String> {

		private int cat_id;
		private String json;

		public GetCategoriesByIdAsyncTask(int cat_id) {
			this.cat_id = cat_id;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null) {
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

				json = JsonParser.makeHttpRequest(Constants.URL_GETCATEGORIESBYID, "GET", paramsUrl);

				if ((json != null) || (!json.equals(""))) {
					JSONArray array = new JSONArray(json);
					FileUtil.listCategory.clear();
					for (int j = 0; j < array.length(); j++) {
						Category temp = new Category();
						temp.setId(array.getJSONObject(j).getInt("id"));
						temp.setName(array.getJSONObject(j).getString("name").trim());
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
			if (!FileUtil.listCategory.isEmpty()) {
				if (FileUtil.listCategory.get(0).getIdParent() != 0) {
					btnBack.setVisibility(View.VISIBLE);
				} else {
					btnBack.setVisibility(View.INVISIBLE);
				}
			}
			try {
				pDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
