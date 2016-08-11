package com.android.dzj.app.dailyreading.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.dzj.app.dailyreading.R;

public class Util {

	/** 获取当前时间 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间
		Date curDate = new Date(System.currentTimeMillis());
		String time = formatter.format(curDate);
		return time;
	}
	
	
	/**设置标题*/
	public static void setTitle(TextView textView, String constellation){
		switch (constellation) {
		case Constants.BAIYANG:
			textView.setText(R.string.baiyang);
			break;
		case Constants.JINNIU:
			textView.setText(R.string.jinniu);
			break;
		case Constants.SHUANGZI:
			textView.setText(R.string.shuangzi);
			break;
		case Constants.JUXIE:
			textView.setText(R.string.juxie);
			break;
		case Constants.SHIZI:
			textView.setText(R.string.shizi);
			break;
		case Constants.CHUNV:
			textView.setText(R.string.chunv);
			break;
		case Constants.TIANCHENG:
			textView.setText(R.string.tiancheng);
			break;
		case Constants.TIANXIE:
			textView.setText(R.string.tianxie);
			break;
		case Constants.SHESHOU:
			textView.setText(R.string.sheshou);
			break;
		case Constants.MOJIE:
			textView.setText(R.string.mojie);
			break;
		case Constants.SHUIPING:
			textView.setText(R.string.shuiping);
			break;
		case Constants.SHUANGYU:
			textView.setText(R.string.shuangyu);
			break;
		}
	}

	/**设置星座星星图片*/
	public static void setImageDrawable(ImageView imageView, int i) {
		switch (i) {
		case 1:
			imageView.setImageResource(R.drawable.ratingbar_star1);
			return;
		case 2:
			imageView.setImageResource(R.drawable.ratingbar_star2);
			return;
		case 3:
			imageView.setImageResource(R.drawable.ratingbar_star3);
			return;
		case 4:
			imageView.setImageResource(R.drawable.ratingbar_star4);
			return;
		case 5:
			imageView.setImageResource(R.drawable.ratingbar_star5);
			return;
		}
	}

	/**设置星座图片*/
	public static void setResource(ImageView imageView, String constellation) {
		switch (constellation) {
		case Constants.BAIYANG:
			imageView.setImageResource(R.drawable.astro_icon_baiyang);
			break;
		case Constants.JINNIU:
			imageView.setImageResource(R.drawable.astro_icon_jinniu);
			break;
		case Constants.SHUANGZI:
			imageView.setImageResource(R.drawable.astro_icon_shuangzi);
			break;
		case Constants.JUXIE:
			imageView.setImageResource(R.drawable.astro_icon_juxie);
			break;
		case Constants.SHIZI:
			imageView.setImageResource(R.drawable.astro_icon_shizi);
			break;
		case Constants.CHUNV:
			imageView.setImageResource(R.drawable.astro_icon_chunv);
			break;
		case Constants.TIANCHENG:
			imageView.setImageResource(R.drawable.astro_icon_tianping);
			break;
		case Constants.TIANXIE:
			imageView.setImageResource(R.drawable.astro_icon_tianxie);
			break;
		case Constants.SHESHOU:
			imageView.setImageResource(R.drawable.astro_icon_sheshou);
			break;
		case Constants.MOJIE:
			imageView.setImageResource(R.drawable.astro_icon_mojie);
			break;
		case Constants.SHUIPING:
			imageView.setImageResource(R.drawable.astro_icon_shuiping);
			break;
		case Constants.SHUANGYU:
			imageView.setImageResource(R.drawable.astro_icon_shuangyu);
			break;
		}
	}

}
