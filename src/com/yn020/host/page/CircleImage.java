package com.yn020.host.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 *  在代码中设置的话 用img.setImageResource(R.drawable.headpic);
 **/
public class CircleImage extends ImageView {

	public CircleImage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Drawable drawable = getDrawable();
		if (drawable == null) {
			return;
		}
		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		Bitmap bit = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmap = bit.copy(Bitmap.Config.ARGB_8888, true);

		Bitmap roundBitmap = getCroppedBitmap(bitmap, getWidth());
		canvas.drawBitmap(roundBitmap, 0, 0, null);

	}
	/* * 对Bitmap裁剪，使其变成圆形，这步最关键    */    
	private Bitmap getCroppedBitmap(Bitmap bitmap, int radius) {
		Bitmap bmp;
		if(bitmap.getWidth()!=radius || bitmap.getHeight()!=radius){
			bmp=Bitmap.createScaledBitmap(bitmap, radius, radius, false);
		}else{
			bmp=bitmap;
		}
		Bitmap outBitmap=Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
		final Rect rect=new Rect(0,0,bmp.getWidth(),bmp.getHeight());
		Paint paint=new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setColor(Color.parseColor("#BAB399"));
		
		Canvas can=new Canvas(outBitmap);
		can.drawARGB(0, 0, 0, 0);
		can.drawCircle(bmp.getWidth()/2+0.7f, bmp.getHeight()/2+0.7f,  bmp.getWidth() / 2+0.1f, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		can.drawBitmap(bmp, rect, rect,paint);
		
		
		return outBitmap;
	}

}
