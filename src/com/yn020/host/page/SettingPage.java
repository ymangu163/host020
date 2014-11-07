package com.yn020.host.page;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yn020.host.R;
import com.yn020.host.utils.ToastUtils;

public class SettingPage extends BasePage implements OnClickListener {

	private View view;
	@ViewInject(R.id.secure_level_image)
	private ImageView secure_level_image;
	@ViewInject(R.id.auto_learn_image)
	private ImageView auto_learn_image;
	@ViewInject(R.id.dup_check_image)
	private ImageView dup_check_image;
	private AlertDialog.Builder builder;
	private int selectedIndex;
	private AlertDialog dialog;
	
	@ViewInject(R.id.secure_level_edittext)
	private EditText secure_level_edittext;
	
	@ViewInject(R.id.dup_check_edittext)
	private EditText dup_check_edittext;

	@ViewInject(R.id.auto_learn_edittext)
	private EditText auto_learn_edittext;
	
	@ViewInject(R.id.auto_set_button)
	private Button auto_set_button;
	
	@ViewInject(R.id.auto_get_button)
	private Button auto_get_button;
	
	public SettingPage(Context ctx) {
		super(ctx);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.setting_layout, null);
		ViewUtils.inject(this, view);
		secure_level_image.setOnClickListener(this);
		auto_learn_image.setOnClickListener(this);
		dup_check_image.setOnClickListener(this);
		auto_set_button.setOnClickListener(this);
		auto_get_button.setOnClickListener(this);
		return view;
	}

	@Override
	public void initData() {

	}

	@Override
	public void onClick(View v) {
		builder = new AlertDialog.Builder(ctx);
		switch (v.getId()) {
		case R.id.secure_level_image:
			showSecureDialog();
			break;
		case R.id.dup_check_image:
			showDupCheckDialog();
			break;
		case R.id.auto_learn_image:
			showAutoLearnDialog();
			break;
		case R.id.auto_get_button:
			procssGetBtn();
			break;
		case R.id.auto_set_button:
			processSetBtn();
			break;

		default:
			break;
		}
	}

	private void processSetBtn() {
		ToastUtils.disToast(ctx, "设置成功！");
	}

	private void procssGetBtn() {
		String str=auto_learn_edittext.getText().toString();
		if(TextUtils.isEmpty(str)){
			auto_learn_edittext.setText("ON");
		}	
		
	}

	private final String[] onOffStr=new String[] {"ON","OFF"};
	private void showAutoLearnDialog() {
		builder.setSingleChoiceItems(onOffStr, 0,new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedIndex=which;
				dialog.dismiss();
				auto_learn_edittext.setText(onOffStr[selectedIndex]);
			}			
		});
		dialog = builder.create();
		dialog.show();
		WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
			params.width=200;
			dialog.getWindow().setAttributes(params);	
	}

	private void showDupCheckDialog() {
		
		builder.setSingleChoiceItems(onOffStr, 0,new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedIndex=which;
				dialog.dismiss();
				dup_check_edittext.setText(onOffStr[selectedIndex]);
			}			
		});
		dialog = builder.create();
		dialog.show();
		WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
			params.width=200;
			dialog.getWindow().setAttributes(params);	
		
		
	}

	private void showSecureDialog() {
		final String[] leverStr=new String[] {"1","2","3","4","5"};
		selectedIndex = 0;
		builder.setSingleChoiceItems(leverStr, 0,new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedIndex=which;
				dialog.dismiss();
				secure_level_edittext.setText(leverStr[selectedIndex]);
			}
			
		});
		dialog = builder.create();
		dialog.show();
		WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
			params.width=200;
			dialog.getWindow().setAttributes(params);	
	}

}
