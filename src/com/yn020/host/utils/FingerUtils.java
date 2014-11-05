package com.yn020.host.utils;

import com.yn020.host.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class FingerUtils {
	public static  long mId=0;
	public static MediaPlayer mediaPlayer;
	public FingerUtils() {
		
	}
	// **************************注册指纹***************************************//
	public static int Enroll_FP(Context ctx){
		boolean w_return = false;
		long[] id = new long[1];
		w_return=FingerManager.getSharedInstance().FPM_getAvailableId(id);	
		Log.i("TAG", "getAvaliableId return " + w_return + "  id=" + id[0]);
		if (!w_return) { // 获取可能id失败
			return -1;
		}
		w_return = FingerManager.getSharedInstance().FPM_enrollFP_Start(id[0]);
		if (!w_return) {
			return -1;
		}
		for (int i = 0; i < 3; i++) {
			w_return = false;
			while (!w_return) {// 获取图像
				w_return = FingerManager.getSharedInstance().FPM_getEnrollImage();

			}				
			mediaPlayer=MediaPlayer.create(ctx, R.raw.detect);	
			mediaPlayer.start(); 
			FingerManager.getSharedInstance().FPM_loopDetectFinger();//等待松开手指
			
			w_return = FingerManager.getSharedInstance().FPM_enrollId_Times(id[0], i + 1);
			Log.i("TAG", "enrollTimes " + i + " return " + w_return);
			if (!w_return) {
				return -1;
			}
		}
		int ret_compose = FingerManager.getSharedInstance().FPM_enrollCompose(id[0]);
		Log.i("TAG", "enollCompose " + id[0] + " return " + ret_compose);
		
		if (ret_compose == FingerManager.ERR_SUCCESS) {
			mId= id[0];			
			return ret_compose;
		} 		
		return ret_compose;
	}
	
	// **************************识别指纹***************************************//
	public boolean Identify_FP(){
		long[] id = new long[1];
		boolean w_return = false;
		while (!w_return) {
			w_return = FingerManager.getSharedInstance().FPM_getIdentifyImage();
		}
		FingerManager.getSharedInstance().FPM_loopDetectFinger(); //等待手指松开
		w_return = FingerManager.getSharedInstance().FPM_identifyFP(id);
		if(w_return){
			this.mId=id[0];
		}
		
		return w_return;
	}
	
	
	// **************************删除单个指纹********************************//
	public int  Delete_FP(long id){
		if(!Identify_FP()){
			return -2;	//识别失败
		}
		boolean ret_delete=FingerManager.getSharedInstance().FPM_deleteFP(id);
		if(ret_delete){
			return 0;	//删除成功
		}else{
			return -1;	//删除失败
		}

	}
	
	// **************************清空所有指纹**********************************//
	public boolean ClearALlFP(){
		boolean re_clear = FingerManager.getSharedInstance().FPM_deleteAllFP();
		
		
		
		
		
		
		return re_clear;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}