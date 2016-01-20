package com.ks.myexceptions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class FileLogger {

	private static FileLogger loger = null;
	private static String FILENAME = "log.txt";
	private static BufferedWriter logFile;
	private static boolean isLogOn = true;
	private static String FILEPATH = null;

	private FileLogger() {
	}

	public static FileLogger getLogger() {
		if (loger == null) {
			synchronized (FileLogger.class) {
				if (loger == null) {
					loger = new FileLogger();
					try {
						FILEPATH = android.os.Environment.getExternalStorageDirectory().getPath() + File.separator
								+ FILENAME;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return loger;
	}

	public void logSwitch(boolean onOrOff)
	{
		isLogOn=onOrOff;
	}
	public synchronized void write(String log) {

		if (isLogOn && FILEPATH!=null) {
			try {
				File file = new File(FILEPATH);
				if (!file.exists()) {
					file.createNewFile();
				}
				if(file.length()>1024*1024)
				{
					file.delete();
					file.createNewFile();
				}
				logFile=new BufferedWriter(new FileWriter(file,true));
				logFile.append(new Date().toString() + "-->" + log);
				logFile.newLine();
				logFile.flush();
				logFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		if (logFile != null)
			logFile.close();
	}

}
