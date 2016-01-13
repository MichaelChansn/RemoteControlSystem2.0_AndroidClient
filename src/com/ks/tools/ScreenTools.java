package com.ks.tools;

import android.content.Context;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ScreenTools {
	private  Display display;
	private  WindowManager wManager;
	private  DisplayMetrics displayMetrics;
	private Context context;
	
	public ScreenTools(Context context)
	{
		if(context==null) throw new RuntimeException("context can not be null");
		this.context=context;
		wManager=(WindowManager)this.context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		display=wManager.getDefaultDisplay();
		displayMetrics=new DisplayMetrics();
		display.getMetrics(displayMetrics);
	}
	
	public int getScreenHeight()
	{
		return displayMetrics.heightPixels;
	}
	public int getScreenWidth()
	{
		return displayMetrics.widthPixels;
	}
	public int getScreenDensity()
	{
		return  displayMetrics.densityDpi;
	}

	
}
