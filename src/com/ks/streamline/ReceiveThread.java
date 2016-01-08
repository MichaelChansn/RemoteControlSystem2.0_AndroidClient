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
}
