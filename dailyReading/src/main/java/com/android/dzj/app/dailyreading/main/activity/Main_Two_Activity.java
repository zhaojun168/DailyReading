package com.android.dzj.app.dailyreading.main.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.callback.ConnectionCallback;
import com.android.dzj.app.dailyreading.R;
import com.android.dzj.app.dailyreading.taogirl.connector.TaoGirl_Connector;
import com.android.dzj.app.dailyreading.taogirl.entity.TaoGirl_Json;
import com.android.util.UiUtils;

public class Main_Two_Activity  extends Fragment {
	
	private Activity_Main mContext;
	private View mView;
	private TaoGirl_Connector mConnector;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (mView == null) {
			mContext = (Activity_Main) getActivity();
			mView = inflater.inflate(R.layout.activity_two, null);
			init();
		}
		ViewGroup group = (ViewGroup) mView.getParent();
		if (group != null) {
			group.removeView(mView);
		}
		return mView;
	}
	
	private void init() {
		mConnector = new TaoGirl_Connector(mContext);
		mConnector.getTaoGirlList(callback_firstload, 1, "韩版", false, "");
	}
	
	/** 第一次加载 */
	ConnectionCallback callback_firstload = new ConnectionCallback() {
		@Override
		public void result(Object obj) {
			TaoGirl_Json json = (TaoGirl_Json) obj;
			/*if (json != null && null != json.showapi_res_body.pagebean.contentlist && json.showapi_res_body.pagebean.contentlist.size() > 0) {
				mainList.clear();
				mainList.addAll(json.showapi_res_body.pagebean.contentlist);
			}
			setInitAdapter();
			mListView.onRefreshComplete();
			if (null != json && !json.showapi_res_error.equals("")) {
				UiUtils.toast(mContext, json.showapi_res_error);
			}*/
		}
	};
}
