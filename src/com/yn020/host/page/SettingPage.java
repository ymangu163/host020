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
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yn020.host.R;
import com.yn020.host.utils.FingerManager;
import com.yn020.host.utils.SharePrefUtil;
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

	private final int DUPLICATION_ON=1;
	private final int DUPLICATION_OFF=0;
	private void processSetBtn() {
		
		SharePrefUtil.saveString(ctx, "Security_Level", secure_level_edittext.getText().toString());
		SharePrefUtil.saveString(ctx, "Auto_Learn", auto_learn_edittext.getText().toString());
		SharePrefUtil.saveString(ctx, "Duplication_Check", dup_check_edittext.getText().toString());
		boolean re_Set=false;
		
		/**
		 *  把参数设置到指纹库中
		 **/
		if("OFF".equals(dup_check_edittext.getText().toString().trim())){
			re_Set=FingerManager.getSharedInstance().FPM_setDuplicateCheck(DUPLICATION_OFF);
		}else{
			re_Set=FingerManager.getSharedInstance().FPM_setDuplicateCheck(DUPLICATION_ON);
			
		}
		if(re_Set){
			ToastUtils.custLocationToast(ctx, "设置成功！");			
		}else{
			ToastUtils.custLocationToast(ctx, "设置失败！");			
		}
		
	}

	private void procssGetBtn() {
		
		int re_check=FingerManager.getSharedInstance().FPM_getDuplicateCheck();
		if(re_check==0){
			dup_check_edittext.setText("OFF");
			
		}else if(re_check==1){
			dup_check_edittext.setText("ON");			
		}
		
		secure_level_edittext.setText(SharePrefUtil.getString(ctx, "Security_Level", "3"));		
		auto_learn_edittext.setText(SharePrefUtil.getString(ctx, "Auto_Learn", "ON"));		
		LogUtils.d(SharePrefUtil.getString(ctx, "Security_Level", null)+"--->"+SharePrefUtil.getString(ctx, "Duplication_Check", null)+
				"---->"+SharePrefUtil.getString(ctx, "Auto_Learn", null));
		
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
