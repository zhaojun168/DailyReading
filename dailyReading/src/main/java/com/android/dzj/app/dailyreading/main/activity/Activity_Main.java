package com.android.dzj.app.dailyreading.main.activity;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;

import com.android.dzj.app.dailyreading.R;
import com.android.ui.activity.BaseActivity;
import com.android.util.UiUtils;
import com.umeng.analytics.MobclickAgent;

public class Activity_Main extends BaseActivity {

	private Context mContext;
	private Article_Activity mArticle;// 文章
//	private Main_Two_Activity fragment_Two;
	private Community_Activity mCommunity;// 社区
	private Play_Activity mPlay;// 玩吧
	private RadioButton mRa_article, mRa_two, mRa_three, mRa_four;
	private FragmentManager manager;
	private long exitTime = 0;

	@Override
	protected void setRootView() {
		MobclickAgent.openActivityDurationTrack(false);
		setContentView(R.layout.activity_main);
		mContext = this;
	}

	@Override
	protected void initWidget() {
		manager = this.getSupportFragmentManager();
		selectionFragment(1);
		mRa_article = (RadioButton) findViewById(R.id.article);
//		mRa_two = (RadioButton) findViewById(R.id.video);
		mRa_three = (RadioButton) findViewById(R.id.community);
		mRa_four = (RadioButton) findViewById(R.id.paly);
	}

	@Override
	protected void initData() {
	}

	@Override
	protected void setListeners() {
		mRa_article.setOnClickListener(this);
//		mRa_two.setOnClickListener(this);
		mRa_three.setOnClickListener(this);
		mRa_four.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int fragment = 0;
		switch (v.getId()) {
		case R.id.article:
			fragment = 1;
			break;
//		case R.id.video:
//			fragment = 2;
//			break;
		case R.id.community:
			fragment = 2;
			break;
		case R.id.paly:
			fragment = 3;
			break;
		}
		if (fragment > 0) {
			selectionFragment(fragment);
		}
	}

	private void selectionFragment(int id) {
		FragmentTransaction transaction = manager.beginTransaction();// 开启Fragment事务
		hideFragments(transaction);// 每次显示Fragment前，先隐藏其它Fragment
		switch (id) {
		case 1:
			if (mArticle == null) {
				mArticle = new Article_Activity();// 如果为空，就实例化
				transaction.add(R.id.flayout, mArticle);// 并且加入集合
			} else {
				transaction.show(mArticle);// 否则显示fragment
			}
			break;
//		case 2:
//			if (fragment_Two == null) {
//				fragment_Two = new Main_Two_Activity();
//				transaction.add(R.id.flayout, fragment_Two);
//			} else {
//				transaction.show(fragment_Two);
//			}
//			break;
		case 2:
			if (mCommunity == null) {
				mCommunity = new Community_Activity();
				transaction.add(R.id.flayout, mCommunity);
			} else {
				transaction.show(mCommunity);
			}
			break;
		case 3:
			if (mPlay == null) {
				mPlay = new Play_Activity();
				transaction.add(R.id.flayout, mPlay);
			} else {
				transaction.show(mPlay);
			}

			break;
		}
		transaction.commit();// 提交事务，显示Fragment
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (mArticle != null)
			transaction.hide(mArticle);
//		if (fragment_Two != null)
//			transaction.hide(fragment_Two);
		if (mCommunity != null)
			transaction.hide(mCommunity);
		if (mPlay != null)
			transaction.hide(mPlay);
	}

	/** 再按一次退出 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				UiUtils.toast(mContext, R.string.toast_exit_info);
				exitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onRelease() {
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
