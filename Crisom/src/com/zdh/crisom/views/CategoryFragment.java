package com.zdh.crisom.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zdh.crisom.R;

public class CategoryFragment extends Fragment {
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View fragview = inflater.inflate(R.layout.fragment_category, container, false);
    	init(fragview);
    	return fragview;
    }
    
    private void init(View fragview){
    	initView(fragview);
    	initData();
    }
    
	private void initView(View fragview) {
	}

	private void initData(){
    
    
	}
}