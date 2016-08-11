package com.android.dzj.app.dailyreading;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.BaseApp;
import com.umeng.comm.core.constants.Constants;
import com.umeng.message.PushAgent;
import com.umeng.message.UHandler;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;

public class App extends BaseApp {
	@Override
	public void onCreate() {
		super.onCreate();
		PlatformConfig.setWeixin("wxe79363f5b3368b3a","5f28c2aa2ae15c59748924d5b706efef");
		// 豆瓣RENREN平台目前只能在服务器端配置
		// 新浪微博
		PlatformConfig.setSinaWeibo("4204776259","9f7d64af965e23641b75a076a7716297");
		PlatformConfig.setQQZone("1105470934", "XVTzS7C0EaGR5E1I");
		PushAgent.getInstance(this).setDebugMode(true);
		PushAgent.getInstance(this).setMessageHandler(
				new UmengMessageHandler() {
					@Override
					public void dealWithNotificationMessage(Context arg0,UMessage msg) {
						// 调用父类方法,这里会在通知栏弹出提示信息
						super.dealWithNotificationMessage(arg0, msg);
						Log.e("", "### 自行处理推送消息");
					}
				});
		PushAgent.getInstance(this).setNotificationClickHandler(new UHandler() {
			@Override
			public void handleMessage(Context context, UMessage uMessage) {
				com.umeng.comm.core.utils.Log.d("notifi", "getting message");
				try {
					JSONObject jsonObject = uMessage.getRaw();
					String feedid = "";
					if (jsonObject != null) {
						com.umeng.comm.core.utils.Log.d("json",jsonObject.toString());
						JSONObject extra = uMessage.getRaw().optJSONObject("extra");
						feedid = extra.optString(Constants.FEED_ID);
					}
					Class myclass = Class.forName(uMessage.activity);
					Intent intent = new Intent(context, myclass);
					Bundle bundle = new Bundle();
					bundle.putString(Constants.FEED_ID, feedid);
					intent.putExtras(bundle);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				} catch (Exception e) {
					com.umeng.comm.core.utils.Log.d("class", e.getMessage());
				}
			}
		});
	}

	// 如果发现Method Over 65K的错误的话就反注释这段代码
	// @Override
	// protected void attachBaseContext(Context base) {
	// super.attachBaseContext(base);
	// MultiDex.install(this);
	// }
}