package com.ks.application;

import com.ks.testndk.R;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class KSApplication extends Application {
	public static Bitmap btmCursor = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		btmCursor = BitmapFactory.decodeResource(getResources(), R.drawable.cursor);
	}
}
