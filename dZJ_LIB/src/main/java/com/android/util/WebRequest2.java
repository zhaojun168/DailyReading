package com.android.util;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;


/**
 * 处理网络GET请求的的类。有同步处理和异步处理两种方法。
 * 
 * @author robin@apexera.com
 * 
 */
public class WebRequest2 {
	/**
	 * @author robin 实现异步网络请求的私有类。
	 */
	private class DownloadTask extends AsyncTask<String, Void, byte[]> {
		public String type;
		public WebRequestHandler handler;

		@Override
		protected byte[] doInBackground(String... urls) {
			for (String url : urls) {
				return SyncGet(url);
			}
			return null;
		}

		@Override
		protected void onPostExecute(byte[] buffer) {
			if (null == handler)
				return;
			handler.handleResponse(type, buffer);
		}
	}

	private HttpParams httpParameters = new BasicHttpParams();
	private final static int timeoutConnection = 15000;
	private final static int timeoutSocket = 15000;
	private static String USER_AGENT;

	private String url;

	public WebRequest2() {
		this("");
	}

	public WebRequest2(String url) {
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		this.url = url;
		USER_AGENT = "";
		MLog.e("test", "user_agent----->" + USER_AGENT);
	}

	/**
	 * 异步的函数，用于界面马上需要返回的。
	 */
	public void perform(WebRequestHandler handler, String type) {
		DownloadTask task = new DownloadTask();
		task.handler = handler;
		task.type = type;
		task.execute(new String[] { url });
	}

	/**
	 * 获取网络图片
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getNetBitmap(String url) {
		Bitmap _bitmap = null;
		byte[] buffer = SyncGet(url);
		if (null != buffer) {
			try {
				_bitmap = BitmapFactory.decodeByteArray(buffer, 0,
						buffer.length);
			} catch (Exception e) {
				try {
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inSampleSize = 4;
					_bitmap = BitmapFactory.decodeByteArray(buffer, 0,
							buffer.length, opts);
				} catch (Exception e1) {
				}
			}
		}
		return _bitmap;
	}

	/**
	 * 阻塞函数，可以被直接调用，
	 */
	public byte[] SyncGet() {
		return SyncGet(this.url);
	}

	public byte[] SyncGet(String url) {
		return SyncGet(url, null);
	}

	public byte[] SyncGet(String url, Map<String, Object> params) {
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);

		// url参数处理
		if (params != null)
			url = __MakeURL(url, params);

		// System.out.println("SyncGet URL: "+url);

		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("User-Agent", USER_AGENT);
		try {
			HttpResponse response = client.execute(httpGet);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				byte[] buffer = EntityUtils.toByteArray(entity);
				// System.out.println("SyncGet Response: "+new String(buffer));
				return buffer;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] SyncPost() {
		return SyncPost(this.url);
	}

	public byte[] SyncPost(String url) {
		return SyncPost(url, new HashMap<String, Object>());
	}

	public byte[] SyncPost(String url, Map<String, Object> params) {
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);

		// System.out.println("SyncPost URL: "+url);

