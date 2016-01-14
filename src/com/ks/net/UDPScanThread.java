package com.ks.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import com.ks.activitys.IndexActivity.IndexHandler;
import com.ks.net.enums.MessageEnums;

public class UDPScanThread extends Thread {

	private static final int UDPPORT = 9999;
	private ArrayList<String> servers;
	private DatagramSocket dgSocket = null;
	private IndexHandler handler;

	public UDPScanThread(ArrayList<String> servers, IndexHandler handler) {
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
			dgSocket.setSoTimeout(500);
			byte b[] = (MessageEnums.UDPSCANMESSAGE + MessageEnums.NETSEPARATOR + android.os.Build.BRAND + "_"
					+ android.os.Build.MODEL).getBytes();
			DatagramPacket dgPacket = null;
			dgPacket = new DatagramPacket(b, b.length, InetAddress.getByName("255.255.255.255"), UDPPORT);
			dgSocket.send(dgPacket);
		} catch (IOException e3) {
			handler.sendEmptyMessage(NUMCODES.NETSTATE.UDPSCANFAIL.getValue());
			e3.printStackTrace();
			if (dgSocket != null)
				dgSocket.close();
			return;
		}

		long start = System.nanoTime();
		/** scan for 5 seconds */
		while (!isInterrupted() && (System.nanoTime() - start) / 1000000 < 1500) {
			byte data[] = new byte[512];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				dgSocket.receive(packet);
				String rec = new String(packet.getData(), packet.getOffset(), packet.getLength());
				System.out.println(rec);
				String[] msgGet = rec.split(MessageEnums.NETSEPARATOR);
				if (msgGet != null && msgGet.length == 3 && msgGet[0].equalsIgnoreCase(MessageEnums.UDPSCANRETURN)) {
					if (msgGet[1].trim().length() == 0) {
						msgGet[1] = "Unknown";
					}
					String server = msgGet[1] + MessageEnums.UDPSEPARATOR + packet.getAddress().toString().substring(1)
							+ ":" + msgGet[2];
					servers.add(server);
				}
			} catch (SocketTimeoutException ex) {
				ex.printStackTrace();
				continue;
			} catch (IOException e) {
				e.printStackTrace();
				//handler.sendEmptyMessage(NUMCODES.NETSTATE.UDPSCANFAIL.getValue());
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
