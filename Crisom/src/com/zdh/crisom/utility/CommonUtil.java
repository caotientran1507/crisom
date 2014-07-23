package com.zdh.crisom.utility;

import java.text.NumberFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class CommonUtil {
	public static String formatMoney(double money){
		NumberFormat fmt = NumberFormat.getCurrencyInstance();
		return fmt.format(money);
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
}
