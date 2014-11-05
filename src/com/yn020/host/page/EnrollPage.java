package com.yn020.host.page;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yn020.host.R;
import com.yn020.host.utils.FingerManager;
import com.yn020.host.utils.FingerUtils;
import com.yn020.host.utils.ToastUtils;

public class EnrollPage extends BasePage implements OnClickListener {
	@ViewInject(R.id.enroll_fp_btn)
	private Button enroll_fp_btn;
	@ViewInject(R.id.auto_enroll_fp_btn)
	private Button auto_enroll_fp_btn;
	private View view;
	private boolean isAuto=false;
	public EnrollPage(Context ctx) {
		super(ctx);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.enroll_layout, null);
		ViewUtils.inject(this, view);
		initData();
		return view;
	}

	@Override
	public void initData() {
		
		auto_enroll_fp_btn.setOnClickListener(this);
		enroll_fp_btn.setOnClickListener(this);
		

	}
	/**
	 * 功能：分别使用异步任务和Handler来完成注册指纹
	 **/
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.enroll_fp_btn:		
			new EnrollTask().execute();  //执行异步任务
			break;
		case R.id.auto_enroll_fp_btn:
			if(!isAuto){
				isAuto=!isAuto;
				auto_enroll_fp_btn.setText("    Free     ");		
			
									
			}else{				
				isAuto=!isAuto;
				auto_enroll_fp_btn.setText("连续注册");					
			}			
			
			break;

		default:
			break;
		}

	}
	
	 /**使用异步任务的规则： 
     * 1. 声明一个类继承AsyncTask标注三个参数的类型 
     * 2. 第一个参数表示要执行的任务通常是网络的路径  
     * 第二个参数表示的是进度的刻度，第三个参数表示执行的返回结果 
     */ 
	public class EnrollTask extends AsyncTask<String, Void, Integer>{
		//完成注册指纹的功能
		@Override
		protected Integer doInBackground(String... params) {
			int b_enroll=0;
			synchronized (fpSynchrLock) {
				b_enroll=FingerUtils.Enroll_FP(ctx);					
				
			}			
			return b_enroll;
		}

		@Override
		protected void onPreExecute() {			
			super.onPreExecute();
			LogUtils.d("entry onPreExecute.");
		}

		@Override
		protected void onPostExecute(Integer result) {			
			super.onPostExecute(result);
			String str="";
			switch (result) {
			case FingerManager.ERR_SUCCESS:
				str="注册成功，ID="+ FingerUtils.mId;
				MediaPlayer.create(ctx, R.raw.success).start();
				break;
			case FingerManager.ERR_DUPLICATION_ID:
				str="指纹重复！";
				MediaPlayer.create(ctx, R.raw.fail).start();
				break;				
			default:
				str= "注册失败！";
				MediaPlayer.create(ctx, R.raw.fail).start();
				break;
			}
			ToastUtils.disToast(ctx, str);			
			
		}	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
