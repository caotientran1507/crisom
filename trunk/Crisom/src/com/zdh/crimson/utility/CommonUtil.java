package com.zdh.crimson.utility;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class CommonUtil {
	public static String formatMoney(double money){
//		NumberFormat fmt = new NumberFormat().
//		return fmt.format(money);
		
		String str = new DecimalFormat("#.00").format(money);
		return "$"+str;
	}
	 
	public static String getExtensionFile(String url){
		return url.substring(url.length() - 4);
	}
	
	public static String getNameFile(String url){
		String[] arr = url.split("=");
		if (arr.length > 1) {
			return arr[arr.length - 1];
		}
		return "";
	}
	
	public static boolean checkNumbericAndGreaterZero(Context context, String number,String msg){
		int i = 0 ;
		try {
			i = Integer.parseInt(number);
		} catch (Exception e) {
			Toast.makeText(context, "Invalid value "+msg+" !", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if (i <= 0) {
			Toast.makeText(context, "Invalid value "+msg+" !", Toast.LENGTH_SHORT).show();
			return false;
		}else{
			return true;
		}
	}
	
	public static double getTotal(){
		double total = 0;
		for (int i = 0; i < FileUtil.listRecent.size(); i++) {
			total += (FileUtil.listRecent.get(i).getPrice() * FileUtil.listRecent.get(i).getQuantity());
		}
		return total;
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void showDialogLogin(final Context mContext){
		AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

		// Setting Dialog Title
		alertDialog.setTitle("Alert Dialog");

		// Setting Dialog Message
		alertDialog.setMessage("Welcome to AndroidHive.info");

		// Setting Icon to Dialog
//		alertDialog.setIcon(R.drawable.delete);

		// Setting OK Button
		alertDialog.setButton("OK",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int which) {
						// Write your code here to execute after dialog
						// closed
						Toast.makeText(mContext,
								"You clicked on OK", Toast.LENGTH_SHORT)
								.show();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}
	
	public static String combineString(String[] s, String glue)
	{
	  int k = s.length;
	  if ( k == 0 )
	  {
	    return null;
	  }
	  StringBuilder out = new StringBuilder();
	  out.append( s[0] );
	  for ( int x=1; x < k; ++x )
	  {
	    out.append(glue).append(s[x]);
	  }
	  return out.toString();
	}
	
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}
	
	public static void hideSoftKeyboard(Context mContext) {
		InputMethodManager inputMethodManager = (InputMethodManager) mContext
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(((Activity)mContext).getCurrentFocus()
				.getWindowToken(), 0);
	}
	
	public static void hideSoftKeyboardPro(Activity activity, View v) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
		
	}
	
	public static void hideSoftKeyboardPro(Context mContext, View v) {
		InputMethodManager inputMethodManager = (InputMethodManager) ((Activity)mContext)
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
		
	}
	
	static public void showWifiNetworkAlert(final Context mContext) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		alertDialog.setTitle("Warning");
		alertDialog.setMessage("Connect internet please!");

		alertDialog.setPositiveButton("Setting",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Settings.ACTION_SETTINGS);
						mContext.startActivity(intent);
					}
				});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		alertDialog.show();

	}
	
	public static boolean TestNetWork(Context mContext) {
		boolean status = false;
		try {
			ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(0);
			if (netInfo != null
					&& netInfo.getState() == NetworkInfo.State.CONNECTED) {
				status = true;
			} else {
				netInfo = cm.getNetworkInfo(1);
				if (netInfo != null
						&& netInfo.getState() == NetworkInfo.State.CONNECTED)
					status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return status;
	}
	
	public static int checkAllCheckBox(ArrayList<Boolean> listCheck){
		int countCheck = 0;
		for (int i = 0; i < listCheck.size(); i++) {
			if (listCheck.get(i)) {
				countCheck++;
			}
		}
		return countCheck;
	}
}
