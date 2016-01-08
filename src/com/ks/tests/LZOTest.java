package com.ks.tests;

import java.io.ByteArrayOutputStream;

import org.minilzo.common.LZOjni;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class LZOTest {
	public static void testLZO()
	{
		Bitmap btm=Bitmap.createBitmap(1920, 1080, Config.ARGB_8888);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		btm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] jpegBytes=baos.toByteArray();
		
		byte[] outPutBytes=new byte[200*1024];
		int[] lzoSize=new int[1];
		/**返回0表示成功，其他表示失败*/
		int result = (new LZOjni()).LZOCompress(jpegBytes, jpegBytes.length, outPutBytes, lzoSize);
		System.out.println("Finsh:"+result+" size before compress:"+jpegBytes.length+" size after  compress:"+lzoSize[0]);
		byte[] buffer=new byte[200*1024];
		int beforeSize=lzoSize[0];
		int[] lzoSize2=new int[1];
		result = (new LZOjni()).LZODecompress(outPutBytes, beforeSize, buffer, lzoSize2);
		System.out.println("Finsh:"+result+" size before decompress:"+beforeSize+" size after decompress:"+lzoSize2[0]);
	}
}
