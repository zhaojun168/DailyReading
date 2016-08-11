package com.android.dzj.app.dailyreading.play.connector;

import android.content.Context;

import com.android.callback.ConnectionCallback;
import com.android.dzj.app.dailyreading.connector.BaseConnector;
import com.android.dzj.app.dailyreading.play.entity.Constellation_Json;
import com.android.dzj.app.dailyreading.play.request.ConstellationRequest;
import com.android.dzj.app.dailyreading.utils.Constants;
import com.android.dzj.app.dailyreading.utils.Util;
import com.android.http.callback.AsyncIncident;
import com.android.util.FileUtils;
import com.android.util.MLog;
import com.android.util.WebRequest2;

public class ConstellationConnector extends BaseConnector {

	private ConstellationRequest request;
	
	public ConstellationConnector(Context context) {
		super(context);

		request = new ConstellationRequest();
		request.star = ConstellationRequest.STAR;
		request.showapi_appid = ConstellationRequest.SHOWAPI_APPID;
//		request.showapi_timestamp = ConstellationRequest.SHOWAPI_TIMESTAMP;
		request.showapi_sign = ConstellationRequest.SHOWAPI_SIGN;

	}

	private void doGet(ConnectionCallback callback) {
		super.AsyncRequest(new AsyncIncident() {

			@Override
			public Object incident() {
				String url = formatApiUrl(Constants.CONSTELLATION_HOST_URL, request.star, request.showapi_appid, Util.getTime(),  request.showapi_sign);
				WebRequest2 wr = new WebRequest2();

				byte[] by = wr.SyncGet(url);
				if (by == null) {
					return null;
				}
				
				Constellation_Json json = (Constellation_Json) FileUtils.ReadFromJsonData(by, Constellation_Json.class);
				MLog.e(Constants.TAG, "请求结果--->" + new String(by));
				return json;
			}
		}, callback);
	}
	
	
	/** star--->星座      timestamp--->时间戳  */
	public void getData(ConnectionCallback callback, String star){
		request.star = star;
		doGet(callback);
	}

}
