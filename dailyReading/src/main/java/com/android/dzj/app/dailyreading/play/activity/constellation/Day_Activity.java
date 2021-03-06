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
import com.android.dzj.app.dailyreading.play.entity.Constellation_Day;
import com.android.dzj.app.dailyreading.play.entity.Constellation_Json;
import com.android.dzj.app.dailyreading.utils.Constants;
import com.android.dzj.app.dailyreading.utils.Util;
import com.android.dzj.app.dailyreading.view.MyDialog;
import com.android.util.NetworkUtils;
import com.android.util.PreferenceUtils;
import com.android.util.UiUtils;
import com.umeng.analytics.MobclickAgent;

/**今日*/
public class Day_Activity extends Fragment {
	
	private Constellation_Activity mContext;
	private View mView;
	private ScrollView mScrollView;
	private TextView tv_summary, tv_love, tv_money, tv_work;
	private ImageView iv_summary_star, iv_love_star, iv_money_star, iv_work_star, imageView;
	private String constellation;
	private ConstellationConnector mConnector;
	private TextView tv_time, tv_grxz, tv_lucky_num, tv_lucky_time, tv_lucky_color, tv_lucky_direction, 
	                 tv_day_notice, tv_general_txt, tv_love_txt, tv_work_txt, tv_money_txt;
	private MyDialog mDialog;
	
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
		mDialog = new MyDialog(mContext);
		mConnector = new ConstellationConnector(mContext);
		constellation = PreferenceUtils.getPrefString(mContext, Constants.CONSTELLATION, "");
		mScrollView = (ScrollView) mView.findViewById(R.id.scrollView);
		View mDay = View.inflate(mContext, R.layout.activity_day, null);
		mScrollView.addView(mDay);
		tv_time = (TextView) mDay.findViewById(R.id.time);
		tv_summary = (TextView) mDay.findViewById(R.id.summary);
		tv_love = (TextView) mDay.findViewById(R.id.love);
		tv_money = (TextView) mDay.findViewById(R.id.money);
		tv_work = (TextView) mDay.findViewById(R.id.work);
		iv_summary_star = (ImageView) mDay.findViewById(R.id.summary_star);
		iv_love_star = (ImageView) mDay.findViewById(R.id.love_star);
		iv_money_star = (ImageView) mDay.findViewById(R.id.money_star);
		iv_work_star = (ImageView) mDay.findViewById(R.id.work_star);
		imageView = (ImageView) mDay.findViewById(R.id.imagView);
		tv_grxz = (TextView) mDay.findViewById(R.id.grxz);
		tv_lucky_num = (TextView) mDay.findViewById(R.id.lucky_num);
		tv_lucky_time = (TextView) mDay.findViewById(R.id.lucky_time);
		tv_lucky_color = (TextView) mDay.findViewById(R.id.lucky_color);
		tv_lucky_direction = (TextView) mDay.findViewById(R.id.lucky_direction);
		tv_day_notice = (TextView) mDay.findViewById(R.id.day_notice);
		tv_general_txt = (TextView) mDay.findViewById(R.id.general_txt);
		tv_love_txt = (TextView) mDay.findViewById(R.id.love_txt);
		tv_money_txt = (TextView) mDay.findViewById(R.id.money_txt);
		tv_work_txt = (TextView) mDay.findViewById(R.id.work_txt);
		mDialog.show();
		if (!NetworkUtils.instance(mContext).isNetworkConnected()) {
			UiUtils.toast(mContext, R.string.toast_http_abnormal);
			mDialog.dismiss();
			return;
		}
		mConnector.getData(callback_firstload, constellation);
	}
	
	
	ConnectionCallback callback_firstload = new ConnectionCallback() {
		@Override
		public void result(Object obj) {
			Constellation_Json json = (Constellation_Json) obj;
			if (null != json && null != json.showapi_res_body) {
				Constellation_Day constellation_Day = json.showapi_res_body.day;
				tv_time.setText(constellation_Day.time + "的运势");
				tv_summary.setText(R.string.summary);
				tv_love.setText(R.string.love);
				tv_money.setText(R.string.money);
				tv_work.setText(R.string.work);
				Util.setResource(imageView, constellation);
				Util.setImageDrawable(iv_summary_star, constellation_Day.summary_star);
				Util.setImageDrawable(iv_love_star, constellation_Day.love_star);
				Util.setImageDrawable(iv_money_star, constellation_Day.money_star);
				Util.setImageDrawable(iv_work_star, constellation_Day.work_star);
				tv_grxz.setText("贵人星座 ：" + constellation_Day.grxz);
				tv_lucky_num.setText("幸运数字 ：" + constellation_Day.lucky_num);
				tv_lucky_time.setText("吉时 ：" + constellation_Day.lucky_time);
				tv_lucky_color.setText("吉色 ：" + constellation_Day.lucky_color);
				tv_lucky_direction.setText("吉利方位 ：" + constellation_Day.lucky_direction);
				tv_day_notice.setText("运势提醒 ：" + constellation_Day.day_notice);
				tv_general_txt.setText("运势简评 ：" + constellation_Day.general_txt);
				tv_love_txt.setText("爱情运势 ：" + constellation_Day.love_txt);
				tv_money_txt.setText("财富运势 ：" + constellation_Day.money_txt);
				tv_work_txt.setText("工作运势 ：" + constellation_Day.work_txt);
			}
			if (null != json && !json.showapi_res_error.equals("")) {
				UiUtils.toast(mContext, json.showapi_res_error);
				mDialog.dismiss();
			}
			mDialog.dismiss();
		}
	};
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(mContext);       //统计时长
	    MobclickAgent.onPageStart("Day_Activity"); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(mContext);  //统计时长
	    MobclickAgent.onPageEnd("Day_Activity"); //统计页面
	}
}