		// url参数处理
		List<NameValuePair> _params = null;
		if (params != null && params.size() > 0) {
			_params = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				_params.add(new BasicNameValuePair(key, params.get(key) + ""));
//				 System.out.println("param___key: "+key +
//				 "  value: "+params.get(key).toString());
			}
		}

		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("User-Agent", USER_AGENT);
		// System.out.println("SyncPost User-Agent: "+USER_AGENT);
		try {
			// 设置参数及编码
			if (_params != null)
				httpPost.setEntity(new UrlEncodedFormEntity(_params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpPost);
			// System.out.println("SyncPost getStatusLine: "+
			// response.getStatusLine().toString());

			// System.out.println("SyncPost ResponseCode: "+ response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				byte[] buffer = EntityUtils.toByteArray(entity);
				// System.out.println("SyncPost Response: "+new String(buffer));
				return buffer;
			}
		} catch (Exception e) {
			// System.out.println("SyncPost Response: error");
			e.printStackTrace();
		}
		return null;
	}

	public byte[] SyncPost(String url, String content) {
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		// System.out.println("SyncPost URL: "+url);
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("User-Agent", USER_AGENT);
		// System.out.println("SyncPost User-Agent: "+USER_AGENT);
		try {
			// 设置JSON参数及编码
			if (content != null && content != "") {
				content = URLEncoder.encode(content, HTTP.UTF_8);
				content = "=" + content;// 特殊处理
				StringEntity entity = new StringEntity(content);
				entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/x-www-form-urlencoded"));
				httpPost.setEntity(entity);
			}
			HttpResponse response = client.execute(httpPost);
//			System.out.println("SyncPost getStatusLine: "+response.getStatusLine().toString());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				byte[] buffer = EntityUtils.toByteArray(entity);
//				System.out.println("SyncPost Response: " + new String(buffer));
				return buffer;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] SyncPost(String url, JSONObject json) {
		String content = json.toString();
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("User-Agent", USER_AGENT);
		try {
			// 设置JSON参数及编码
			if (content != null && content != "") {
				StringEntity entity = new StringEntity(content, HTTP.UTF_8);
				entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				httpPost.setEntity(entity);
			}
			HttpResponse response = client.execute(httpPost);
			// System.out.println("SyncPost getStatusLine: "+
			// response.getStatusLine().toString());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				byte[] buffer = EntityUtils.toByteArray(entity);
				return buffer;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] SyncPost(String url, Map<String, Object> params, File file) {
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		// System.out.println("SyncPost URL: "+url);
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("User-Agent", USER_AGENT);
		// System.out.println("SyncPost User-Agent: "+USER_AGENT);
		try {
			MultipartEntity entities = new MultipartEntity();
			// 设置文字参数
			if (params != null) {
				for (String key : params.keySet()) {
					entities.addPart(key, new StringBody(params.get(key)
							.toString()));
					// System.out.println("param___key: "+key +
					// "  value: "+params.get(key).toString());
				}
			}
			// 设置文件参数
			if (file != null) {
				entities.addPart(file.getName(), new FileBody(file));
			}
			httpPost.setEntity(entities);

			byte[] buffer = null;

			// 可能需要提交3次
			for (int i = 0; i < 3; i++) {
				HttpResponse response = client.execute(httpPost);
				// System.out.println("SyncPost getStatusLine: "+
				// response.getStatusLine().toString());

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					buffer = EntityUtils.toByteArray(entity);
					// System.out.println("SyncPost Response: "+new
					// String(buffer));
					break;
				}
			}

			return buffer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] SyncPost(String url, byte[] bytes) {
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);

		// System.out.println("SyncPost URL: "+url);

		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("User-Agent", USER_AGENT);
		// System.out.println("SyncPost User-Agent: "+USER_AGENT);
		try {
			// 设置Byte参数
			if (bytes != null) {
				MultipartEntity entities = new MultipartEntity();
				entities.addPart("", new ByteArrayBody(bytes, ""));
				httpPost.setEntity(entities);
			}

			byte[] buffer = null;

			// 可能需要提交3次
			for (int i = 0; i < 3; i++) {
				HttpResponse response = client.execute(httpPost);
				// System.out.println("SyncPost getStatusLine: "+
				// response.getStatusLine().toString());

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					buffer = EntityUtils.toByteArray(entity);
					// System.out.println("SyncPost Response: "+new
					// String(buffer));
					break;
				}
			}

			return buffer;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] SyncPostForPlayer(String url, byte[] bytes) {
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);

		// System.out.println("SyncPost URL: "+url);

		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("User-Agent", USER_AGENT);
		httpPost.addHeader("Content-Type", "application/octet-stream");
		// System.out.println("SyncPost User-Agent: "+USER_AGENT);
		try {
			// 设置Byte参数
			if (bytes != null) {
				ByteArrayEntity entities = new ByteArrayEntity(bytes);
				
				httpPost.setEntity(entities);
			}

			byte[] buffer = null;

			// 可能需要提交3次
			for (int i = 0; i < 3; i++) {
				HttpResponse response = client.execute(httpPost);
				// System.out.println("SyncPost getStatusLine: "+
				// response.getStatusLine().toString());

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					buffer = EntityUtils.toByteArray(entity);
					// System.out.println("SyncPost Response: "+new
					// String(buffer));
					break;
				}
			}

			return buffer;

		} catch (Exception e) {
			System.out.println("post请求异常----"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public byte[] SyncPost(String url, byte[] bytes, File file) {
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		// System.out.println("SyncPost URL: "+url);
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("User-Agent", USER_AGENT);
		// System.out.println("SyncPost User-Agent: "+USER_AGENT);
		try {
			MultipartEntity entities = new MultipartEntity();
			// 设置Byte参数
			if (bytes != null) {
				entities.addPart("", new ByteArrayBody(bytes, ""));
			}
			// 设置文件参数
			if (file != null) {
				entities.addPart(file.getName(), new FileBody(file));
			}
			httpPost.setEntity(entities);

			byte[] buffer = null;

			// 可能需要提交3次
			for (int i = 0; i < 3; i++) {
				HttpResponse response = client.execute(httpPost);
				// System.out.println("SyncPost getStatusLine: "+
				// response.getStatusLine().toString());

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					buffer = EntityUtils.toByteArray(entity);
					// System.out.println("SyncPost Response: "+new
					// String(buffer));
					break;
				}
			}

			return buffer;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] SyncPost(String url, File file) {
		return SyncPost(url, new File[] { file });
	}

	public byte[] SyncPost(String url, File[] files) {
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);

		// System.out.println("SyncPost URL: "+url);

		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("User-Agent", USER_AGENT);
		// System.out.println("SyncPost User-Agent: "+USER_AGENT);
		try {
			// 设置文件参数
			if (files != null && files.length > 0) {
				MultipartEntity entities = new MultipartEntity();
				for (File file : files) {
					if (file != null) {
						entities.addPart(file.getName(), new FileBody(file));
						// System.out.println("SyncPost FileName: "+file.getName());
					}
				}
				httpPost.setEntity(entities);
			}

			byte[] buffer = null;

			// 可能需要提交3次
			for (int i = 0; i < 3; i++) {
				HttpResponse response = client.execute(httpPost);
				// System.out.println("SyncPost getStatusLine: "+
				// response.getStatusLine().toString());

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					buffer = EntityUtils.toByteArray(entity);
					// System.out.println("SyncPost Response: "+new
					// String(buffer));
					break;
				}
			}

			return buffer;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] SyncPut() {
		return SyncPut(this.url);
	}

	public byte[] SyncPut(String url) {
		return SyncPut(url, null);
	}

	public byte[] SyncPut(String url, Map<String, Object> params) {
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);

		// System.out.println("SyncPut URL: "+url);

		// url参数处理
		List<NameValuePair> _params = null;
		if (params != null) {
			_params = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				_params.add(new BasicNameValuePair(key, params.get(key)
						.toString()));
				// System.out.println("param___key: "+key +
				// "  value: "+params.get(key).toString());
			}
		}

		HttpPut httpPut = new HttpPut(url);
		httpPut.addHeader("User-Agent", USER_AGENT);
		// System.out.println("SyncPut User-Agent: "+USER_AGENT);
		try {
			// 设置参数及编码
			if (_params != null)
				httpPut.setEntity(new UrlEncodedFormEntity(_params, HTTP.UTF_8));
			HttpResponse response = client.execute(httpPut);
			// System.out.println("SyncPut getStatusLine: "+
			// response.getStatusLine().toString());

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				byte[] buffer = EntityUtils.toByteArray(entity);
				// System.out.println("SyncPut Response: "+new String(buffer));
				return buffer;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] SyncDelete() {
		return SyncDelete(this.url);
	}

	public byte[] SyncDelete(String url) {
		return SyncDelete(url, null);
	}

	public byte[] SyncDelete(String url, Map<String, Object> params) {
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);

		// url参数处理
		if (params != null)
			url = __MakeURL(url, params);

		// System.out.println("SyncDelete URL: "+url);

		HttpDelete httpDelete = new HttpDelete(url);
		httpDelete.addHeader("User-Agent", USER_AGENT);
		// System.out.println("SyncDelete User-Agent: "+USER_AGENT);
		try {
			HttpResponse response = client.execute(httpDelete);
			// System.out.println("SyncDelete getStatusLine: "+
			// response.getStatusLine().toString());

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				byte[] buffer = EntityUtils.toByteArray(entity);
				// System.out.println("SyncDelete Response: "+new
				// String(buffer));
				return buffer;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String __MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0)
			url.append('?');

		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
		}

		return url.toString().replace("?&", "?");
	}
}
