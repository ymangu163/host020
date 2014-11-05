package com.yn020.host.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yn020.host.R;
import com.yn020.host.page.BasePage;
import com.yn020.host.page.EnrollPage;
import com.yn020.host.page.IdentifyPage;
import com.yn020.host.page.SettingPage;

public class HomeFragment extends BaseFragment {
	private View view;
	@ViewInject(R.id.viewpager)
	public ViewPager viewPager;
	@ViewInject(R.id.fp_listview)
	private ListView fp_listview;
	@ViewInject(R.id.fp_image)
//	private com.yn020.host.page.CircleImage fp_image;
	private ImageView fp_image;
	
	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.home_frag_layout, null);
		ViewUtils.inject(this, view);
		return view;
	}

	List<BasePage> list = new ArrayList<BasePage>();
	private HomeBaseAdapter homeBaseAdapter;


	@Override
	public void initData(Bundle savedInstanceState) {		
		list.add(new EnrollPage(ctx));
		list.add(new IdentifyPage(ctx));
		list.add(new SettingPage(ctx));

		HomePageAdapter homePageAdapter = new HomePageAdapter(ctx, list);
		viewPager.setAdapter(homePageAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (0 == position) {
					slidingMenu
							.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				} else {
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}
	
	public void freshListViewData(List<Map<String,Long>> list){
		
		if(homeBaseAdapter==null){
			homeBaseAdapter = new HomeBaseAdapter(ctx, list);
			fp_listview.setAdapter(homeBaseAdapter);		
		}else{
			homeBaseAdapter.notifyDataSetChanged();
		}	
		
		
	}
	
//	  @SuppressLint("NewApi")
//	public void DrawFpImage(Drawable drawable){
//		  int i=0;
//		  LogUtils.d("DrawFpImage-->"+ i++);
//		  if(drawable!=null){
//			  fp_image.setImageDrawable(drawable);	  
//		  }
//		  
//	  }
	
	public Handler imageHandler=new Handler(){		
		public void handleMessage(android.os.Message msg) {
			Drawable fingerDrawable = (Drawable) msg.obj;
			 fp_image.setImageDrawable(fingerDrawable);	  
			
		};
	};
	
	
	class HomePageAdapter extends PagerAdapter {
		private Context ctx;
		private List<BasePage> list;

		public HomePageAdapter(Context ctx, List<BasePage> list) {
			this.ctx = ctx;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container)
					.removeView(list.get(position).getRootView());
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container)
					.addView(list.get(position).getRootView(), 0);

			return list.get(position).getRootView();

		}

	}
	
	
	
	class HomeBaseAdapter extends BaseAdapter{
		private List<Map<String,Long>> list;	
		private Context ctx;
		
		public HomeBaseAdapter(Context ctx,List<Map<String,Long>> list) {
			super();
			this.list=list;
			this.ctx = ctx;
		}

		@Override
		public int getCount() {			
			return list.size();
		}

		@Override
		public Object getItem(int position) {			
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView=View.inflate(ctx, R.layout.item_fp_list, null);
			}
			TextView tvFpNo=(TextView) convertView.findViewById(R.id.tv_fp_no);
			TextView tvFpId=(TextView) convertView.findViewById(R.id.tv_fp_id);
			tvFpNo.setText(list.get(position).get("fp_No").toString());
			tvFpId.setText(list.get(position).get("fp_Id").toString());
			LogUtils.d("getView-->"+list.get(position).get("fp_No")+"-->"+list.get(position).get("fp_Id"));
			
			
			return convertView;			
		}			
		
	}
	
	
	
}
