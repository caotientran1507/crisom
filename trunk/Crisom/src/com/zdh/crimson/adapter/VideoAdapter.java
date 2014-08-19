package com.zdh.crimson.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdh.crimson.R;
import com.zdh.crimson.lazylist.ImageLoader;
import com.zdh.crimson.model.VideoObject;

public class VideoAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<VideoObject> listVideo = new ArrayList<VideoObject>();
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader; 

	public VideoAdapter(Activity a, ArrayList<VideoObject> listVideo) {
		activity = a;
		this.listVideo = listVideo;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		imageLoader=new ImageLoader(activity);
	}

	public int getCount() {
		return listVideo.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.row_videos, null);

		TextView name = (TextView) vi.findViewById(R.id.row_video_tvUrl); 	
		ImageView ivAvatar = (ImageView) vi.findViewById(R.id.row_video_iv); 
		name.setText(listVideo.get(position).getName());
		
		imageLoader.DisplayImage(listVideo.get(position).getThumbnail(), ivAvatar);
		
		return vi;
	}
}