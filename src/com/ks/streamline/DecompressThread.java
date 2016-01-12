package com.ks.streamline;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.minilzo.common.LZOjni;

import com.ks.myexceptions.FileLogger;
import com.ks.net.TcpNet;
import com.ks.streamline.Recpacket.PacketType;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DecompressThread extends Thread {
	
	private TcpNet tcpNet;
	private LinkedBlockingQueue<Recpacket> recPacketQueue;
	private LinkedBlockingQueue<BitmapWithCursor> difBtmQueue;
	private boolean isRun=false;
	public DecompressThread(TcpNet tcpNet)
	{
		if(tcpNet==null )
			throw new RuntimeException("DecompressThread constructor param cannot be null");
		this.difBtmQueue=tcpNet.getDifBtmQueue();
		this.recPacketQueue=tcpNet.getRecPacketQueue();
		this.tcpNet=tcpNet;
		this.isRun=true;
	}

	public void stopThread()
	{
		isRun=false;
		this.interrupt();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		LZOjni lzo=new LZOjni();
		while(isRun && !Thread.interrupted() && tcpNet.isConnecting())
		{
			//System.out.println("decompressThread*********************");
			try
			{
			Recpacket rec=recPacketQueue.poll(200,TimeUnit.MILLISECONDS);
			if(rec==null)
				continue;
			
			PacketType packetType=rec.getPacketType();
			BitmapWithCursor difBtm=new BitmapWithCursor();
			difBtm.setPacketType(packetType);
			switch(packetType)
			{
			case BITMAP:
				difBtm.setCursorPoint(rec.getCursorPoint());
				difBtm.setType(rec.getBitmapType());
				difBtm.setDifPointsList(rec.getDifPointsList());
				byte[] dataBytes = rec.getBitByts();
				int beforeSize=dataBytes.length;
				int[] afterSize=new int[1];
				byte[] deCompressBytes=new byte[512*1024];//512kb buffer
				lzo.LZODecompress(dataBytes,beforeSize,deCompressBytes,afterSize);
				//long start=System.nanoTime();
				Bitmap btm=BitmapFactory.decodeByteArray(deCompressBytes, 0, afterSize[0]);
				//long stop=System.nanoTime();
				//System.out.println((stop-start)/1000000);
				difBtm.setDifBitmap(btm);
				difBtmQueue.put(difBtm);
				break;
			case TEXT:
				difBtm.setStringValue(rec.getStringValue());
				difBtmQueue.put(difBtm);
				break;
			default:
				break;
			
			}
			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				FileLogger.getLogger().write(e.getMessage());
				//tcpNet.disConnect();
			}
		}
	}
}
