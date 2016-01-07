package com.ks.myexceptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

public class FileLogger {

	
	private static FileLogger loger=null;
	private static String LOGEPATH="log.txt";
	private static PrintStream logFile;
	private static boolean isInitFileOK=false;
	private FileLogger(){}
	public static FileLogger getLogger()
	{
		if(loger==null)
		{
			synchronized(FileLogger.class)
			{
				if(loger==null)
				{
					loger=new FileLogger();
					try {
						String path=android.os.Environment.getExternalStorageDirectory().getPath();
						logFile=new PrintStream(new FileOutputStream(path+File.separator+LOGEPATH, true));
						isInitFileOK=true;
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return loger;
	}
	
	public synchronized void write(String log)
	{
		if(isInitFileOK)
		{
			logFile.println(new Date().toString()+"-->"+log);
		}
		else
		{
			throw new RuntimeException("the log file is not create success");
		}
		
	}
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		if(logFile!=null)
			logFile.close();
	}

}
