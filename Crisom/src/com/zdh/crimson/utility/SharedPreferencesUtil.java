package com.zdh.crimson.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {		
	
	//---------save color code 
	static public boolean saveFlagLogin(boolean flagLogin, int idCustomer, Context mContext) {   
	    SharedPreferences prefs = mContext.getSharedPreferences("Login", Context.MODE_PRIVATE);  
	    SharedPreferences.Editor editor = prefs.edit();  	    
	    editor.putBoolean("flagLogin", flagLogin);  
	    editor.putInt("idCustomer", idCustomer);
	    return editor.commit();  
	} 
	
	static public boolean getFlagLogin(Context mContext) {  
	    SharedPreferences prefs = mContext.getSharedPreferences("Login", Context.MODE_PRIVATE);  
	    boolean flagLogin = prefs.getBoolean("flagLogin", false);  	   
	    return flagLogin;  
	}
	
	static public int getIdCustomerLogin(Context mContext) {  
	    SharedPreferences prefs = mContext.getSharedPreferences("Login", Context.MODE_PRIVATE);  
	    int idCustomer = prefs.getInt("idCustomer", 0);  	   
	    return idCustomer;  
	}
}
