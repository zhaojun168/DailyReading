package com.android.dzj.app.dailyreading.play.connector;

import android.content.Context;

import com.android.callback.ConnectionCallback;
import com.android.dzj.app.dailyreading.connector.BaseConnector;
import com.android.dzj.app.dailyreading.play.entity.Qq_Json;
import com.android.dzj.app.dailyreading.play.request.QqRequest;
import com.android.dzj.app.dailyreading.utils.Constants;
import com.android.dzj.app.dailyreading.utils.Util;
import com.android.http.callback.AsyncIncident;
import com.android.util.FileUtils;
import com.android.util.MLog;
import com.android.util.WebRequest2;

public class QqConnector extends BaseConnector {

	private QqRequest request;
	
	public QqConnector(Context context) {
		super(context);

		request = new QqRequest();
		request.qq = QqRequest.QQ;
		request.showapi_appid = QqRequest.SHOWAPI_APPID;
//		request.showapi_timestamp = QqRequest.SHOWAPI_TIMESTAMP;
		request.showapi_sign = QqRequest.SHOWAPI_SIGN;

	}

	private void doGet(ConnectionCallback callback) {
		super.AsyncRequest(new AsyncIncident() {

			@Override
			public Object incident() {
				String url = formatApiUrl(Constants.QQ_HOST_URL, request.qq, request.showapi_appid, Util.getTime(),  request.showapi_sign);
				WebRequest2 wr = new WebRequest2();

				byte[] by = wr.SyncGet(url);
				if (by == null) {
					return null;
				}
				
				Qq_Json json = (Qq_Json) FileUtils.ReadFromJsonData(by, Qq_Json.class);
				MLog.e(Constants.TAG, "请求结果--->" + new String(by));
				return json;
			}
		}, callback);
	}
	
	
	/** qq--->QQ号码      */
	public void getData(ConnectionCallback callback, String qq){
		request.qq = qq;
		doGet(callback);
	}
	

}
