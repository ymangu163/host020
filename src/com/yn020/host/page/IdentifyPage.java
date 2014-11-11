package com.yn020.host.page;

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
import com.yn020.host.fragment.HomeFragment;
import com.yn020.host.utils.FingerManager;
import com.yn020.host.utils.FingerUtils;
import com.yn020.host.utils.ToastUtils;

public class IdentifyPage extends BasePage implements OnClickListener {

	private View view;
	@ViewInject(R.id.verify_fp_btn)
	private Button verify_fp_btn;
	@ViewInject(R.id.identify_fp_btn)
	private Button identify_fp_btn;
	public boolean isAuto=false;
	public boolean singleIdentify=false;
	private Handler idenfityHandler;

	public IdentifyPage(Context ctx) {
		super(ctx);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.identify_layout, null);
		ViewUtils.inject(this, view);
		initData();
		return view;
	}

	@Override
	public void initData() {
		identify_fp_btn.setOnClickListener(this);
		verify_fp_btn.setOnClickListener(this);
		idenfityHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				String str=(String) msg.obj;
				ToastUtils.custLocationToast(ctx, str);				
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.verify_fp_btn:
			identify_fp_btn.setEnabled(false);
			if(!isOperating()){
				isOperating=true;
				setOperating(isOperating);
				
				if(!isAuto){				
					verify_fp_btn.setText("  取 消     ");	
					identify_fp_btn.setEnabled(false);
				}else{				
					verify_fp_btn.setText("1:1  识别");	
					identify_fp_btn.setEnabled(true);
				}					
				isAuto=!isAuto;
							
				
				new VerifyTask().execute(); 
			}else {
				if(isAuto){ //正在操作单个注册
					verify_fp_btn.setText("1:1  识别");	
					identify_fp_btn.setEnabled(true);
					isAuto=!isAuto;
				}	
				LogUtils.d("quit single enroll");
				return;	
				
			}
			
			break;
		case R.id.identify_fp_btn:
			verify_fp_btn.setEnabled(false);
			if(isOperating()){
				if(isAuto){ //正在循环识别的话,则退出循环
					identify_fp_btn.setText("1:N 识别");	
					verify_fp_btn.setEnabled(true);
					isAuto=!isAuto;
				}	
				return;
			}
			isOperating=true;
			setOperating(isOperating);
			if(!isAuto){				
				identify_fp_btn.setText("  取 消    ");									
			}else{				
				identify_fp_btn.setText("1:N 识别");	 
				verify_fp_btn.setEnabled(true);
			}	
			isAuto=!isAuto;			
			
			
			new Thread(new Runnable() {				
				@Override
				public void run() {
					int ret_identify=0;
					String str="";
				while(true){
					if(!isAuto){ //isAuto 为true循环，false为循环
						break;
					}
					Message msg=Message.obtain();					
					synchronized(fpSynchrLock){
						ret_identify=FingerUtils.Identify_FP(ctx,homeFragment);						
					}
					switch (ret_identify) {
					case FingerManager.ERR_SUCCESS:
						str="识别成功，ID="+ FingerUtils.mId;
						MediaPlayer.create(ctx, R.raw.success).start();	
						break;
					case FingerManager.ERR_FAIL:
						str="识别失败！";
						MediaPlayer.create(ctx, R.raw.fail).start();	
						break;
					default:
						str="退出识别.";
						MediaPlayer.create(ctx, R.raw.fail).start();	
						break;
					}
					msg.obj=str;
					idenfityHandler.sendMessage(msg);
					isOperating=false;
					setOperating(isOperating);
					
					}
					
				}
			}).start();			
		}
	}

	class VerifyTask extends AsyncTask<Void, Void, Integer>{

		@Override
		protected Integer doInBackground(Void... params) {
			
			singleIdentify=true;
			int re_identify=0;
			synchronized (fpSynchrLock) {
				re_identify=FingerUtils.Identify_FP(ctx, homeFragment);
			}
			return re_identify;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			String str="";
			switch (result) {
			case FingerManager.ERR_SUCCESS:
				if(FingerUtils.mId==homeFragment.fp_Id){
					str="识别成功，ID="+ FingerUtils.mId;
					MediaPlayer.create(ctx, R.raw.success).start();
					break;					
				}
			case FingerManager.ERR_FAIL:
				str="识别失败！";
				MediaPlayer.create(ctx, R.raw.fail).start();	
				break;
			default:
				str="退出识别！";
				MediaPlayer.create(ctx, R.raw.fail).start();	
				break;
			}
			ToastUtils.custLocationToast(ctx, str);
			singleIdentify=false;	
			isOperating=false;
			setOperating(isOperating);
			identify_fp_btn.setEnabled(true);
			verify_fp_btn.setText("1:1  识别");
			isAuto=false;
		}
		
		
	}
	
	
}
