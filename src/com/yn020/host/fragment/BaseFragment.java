package com.yn020.host.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yn020.host.MainActivity;

public abstract class BaseFragment extends Fragment {
	public Context ctx;
	public SlidingMenu slidingMenu;
	public View view;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * fragment 提供的得到Activity的方法
		 **/
		ctx = getActivity();
		slidingMenu = ((MainActivity) getActivity()).getSlidingMenu();
	}

	/**
	 * onCreateView 等价于 setContentView()
	 **/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(inflater);
		return view;
	}

	public View getContentView() {
		return view;
	}

	/**
	 * 初始化view
	 */
	public abstract View initView(LayoutInflater inflater);

	public abstract void initData(Bundle savedInstanceState);

}
