package com.android;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;

import com.android.util.ImageLoaderUtil;


/**
 * 与业务无关的逻辑可以放这里<br />
 * 与业务无关的方法需要 context 时，也可以使用这里的 {@link #sharedInstance()}
 * @author FreddyChen 2015-04-02
 */
public class BaseApp extends Application {

	private static BaseApp INSTANCE = null;
	/** 用于数据传递   */
	public Map<String, Object> Map_Share = new HashMap<String, Object>();
	/** 用于数据缓存   */
	public Map<String, Object> Map_Cache = new HashMap<String, Object>();
	
	public static BaseApp getInstance() {
		if (INSTANCE == null) {
			throw new IllegalStateException("not init");
		}
		return INSTANCE;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		INSTANCE = this;
		ImageLoaderUtil.initImageLoader(getApplicationContext());
	}
	
	/**
	 * 当后台程序已经终止资源还匮乏时会调用这个方法
	 * 用于释放一些不必要的资源来应付后台程序已经终止，前台应用程序内存还不够时的情况。
	 */
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Map_Share.clear();
		Map_Cache.clear();
	}
}

