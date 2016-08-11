package com.android.dzj.app.dailyreading.play.activity;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.callback.ConnectionCallback;
import com.android.dzj.app.dailyreading.R;
import com.android.dzj.app.dailyreading.play.connector.QqConnector;
import com.android.dzj.app.dailyreading.play.entity.Qq_Json;
import com.android.dzj.app.dailyreading.view.MyDialog;
import com.android.ui.activity.BaseActivity;
import com.android.util.NetworkUtils;
import com.android.util.UiUtils;
import com.umeng.analytics.MobclickAgent;

public class Qq_Activity extends BaseActivity {

	private Context mContext;
	private MyDialog dialog;
	private ImageView mBack;
	private QqConnector mConnector;
	private TextView title, test, desc, score, grade, analysis;
	private EditText etView;

	@Override
	protected void setRootView() {
		setContentView(R.layout.activity_qq);
		mContext = this;
	}

	@Override
	protected void initWidget() {
		mConnector = new QqConnector(mContext);
		dialog = new MyDialog(mContext);
		mBack = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.tv_title);
		etView = (EditText) findViewById(R.id.et);
		test = (TextView) findViewById(R.id.test);
		desc = (TextView) findViewById(R.id.desc);
		score = (TextView) findViewById(R.id.score);
		grade = (TextView) findViewById(R.id.grade);
		analysis = (TextView) findViewById(R.id.analysis);
	}

	@Override
	protected void initData() {
		// TODO testAuto-generated method stub

	}

	@Override
	protected void setListeners() {
		title.setText(R.string.qq);
		mBack.setOnClickListener(this);
		test.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.test:
			if (!NetworkUtils.instance(mContext).isNetworkConnected()) {
				UiUtils.toast(mContext, R.string.toast_http_abnormal);
				return;
			}
			getData();
			break;
		}
	}

	private void getData() {
		dialog.show();
		String qq_number = etView.getText().toString();
		if (!qq_number.equals("")) {
			mConnector.getData(callback_firstload, qq_number);
		} else {
			UiUtils.toast(mContext, R.string.qq_input);
			dialog.dismiss();
		}
		
	}

	ConnectionCallback callback_firstload = new ConnectionCallback() {
		@Override
		public void result(Object obj) {
			Qq_Json json = (Qq_Json) obj;
			if (null != json && null != json.showapi_res_body) {
				if (null != json.showapi_res_body.desc) {
					desc.setText("结果:" + json.showapi_res_body.desc);
				}
				if (null != json.showapi_res_body.score) {
					score.setText("打分:" + json.showapi_res_body.score);
				}
				if (null != json.showapi_res_body.grade) {
					grade.setText("凶吉:" + json.showapi_res_body.grade);
				}
				if (null != json.showapi_res_body.analysis) {
					analysis.setText("总结:" + json.showapi_res_body.analysis);
				}
				if (null != json.showapi_res_body.remark) {
					UiUtils.toast(mContext, json.showapi_res_body.remark);
				}
			}
			if (null != json && !json.showapi_res_error.equals("")) {
				UiUtils.toast(mContext, json.showapi_res_error);
			}
			dialog.dismiss();
		}
	};

	@Override
	protected void onRelease() {
	}

	/** 隐藏键盘 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (UiUtils.isShouldHideInput(v, ev)) {
				UiUtils.hideSoftInput(mContext, etView);
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}
}
