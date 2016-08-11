package com.android.ui.activity;



import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.BaseApp;
import com.android.broadcastreceiver.BroadcastReceiverCallback;
import com.android.broadcastreceiver.BroadcastReceiverUtil;
import com.android.util.MActivityManager;
import com.android.util.MLog;
import com.android.util.UiUtils;


public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
	
	boolean hasRelease = false;
	public static boolean isLog = false;//是否打印log信息
	public static final String ACTIVITY_FLAG_CLEAR_HISTORY = "clearHistory";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MLog.v(this.getClass().getSimpleName(), "----->OnCreate()");
		
		if (getIntent().getBooleanExtra(ACTIVITY_FLAG_CLEAR_HISTORY,
				false)) {
			MActivityManager.getInstance().finishAllActivity();
		}
		MActivityManager.getInstance().addActivityToStack(this);
		
		setRootView();
		initData();
		initWidget();
		setAdapter();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MLog.v(this.getClass().getSimpleName(), "----->onResume()");
		setListeners();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		MLog.v(this.getClass().getSimpleName(), "----->onStart()");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MLog.v(this.getClass().getSimpleName(), "----->onPause()");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MLog.v(this.getClass().getSimpleName(), "----->onStop()");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		MLog.v(this.getClass().getSimpleName(), "----->onRestart()");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MLog.v(this.getClass().getSimpleName(), "----->onDestroy()");
	}
	
	/**
	 * 绑定布局，例如setContentView();
	 */
	protected abstract void setRootView();
	
	/**
	 * 初始化UI控件，例如findViewById();
	 */
	protected abstract void initWidget();
	
	/**
	 * 初始化数据，例如getIntent().getStringExtra();
	 */
	protected abstract void initData();
	
	/**
	 * 绑定事件监听器，例如setOnClickListener();
	 */
	protected abstract void setListeners();
	
	/**
	 * 释放资源（不可逆）
	 */
	protected abstract void onRelease();
	
	@Override
	public void onClick(View v) {
		
	}
	
	protected void setAdapter() {
		
	}
	
	/**
	 * 根据控件ID获得控件
	 * @param id 控件ID
	 * @return 控件实例
	 */
	protected View $(int id) {
		return findViewById(id);
	}
	
	protected void toast(String msg) {
		UiUtils.toast(BaseApp.getInstance().getApplicationContext(), msg);
	}
	
	protected void toast(int resStringId) {
		UiUtils.toast(BaseApp.getInstance().getApplicationContext(), getString(resStringId));
	}
	
	/**
	 * 启动下一页面（无参）
	 * @param cls
	 */
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}
	
	/**
	 * 启动下一页面（带参）
	 * @param cla
	 * @param params
	 */
	protected void startActivity(Class<?> cla, Map<String, Object> params) {
		Intent intent = new Intent(BaseActivity.this, cla);
		if(params != null) {
			for(String key : params.keySet()) {
				intent.putExtra(key, String.valueOf(params.get(key)));
			}
		}
		startActivity(intent);
	}
	
	/**
	 * 注册广播
	 * @param callback 广播回调函数
	 * @param broadcastNames 广播名称（支持多个广播）
	 * @return 广播实例
	 */
	protected BroadcastReceiver registerReceivers(BroadcastReceiverCallback callback, 
			String... broadcastNames) {
		return BroadcastReceiverUtil.registerReceiver(this, callback, broadcastNames);
	}
	
	/**
	 * 注销广播
	 * @param broadcastReceivers 广播实例（支持多个广播）
	 */
	protected void destoryReceivers(BroadcastReceiver... broadcastReceivers) {
		BroadcastReceiverUtil.destroyReceiver(this, broadcastReceivers);
	}
	
	public boolean isReleased() {
		return hasRelease;
	}
}
