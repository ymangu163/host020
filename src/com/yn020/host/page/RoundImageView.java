package com.yn020.host.page;

import com.yn020.host.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 圆形ImageView，可设置最多两个宽度不同且颜色不同的圆形边框。
 */

public class RoundImageView extends ImageView {
	private int mBorderThickness=0;
	private Context context;
	private int defaultColor=0xFFFFFFFF;
	// 如果只有其中一个有值，则只画一个圆形边框
	private int mBorderOutsideColor=0;
	private int mBorderInsideColor=0;
	// 控件默认长、宽
	private int defaultWidth=0;
	private int defaultHeight=0;
	
	public RoundImageView(Context context) {
		super(context);
		this.context = context;
	}
	
	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		setCustomAttributes(attrs);
	
	}
	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
		setCustomAttributes(attrs);
	}

	
	
	

	/**
	 * 功能：从自定义属性集中获取属性的值
	 **/
	private void setCustomAttributes(AttributeSet attrs) {
		//取得自定义的属性集
		TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.roundedimageview);
		//得到自定义属性项的值，集合名+变量名
		mBorderThickness=array.getDimensionPixelSize(R.styleable.roundedimageview_border_thickness, 0);
		mBorderOutsideColor=array.getColor(R.styleable.roundedimageview_border_outside_color, defaultColor);
		mBorderInsideColor=array.getColor(R.styleable.roundedimageview_border_inside_color, defaultColor);	
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		//从ImageView组件上获取到图片源
		Drawable drawable=getDrawable();
		if(drawable==null){
			return;
		}
		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		this.measure(0, 0);
		if(drawable.getClass()==NinePatchDrawable.class){
			return;			
		}
		
		//传递进来的要是一个 Bitmap 对象
		Bitmap b=((BitmapDrawable)drawable).getBitmap();
		Bitmap bitmap=b.copy(Bitmap.Config.ARGB_8888, true);
		if(defaultWidth==0){
			defaultWidth=getWidth();
		}
		if(defaultHeight==0){
			defaultHeight=getHeight();
		}
		
		int radius=0;
		//假如要画的颜色与默认的颜色不同
		if(mBorderInsideColor!=defaultColor &&
				mBorderOutsideColor!=defaultColor){// 定义画两个边框，分别为外圆边框和内圆边框
			radius=(defaultWidth<defaultHeight?defaultWidth : defaultHeight)/2-2*mBorderThickness;
			// 画内圆
			drawCircleBorder(canvas,radius+mBorderThickness/2,mBorderInsideColor);
			// 画外圆
			drawCircleBorder(canvas, radius + mBorderThickness+ mBorderThickness / 2, mBorderOutsideColor);
		}else if(mBorderInsideColor!=defaultColor && mBorderOutsideColor==defaultColor){// 定义画一个边框
			radius=(defaultWidth<defaultHeight?defaultWidth:defaultHeight)/2-mBorderThickness;
			drawCircleBorder(canvas, radius+ mBorderThickness / 2, mBorderInsideColor);
			
		}else if(mBorderInsideColor==defaultColor && mBorderOutsideColor!=defaultColor){
			radius = (defaultWidth < defaultHeight ? defaultWidth
					: defaultHeight) / 2 - mBorderThickness;
			drawCircleBorder(canvas, radius + mBorderThickness / 2,
					mBorderOutsideColor);
		}else{// 没有边框
			radius = (defaultWidth < defaultHeight ? defaultWidth
					: defaultHeight) / 2;
		}
		
		Bitmap roundBitmap = getCroppedRoundBitmap(bitmap, radius);
		canvas.drawBitmap(roundBitmap, defaultWidth / 2 - radius, defaultHeight
				/ 2 - radius, null);
		
		
		
	}
	
	/** 获取裁剪后的圆形图片* 
	 * @param radius    半径
	 */
	private Bitmap getCroppedRoundBitmap(Bitmap bitmap, int radius) {
		Bitmap scaledScrBmp;
		int diameter=radius*2;
		
		// 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
		int bmpWidth=bitmap.getWidth();
		int bmpHeight=bitmap.getHeight();
		int squareWidth=0,squareHeight=0;
		int x=0,y=0;
		Bitmap squareBitmap;
		if(bmpHeight>bmpWidth){// 高大于宽
			squareWidth = squareHeight = bmpWidth;
			x=0;
			y=(bmpHeight-bmpWidth)/2;
			// 截取正方形图片
			squareBitmap=Bitmap.createBitmap(bitmap,x,y,squareWidth,squareHeight);						
		}else if(bmpHeight < bmpWidth) {// 宽大于高
			squareWidth = squareHeight = bmpHeight;
			x = (bmpWidth - bmpHeight) / 2;
			y = 0;
			squareBitmap = Bitmap.createBitmap(bitmap, x, y, squareWidth,
					squareHeight);
		}else{
			squareBitmap=bitmap;
		}
		
		
		if(squareBitmap.getWidth()!=diameter || squareBitmap.getHeight()!=diameter){
			scaledScrBmp=Bitmap.createScaledBitmap(squareBitmap, diameter, diameter, true);			
		}else{
			scaledScrBmp=squareBitmap;
		}
		
		
		Bitmap output=Bitmap.createBitmap(scaledScrBmp.getWidth(), scaledScrBmp.getHeight(), Config.ARGB_8888);
		Canvas canvas=new Canvas(output);
		
		Paint paint=new Paint();
		Rect rect=new Rect(0,0,scaledScrBmp.getWidth(),scaledScrBmp.getHeight());
		
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(scaledScrBmp.getWidth()/2, scaledScrBmp.getHeight()/2, scaledScrBmp.getWidth()/2, paint);
		/**
		 *  http://www.cnblogs.com/jacktu/archive/2012/01/02/2310326.html
		 **/
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  //设置转换模式这句要放在drawBitmap之前，不然图片不显示
		canvas.drawBitmap(scaledScrBmp, rect,rect, paint);
		//让GC回收用完的 Bitmap
		bitmap=null;
		squareBitmap=null;
		scaledScrBmp=null;
		
		return output;
	}
	/**
	 * 功能：边缘画圆
	 **/
	private void drawCircleBorder(Canvas canvas, int radius, int color) {
		Paint paint=new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setColor(color);
		/* 设置paint的　style　为STROKE：空心 */
		paint.setStyle(Paint.Style.STROKE);
		/* 设置paint的外框宽度 */
		paint.setStrokeWidth(mBorderThickness);
		canvas.drawCircle(defaultWidth/2, defaultHeight/2, radius, paint);
		
	}
	
}
