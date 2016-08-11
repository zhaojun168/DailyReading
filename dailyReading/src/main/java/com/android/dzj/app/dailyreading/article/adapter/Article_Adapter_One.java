package com.android.dzj.app.dailyreading.article.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.dzj.app.dailyreading.R;
import com.android.dzj.app.dailyreading.article.activity.Article_Details_Activity;
import com.android.dzj.app.dailyreading.article.entity.Article_Inof;
import com.android.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.comm.core.constants.StringUtil;

public class Article_Adapter_One extends BaseAdapter {

	private Context mContext;
	private List<Article_Inof> mMainList ;
	private LayoutInflater inflater;
	
	
	public class ListViewItem {
		private ImageView Iv_userLogo, Iv_contentImg;
		private TextView Tv_username, Tv_date, Tv_title;
		private int position;
		private Context context;
		
		public ListViewItem(Context context, View v) {
			this.context = context;
			Iv_userLogo = (ImageView) v.findViewById(R.id.Iv_userLogo);
			Tv_username = (TextView) v.findViewById(R.id.Tv_username);
			Tv_date = (TextView) v.findViewById(R.id.Tv_date);
			Tv_title = (TextView) v.findViewById(R.id.Tv_title);
			Iv_contentImg = (ImageView) v.findViewById(R.id.Iv_contentImg);
		}
		
		private void setData(int position) {
			this.position = position;
			Article_Inof info = mMainList.get(position);
			
			if(!StringUtils.isEmpty(info.userLogo)) {
				ImageLoaderUtil.setImage(Iv_userLogo, info.userLogo,R.drawable.ic_avatar_default);
			}else {
				Iv_userLogo.setImageResource(R.drawable.ic_avatar_default);
			}
			
			if(!StringUtils.isEmpty(info.userName)) {
				Tv_username.setText(info.userName);
			}
			
			if(!StringUtils.isEmpty(info.date)) {
				Tv_date.setText(info.date);
			}
			
			if(!StringUtils.isEmpty(info.title)) {
				Tv_title.setText(info.title);
			}
			
			if(!StringUtils.isEmpty(info.contentImg)) {
//				ImageLoader.getInstance().displayImage(info.contentImg, Iv_contentImg, new SimpleImageLoadingListener());
				ImageLoaderUtil.setImage(Iv_contentImg, info.contentImg,R.drawable.pictures_no, new SimpleImageLoadingListener());
			}else {
				Iv_contentImg.setImageResource(R.drawable.pictures_no);
			}
		}
		
		public void showArticle() {
			Article_Inof info = mMainList.get(position);
			Intent intent = new Intent(context, Article_Details_Activity.class);
			intent.putExtra("ARTICLEURL", info.url);
			intent.putExtra("TITLE", info.title);
			intent.putExtra("CONTENTIMG", info.contentImg);
			context.startActivity(intent);
		}
		
	}
	
	public Article_Adapter_One(Context context, List<Article_Inof> mainList) {
		this.mContext = context;
		this.mMainList = new ArrayList<Article_Inof>();
		setList(mainList);
		inflater = LayoutInflater.from(context);
	}
	
	
	public void setList(List<Article_Inof> mainList)
	{
		if(null == mainList)
			return;
		this.mMainList.clear();
		this.mMainList.addAll(mainList);
		this.notifyDataSetChanged();
	}
	
	public List<Article_Inof> getList()
	{
		return this.mMainList;
	}

	
	
	@Override
	public int getCount() {
		return mMainList == null ? 0 : mMainList.size();
	}

	@Override
	public Object getItem(int position) {
		return mMainList == null ? null : mMainList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mMainList == null ? 0 : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListViewItem item;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.article_item_one, null);
			item = new ListViewItem(mContext, convertView);
			convertView.setTag(item);
		}else {
			item = (ListViewItem) convertView.getTag();
		}
		item.setData(position);
		return convertView;
	}

}
