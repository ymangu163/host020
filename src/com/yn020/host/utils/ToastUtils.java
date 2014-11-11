package com.yn020.host.utils;



import com.yn020.host.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ToastUtils {
	private static Toast toast;
	
	/**
	 * 	默认样式
	 **/
	public static void disToast(Context ctx,String message){
		Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();		
	}
	/**
	 * 功能：自定义 Toast 位置
	 * xOffset  x轴偏移坐标
	 * yOffset  y轴偏移坐标
	 **/
	public static void custLocationToast(Context ctx,String message){
		
		toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);		
		toast.setGravity(Gravity.CENTER, -100, 50);
		toast.show();		
	}
	
	/**
	 * 功能：带图片的Toast
	 * imageId --- 要显示的Image的id 
	 **/
	public static void custImageToast(Context ctx,String message,int imageId){
		
		toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);		
		toast.setGravity(Gravity.CENTER, 300, 300);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView image = new ImageView(ctx);
		image.setImageResource(imageId);
		toastView.addView(image);		
		toast.show();		
	}
	
	/**
	 * 功能：自定义布局的Toast
	 * 要一个layout 资源
	 **/
	public static void custLayoutToast(Context ctx){		
		
		LayoutInflater inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=inflater.inflate(R.layout.item_fp_list, null);
		 ImageView image = (ImageView) view.findViewById(R.id.iv_fp_lv);
		  image.setImageResource(R.drawable.huoying);
	      TextView title = (TextView) view.findViewById(R.id.tv_fp_no);
	      title.setText("Attention");
	      TextView text = (TextView) view.findViewById(R.id.tv_fp_id);
	      text.setText("完全自定义Toast");
	      toast = new Toast(ctx);
	      toast.setGravity(Gravity.RIGHT | Gravity.TOP, 12, 40);
	      toast.setDuration(Toast.LENGTH_LONG);
	      toast.setView(view);
	      toast.show();		
	}
	
	
	
}
