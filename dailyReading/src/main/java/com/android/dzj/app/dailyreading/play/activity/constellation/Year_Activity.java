package com.android.dzj.app.dailyreading.play.activity.constellation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.callback.ConnectionCallback;
import com.android.dzj.app.dailyreading.R;
import com.android.dzj.app.dailyreading.play.connector.ConstellationConnector;
import com.android.dzj.app.dailyreading.play.entity.Constellation_Json;
import com.android.dzj.app.dailyreading.play.entity.Constellation_Year;
import com.android.dzj.app.dailyreading.utils.Constants;
import com.android.dzj.app.dailyreading.utils.Util;
import com.android.util.NetworkUtils;
import com.android.util.PreferenceUtils;
import com.android.util.UiUtils;
import com.umeng.analytics.MobclickAgent;


public class Year_Activity extends Fragment {
	
	private Constellation_Activity mContext;
	private View mView;
	private ScrollView mScrollView;
	private String constellation;
	private ConstellationConnector mConnector;
	private ImageView imageView;
	private TextView tv_time, general_index, love_index, money_index, work_index, oneword,
	                 tv_general_txt, tv_love_txt, tv_work_txt, tv_money_txt;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (mView == null) {
			mContext = (Constellation_Activity) getActivity();
			mView = inflater.inflate(R.layout.constellation_fagment, null);
			initView();
		}
		ViewGroup group = (ViewGroup) mView.getParent();
		if (group != null) {
			group.removeView(mView);
		}
		return mView;
	}
	
	private void initView() {
		mConnector = new ConstellationConnector(mContext);
		constellation = PreferenceUtils.getPrefString(mContext, Constants.CONSTELLATION, "");
		mScrollView = (ScrollView) mView.findViewById(R.id.scrollView);
		View mYear = View.inflate(mContext, R.layout.activity_year, null);
		mScrollView.addView(mYear);
		tv_time = (TextView) mYear.findViewById(R.id.time);
		imageView = (ImageView) mYear.findViewById(R.id.imagView);
		general_index = (TextView) mYear.findViewById(R.id.general_index);
		love_index = (TextView) mYear.findViewById(R.id.love_index);
		money_index = (TextView) mYear.findViewById(R.id.money_index);
		work_index = (TextView) mYear.findViewById(R.id.work_index);
		oneword = (TextView) mYear.findViewById(R.id.oneword);
		tv_general_txt = (TextView) mYear.findViewById(R.id.general_txt);
		tv_love_txt = (TextView) mYear.findViewById(R.id.love_txt);
		tv_money_txt = (TextView) mYear.findViewById(R.id.money_txt);
		tv_work_txt = (TextView) mYear.findViewById(R.id.work_txt);
		if (!NetworkUtils.instance(mContext).isNetworkConnected()) {
			UiUtils.toast(mContext, R.string.toast_http_abnormal);
			return;
		}
		mConnector.getData(callback_firstload, constellation);
	}
	
	
	ConnectionCallback callback_firstload = new ConnectionCallback() {
		@Override
		public void result(Object obj) {
			Constellation_Json json = (Constellation_Json) obj;
			if (null != json && null != json.showapi_res_body) {
				Constellation_Year constellation_Year = json.showapi_res_body.year;
				tv_time.setText(constellation_Year.time + "的运势");
				Util.setResource(imageView, constellation);
				general_index.setText("综合指数 ：" + constellation_Year.general_index);
				love_index.setText("爱情指数 ：" + constellation_Year.love_index);
				money_index.setText("财富指数 ：" + constellation_Year.money_index);
				work_index.setText("工作指数 ：" + constellation_Year.work_index);
				oneword.setText("一句话简评 ：" + constellation_Year.oneword);
				tv_general_txt.setText("运势简评 ：" + constellation_Year.general_txt);
				tv_love_txt.setText("爱情运势 ：" + constellation_Year.love_txt);
				tv_money_txt.setText("财富运势 ：" + constellation_Year.money_txt);
				tv_work_txt.setText("工作运势 ：" + constellation_Year.work_txt);
					
			}
			if (null != json && !json.showapi_res_error.equals("")) {
				UiUtils.toast(mContext, json.showapi_res_error);
			}
		}
	};
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(mContext);       //统计时长
	    MobclickAgent.onPageStart("Constellation_Activity"); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(mContext);  //统计时长
	    MobclickAgent.onPageEnd("Constellation_Activity"); //统计页面
	}
	
	 
}
