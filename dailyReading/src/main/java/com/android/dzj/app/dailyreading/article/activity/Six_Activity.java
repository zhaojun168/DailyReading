package com.android.dzj.app.dailyreading.article.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.callback.ConnectionCallback;
import com.android.dzj.app.dailyreading.R;
import com.android.dzj.app.dailyreading.article.adapter.Article_Adapter_Two;
import com.android.dzj.app.dailyreading.article.connector.ArticleConnector;
import com.android.dzj.app.dailyreading.article.entity.Article_Inof;
import com.android.dzj.app.dailyreading.article.entity.Article_Json;
import com.android.dzj.app.dailyreading.main.activity.Activity_Main;
import com.android.util.NetworkUtils;
import com.android.util.PreferenceUtils;
import com.android.util.UiUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.umeng.analytics.MobclickAgent;

public class Six_Activity extends Fragment implements OnRefreshListener2<ListView>, OnItemClickListener {
	
	private Activity_Main mContext;
	private View mView;
	private PullToRefreshListView mListView;
	private List<Article_Inof> mainList = new ArrayList<Article_Inof>();
	private ArticleConnector mConnector;
	private Article_Adapter_Two adapter;
	private int page = 1;
	private boolean isFirstInSpecial;// 是否是第一次进入此页面
	public static final String TYPEID = "6";
	public static final String DATA_CACHE_NAME = "Six_data";
	
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (mView == null) {
			mContext = (Activity_Main) getActivity();
			mView = inflater.inflate(R.layout.article_fragment, null);
			initView();
		}
		ViewGroup group = (ViewGroup) mView.getParent();
		if (group != null) {
			group.removeView(mView);
		}
		return mView;
	}
	
	private void initView() {
		mListView = (PullToRefreshListView) mView.findViewById(R.id.listView);
		setListeners();
		setInitListViewData();
		networkUtils();
	}
	
	private void setListeners() {
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
	}
	
	
	
	private void setInitListViewData() {
		mConnector = new ArticleConnector(mContext);
		adapter = new Article_Adapter_Two(mContext, mainList);
		isFirstInSpecial = PreferenceUtils.getPrefBoolean(mContext, "Zero_isFirstInSpecial", true);
		if (isFirstInSpecial) {// 如果是第一次进入此页面，则从网络拉取数据，并缓存到本地
			mConnector.getArticleList(callback_firstload, page, TYPEID, true, DATA_CACHE_NAME);
		} else {// 否则，直接读取缓存数据
			if (null != mConnector.getCacheData(DATA_CACHE_NAME)) {
				mainList.addAll(mConnector.getCacheData(DATA_CACHE_NAME));
			}
			adapter.setList(mainList);
		}
		mListView.setAdapter(adapter);
		mConnector.getArticleList(callback_firstload, page, TYPEID, true, DATA_CACHE_NAME);// 重新请求网络数据，把新的数据缓存到本地
	}

	/** 第一次加载 */
	ConnectionCallback callback_firstload = new ConnectionCallback() {
		@Override
		public void result(Object obj) {
			Article_Json json = (Article_Json) obj;
			if (json != null && null != json.showapi_res_body.pagebean.contentlist && json.showapi_res_body.pagebean.contentlist.size() > 0) {
				mainList.clear();
				mainList.addAll(json.showapi_res_body.pagebean.contentlist);
			}
			setInitAdapter();
			mListView.onRefreshComplete();
			if (null != json && !json.showapi_res_error.equals("")) {
				UiUtils.toast(mContext, json.showapi_res_error);
			}
		}
	};

	/** 上拉加载 */
	ConnectionCallback callback_loadmore = new ConnectionCallback() {

		@Override
		public void result(Object obj) {
			Article_Json json = (Article_Json) obj;
			
			if (json != null && null != json.showapi_res_body.pagebean.contentlist
					&& json.showapi_res_body.pagebean.contentlist.size() > 0) {
				mainList.addAll(json.showapi_res_body.pagebean.contentlist);
				adapter.setList(mainList);
				adapter.notifyDataSetChanged();
			}
			mListView.onRefreshComplete();
			if (null != json && !json.showapi_res_error.equals("")) {
				UiUtils.toast(mContext, json.showapi_res_error);
			}
		}

	};

	/** 下拉刷新 */
	ConnectionCallback callback_refresh = new ConnectionCallback() {

		@Override
		public void result(Object obj) {
			
			Article_Json json = (Article_Json) obj;
			if (null != json && null != json.showapi_res_body.pagebean.contentlist
					&& json.showapi_res_body.pagebean.contentlist.size() > 0) {
				mainList.clear();
				mainList.addAll(json.showapi_res_body.pagebean.contentlist);
				adapter.setList(mainList);
				adapter.notifyDataSetChanged();
			}
			mListView.onRefreshComplete();
			if (null != json && !json.showapi_res_error.equals("")) {
				UiUtils.toast(mContext, json.showapi_res_error);
			}
		}

	};

	private void setInitAdapter() {
		if (0 == mainList.size() && null != mConnector.getCacheData(DATA_CACHE_NAME)) {
			mainList.addAll(mConnector.getCacheData(DATA_CACHE_NAME));
		}
		if (mainList.size() > 0) {
			adapter.setList(mainList);
		}
		adapter.setList(mainList);
		PreferenceUtils.setPrefBoolean(mContext, "Zero_isFirstInSpecial", false);// 当执行到此时，则代表不是第一次进入此页面
	}

	/**下拉刷新*/
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!NetworkUtils.instance(mContext).isNetworkConnected()) {
			UiUtils.toast(mContext, R.string.toast_http_abnormal);
			new Handler().postDelayed(new Runnable(){    
				public void run() {    
					mListView.onRefreshComplete();
				}    
			}, 1000);
			return;
		}
		page = 1;
		mConnector.getArticleList(callback_refresh, page, TYPEID, true, DATA_CACHE_NAME);
	}

	/**上拉加载*/
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!NetworkUtils.instance(mContext).isNetworkConnected()) {
			UiUtils.toast(mContext, R.string.toast_http_abnormal);
			new Handler().postDelayed(new Runnable(){    
				public void run() {    
					mListView.onRefreshComplete();
				}    
			}, 1000);
			return;
		}
		page++;
		mConnector.getArticleList(callback_loadmore, page, TYPEID, false, DATA_CACHE_NAME);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		((Article_Adapter_Two.ListViewItem) view.getTag()).showArticle();
	}
	
	private void networkUtils() {
		if (!NetworkUtils.instance(mContext).isNetworkConnected()) {
			UiUtils.toast(mContext, R.string.toast_http_abnormal);
		}
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(mContext);       //统计时长
	    MobclickAgent.onPageStart("Six_Activity"); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(mContext);  //统计时长
	    MobclickAgent.onPageEnd("Six_Activity"); //统计页面
	}
}

