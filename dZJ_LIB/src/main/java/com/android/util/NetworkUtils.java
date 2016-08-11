package com.android.util;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	
static NetworkUtils INSTANCE = null;
	
	private ConnectivityManager mConnectivity;
	
	private NetworkUtils(Context context)
	{
		mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	public static NetworkUtils instance(Context context)
	{
		if (INSTANCE == null) {
			synchronized (NetworkUtils.class) {
				if (INSTANCE == null) {
					INSTANCE = new NetworkUtils(context);
				}
			}
		}
		return INSTANCE;
	}
	
	/**
	 * 判断是否有网络连接
	 * @return
	 */
	public boolean isNetworkConnected()
	{
		if (mConnectivity == null) {
			return false;
		} else {
	        NetworkInfo mNetworkInfo = mConnectivity.getActiveNetworkInfo();    
	        if (mNetworkInfo != null) {    
	            return mNetworkInfo.isAvailable();    
	        }
//			
//			mNetworkInfo = mConnectivity.getAllNetworkInfo();
//			if (mNetworkInfo != null)
//			{
//				for (int i = 0; i < mNetworkInfo.length; i++)
//				{
//					if (mNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED)
//					{
//						return true;
//					}
//				}
//			}
		}
		return false;
	}
	/**
	 * 判断WIFI网络是否可用
	 * @return
	 */
	public boolean isWifiConnected()
	{
		if (null != mConnectivity) {
			NetworkInfo mWiFiNetworkInfo = mConnectivity
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null)
			{
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	/**
	 * 判断MOBILE网络是否可用
	 * @return
	 */
	public boolean isMobileConnected()
	{
		if (null != mConnectivity) {
			NetworkInfo mMobileNetworkInfo = mConnectivity
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null)
			{
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
}
