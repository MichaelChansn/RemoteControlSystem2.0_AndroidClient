package com.ks.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.ks.activitys.IndexActivity.MyHandler;
import com.ks.net.enums.MessageEnums;

public class UDPScanThread extends Thread {

	private static final int UDPPORT = 9999;
	private ArrayList<String> servers;
	private DatagramSocket dgSocket = null;
	private MyHandler handler;

	public UDPScanThread(ArrayList<String> servers, MyHandler handler) {
		this.servers = servers;
		this.handler = handler;
	}

	public void stopUDP() {
		this.interrupt();
		if (dgSocket != null) {
			dgSocket.close();
			dgSocket = null;
		}
	}

	@Override
	public void run() {
		super.run();

		try {
			dgSocket = new DatagramSocket();
			dgSocket.setSoTimeout(1000);
			byte b[] = (MessageEnums.UDPSCANMESSAGE + MessageEnums.NETSEPARATOR + android.os.Build.BRAND + "_"
					+ android.os.Build.MODEL).getBytes();
			DatagramPacket dgPacket = null;
			dgPacket = new DatagramPacket(b, b.length, InetAddress.getByName("255.255.255.255"), UDPPORT);
			dgSocket.send(dgPacket);
		} catch (IOException e3) {
			e3.printStackTrace();
			if (dgSocket != null)
				dgSocket.close();
			return;
		}
		
		long start = System.nanoTime();
		/** scan for 5 seconds */
		while (!isInterrupted() && (System.nanoTime() - start) / 1000000 < 3000) {
			byte data[] = new byte[512];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				dgSocket.receive(packet);
				String rec=new String(packet.getData(), packet.getOffset(), packet.getLength());
				String server=rec.split( MessageEnums.NETSEPARATOR)[0]+" "+packet.getAddress().toString()+":"+rec.split( MessageEnums.NETSEPARATOR)[1];
				servers.add(server);
			}
			catch(SocketTimeoutException ex)
			{
				ex.printStackTrace();
				continue;
			}
			catch (IOException e) {
				e.printStackTrace();
				if (dgSocket != null)
					dgSocket.close();
				return;
			}
		}

		if (dgSocket != null) {
			dgSocket.close();
		}
		
		if (!isInterrupted()) {
			handler.sendEmptyMessage(NUMCODES.NETSTATE.UDPSCANOK.getValue());
		}

	}
}
