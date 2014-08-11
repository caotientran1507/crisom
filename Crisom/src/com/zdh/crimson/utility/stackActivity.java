package com.zdh.crimson.utility;

import java.util.Iterator;
import java.util.Stack;

import android.app.Activity;

public class stackActivity {

	private static stackActivity sActivity = null;
	private Stack<Activity> stack = null;

	/**
	 * Get Instance stackActivity
	 * 
	 * @return
	 */
	public static stackActivity getInstance() {
		if (sActivity == null) {
			// Init new CommonActivityManager
			sActivity = new stackActivity();
		}

		return sActivity;
	}

	/**
	 * Init new
	 */
	private stackActivity() {
		stack = new Stack<Activity>();
	}

	/**
	 * Push new activity to stack
	 * 
	 * @param activity
	 */
	public void push(Activity activity) {
		removeIsFinishingActivity();
		stack.push(activity);
	}

	/**
	 * Finish all activity button save first activity
	 */
	public void finishAll() {
		try {
			for (Activity activity : stack) {
				if (activity != null && !activity.isFinishing()
						&& stack.lastIndexOf(activity) != 0) {
					activity.finish();
				}
			}
			
			removeIsFinishingActivity();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check and remove activity if it fisnish
	 */
	private void removeIsFinishingActivity() {
		Iterator<Activity> itr = stack.iterator();
		while (itr.hasNext()) {
			Activity act = itr.next();
			if (act == null || act.isFinishing())
				itr.remove();
		}
	}
}
