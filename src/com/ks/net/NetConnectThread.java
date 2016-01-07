package com.ks.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.ks.myexceptions.FileLogger;
import com.ks.myexceptions.NetExceptions;

import android.os.Handler;

public class NetConnectThread extends Thread {
	
	private Handler handler;
	private SocketAddress skAddress;
	private TcpNet tcpNet;
	
	public NetConnectThread(TcpNet tcpNet,String IP,int port,Handler handler)
	{
		this.handler=handler;
		this.tcpNet=tcpNet;
		skAddress=new InetSocketAddress(IP, port);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			handler.sendEmptyMessage(NUMCODES.NETSTATE.CONNECTING.getValue());
			tcpNet.connect2Server(skAddress, 0);
			handler.sendEmptyMessage(NUMCODES.NETSTATE.CONNECTOK.getValue());
		} catch (NetExceptions e) {
			// TODO Auto-generated catch block
			handler.sendEmptyMessage(NUMCODES.NETSTATE.CONNECTFAILE.getValue());
			e.printStackTrace();
			FileLogger.getLogger().write(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
	}
}
