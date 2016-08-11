package com.android.dzj.app.dailyreading.article.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dzj.app.dailyreading.R;
import com.android.dzj.app.dailyreading.view.CustomWebView;
import com.android.dzj.app.dailyreading.view.MyDialog;
import com.android.ui.activity.BaseActivity;
import com.android.util.NetworkUtils;
import com.android.util.UiUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;

public class Article_Details_Activity extends BaseActivity {

	private String url, title, contentImg;
	private ImageView mBack, mShare;
	private TextView mTitle, mNetwork;
	private CustomWebView mWebView;
	private Context mContext;
	private MyDialog dialog;
	private UMImage image;

	@Override
	protected void setRootView() {
		setContentView(R.layout.activity_article_details);
		mContext = this;
	}

	@Override
	protected void initWidget() {
		dialog = new MyDialog(mContext);
		mBack = (ImageView) findViewById(R.id.back);
		mShare = (ImageView) findViewById(R.id.share);
		mShare.setVisibility(View.VISIBLE);
		mTitle = (TextView) findViewById(R.id.tv_title);
		mNetwork = (TextView) findViewById(R.id.network);
		mWebView = (CustomWebView) findViewById(R.id.weview);
		mWebView.getSettings().setBlockNetworkImage(false);
		mWebView.getSettings().setJavaScriptEnabled(true);// 支持Javascript
		image = new UMImage(Article_Details_Activity.this, contentImg);
	}

	@Override
	protected void initData() {
		url = getIntent().getStringExtra("ARTICLEURL");
		title = getIntent().getStringExtra("TITLE");
		contentImg = getIntent().getStringExtra("CONTENTIMG");
	}

	@Override
	protected void setListeners() {
		mBack.setOnClickListener(this);
		mShare.setOnClickListener(this);
		mTitle.setText(title);
		if (!NetworkUtils.instance(this).isNetworkConnected()) {
			mNetwork.setVisibility(View.VISIBLE);
		} else {
			mWebView.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
					dialog.dismiss();
				}

				@Override
				public void onPageStarted(WebView view, String url,
						Bitmap favicon) {
					super.onPageStarted(view, url, favicon);
					dialog.show();
				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					return super.shouldOverrideUrlLoading(view, url);
				}
			});
			mWebView.loadUrl(url);
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onPause (){
		super.onPause ();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("Main_Details_Activity");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mWebView.onPause(); // 暂停网页中正在播放的视频
	    }
	}

	@Override
	protected void onRelease() {
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("Main_Details_Activity");
	}
	

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.share:
			 new ShareAction(this).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
			 .withTitle(getString(R.string.share_title))
			 .withText(title)
			 .withMedia(image)
			 .withTargetUrl(url)
			 .open();
			break;
		}
	}
	
	
	/*private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            UiUtils.toast(mContext, R.string.toast_success);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
        	 UiUtils.toast(mContext, R.string.toast_cancel);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        	 UiUtils.toast(mContext, R.string.toast_fail);
        }
    };*/
	
}
