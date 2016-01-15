package com.ks.application;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Vibrator;

public class KSApplication extends Application {
	public static Bitmap btmCursor = null;

	public static String controlpcinfo="当前还没有设置定时关机";
	public static boolean issure=true;
	public static final long VIBRATETIME = 13;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		btmCursor = BitmapFactory.decodeResource(getResources(), R.drawable.cursor);
	}

	public static void Vibrate(final Activity activity, long milliseconds) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}
}
