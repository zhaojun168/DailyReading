package com.android.dzj.app.dailyreading.play.activity.constellation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.dzj.app.dailyreading.R;
import com.android.dzj.app.dailyreading.utils.Constants;
import com.android.dzj.app.dailyreading.utils.Util;
import com.android.ui.anim.DepthPageTransformer;
import com.android.ui.view.SyncHorizontalScrollView;
import com.android.util.PreferenceUtils;

public class Constellation_Activity extends FragmentActivity implements OnPageChangeListener, OnClickListener {
	
	private RelativeLayout rl_nav;
	private SyncHorizontalScrollView mHsv;
	private RadioGroup rg_nav_content;
	private ImageView back, share;
	private TextView title;
	private ImageView iv_nav_indicator, iv_nav_left, iv_nav_right;
	private ViewPager mViewPager;
	private int indicatorWidth;
	public static String[] tabTitle = { "今日", "明日", "本周", "本月", "本年"}; // 标题    运势
	private LayoutInflater mInflater;
	private TabFragmentPagerAdapter mAdapter;
	private int currentIndicatorLeft = 0;
	private String constellation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_constellation);
		initView();
		init();
	}
	
	private void initView(){
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.tv_title);
		rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);
		mHsv = (SyncHorizontalScrollView) findViewById(R.id.mHsv);
		rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);
		iv_nav_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setPageTransformer(true, new DepthPageTransformer());
		constellation = PreferenceUtils.getPrefString(this, Constants.CONSTELLATION, "");
		Util.setTitle(title, constellation);
		setListener();
	}
	
	private void setListener() {
		back.setOnClickListener(this);
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
	
	
	
	
	private void init(){
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		indicatorWidth = dm.widthPixels / 5;
		LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		iv_nav_indicator.setLayoutParams(cursor_Params);
		mHsv.setSomeParam(rl_nav, iv_nav_left, iv_nav_right, this);
		// 获取布局填充器
		mInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		initNavigationHSV();
		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
	}
	
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
				ft = new Day_Activity();
				break;
			case 1:
				ft = new Tomorrow_Activity();
				break;
			case 2:
				ft = new Week_Activity();
				break;
			case 3:
				ft = new Month_Activity();
				break;
			case 4:
				ft = new Year_Activity();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
	}
}
