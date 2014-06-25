package com.zdh.crisom.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zdh.crisom.R;

public class MoreFragment extends Fragment implements View.OnClickListener {
	LinearLayout lnAbout, lnSupport;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View fragview = inflater.inflate(R.layout.fragment_more, container, false);
    	init(fragview);
    	return fragview;
    }
    
    private void init(View fragview){
    	initView(fragview);
    	initData();
    }
    
	private void initView(View fragview) {
		lnAbout = (LinearLayout)fragview.findViewById(R.id.more_ln_about);
		lnSupport = (LinearLayout)fragview.findViewById(R.id.more_ln_support);
	}

	private void initData(){
    
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_ln_about:
			
			break;
		case R.id.more_ln_support:
					
			break;
		default:
			break;
		}
	}
}