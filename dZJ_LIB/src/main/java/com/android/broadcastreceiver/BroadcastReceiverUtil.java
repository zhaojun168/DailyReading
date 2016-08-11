package com.android.broadcastreceiver;

import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BroadcastReceiverUtil {

	/**
	 * 发送自定义广播（无参）
	 * @param context
	 * @param broadcastName
	 */
	public static void sendCustomReceiver(Context context, String broadcastName) {
		sendCustomReceiver(context, broadcastName, null);
	}
	
	/**
	 * 发送自定义广播（带参）
	 * @param context
	 * @param broadcastName
	 * @param params
	 */
	public static void sendCustomReceiver(Context context, String broadcastName, 
			Map<String, Object> params) {
		Intent intent = new Intent(broadcastName);
		if(params != null) {
			for(String key : params.keySet()) {
				intent.putExtra(key, String.valueOf(params.get(key)));
			}
		}
		context.sendBroadcast(intent);
	}
	
	/**
	 * 注册自定义广播
	 * <p>
	 * 请在程序退出时销毁广播：context.unregisterReceiver(broadcastName);
	 * </p> 
	 * @param context
	 * @param broadcastName
	 *            自定义广播别名
	 * @param callback
	 *            广播回调函数
	 */
	public static BroadcastReceiver registerReceiver(Context context, String broadcastName,
			BroadcastReceiverCallback callback) {
		MyBroadcastReceiver myBroadCastReceiver = new MyBroadcastReceiver(callback);
		context.registerReceiver(myBroadCastReceiver, new IntentFilter(broadcastName));
		return myBroadCastReceiver;
	}
	
	/**
	 * 注册自定义广播
	 * <p>
	 * 请在程序退出时销毁广播：context.unregisterReceiver(broadcastName);
	 * </p> 
	 * @param context
	 * @param broadcastNames
	 *            自定义广播别名(多个过滤)
	 * @param callback
	 *            广播回调函数
	 */
	public static BroadcastReceiver registerReceiver(Context context,
			BroadcastReceiverCallback callback, String ...broadcastNames) {
		MyBroadcastReceiver myBroadCastReceiver = new MyBroadcastReceiver(callback);
		IntentFilter filter = new IntentFilter();
		for (String name : broadcastNames) {
			filter.addAction(name);
		}
		context.registerReceiver(myBroadCastReceiver, filter);
		return myBroadCastReceiver;
	}

	/**
	 * 销毁广播
	 * @param context
	 * @param receivers
	 */
	public static void destroyReceiver(Context context, BroadcastReceiver ...receivers) {
		if (receivers == null) 
			return;
		for (BroadcastReceiver receiver : receivers) {
			if (receiver != null) {
				try {
					context.unregisterReceiver(receiver);
				} catch (Exception e) {
					// 
				}
			}
		}
	}
}
