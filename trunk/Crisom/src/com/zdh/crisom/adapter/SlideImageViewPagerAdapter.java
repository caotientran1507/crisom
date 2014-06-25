package com.zdh.crisom.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.zdh.crisom.R;
import com.zdh.crisom.views.SlideImageFragment;

public class SlideImageViewPagerAdapter extends FragmentPagerAdapter {

	ArrayList<Integer> imagesShow = new ArrayList<Integer>();
	public SlideImageFragment slideImageFragment;

	public SlideImageViewPagerAdapter(FragmentManager fm) {
		super(fm);
		addImage();
	}

	private void addImage() {
		imagesShow.clear();
		imagesShow.add(R.drawable.imageslidea);
		imagesShow.add(R.drawable.imageslideb);
		imagesShow.add(R.drawable.imageslidec);
		imagesShow.add(R.drawable.imageslided);
	}

	@Override
	public Fragment getItem(int pos) {
		Log.d("pos->>", ""+pos);
		
		return SlideImageFragment.newInstance(imagesShow.get(pos%4));

	}

	@Override
	public int getCount() {
		return imagesShow.size() + 1;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
