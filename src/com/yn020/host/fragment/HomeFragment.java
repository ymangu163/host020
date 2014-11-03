package com.yn020.host.fragment;

import com.lidroid.xutils.ViewUtils;
import com.yn020.host.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class HomeFragment extends BaseFragment {
	private View view;
	
	@Override
	public View initView(LayoutInflater inflater) {
		view=inflater.inflate(R.layout.home_frag_layout, null);
		ViewUtils.inject(this,view);
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {

	}

}
