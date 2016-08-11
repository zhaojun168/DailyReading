package com.android.dzj.app.dailyreading.article.connector;

import java.util.List;

import android.content.Context;

import com.android.callback.ConnectionCallback;
import com.android.dzj.app.dailyreading.article.entity.Article_Inof;
import com.android.dzj.app.dailyreading.article.entity.Article_Json;
import com.android.dzj.app.dailyreading.article.request.ArticleRequest;
import com.android.dzj.app.dailyreading.connector.BaseConnector;
import com.android.dzj.app.dailyreading.utils.Constants;
import com.android.dzj.app.dailyreading.utils.Util;
import com.android.http.callback.AsyncIncident;
import com.android.util.FileUtils;
import com.android.util.MLog;
import com.android.util.WebRequest2;

public class ArticleConnector extends BaseConnector {

	private ArticleRequest request;
	
	public ArticleConnector(Context context) {
		super(context);

		request = new ArticleRequest();
		request.page = ArticleRequest.PAGE;
		request.showapi_appid = ArticleRequest.SHOWAPI_APPID;
//		request.showapi_timestamp = ArticleRequest.SHOWAPI_TIMESTAMP;
		request.typeId = ArticleRequest.TYPEID;
		request.showapi_sign = ArticleRequest.SHOWAPI_SIGN;

	}

	private void doGet(ConnectionCallback callback, final boolean isFirstLoad, final String data_cache_name) {
		super.AsyncRequest(new AsyncIncident() {

			@Override
			public Object incident() {
				String url = formatApiUrl(Constants.ARTICLE_HOST_URL, request.page, request.typeId, request.showapi_appid, Util.getTime(), request.showapi_sign);
				WebRequest2 wr = new WebRequest2();

				byte[] by = wr.SyncGet(url);
				if (by == null) {
					return null;
				}
				
				Article_Json json = (Article_Json) FileUtils.ReadFromJsonData(by, Article_Json.class);
				MLog.e(Constants.TAG, "请求结果--->" + new String(by));

				if(isFirstLoad) {
					FileUtils.saveDataToFile(context, data_cache_name, new String(by));//缓存数据
				}
				return json;
			}
		}, callback);
	}
	
	
	/** page-->页数      time--->时间      typeId--->类型    isFirstLoad--->是否缓存     data_cache_name--->缓存名称     */
	public void getArticleList(ConnectionCallback callback, int page, String typeId, boolean isFirstLoad, String data_cache_name ){
		request.page = page;
		request.typeId = typeId;
		doGet(callback, isFirstLoad, data_cache_name);
	}
	
	/**
	 * 获取缓存的第一页数据
	* <p>Description: </p> 
	* @return
	 */
	public List<Article_Inof> getCacheData(String data_cache_name)
	{
		Article_Json json = (Article_Json) FileUtils.getCacheData(context, data_cache_name, Article_Json.class);
		if(null != json)
			return json.showapi_res_body.pagebean.contentlist;//读取缓存
		else
			return null;
	}

}
