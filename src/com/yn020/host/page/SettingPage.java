package com.yn020.host.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class SettingPage extends BasePage {

	public SettingPage(Context ctx) {
		super(ctx);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		TextView textView=new TextView(ctx);
		textView.setText(" 这里是设置！");
		
		
		return textView;
	}

	@Override
	public void initData() {

	}

}
