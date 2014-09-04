/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zdh.crimson;

import static com.zdh.crimson.CommonUtilities.SENDER_ID;
import static com.zdh.crimson.CommonUtilities.displayMessage;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.zdh.crimson.R;
import com.zdh.crimson.utility.ALog;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;
import com.zdh.crimson.utility.UserEmailFetcher;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	@SuppressWarnings("hiding")
	public static int pid = 0;
	private static final String TAG = "GCMIntentService";
	private static final String MESSAGE = "message";
	private static final String PRODUCT_ID = "product_id";
	private static final String COUPON_ID = "coupon_id";
	private static final String TYPE = "type";
	private static final int TYPE_NEWPRODUCT = 0;
	private static final int TYPE_COUPON = 1;

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		displayMessage(context, getString(R.string.gcm_registered));
		String email = UserEmailFetcher.getEmail(context);
		ServerUtilities.register(context, registrationId, "",email);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		displayMessage(context, getString(R.string.gcm_unregistered));
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			ServerUtilities.unregister(context, registrationId);
		} else {
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			Log.i(TAG, "Ignoring unregister callback");
		}
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		String type = intent.getExtras().getString(TYPE); // 0: new product, 1: coupon
		String message = intent.getExtras().getString(MESSAGE);
		String product_id = intent.getExtras().getString(PRODUCT_ID);
		String coupon_id = intent.getExtras().getString(COUPON_ID);
		int notifyType = 2;
		if(type!=null && !type.equals(""))
			notifyType = Integer.parseInt(type);


		ALog.d("TAG", "type:"+type);
		ALog.d("TAG", "message:"+message);
		ALog.d("TAG", "product_id:"+product_id);
		ALog.d("TAG", "coupon_id:"+coupon_id);

		//        displayMessage(context, message);
		// notifies user
		generateNotification(context,notifyType,product_id, coupon_id, message);
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		//        generateNotification(context, message);
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		displayMessage(context, getString(R.string.gcm_recoverable_error,errorId));
		return super.onRecoverableError(context, errorId);
	} 

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context,int type,String product_id,String coupon, String message) {
		switch (type) {
		case TYPE_NEWPRODUCT:
			ALog.d("TAG", "product_id1:"+product_id);
			if(product_id!=null && !product_id.equals(""))
				pid = Integer.parseInt(product_id);
			generateNotificationNewProduct(context,message,product_id);
			break;
		case TYPE_COUPON:
			generateNotificationCoupon(context,  message, coupon);
			break;
		default:
			GCMIntentService.generateNotificationCheckOut(context,"It will notify them that they checked out with thing in their cart");
			break;
		}
	}

	private static void generateNotificationNewProduct(Context context, String message, String product_id) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager)
				context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context, ProductDetailActivity.class);
		notificationIntent.putExtra(Constants.KEY_PRODUCTID, product_id);
		ALog.d("TAG", "product_id2:"+product_id);

		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		notificationManager.notify(0, notification); 
	}
	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	public static void generateNotificationCoupon(Context context, String message, String coupon_id) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context, HomeActivity.class);

		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);
	}
	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	public static void generateNotificationCheckOut(Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent;
		if(!SharedPreferencesUtil.getFlagLogin(context)){
			 notificationIntent = new Intent(context, HomeActivity.class);
		}else{
			 notificationIntent = new Intent(context, CartActivity.class);
		}
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);
	}

	//    private static void getRequestFriendNotification(String message,
	//			Context context, String contentId) {
	//		int icon = R.drawable.ic_launcher;
	//		long when = System.currentTimeMillis();
	//		NotificationManager notificationManager = (NotificationManager) context
	//				.getSystemService(Context.NOTIFICATION_SERVICE);
	//		Notification notification = new Notification(icon, message, when);
	//
	//		Setting setting = new Setting(context);
	//
	//		String title = context.getString(R.string.app_name);
	//
	//		Intent requestIntent = new Intent(context, FriendActivity.class);
	//		requestIntent.putExtra(Constants.EXTRA_NOTIFY, Constants.EXTRA_NOTIFY);
	//		Utils.setLog(TAG, "ContentID: " + contentId);
	//		requestIntent.putExtra(Constants.EXTRA_OTHER_ID, contentId);
	//		requestIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	//		requestIntent.setAction("actionstring" + System.currentTimeMillis());
	//		PendingIntent requestPI = PendingIntent.getActivity(context, 0,
	//				requestIntent, PendingIntent.FLAG_CANCEL_CURRENT);
	//		notification.setLatestEventInfo(context, title, message, requestPI);
	//		// Notification should be canceled when it is clicked by the user
	//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
	//
	//		// Play default notification sound
	//		notification.defaults |= Notification.DEFAULT_SOUND;
	//
	//		// Vibrate if vibrate is enabled
	//		notification.defaults |= Notification.DEFAULT_VIBRATE;
	//		notificationManager.notify((int) when, notification);
	//
	//		setting.putString(Constants.PREFERENCE_NEW_NOTIFY, Constants.TRUE);
	//
	//		// Send broad cast intent
	//		Intent requestedIntent = new Intent();
	//		requestedIntent.setAction(Constants.ACTION_REQUEST);
	//		context.sendBroadcast(requestedIntent);
	//	}
	
}
