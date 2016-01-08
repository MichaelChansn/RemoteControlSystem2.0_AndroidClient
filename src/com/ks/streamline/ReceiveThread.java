package com.ks.streamline;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import com.ks.net.TcpNet;

public class ReceiveThread extends Thread {

	private LinkedBlockingQueue<Recpacket> recPacketQueue;
	private TcpNet tcpNet;
    public ReceiveThread(TcpNet tcpNet,LinkedBlockingQueue<Recpacket> recPacketQueue) {
    	this.tcpNet=tcpNet;
		this.recPacketQueue=recPacketQueue;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(!Thread.currentThread().isInterrupted()&&tcpNet.isConnecting())
		{
			Recpacket recpacket=new Recpacket();
			try {
				tcpNet.getInputStream().readInt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**数据转换过程中一定要注意符号位扩展问题，自动转型问题,因为&符号两边默认都是int型*/
	private int conventSmall2Big(int small)
	{
		byte[] bits=new byte[4];
		bits[0]=(byte) (small&0x000000ff);
		bits[1]=(byte)((small>>>8)&0x000000ff);
		bits[2]=(byte)((small>>>16)&0x000000ff);
		bits[3]=(byte)((small>>>24)&0x000000ff);
		int ret=(bits[0]&0x000000ff)<<24 | (bits[1]&0x000000ff)<<16 | (bits[2]&0x000000ff)<<8 | (bits[3]&0x000000ff);
		return ret;
	}
	private short conventSmall2Big(short small)
	{
		byte[] bits=new byte[2];
		bits[0]=(byte) (small&0x000000ff);
		bits[1]=(byte)((small>>8)&0x000000ff);
		short ret=(short)((bits[0]&0x000000ff)<<8 | (bits[1]&0x000000ff));
		return ret;
	}
}
