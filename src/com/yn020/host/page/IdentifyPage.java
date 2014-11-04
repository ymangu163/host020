package com.yn020.host.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yn020.host.R;

public class IdentifyPage extends BasePage {

	private View view;
	@ViewInject(R.id.verify_fp_btn)
	private Button verify_fp_btn;
	@ViewInject(R.id.identify_fp_btn)
	private Button identify_fp_btn;
	

	public IdentifyPage(Context ctx) {
		super(ctx);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.identify_layout, null);
		ViewUtils.inject(this, view);
		
		return view;
	}

	@Override
	public void initData() {

	}

}
