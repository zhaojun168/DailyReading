package com.android.connector;

import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.callback.ConnectionCallback;
import com.android.http.callback.AsyncIncident;
import com.android.util.FileUtils;
import com.android.util.MLog;
import com.android.util.StringUtils;
import com.android.util.WebRequest2;


public abstract class BaseConnector{
	protected String dk;
	protected String ak;
	protected final String tag = "网络访问";
	
	protected Context context;
	public BaseConnector(Context context) {
		this.context = context;
	}
	
	/**
	 * 根据超好玩家论坛API访问格式，格式化API访问URL
	* <p>
	* Description: 约定API访问URL格式："http://tq.18touch.com/api/Plugin?id=%d
	* 另："dk，ak"默认的请求参数
	* </p> 
	* @param apiUrl
	* @param param
	* @return
	 */
	public String formatApiUrl(String apiUrl, Object ...param){
		for (int i = 0; param != null && i < param.length; i++) {
			apiUrl = setApiParam(apiUrl, param[i] + "");
		}
		if (apiUrl.indexOf("?") == -1) {
			apiUrl = apiUrl + "?";
		}else {
			apiUrl = apiUrl + "&";
		}
		MLog.e("test","请求URL----->"+apiUrl);
		return apiUrl;
	}
	

	/**
	 * 添加API参数
	* @param api
	* @param param
	* @return
	 */
	private String setApiParam(String api, String param){
		String intFormat = "%d";
		String strFormat = "%s";
		api = api.replace(intFormat, strFormat);
		int index = 0;
		if (api.indexOf(strFormat) != -1) {
			index = api.indexOf(strFormat);
		}else {
			new Exception("字符串格式转换错误");
		}
		String start = api.substring(0, index);
		String end = api.substring(index + 2, api.length());
		return start + param + end;
	}

	/**
	 * get请求异步获取数据
	* @param url 请求路径
	* @param json
	* @param callback 获取数据完成回调方法
	 */
	public void AsyncGet(String url, Class<?> json , ConnectionCallback callback){
		AsyncGet(url, "", json, callback);
	}
	/**
	 * get请求异步获取数据
	* <p>Description: 获取的数据可以写入缓存</p> 
	* @param url
	* @param cacheName 缓存名称
	* @param json
	* @param callback
	 */
	public void AsyncGet(final String url, final String cacheName,
			final Class<?> json ,final ConnectionCallback callback){
		AsyncIncident incident = new AsyncIncident() {
			@Override
			public Object incident() {
				WebRequest2 req = new WebRequest2(url);
				byte[] buffer = req.SyncGet();
				if (buffer == null) {
					return null;
				}
				System.out.println("请求结果："+new String(buffer));
				if (!StringUtils.isEmpty(cacheName)) {
					FileUtils.saveDataToFile(context, cacheName, new String(buffer));
				}
				if(json==null) return null;
				return FileUtils.ReadFromJsonData(buffer, json);
			}
		};
		AsyncRequest(incident, callback);
	}
	
	/**
	 * put请求异步获取数据
	* @param url 请求路径
	* @param json
	* @param callback 获取数据完成回调方法
	 */
	public void AsyncPut(String url, Class<?> json , ConnectionCallback callback){
		AsyncPut(url, "", json, callback);
	}
	/**
	 * put请求异步获取数据
	* <p>Description: 获取的数据可以写入缓存</p> 
	* @param url
	* @param cacheName 缓存名称
	* @param json
	* @param callback
	 */
	public void AsyncPut(final String url, final String cacheName,
			final Class<?> json ,final ConnectionCallback callback){
		AsyncIncident incident = new AsyncIncident() {
			@Override
			public Object incident() {
				WebRequest2 req = new WebRequest2(url);
				byte[] buffer = req.SyncPut();
				if (buffer == null) {
					return null;
				}
				System.out.println("请求结果：" + new String(buffer));
				if (!StringUtils.isEmpty(cacheName)) {
					FileUtils.saveDataToFile(context, cacheName, new String(buffer));
				}
				if(json==null) return null;
				return FileUtils.ReadFromJsonData(buffer, json);
			}
		};
		AsyncRequest(incident, callback);
	}
	/**
	 * 常用的异步post请求
	* @param url
	* @param params 请求参数，可为null
	* @param json
	* @param callback
	 */
	public void AsyncPost(final String url, final Map<String, Object> params,
			final Class<?> json ,final ConnectionCallback callback){
		AsyncRequest(new AsyncIncident() {
			@Override
			public Object incident() {
				WebRequest2 request = new WebRequest2();
				byte[] buffer = request.SyncPost(url, params);
				if (buffer==null) {
					return null;
				}
				System.out.println("请求结果："+new String(buffer));
				return FileUtils.ReadFromJsonData(buffer, json);
			}
		}, callback);
	}
	
	/**
	 * 常用的异步put请求
	* @param url
	* @param params 请求参数，可为null
	* @param json
	* @param callback
	 */
	public void AsyncPut(final String url, final Map<String, Object> params,
			final Class<?> json ,final ConnectionCallback callback){
		AsyncRequest(new AsyncIncident() {
			@Override
			public Object incident() {
				WebRequest2 webRequest2 = new WebRequest2();
				byte[] by = webRequest2.SyncPut(url, params);
				if (by == null) {
					return null;
				}
				System.out.println("请求结果："+new String(by));
				return FileUtils.ReadFromJsonData(by, json);
			}
		}, callback);
	}
	
	
	/**
	 * 异步请求事件
	* @param incident 需要执行的事件
	* @param callback 执行完毕回调接口
	 */
	public void AsyncRequest(final AsyncIncident incident, final ConnectionCallback callback){
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(callback!=null)
					callback.result(msg.obj);
			}
		};
		new Thread(){
			public void run() {
				Object obj = incident.incident();
				Message msg = new Message();
				msg.obj = obj;
				handler.sendMessage(msg);
			};
		}.start();
	}	
	
	/**
	 * 根据缓存名称获取缓存数据
	* @param class1 缓存解析接收类
	* @param cacheName
	* @return
	 */
	public Object getCacheDataByName(String cacheName ,Class<?> class1){
		return FileUtils.getCacheData(context, cacheName, class1);
	}
	
	/**
	 * 保存缓存数据，这里需要保存的是String类型的json数据
	* @param cacheName
	* @param content
	 */
	public void saveCacheDataByName(String cacheName, String content){
		FileUtils.saveDataToFile(context, cacheName, content);
	}
	
}
