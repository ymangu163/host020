package com.yn020.host.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
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
	public boolean isAuto=false;
	public boolean singleEnroll=false;
	private List<Map<String,Long>> list;
	private long  fp_No=0;
	private Handler autoEnrollHandler;
	

	
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
		list = new ArrayList<Map<String,Long>>();
		
		autoEnrollHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
			  int id=msg.arg1;
			  String str=(String) msg.obj;
			  if(id!=0){
				  AddToList(id);			  
			  }
			  ToastUtils.disToast(ctx, str);	
			}
		};

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
				auto_enroll_fp_btn.setText("    Free     ");									
			}else{				
				auto_enroll_fp_btn.setText("连续注册");					
			}					
			isAuto=!isAuto;
//			while(isAuto){ //isAuto 为true时循环注册
//				new EnrollTask().execute();  //执行异步任务			
//			}			
			
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					if(!isAuto){ //isAuto 为true循环，false为循环
						break;
					}
					int b_enroll=0;
					String str="";
					Message msg=Message.obtain();
					synchronized (fpSynchrLock) {
						b_enroll=FingerUtils.Enroll_FP(ctx,homeFragment);	
						LogUtils.d("b_enroll-->"+b_enroll);
						switch (b_enroll) {
						case FingerManager.ERR_SUCCESS:
							str="注册成功，ID="+ FingerUtils.mId;
							MediaPlayer.create(ctx, R.raw.success).start();
							msg.arg1=(int) FingerUtils.mId;	 //把id发送到mq						
//							AddToList(FingerUtils.mId);
							break;
						case FingerManager.ERR_DUPLICATION_ID:
							str="指纹重复！";
							MediaPlayer.create(ctx, R.raw.fail).start();
							break;				
						case FingerManager.ERR_FAIL:
							str= "注册失败！";
							MediaPlayer.create(ctx, R.raw.fail).start();
							break;
						default:
							str="退出循环.";
							break;
						}
//						ToastUtils.disToast(ctx, str);	
						msg.obj=str;
						autoEnrollHandler.sendMessage(msg);
						
						
					}	
					
				}
			}
		}).start();	
			
			
			
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
			enroll_fp_btn.setEnabled(false);
			synchronized (fpSynchrLock) {
				singleEnroll=true;
				b_enroll=FingerUtils.Enroll_FP(ctx,homeFragment);					
				
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
				AddToList(FingerUtils.mId);
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
			singleEnroll=false;
			enroll_fp_btn.setEnabled(true);
		}	
		
	}

	public void AddToList(long fp_id) {
		fp_No++;
		Map<String,Long> map=new HashMap<String, Long>();
		map.put("fp_No", fp_No);
		map.put("fp_Id",fp_id);
		list.add(map);
		
		homeFragment.freshListViewData(list);				
	}
	
	
	
	
	
	
	
	
	
	
}
