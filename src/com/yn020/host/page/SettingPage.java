package com.yn020.host.page;

import com.lidroid.xutils.ViewUtils;
import com.yn020.host.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class SettingPage extends BasePage {

	private View view;

	public SettingPage(Context ctx) {
		super(ctx);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.setting_layout, null);
		ViewUtils.inject(this, view);
		
		return view;
	}

	@Override
	public void initData() {

	}

}
