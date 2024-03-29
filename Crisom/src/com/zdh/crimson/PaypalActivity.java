package com.zdh.crimson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.StackActivity;

public class PaypalActivity extends BaseActivity implements View.OnClickListener{
	
	public static WebView webView,webViewInvisible;
	String url = "";
	TextView tvTitle;	
	private ImageView ivCart;
	private LinearLayout lnHome,lnSearch,lnCategory,lnContact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paypal);
		url = getIntent().getExtras().getString(Constants.KEY_URL_PAYPAL);
		webView = (WebView)findViewById(R.id.paypal_wv);
		webViewInvisible = (WebView)findViewById(R.id.paypal_wvInvisible);
		
		
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lnContact);
		
		ivCart = (ImageView)findViewById(R.id.include_footer_ivCart);	
		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnBack = (Button)findViewById(R.id.include_header_btnBack);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		ivCart.setImageResource(R.drawable.ico_category_active);

		tvTitle.setText("PAYPAL");
		btnBack.setVisibility(View.VISIBLE);
		btnLogin.setVisibility(View.INVISIBLE);
		ivCart.setImageResource(R.drawable.ico_cart_active);
		
		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnContact.setOnClickListener(this);		
		btnBack.setOnClickListener(this);
		startWebViewInvisible(Constants.URL_MOBILE);
		startWebView(url);
	}
	
	
	private void startWebView(String url) {
         
        webView.setWebViewClient(new WebViewClient() { 
            public boolean shouldOverrideUrlLoading(WebView view, String url) {              
                view.loadUrl(url);
                return true;
            }
        
            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
            	if (webView.getUrl() != null) {
            		if (webView.getUrl().contains("checkout/onepage/success")) {
                		Intent intent = new Intent(PaypalActivity.this, PaypalSuccessActivity.class);    				
        				startActivity(intent);
        				overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
        				FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_PAYPAL_SUCCESS;
        				StackActivity.getInstance().finishAll();
    				}
				}

            }
            public void onPageFinished(WebView view, String url) {
            	
            }
             
        }); 
          

                
        webView.clearCache(true);
		webView.clearHistory();
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setScrollbarFadingEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
         
        //Load url in webview
        webView.loadUrl(url);
          
          
    }
	
	private void startWebViewInvisible(String url) {
        
		webViewInvisible.setWebViewClient(new WebViewClient() { 
            public boolean shouldOverrideUrlLoading(WebView view, String url) {              
                view.loadUrl(url);
                return true;
            }

            public void onLoadResource (WebView view, String url) {
         
            }
            public void onPageFinished(WebView view, String url) {
            	
            }
             
        }); 

		webViewInvisible.loadUrl(url);
          
          
    }
}
