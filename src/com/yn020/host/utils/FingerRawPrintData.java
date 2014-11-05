package com.yn020.host.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class FingerRawPrintData {
	private int image_W = -1;
	private int image_H = -1;
	byte[] rawData = null; // 用来存放从byteArray中解码生成位图的byte[]

	public FingerRawPrintData() {

	}

	public static String encodeFingerRawPrintDataToBase64String(
			FingerRawPrintData fingerRawPrintData) {
		return "";
	}

	public String toBase64String() {
		return encodeFingerRawPrintDataToBase64String(this);
	}

	public static FingerRawPrintData decodeFingerRawPrintDataFromBase64String(
			String base64String) {
		return null;

	}

	public Bitmap toBitmap() {
		return FingerBitmapUtil.fingerRawDataToBitmap(getRawData(),
				getImage_W(), getImage_H());
	}

	public Drawable toDrawable() {
		return FingerBitmapUtil.fingerRawDataToDrawable(getRawData(),
				getImage_W(), getImage_H());
	}

	public int getImage_W() {
		return image_W;
	}

	public void setImage_W(int image_W) {
		this.image_W = image_W;
	}

	public int getImage_H() {
		return image_H;
	}

	public void setImage_H(int image_H) {
		this.image_H = image_H;
	}

	public byte[] getRawData() {
		return rawData;
	}

	public void setRawData(byte[] rawData) {
		this.rawData = rawData;
	}
}
