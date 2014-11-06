package com.yn020.host.page;

import com.lidroid.xutils.util.LogUtils;
import com.yn020.host.MainActivity;
import com.yn020.host.fragment.HomeFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BasePage {
	private View view;
	public Context ctx;
	public Object  fpSynchrLock =new Object(); //指纹操作的同步锁
	public HomeFragment homeFragment;
	public boolean isOperating;   //定义一个标志，解决各个按键冲突的问题
	
	public BasePage(Context ctx) {
		this.ctx = ctx;
		LayoutInflater inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = initView(inflater);
		if(homeFragment==null){
			if(ctx instanceof MainActivity){
				homeFragment = ((MainActivity)ctx).getHomeFragment();
			}			
		}	

	}
	
	
	public boolean isOperating() {
		LogUtils.d(" isOperating()--->"+isOperating);
		return isOperating;
	}
	public void setOperating(boolean isOperating) {
		this.isOperating = isOperating;
		LogUtils.d("setOperating isOperating--->"+isOperating);
	}


	public View getRootView() {
		return view;
	}

	public abstract View initView(LayoutInflater inflater);

	public abstract void initData();

}
