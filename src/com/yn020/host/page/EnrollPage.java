package com.yn020.host.page;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yn020.host.R;

public class EnrollPage extends BasePage {
	@ViewInject(R.id.enroll_fp_btn)
	private Button enroll_fp_btn;
	private View view;

	public EnrollPage(Context ctx) {
		super(ctx);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.enroll_layout, null);
		ViewUtils.inject(this,view);
		
		return view;
	}

	@Override
	public void initData() {

	}

}
