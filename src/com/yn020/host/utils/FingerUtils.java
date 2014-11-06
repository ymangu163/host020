package com.yn020.host.utils;

import com.lidroid.xutils.util.LogUtils;
import com.yn020.host.R;
import com.yn020.host.fragment.HomeFragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FingerUtils {
	public static  long mId=0;
	public static MediaPlayer mediaPlayer;
	private static byte[] imageBytes;
	private static long[] imageSizes;
	public FingerUtils() {
		
	}
	// **************************注册指纹***************************************//
	public static int Enroll_FP(Context ctx,HomeFragment homeFragment){
	
		boolean w_return = false;
		long[] id = new long[1];
		w_return=FingerManager.getSharedInstance().FPM_getAvailableId(id);	
		Log.i("TAG", "getAvaliableId return " + w_return + "  id=" + id[0]);
		if (!w_return) { // 获取可能id失败
			return 1;
		}
		w_return = FingerManager.getSharedInstance().FPM_enrollFP_Start(id[0]);
		if (!w_return) {
			return 1;
		}
		for (int i = 0; i < 3; i++) {			
			w_return = false;
			while (!w_return) {// 获取图像
				//在这里检测一下 是否已退出了循环注册且是不是单次注册
				if(!homeFragment.enrollPage.isAuto &&!homeFragment.enrollPage.singleEnroll){
					if(i==0){ //新开始的注册
						return -1;
					}else{
						return 1;						
					}
				}
				w_return = FingerManager.getSharedInstance().FPM_getEnrollImage();

			}							
			mediaPlayer=MediaPlayer.create(ctx, R.raw.detect);	
			mediaPlayer.start(); 		
			
			Get_FingerImage(homeFragment.imageHandler);//画指纹图像
			
			FingerManager.getSharedInstance().FPM_loopDetectFinger();//等待松开手指
			
			w_return = FingerManager.getSharedInstance().FPM_enrollId_Times(id[0], i + 1);
			Log.i("TAG", "enrollTimes " + i + " return " + w_return);
			if (!w_return) {
				return 1;
			}
		}
		if(!homeFragment.enrollPage.isAuto &&!homeFragment.enrollPage.singleEnroll){
			return 1;
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
	public static int Identify_FP(Context ctx,HomeFragment homeFragment){
		long[] id = new long[1];
		boolean w_return = false;
		while (!w_return) {
			//在这里检测一下 是否已退出了循环识别且是不是单次识别
			if(!homeFragment.identifyPage.isAuto &&!homeFragment.identifyPage.singleIdentify){
				return -1;
			}
			w_return = FingerManager.getSharedInstance().FPM_getIdentifyImage();
		}
		MediaPlayer.create(ctx, R.raw.detect).start();
		Get_FingerImage(homeFragment.imageHandler);//画指纹图像
		
		FingerManager.getSharedInstance().FPM_loopDetectFinger(); //等待手指松开
		w_return = FingerManager.getSharedInstance().FPM_identifyFP(id);
		if(w_return){
			mId=id[0];
			return 0;
		}else{
			return 1;			
		}
		
	}
	
	
	// **************************删除单个指纹********************************//
	public static boolean  Delete_FP(long id){
	
		boolean ret_delete=FingerManager.getSharedInstance().FPM_deleteFP(id);
		return ret_delete;
	}
	
	// **************************清空所有指纹**********************************//
	public static boolean ClearALlFP(){
		boolean re_clear = FingerManager.getSharedInstance().FPM_deleteAllFP();
		
		return re_clear;
	}
	
	// **************************获取指纹图像**********************************//
	public static boolean Get_FingerImage(Handler handler){
		if(imageBytes==null){
			imageBytes = new byte[242 * 266];			
		}
		if(imageSizes==null){
			imageSizes = new long[2];			
		}
		FingerRawPrintData fingerRawPrintData = null;
		boolean ret_FingerImage=false;	//采集指纹图像标志
		Drawable fingerDrawable=null;
		ret_FingerImage =FingerManager.getSharedInstance().FPM_getFingerImage(imageBytes, imageSizes);
		if (ret_FingerImage){
			fingerRawPrintData = new FingerRawPrintData();
			fingerRawPrintData.setRawData(imageBytes);
			fingerRawPrintData.setImage_W((int) imageSizes[0]);
			fingerRawPrintData.setImage_H((int) imageSizes[0]);
			fingerDrawable = fingerRawPrintData.toDrawable(); // 处理的部分放在这里，显示才不会卡
			
			Message msg =Message.obtain();
			msg.obj=fingerDrawable;
			handler.sendMessage(msg);
						
		}				
		
		return ret_FingerImage;
	}
	
	
	
	
	
	
	
	
	

}
