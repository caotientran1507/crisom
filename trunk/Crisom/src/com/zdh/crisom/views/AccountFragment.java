package com.zdh.crisom.views;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zdh.crisom.R;
import com.zdh.crisom.RegisterActivity;

public class AccountFragment extends Fragment  implements View.OnClickListener{
	
	LinearLayout lnAddress, lnBilling, lnCart, lnOrder, lnNewsletter, lnRegister, lnLogin, lnLogout;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View fragview = inflater.inflate(R.layout.fragment_account, container, false);
    	init(fragview);
    	return fragview;
    }
    
    private void init(View fragview){
    	initView(fragview);
    	initData();
    }
    
	private void initView(View fragview) {
		lnAddress = (LinearLayout)fragview.findViewById(R.id.account_ln_address);
		lnBilling = (LinearLayout)fragview.findViewById(R.id.account_ln_billing);
		lnCart = (LinearLayout)fragview.findViewById(R.id.account_ln_cart);
		lnOrder = (LinearLayout)fragview.findViewById(R.id.account_ln_order);
		lnNewsletter = (LinearLayout)fragview.findViewById(R.id.account_ln_newsletter);
		lnRegister = (LinearLayout)fragview.findViewById(R.id.account_ln_register);
		lnLogin = (LinearLayout)fragview.findViewById(R.id.account_ln_login);
		lnLogout = (LinearLayout)fragview.findViewById(R.id.account_ln_logout);
	}

	private void initData(){
    
    
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.account_ln_address:
			
			break;
		case R.id.account_ln_billing:
					
			break;
		case R.id.account_ln_cart:
			
			break;
		case R.id.account_ln_order:
			
			break;
		case R.id.account_ln_newsletter:
			
			break;
		case R.id.account_ln_register:
			Intent i = new Intent(getActivity(), RegisterActivity.class);			
			startActivity(i);		
			break;
					
		case R.id.account_ln_login:
			
			break;
			
		case R.id.account_ln_logout:
			
			break;

		default:
			break;
		}
	}
}