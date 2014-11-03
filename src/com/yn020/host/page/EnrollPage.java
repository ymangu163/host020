package com.yn020.host.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class EnrollPage extends BasePage {

	public EnrollPage(Context ctx) {
		super(ctx);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		TextView textView=new TextView(ctx);
		textView.setText(" 这里是注册！");
		
		
		return textView;
	}

	@Override
	public void initData() {

	}

}
