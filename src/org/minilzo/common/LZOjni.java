/* LZOjni.java -- JNI implementation of LZO library

   This file is part of the LZOjni library.

   Copyright (C) 2013 BOUVIER-VOLAILLE Julien
   All Rights Reserved.

   The LZOjni library is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License as
   published by the Free Software Foundation; either version 2 of
   the License, or (at your option) any later version.

   The LZOjni library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with the LZO library; see the file COPYING.
   If not, write to the Free Software Foundation, Inc.,
   51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

   BOUVIER-VOLAILLE Julien
   <bouviervj@gmail.com>

 */
package org.minilzo.common;

import com.ks.myexceptions.FileLogger;

public class LZOjni {
	/*
	private static final String LIB_BIN = "/dll/";
	
	private final static String LIBRARYNAME = "lzojni";
	
	private final static String DEPENDENT = "libgcc_s_dw2-1";
	
	static {
		System.out.println("LZO Loading DLL/Library");
		
		System.out.println("Operating System:"+getOperating());
		
		if (!"Linux".equals(getOperating()) && "32".equals(getArchitecture())) {
			
			loadLib(DEPENDENT,false);
			
		}
		
		loadLib(LIBRARYNAME,true);
	}
	

	private static void loadLib(String iName, boolean archi) {
		
		String aName = iName;
		if (archi) {
			aName += getArchitecture();
		}
		
		try {
			
			System.loadLibrary(aName);
			System.out.println("DLL is loaded from memory");
		} catch (UnsatisfiedLinkError e) {
			try {
				loadFromJar(aName);
			} catch (Exception e1) {
				System.out.println("Unable to load this library, exiting ...");
				e1.printStackTrace();
				System.exit(1);
			}
		}
		
		
	}
	
	// To switch from a DLL to an other
	private static String getOperating(){
		return System.getProperty("os.name");
	}
	
	// To switch from a DLL to an other
	private static String getArchitecture(){
		return System.getProperty("sun.arch.data.model");
	}
	
	// To switch from a DLL to an other
	private static String getTempDir(){
		if ("Linux".equals(getOperating())) {
			return android.os.Environment.getExternalStorageDirectory().getPath()+File.separator+"tmp";
			//return System.getProperty("user.home")+"/tmp";
		}
		return System.getProperty("java.io.tmpdir");
	}
	
	/**
	 * When packaged into JAR extracts DLLs, places these into
	 * @throws Exception 
	 *
	private static void loadFromJar(String iName) throws Exception {
		// we need to put both DLLs to temp dir
		String path = "LZOJNI_" + new Date().getTime();
		loadLib(path, iName );

	}*/

	/*
	 * Puts library to temp dir and loads to memory
	 * @throws Exception 
	 *
	private static void loadLib(String path, String name) throws Exception {
		
		if ("Linux".equals(getOperating())) {
			name = name + ".a";
		} else {
			name = name + ".dll";
		}
		try {
			// have to use a stream
			InputStream in = LZOjni.class.getResourceAsStream(LIB_BIN + name);
			// always write to different location
			
			// create the new folder
			File theDir = new File( getTempDir() + "/" + path + LIB_BIN);
			if (!theDir.exists()) {
				boolean result = theDir.mkdirs();  
				if (!result) {
					System.out.println("Unable to create the directory ...");
				}
			}
			
			File fileOut = new File(getTempDir() + "/" + path + LIB_BIN + name);
			System.out.println("Writing dll to: " + fileOut.getAbsolutePath());
			OutputStream out = new FileOutputStream(fileOut);
			copy(in, out);
			in.close();
			out.close();
			System.load(fileOut.toString());
		} catch (Exception e) {
			throw new Exception("Failed to load required DLL", e);
		}
	}
	
	private static void copy(InputStream in, OutputStream out) {
		byte[] buffer = new byte[1024];
		int len;
		try {
			len = in.read(buffer);
			while (len != -1) {
			    out.write(buffer, 0, len);
			    len = in.read(buffer);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	static{
		try{
		System.out.println("loadLibrary..................");
		System.loadLibrary("LZOjni");
		System.out.println("loadLibrary OK...............");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			FileLogger.getLogger().write(e.getMessage());
		}
		
	}
	
	// Our Native method
	public native int LZOCompress(final byte[] in, int iLen, byte[] out, int[] ioOutlen);
	
	// Our Native method
	public native int LZODecompress(final byte[] in, int iLen, byte[] out, int[] ioOutlen);
	
	// Our Native method
	public native int LZODecompress_safe(final byte[] in, int iLen, byte[] out, int[] ioOutlen);
	

}
