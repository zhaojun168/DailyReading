package com.android.util;



import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class UiUtils {


	public static Toast toast;
	
	public static void toast(Context context, String msg) {
		if(null == toast) 
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setText(msg);
		toast.show();
	}
	
	public static void toast(Context context, String msg, int duration) {
		if(null == toast)
			toast = Toast.makeText(context, msg, duration);
		toast.setText(msg);
		toast.show();
	}
	
	public static void toast(Context context, int resStringId) {
		toast(context, context.getString(resStringId));
	}
	
	public static void toast(Context context, int resStringId, int duration) {
		toast(context, context.getString(resStringId), duration);
	}

	/**是否应该隐藏键盘*/
	public static boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**隐藏键盘*/
	public static void hideSoftInput(Context context, View v) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		}
	}
}
