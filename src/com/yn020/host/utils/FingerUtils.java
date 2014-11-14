package com.yn020.host.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.util.LogUtils;
import com.yn020.host.R;
import com.yn020.host.fragment.HomeFragment;

public class FingerUtils {
	public static  long mId=0;
	public static MediaPlayer mediaPlayer;
	private static byte[] imageBytes;
	private static long[] imageSizes;
	private static File file;
	private static FileOutputStream fos;
	private static byte[] templateBytes;
	private static String sdState=Environment.getExternalStorageState();
	private final static String path=Environment.getExternalStorageDirectory().toString()+"/FingerTemplate";
	private static List<String> fpcList = new ArrayList<String>();
	private static FileInputStream  fis;
	private static FPCFilter fpcFilter;

	
	
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
//				if(!homeFragment.enrollPage.isAuto &&!homeFragment.enrollPage.singleEnroll){
				if(!homeFragment.enrollPage.isAuto){
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
		if(!homeFragment.enrollPage.isAuto ){
			return 1;
		}
		
		int ret_compose = FingerManager.getSharedInstance().FPM_enrollCompose(id[0]);
		Log.i("TAG", "enollCompose " + id[0] + " return " + ret_compose);
		
		if (ret_compose == FingerManager.ERR_SUCCESS) {
			mId= id[0];	
			//在这里把模板写入到SDcard中
			Read_Template(path,id[0]);
			
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
			if(!homeFragment.identifyPage.isAuto ){
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
		file=new File(path+"/"+id+".fpc");
		deleteFile(file);
		
		return ret_delete;
	}	
	//*删除SD卡上的指纹模板文件
	private static void deleteFile(File temFile) {
		if (temFile.exists()) {
			if (temFile.isFile()) {
				temFile.delete();
			}
			// 如果它是一个目录
			else if (temFile.isDirectory()) {
				// 声明目录下所有的文件 files[];
				File files[] = temFile.listFiles();
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
				// temFile.delete();  //最后把文件夹也删掉
			}

		}
		
		
	}
	
	// **************************清空所有指纹**********************************//
	public static boolean ClearALlFP(){
		boolean re_clear = FingerManager.getSharedInstance().FPM_deleteAllFP();
		file=new File(path);
		deleteFile(file);
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
			fingerDrawable = fingerRawPrintData.toDrawable(); // 把byte[] 转换成Drawable			
			
			Message msg =Message.obtain();
			msg.obj=fingerDrawable;
			handler.sendMessage(msg);
						
		}				
		
		return ret_FingerImage;
	}
	
	// ************************** 从内存读取指纹模板，写到SDCard ************************ *//
	public static boolean Read_Template(String path,long id) {
		if(templateBytes==null){
			templateBytes = new byte[FingerManager.MaxFingerTemplateSize];			
		}
		boolean re_read = FingerManager.getSharedInstance().FPM_readTemplate(id, templateBytes);
		if(!re_read){
			LogUtils.d("读取模板失败！");
			return false;
		}
		LogUtils.d("从内存读取模板成功！");
		//把指纹模板保存到SDCard中
		if(sdState.equals(Environment.MEDIA_MOUNTED)){
			file = new File(path);
			if(!file.exists()){
				file.mkdirs();
			}
			file=new File(file,"/"+id+".fpc");
			LogUtils.d(file.getName());
			try {
				if(!file.exists()){
					file.createNewFile();
				}				
				fos = new FileOutputStream(file);
				fos.write(templateBytes, 0, templateBytes.length);		
				fos.flush();
				LogUtils.d("fos.flush() 执行完了！");
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(fos!=null){
					try {
						fos.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}			
			}
			LogUtils.d("读取模板成功！ id="+id);
			return true;
			
		}
		return false;
	}
	
	// ****************************循环导入指纹模板***************************//
	public static boolean circul_write_tmplate() {
		getFPCFileList(); // 得到文件列表
		String strName,str;
		if(fpcList.size()>0){
			for(int i=0;i<fpcList.size();i++){
				strName=fpcList.get(i);
				str=getFileNameNoEx(strName);
				int id=Integer.valueOf(str);
				Write_Template(strName,id);   // 导入相应指纹模板到相应ID中			
			}
//			homeFrag.homeBaseAdapter.setCurPosition(homeFrag.fpDataList.size()-1);  //每添加一个就跳到新的位置
//			homeFrag.homeBaseAdapter.notifyDataSetChanged();			
		}
		return false;
	}
	
	// 导入指纹模板到内存中
	private static boolean Write_Template(String strName, int id) {
		file = new File(path + "/" + strName);
		if (templateBytes == null) {
			templateBytes = new byte[FingerManager.MaxFingerTemplateSize];
		}
		try {
			fis = new FileInputStream(file);
			try {
				fis.read(templateBytes, 0, templateBytes.length);
				FingerManager.getSharedInstance().FPM_writeTemplate(id,templateBytes);
				
//				addToListViewDate(id);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return false;

	}
	private static long fp_No=0;
	private static HomeFragment homeFrag;
	
	/**
	 * 功能： 更新 ListView 中数据列表
	 **/
	private static void addToListViewDate(int id) {
		fp_No++;
		Map<String,Long> map=new HashMap<String, Long>();
		map.put("fp_No", fp_No);
		map.put("fp_Id",(long)id);	
//		HomeFragment.fpDataList.add(map);	
	}
	/**
	 * 功能：获取不带扩展名的文件名，得到指纹的id
	 **/
	private static String getFileNameNoEx(String filename) {
		if(filename!=null && filename.length()>0){
			int dot=filename.lastIndexOf('.');
			if((dot>-1)&&(dot<(filename.length()))){
				return filename.substring(0,dot);
			}			
		}		
		return filename;
	}	
	
	/**
	 * 功能：得到FPC 文件列表
	 **/
	private static void getFPCFileList() {
		// 取得指定位置的文件列表
		file = new File(path );
		if (!file.exists()) {
			return;
		}
		if(fpcFilter==null){
			fpcFilter = new FPCFilter();		
		}
		if (file.listFiles(fpcFilter).length > 0) {
			for (File file2 : file.listFiles(fpcFilter)) {
				fpcList.add(file2.getName());

			}

		}

	}
	
	
	
	
	
	
	
	 

}

/* 过滤文件类型 */
class FPCFilter implements FilenameFilter {
	
	@Override
	public boolean accept(File dir, String filename) {
		// 这里设置要过滤的文件后缀名
		return filename.endsWith(".fpc");
	}
	
}