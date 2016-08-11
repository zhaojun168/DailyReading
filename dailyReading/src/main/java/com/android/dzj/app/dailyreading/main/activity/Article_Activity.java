package com.android.dzj.app.dailyreading.main.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.android.dzj.app.dailyreading.R;
import com.android.dzj.app.dailyreading.article.activity.Eight_Activity;
import com.android.dzj.app.dailyreading.article.activity.Eighteen_Activity;
import com.android.dzj.app.dailyreading.article.activity.Eleven_Activity;
import com.android.dzj.app.dailyreading.article.activity.Fifteen_Activity;
import com.android.dzj.app.dailyreading.article.activity.Five_Activity;
import com.android.dzj.app.dailyreading.article.activity.Four_Activity;
import com.android.dzj.app.dailyreading.article.activity.Fourteen_Activity;
import com.android.dzj.app.dailyreading.article.activity.Nine_Activity;
import com.android.dzj.app.dailyreading.article.activity.Nineteen_Activity;
import com.android.dzj.app.dailyreading.article.activity.One_Activity;
import com.android.dzj.app.dailyreading.article.activity.Seven_Activity;
import com.android.dzj.app.dailyreading.article.activity.Seventeen_Activity;
import com.android.dzj.app.dailyreading.article.activity.Six_Activity;
import com.android.dzj.app.dailyreading.article.activity.Sixteen_Activity;
import com.android.dzj.app.dailyreading.article.activity.Ten_Activity;
import com.android.dzj.app.dailyreading.article.activity.Thirteen_Activity;
import com.android.dzj.app.dailyreading.article.activity.Three_Activity;
import com.android.dzj.app.dailyreading.article.activity.Twelve_Activity;
import com.android.dzj.app.dailyreading.article.activity.Two_Activity;
import com.android.dzj.app.dailyreading.article.activity.Zero_Activity;
import com.android.ui.anim.DepthPageTransformer;
import com.android.ui.view.SyncHorizontalScrollView;

public class Article_Activity  extends Fragment implements OnPageChangeListener {
	
	private View mView;
	private Activity_Main mContext;
	private RelativeLayout rl_nav;
	private SyncHorizontalScrollView mHsv;
	private RadioGroup rg_nav_content;
	private ImageView iv_nav_indicator, iv_nav_left, iv_nav_right;
	private ViewPager mViewPager;
	private int indicatorWidth;
	public static String[] tabTitle = { "热点", "推荐", "段子", "养生", "私房", "八卦", "生活", "财经", 
		"汽车", "科技", "潮人", "辣妈", "点赞", "旅行", "职场", "美食", "古今", "学霸","星座", "体育"}; // 标题
	private LayoutInflater mInflater;
	private TabFragmentPagerAdapter mAdapter;
	private int currentIndicatorLeft = 0;
	
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (mView == null) {
			mContext = (Activity_Main) getActivity();
			mView = inflater.inflate(R.layout.activity_article, null);
			initView();
			init();
		}
		ViewGroup group = (ViewGroup) mView.getParent();
		if (group != null) {
			group.removeView(mView);
		}
		return mView;
	}
	
	private void initView(){
		rl_nav = (RelativeLayout) mView.findViewById(R.id.rl_nav);
		mHsv = (SyncHorizontalScrollView) mView.findViewById(R.id.mHsv);
		rg_nav_content = (RadioGroup) mView.findViewById(R.id.rg_nav_content);
		iv_nav_indicator = (ImageView) mView.findViewById(R.id.iv_nav_indicator);
		mViewPager = (ViewPager) mView.findViewById(R.id.pager);
		mViewPager.setPageTransformer(true, new DepthPageTransformer());
		
		setListener();
	}
	
	private void setListener() {
		mViewPager.setOnPageChangeListener(this);
		
		rg_nav_content.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (rg_nav_content.getChildAt(checkedId) != null) {
					TranslateAnimation animation = new TranslateAnimation(currentIndicatorLeft,
					((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft(),0f, 0f);
					animation.setInterpolator(new LinearInterpolator());
					animation.setDuration(100);
					animation.setFillAfter(true);

					// 执行位移动画
					iv_nav_indicator.startAnimation(animation);

					mViewPager.setCurrentItem(checkedId); // ViewPager跟随一起 切换

					// 记录当前 下标的距最左侧的 距离
					currentIndicatorLeft = ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft();

					mHsv.smoothScrollTo((checkedId > 1 ? ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft(): 0)- ((RadioButton) rg_nav_content.getChildAt(2)).getLeft(),0);
				}
			}
		});
	}
	
	
	@SuppressWarnings("static-access")
	private void init(){
		DisplayMetrics dm = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
		indicatorWidth = dm.widthPixels / 7;
		LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		iv_nav_indicator.setLayoutParams(cursor_Params);
		mHsv.setSomeParam(rl_nav, iv_nav_left, iv_nav_right, mContext);
		// 获取布局填充器
		mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
		initNavigationHSV();
		mAdapter = new TabFragmentPagerAdapter(mContext.getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
	}
	
	@SuppressLint("InflateParams")
	private void initNavigationHSV() {
		rg_nav_content.removeAllViews();
		for (int i = 0; i < tabTitle.length; i++) {
			RadioButton rb = (RadioButton) mInflater.inflate(R.layout.nav_radiogroup_item, null);
			rb.setId(i);
			rb.setText(tabTitle[i]);
			rb.setLayoutParams(new LayoutParams(indicatorWidth,LayoutParams.MATCH_PARENT));
			rg_nav_content.addView(rb);
		}

	}
	
	
	
	
	
	public static class TabFragmentPagerAdapter extends FragmentPagerAdapter {

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment ft = null;
			switch (arg0) {
			case 0:
				ft = new Zero_Activity();
				break;
			case 1:
				ft = new One_Activity();
				break;
			case 2:
				ft = new Two_Activity();
				break;
			case 3:
				ft = new Three_Activity();
				break;
			case 4:
				ft = new Four_Activity();
				break;
			case 5:
				ft = new Five_Activity();
				break;
			case 6:
				ft = new Six_Activity();
				break;
			case 7:
				ft = new Seven_Activity();
				break;
			case 8:
				ft = new Eight_Activity();
				break;
			case 9:
				ft = new Nine_Activity();
				break;
			case 10:
				ft = new Ten_Activity();
				break;
			case 11:
				ft = new Eleven_Activity();
				break;
			case 12:
				ft = new Twelve_Activity();
				break;
			case 13:
				ft = new Thirteen_Activity();
				break;
			case 14:
				ft = new Fourteen_Activity();
				break;
			case 15:
				ft = new Fifteen_Activity();
				break;
			case 16:
				ft = new Sixteen_Activity();
				break;
			case 17:
				ft = new Seventeen_Activity();
				break;
			case 18:
				ft = new Eighteen_Activity();
				break;
			case 19:
				ft = new Nineteen_Activity();
				break;
			}
			return ft;
		}

		@Override
		public int getCount() {
			return tabTitle.length;
		}

	}
	

	@Override
	public void onPageSelected(int position) {
		if (rg_nav_content != null && rg_nav_content.getChildCount() > position) {
			((RadioButton) rg_nav_content.getChildAt(position)).performClick();
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}
}
