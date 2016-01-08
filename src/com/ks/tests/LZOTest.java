package com.ks.tests;

import org.minilzo.common.LZOjni;

public class LZOTest {
	public static void testLZO()
	{

		String aString = new String("ABCABCABCABCABCAB C'est super de compresser des donnees");
		
		byte[] aDataIn = aString.getBytes();
		byte[] aDataOut = new byte[1024];
		int[] outsize = new int[1];
		
		int result = (new LZOjni()).LZOCompress(aDataIn, aDataIn.length, aDataOut, outsize);
		
		System.out.println("Input Length "+aDataIn.length);
		System.out.println("Finished "+result);
		System.out.println("OutputSize "+outsize[0]);
		for (int i=0; i<1024; i++) {
			System.out.printf("%d",aDataOut[i]);
		}
		System.out.println();
		
		byte[] aDataOut2 = new byte[1024];
		
		int out = outsize[0];
		outsize[0] = 1024; // Set available output size
		result = (new LZOjni()).LZODecompress(aDataOut, out, aDataOut2, outsize);
		
		System.out.println("Finished Decompress "+result);
		System.out.println("OutputSize "+outsize[0]);
		for (int i=0; i<1024; i++) {
			System.out.printf("%d",aDataOut2[i]);
		}
		System.out.println();
		String aData = new String(aDataOut2);
		System.out.println(aData);
	}
}
