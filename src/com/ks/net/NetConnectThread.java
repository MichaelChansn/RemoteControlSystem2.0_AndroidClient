package com.ks.net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.ks.myexceptions.FileLogger;
import com.ks.myexceptions.NetExceptions;

import android.os.Handler;

public class NetConnectThread extends Thread {

	private String IP;
	private int port;
	private Handler handler;
	private SocketAddress skAddress;
	private TcpNet tcpNet;

	public NetConnectThread(TcpNet tcpNet, String IP, int port, Handler handler) {
		if(tcpNet==null || IP==null ||IP.trim().length()==0 || port<1024 || handler==null) throw new RuntimeException("int NetConnectThread params can not be empty");
		this.handler = handler;
		this.tcpNet = tcpNet;
		this.IP = IP;
		this.port = port;
	}

	public void cancelConnecting() {
		this.interrupt();
		tcpNet.cancelConnecting();
	}

	@Override
	public void run() {
		super.run();
		try {
			skAddress = new InetSocketAddress(IP, port);
			handler.sendEmptyMessage(NUMCODES.NETSTATE.CONNECTING.getValue());
			tcpNet.connect2Server(skAddress, 5000);
			handler.sendEmptyMessage(NUMCODES.NETSTATE.CONNECTOK.getValue());
		} catch (NetExceptions e) {
			e.printStackTrace();
			FileLogger.getLogger().write(e.getMessage());
			String error = e.getMessage();
			if (error.contains("ms")) {
				handler.sendEmptyMessage(NUMCODES.NETSTATE.CONNECTTIMEOUT.getValue());
			} else {
				handler.sendEmptyMessage(NUMCODES.NETSTATE.CONNECTFAILE.getValue());
			}
		} catch (Exception e) {// IP和端口输入不符合规范
			e.printStackTrace();
			FileLogger.getLogger().write(e.getMessage());
			handler.sendEmptyMessage(NUMCODES.NETSTATE.CONNECTFAILE.getValue());
		}

	}
}
