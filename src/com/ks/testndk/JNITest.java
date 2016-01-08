package com.ks.testndk;

import java.util.List;

import com.ks.myexceptions.FileLogger;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class JNITest {
	/*
	public native int add(int a,int b);
	public native String getHello();*/
	/**返回一张新的图片，是globalbtm 的副本*/
	private native Bitmap getBitmapNew(int[] points,Bitmap globalBtm,Bitmap lastFrame);
	 /**在原来的图像上处理，处理结果绘制到globalBtm上*/
	private native void getBitmapOrl(int[] points,Bitmap globalBtm,Bitmap lastFrame);
	
	/**只有使用{@link #getBitmapNew(int[], Bitmap, Bitmap)} 这个函数才会用到本函数，需要手动释放全局引用，防止内存泄漏。
	 * 当前JNITest对象不在使用{@link #getBitmapNew(int[], Bitmap, Bitmap)}这个函数的时候，要记得调用本方法释放资源。
	 * */
	private native void finsh();
	/**确定是都使用 {@link #getBitmapNew(int[], Bitmap, Bitmap)}函数，默认不使用 */
	private boolean isUsingNew=false;
	public void release(){if(isUsingNew){finsh();}}
	public JNITest(){}
	public JNITest(boolean isUsingNew){this.isUsingNew=isUsingNew;}
	/**对 {@link #getBitmapNew(int[], Bitmap, Bitmap)}的封装，用于根据isUsingNew来决定是否使用次函数
	 * 若没有启用isUsingNew会返回null
	 *  */
	public Bitmap getBitmapNewBtm(List<Rect> rects,Bitmap globalBtm,Bitmap lastFrame)
	{
		Bitmap retBtm=null;
		if(isUsingNew)
		{
			int[] points=conventList2intArray(rects);
			retBtm=getBitmapNew( points, globalBtm,lastFrame);
		}
		return retBtm;
	}
	public void getBitmapOrlBtm(List<Rect> rects,Bitmap globalBtm,Bitmap lastFrame)
	{
		int[] points=conventList2intArray(rects);
		getBitmapOrl(points,globalBtm,lastFrame);
	}
	private int[] conventList2intArray(List<Rect> res)
	{
		int len=res.size();
		int[] ret=new int[len*4];
		for(int i=0;i<len;i++)
		{
				ret[0+i*4]=res.get(i).left;
				ret[1+i*4]=res.get(i).top;
				ret[2+i*4]=res.get(i).width();
				ret[3+i*4]=res.get(i).height();
		}
		return ret;
	}
	static{
		/**lib的前缀后缀都不能加libTestNDK.so,只要写TestNDK就ok*/
		try{
		System.loadLibrary("TestNDK");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			FileLogger.getLogger().write(e.getMessage());
		}
	}
}
