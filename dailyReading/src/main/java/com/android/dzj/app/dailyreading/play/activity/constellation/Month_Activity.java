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
import com.android.dzj.app.dailyreading.play.entity.Constellation_Month;
import com.android.dzj.app.dailyreading.utils.Constants;
import com.android.dzj.app.dailyreading.utils.Util;
import com.android.util.NetworkUtils;
import com.android.util.PreferenceUtils;
import com.android.util.UiUtils;
import com.umeng.analytics.MobclickAgent;

/**本月*/
public class Month_Activity extends Fragment {
	
	private Constellation_Activity mContext;
	private View mView;
	private ScrollView mScrollView;
	private TextView tv_summary, tv_love, tv_money, tv_work;
	private ImageView iv_summary_star, iv_love_star, iv_money_star, iv_work_star, imageView;
	private String constellation;
	private ConstellationConnector mConnector;
	private TextView tv_time, tv_grxz, tv_xrxz, tv_yfxz, tv_month_advantage, tv_month_weakness,
	                 tv_lucky_direction,tv_general_txt, tv_love_txt, tv_work_txt, tv_money_txt;
	
	
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
		View mMonth = View.inflate(mContext, R.layout.activity_month, null);
		mScrollView.addView(mMonth);
		tv_time = (TextView) mMonth.findViewById(R.id.time);
		tv_summary = (TextView) mMonth.findViewById(R.id.summary);
		tv_love = (TextView) mMonth.findViewById(R.id.love);
		tv_money = (TextView) mMonth.findViewById(R.id.money);
		tv_work = (TextView) mMonth.findViewById(R.id.work);
		iv_summary_star = (ImageView) mMonth.findViewById(R.id.summary_star);
		iv_love_star = (ImageView) mMonth.findViewById(R.id.love_star);
		iv_money_star = (ImageView) mMonth.findViewById(R.id.money_star);
		iv_work_star = (ImageView) mMonth.findViewById(R.id.work_star);
		imageView = (ImageView) mMonth.findViewById(R.id.imagView);
		tv_grxz = (TextView) mMonth.findViewById(R.id.grxz);
		tv_xrxz = (TextView) mMonth.findViewById(R.id.xrxz);
		tv_yfxz = (TextView) mMonth.findViewById(R.id.yfxz);
		tv_lucky_direction = (TextView) mMonth.findViewById(R.id.lucky_direction);
		tv_month_advantage = (TextView) mMonth.findViewById(R.id.month_advantage);
		tv_month_weakness = (TextView) mMonth.findViewById(R.id.month_weakness);
		tv_general_txt = (TextView) mMonth.findViewById(R.id.general_txt);
		tv_love_txt = (TextView) mMonth.findViewById(R.id.love_txt);
		tv_work_txt = (TextView) mMonth.findViewById(R.id.work_txt);
		tv_money_txt = (TextView) mMonth.findViewById(R.id.money_txt);
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
				Constellation_Month constellation_Month = json.showapi_res_body.month;
				tv_time.setText(constellation_Month.time + "的运势");
				tv_summary.setText(R.string.summary);
				tv_love.setText(R.string.love);
				tv_money.setText(R.string.money);
				tv_work.setText(R.string.work);
				Util.setResource(imageView, constellation);
				Util.setImageDrawable(iv_summary_star, constellation_Month.summary_star);
				Util.setImageDrawable(iv_love_star, constellation_Month.love_star);
				Util.setImageDrawable(iv_money_star, constellation_Month.money_star);
				Util.setImageDrawable(iv_work_star, constellation_Month.work_star);
				tv_grxz.setText("贵人星座 ：" + constellation_Month.grxz);
				tv_xrxz.setText("小人星座 ：" + constellation_Month.xrxz);
				tv_yfxz.setText("缘分星座 ："+constellation_Month.yfxz);
				tv_lucky_direction.setText("吉利方位 ：" + constellation_Month.lucky_direction);
				tv_month_advantage.setText("本月优势 ：" + constellation_Month.month_advantage);
				tv_month_weakness.setText("本月弱势 ：" + constellation_Month.month_weakness);
				tv_general_txt.setText("运势简评 ：" + constellation_Month.general_txt);
				tv_love_txt.setText("爱情运势 ：" + constellation_Month.love_txt);
				tv_money_txt.setText("财富运势 ：" + constellation_Month.money_txt);
				tv_work_txt.setText("工作运势 ：" + constellation_Month.work_txt);
					
			}
			if (null != json && !json.showapi_res_error.equals("")) {
				UiUtils.toast(mContext, json.showapi_res_error);
			}
		}
	};
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(mContext);       //统计时长
	    MobclickAgent.onPageStart("Month_Activity"); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(mContext);  //统计时长
	    MobclickAgent.onPageEnd("Month_Activity"); //统计页面
	}
	
	 
}

