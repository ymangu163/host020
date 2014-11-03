package com.yn020.host.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BasePage {
	private View view;
	public Context ctx;

	public BasePage(Context ctx) {
		this.ctx = ctx;
		LayoutInflater inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = initView(inflater);

	}

	public View getRootView() {
		return view;
	}

	public abstract View initView(LayoutInflater inflater);

	public abstract void initData();

}
