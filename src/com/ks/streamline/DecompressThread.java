package com.ks.streamline;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.minilzo.common.LZOjni;

import com.ks.myexceptions.FileLogger;
import com.ks.net.TcpNet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DecompressThread extends Thread {
	
	private TcpNet tcpNet;
	private LinkedBlockingQueue<Recpacket> recPacketQueue;
	private LinkedBlockingQueue<BitmapWithCursor> difBtmQueue;
	public DecompressThread(TcpNet tcpNet,LinkedBlockingQueue<Recpacket> recPacketQueue,LinkedBlockingQueue<BitmapWithCursor> difBtmQueue)
	{
		if(tcpNet==null || recPacketQueue==null || difBtmQueue==null)
			throw new RuntimeException("DecompressThread constructor param cannot be null");
		this.difBtmQueue=difBtmQueue;
		this.recPacketQueue=recPacketQueue;
		this.tcpNet=tcpNet;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		LZOjni lzo=new LZOjni();
		while(!isInterrupted() && tcpNet.isConnecting())
		{
			try
			{
			Recpacket rec=recPacketQueue.poll(200,TimeUnit.MILLISECONDS);
			if(rec==null)
				continue;
			BitmapWithCursor difBtm=new BitmapWithCursor();
			difBtm.setCursorPoint(rec.getCursorPoint());
			difBtm.setType(rec.getBitmapType());
			difBtm.setDifPointsList(rec.getDifPointsList());
			byte[] dataBytes = rec.getBitByts();
			int beforeSize=dataBytes.length;
			int[] afterSize=new int[1];
			byte[] deCompressBytes=new byte[512*1024];//512kb buffer
			lzo.LZODecompress(dataBytes,beforeSize,deCompressBytes,afterSize);
			Bitmap btm=BitmapFactory.decodeByteArray(deCompressBytes, 0, afterSize[0]);
			difBtm.setDifBitmap(btm);
				difBtmQueue.put(difBtm);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				FileLogger.getLogger().write(e.getMessage());
				tcpNet.disConnect();
			}
		}
	}
}
