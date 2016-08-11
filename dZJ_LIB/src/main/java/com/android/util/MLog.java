package com.android.util;

import android.util.Log;

public class MLog {

	private static final boolean ENABLE = false;// 日志开关，true为可输出，false为关闭
	/** 是否允许输出信息到LogCat */
	private static final boolean IsShowToLogCat = true;
	
	public enum MLogType {
		V(2), D(3), I(4), W(5), E(6);
		
		private int value;
		
		MLogType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	/**
	 * 控制台LogCat输出信息
	 * @param tag 标志符
	 * @param infos 内容
	 */
	public static void showLog(String tag, Object infos) {
		if(!ENABLE)
			return;
		
		if(infos == null)
			infos = "";
		
		showLog(tag, infos.toString(), MLogType.I);
	}
	
	/**
	 * 控制台LogCat输出信息
	 * @param tag 标志符
	 * @param infos 内容
	 * @param logType 输出日志类型级别
	 */
	public static void showLog(String tag, Object infos, MLogType logType) {
		if(!ENABLE)
			return;
		
		if(!IsShowToLogCat)
			return;
		
		switch (logType) {
		case V :
			Log.v(tag, infos.toString());
			break;
		case D :
			Log.d(tag, infos.toString());
			break;
		case I :
			Log.i(tag, infos.toString());
			break;
		case W :
			Log.w(tag, infos.toString());
			break;
		case E :
			Log.e(tag, infos.toString());
			break;
		}
	}
	
	/**
	 * 保持与传统Log一致的使用
	 * @param tag 标识符
	 * @param infos 内容
	 */
	public static void v(String tag, Object infos) {
		if (!ENABLE)
			return;
		
		Log.v(tag, infos.toString());
	}
	
	/**
	 * 保持与传统Log一致的使用
	 * @param tag 标识符
	 * @param infos 内容
	 */
	public static void d(String tag, Object infos) {
		if (!ENABLE)
			return;
		
		Log.d(tag, infos.toString());
	}
	
	/**
	 * 保持与传统Log一致的使用
	 * @param tag 标识符
	 * @param infos 内容
	 */
	public static void i(String tag, Object infos) {
		if (!ENABLE)
			return;
		
		Log.i(tag, infos.toString());
	}
	
	/**
	 * 保持与传统Log一致的使用
	 * @param tag 标识符
	 * @param infos 内容
	 */
	public static void w(String tag, Object infos) {
		if (!ENABLE)
			return;
		
		Log.w(tag, infos.toString());
	}
	
	/**
	 * 保持与传统Log一致的使用
	 * @param tag 标识符
	 * @param infos 内容
	 */
	public static void e(String tag, Object infos) {
		if (!ENABLE)
			return;
		
		Log.e(tag, infos.toString());
	}
}
