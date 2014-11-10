package com.yn020.host.utils;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class FingerBitmapUtil {
	// 将原始指纹图片数据转为drawable对象
	public static Drawable fingerRawDataToDrawable(byte[] rawData, int image_W,
			int image_H) {
		Bitmap bitmap = fingerRawDataToBitmap(rawData, image_W, image_H);
		@SuppressWarnings("deprecation")
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		return bitmapDrawable;

	}

	// 将原始指纹图片数据转为Bitmap对象
	public static Bitmap fingerRawDataToBitmap(byte[] rawData, int image_W,
			int image_H) {
		class ColorPanal { // 定义图像的ARGB格式
			public int alpha;
			public int rgbRed;
			public int rgbGreen;
			public int rgbBlue;
		}
		List<ColorPanal> colorPanallist = new ArrayList<ColorPanal>(); // 定义一个列表来保存这些ARGB格式的值
		for (int i = 0; i < 256; i++) {
			ColorPanal colorPanal = new ColorPanal();
			colorPanal.alpha = 0xff;
			colorPanal.rgbRed = i & 0xff;
			colorPanal.rgbGreen = i & 0xff;
			colorPanal.rgbBlue = i & 0xff;
			colorPanallist.add(colorPanal);

		}
		Bitmap bitmap = Bitmap.createBitmap((int) image_W, (int) image_H,
				Bitmap.Config.ARGB_8888);// ARGB每个8位
		for (int x = 0; x < image_W; ++x) {
			for (int y = 0; y < image_H; ++y) {
				int colorIndex = (rawData[x * image_W + y]) & 0xff;
				ColorPanal colorPanal2 = colorPanallist.get(colorIndex);// 得到该像素点的ARGB值
				int color = Color.argb(colorPanal2.alpha, colorPanal2.rgbRed,
						colorPanal2.rgbGreen, colorPanal2.rgbBlue);
				bitmap.setPixel(x, y, color);
			}

		}

		return bitmap;
	}

}