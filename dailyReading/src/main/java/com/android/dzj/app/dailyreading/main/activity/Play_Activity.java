package com.android.dzj.app.dailyreading.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.dzj.app.dailyreading.R;
import com.android.dzj.app.dailyreading.play.activity.Qq_Activity;
import com.android.dzj.app.dailyreading.play.activity.constellation.Constellation_Activity;
import com.android.dzj.app.dailyreading.utils.Constants;
import com.android.util.PreferenceUtils;

public class Play_Activity  extends Fragment implements OnClickListener {
	
	private Activity_Main mContext;
	private View mView;
	private ImageView qq;
	private LinearLayout baiyang, shuangyu, shuiping, mojie, sheshou, tianxie,
	                  tiancheng, chunv, shizi, juxie, shuangzi, jinniu;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (mView == null) {
			mContext = (Activity_Main) getActivity();
			mView = inflater.inflate(R.layout.activity_play, null);
			initWidget();
		}
		ViewGroup group = (ViewGroup) mView.getParent();
		if (group != null) {
			group.removeView(mView);
		}
		return mView;
	}
	
	
	private void initWidget() {
		qq = (ImageView) mView.findViewById(R.id.qq);
		baiyang = (LinearLayout) mView.findViewById(R.id.baiyang);
		shuangyu = (LinearLayout) mView.findViewById(R.id.shuangyu);
		shuiping = (LinearLayout) mView.findViewById(R.id.shuiping);
		mojie = (LinearLayout) mView.findViewById(R.id.mojie);
		sheshou = (LinearLayout) mView.findViewById(R.id.sheshou);
		tianxie = (LinearLayout) mView.findViewById(R.id.tianxie);
		tiancheng = (LinearLayout) mView.findViewById(R.id.tiancheng);
		chunv = (LinearLayout) mView.findViewById(R.id.chunv);
		shizi = (LinearLayout) mView.findViewById(R.id.shizi);
		juxie = (LinearLayout) mView.findViewById(R.id.juxie);
		shuangzi = (LinearLayout) mView.findViewById(R.id.shuangzi);
		jinniu = (LinearLayout) mView.findViewById(R.id.jinniu);
		
		setListeners(); 
	}
	
	private void setListeners() {
		qq.setOnClickListener(this);
		baiyang.setOnClickListener(this);
		shuangyu.setOnClickListener(this);
		shuiping.setOnClickListener(this);
		mojie.setOnClickListener(this);
		sheshou.setOnClickListener(this);
		tianxie.setOnClickListener(this);
		tiancheng.setOnClickListener(this);
		chunv.setOnClickListener(this);
		shizi.setOnClickListener(this);
		juxie.setOnClickListener(this);
		shuangzi.setOnClickListener(this);
		jinniu.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.qq:
			intent.setClass(mContext, Qq_Activity.class);
			startActivity(intent);
			break;
		case R.id.baiyang:
			intent.setClass(mContext, Constellation_Activity.class);
			PreferenceUtils.setPrefString(mContext, Constants.CONSTELLATION, Constants.BAIYANG);
			startActivity(intent);
			break;
		case R.id.shuangyu:
			intent.setClass(mContext, Constellation_Activity.class);
			PreferenceUtils.setPrefString(mContext, Constants.CONSTELLATION, Constants.SHUANGYU);
			startActivity(intent);
			break;
		case R.id.shuiping:
			intent.setClass(mContext, Constellation_Activity.class);
			PreferenceUtils.setPrefString(mContext, Constants.CONSTELLATION, Constants.SHUIPING);
			startActivity(intent);
			break;
		case R.id.mojie:
			intent.setClass(mContext, Constellation_Activity.class);
			PreferenceUtils.setPrefString(mContext, Constants.CONSTELLATION, Constants.MOJIE);
			startActivity(intent);
			break;
		case R.id.sheshou:
			intent.setClass(mContext, Constellation_Activity.class);
			PreferenceUtils.setPrefString(mContext, Constants.CONSTELLATION, Constants.SHESHOU);
			startActivity(intent);
			break;
		case R.id.tianxie:
			intent.setClass(mContext, Constellation_Activity.class);
			PreferenceUtils.setPrefString(mContext, Constants.CONSTELLATION, Constants.TIANXIE);
			startActivity(intent);
			break;
		case R.id.tiancheng:
			intent.setClass(mContext, Constellation_Activity.class);
			PreferenceUtils.setPrefString(mContext, Constants.CONSTELLATION, Constants.TIANCHENG);
			startActivity(intent);
			break;
		case R.id.chunv:
			intent.setClass(mContext, Constellation_Activity.class);
			PreferenceUtils.setPrefString(mContext, Constants.CONSTELLATION, Constants.CHUNV);
			startActivity(intent);
			break;
		case R.id.shizi:
			intent.setClass(mContext, Constellation_Activity.class);
			PreferenceUtils.setPrefString(mContext, Constants.CONSTELLATION, Constants.SHIZI);
			startActivity(intent);
			break;
		case R.id.juxie:
			intent.setClass(mContext, Constellation_Activity.class);
			PreferenceUtils.setPrefString(mContext, Constants.CONSTELLATION, Constants.JUXIE);
			startActivity(intent);
			break;
		case R.id.shuangzi:
			intent.setClass(mContext, Constellation_Activity.class);
			PreferenceUtils.setPrefString(mContext, Constants.CONSTELLATION, Constants.SHUANGZI);
			startActivity(intent);
			break;
		case R.id.jinniu:
			intent.setClass(mContext, Constellation_Activity.class);
			PreferenceUtils.setPrefString(mContext, Constants.CONSTELLATION, Constants.JINNIU);
			startActivity(intent);
			break;
		}
	}
}
