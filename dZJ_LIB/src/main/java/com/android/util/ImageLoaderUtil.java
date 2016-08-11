package com.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImageLoaderUtil {
	
	/**
	 * 初始化ImageLoader
	* @param context
	 */
	public static void initImageLoader(Context context) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.ARGB_8888).build();
		int memoryCacheSize = (int) (Math.min(
				Runtime.getRuntime().maxMemory() / 8, 8 * 1024 * 1024));
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.defaultDisplayImageOptions(defaultDisplayImageOptions)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.FIFO).build();
		imageLoader.init(config);
	}
	
	private static DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
	    .cacheOnDisc()
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.EXACTLY);
	
	private static DisplayImageOptions.Builder builderForImageRes = new DisplayImageOptions.Builder()
		.cacheOnDisc()
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.EXACTLY);
	
	public static void displayImage(ImageView iv, String uri)
	{
//		builderForImageRes.showStubImage(R.drawable.pic_food_default);
//		builderForImageRes.showImageForEmptyUri(R.drawable.pic_food_default);
		ImageLoader.getInstance().displayImage(uri, iv, builderForImageRes.build());
	}
	
	public static void setImage(ImageView iv, String uri) {
		ImageLoader.getInstance().displayImage(uri, iv, builder.build());
	}
	
	/**
	 * 图片下载未完成时，显示指定默认图
	 * @param iv
	 * @param uri
	 * @param res
	 */
	public static void setImage(ImageView iv, String uri, int resId) {
//		builderForImageRes.showStubImage(resId);
//		builderForImageRes.showImageForEmptyUri(resId);
		setImage(iv, uri, resId, null);
	}
	
	public static void setImage(ImageView iv, String uri, int resId, SimpleImageLoadingListener listener) {
		builderForImageRes.showStubImage(resId);
		builderForImageRes.showImageForEmptyUri(resId);
		ImageLoader.getInstance().displayImage(uri, iv, builderForImageRes.build(), listener);
	}
	/**
	 * 显示本地图片
	* @param iv
	* @param uri
	 */
	public static void setFileImage(ImageView iv, String uri){
		uri = "file://" + uri;
		setImage(iv, uri);
	}
	
	/**
	 * 显示本地图片
	* @param iv
	* @param uri
	* @param res
	 */
	public static void setFileImage(ImageView iv, String uri, int resId){
		uri = "file://" + uri;
		setImage(iv, uri, resId);
	}
	
	/**
	 * 显示assets目录下的图片
	 * @param iv
	 * @param uri
	 */
	public static void setAssetsImage(ImageView iv, String uri) {
		uri = "assets://" + uri;
		setImage(iv, uri);
	}
	
	/**
	 * 显示assets目录下的图片
	 * @param iv
	 * @param uri
	 * @param resId
	 */
	public static void setAssetsImage(ImageView iv, String uri, int resId) {
		uri = "assets://" + uri;
		setImage(iv, uri, resId);
	}
	
	/**
	 * 显示drawable目录下的图片
	 * @param iv
	 * @param uri
	 */
	public static void setDrawableImage(ImageView iv, int imgResId) {
		String uri = "drawable://" + imgResId;
		setImage(iv, uri);
	}
	
	/**
	 * 显示drawable目录下的图片
	 * @param iv
	 * @param uri
	 * @param resId
	 */
	public static void setDrawableImage(ImageView iv, int imgResId, int resId) {
		String uri = "drawable://" + imgResId;
		setImage(iv, uri, resId);
	}
	
	/**
	 * 显示ContentProvider下的图片
	 * @param iv
	 * @param uri
	 */
	public static void setContentProviderImage(ImageView iv, String uri) {
		uri = "content://" + uri;
		setImage(iv, uri);
	}
	
	/**
	 * 显示ContentProvider下的图片
	 * @param iv
	 * @param uri
	 */
	public static void setContentProviderImage(ImageView iv, String uri, int resId) {
		uri = "content://" + uri;
		setImage(iv, uri, resId);
	}

	/**
	 * 算出需要缩放的比例
	 */
	public static float calculateScale(int widthOrg,int heightOrg,int widthDes,int heightDes){
		float widthScale = (float)widthDes/widthOrg;
		float heightScale = (float)heightDes/heightOrg;
		return widthScale > heightScale ? widthScale:heightScale;
	}
}
