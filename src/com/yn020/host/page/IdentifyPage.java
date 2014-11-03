package com.yn020.host.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class IdentifyPage extends BasePage {

	public IdentifyPage(Context ctx) {
		super(ctx);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		TextView textView=new TextView(ctx);
		textView.setText(" 这里是识别！");
		
		
		return textView;
	}

	@Override
	public void initData() {

	}

}
