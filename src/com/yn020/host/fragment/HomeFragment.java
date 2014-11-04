package com.yn020.host.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
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

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.home_frag_layout, null);
		ViewUtils.inject(this, view);
		return view;
	}

	List<BasePage> list = new ArrayList<BasePage>();

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

}
