package com.ks.streamline;

import java.io.DataOutputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.ks.net.TcpNet;
import com.ks.net.enums.MessageEnums.MessageType;

public class SendThread extends Thread {

	private TcpNet tcpNet;
	private LinkedBlockingQueue<SendPacket> sendPacketQueue;
	private boolean isRun = false;
	private DataOutputStream dataOutputStream;

	public SendThread(TcpNet tcpNet) {
		if (tcpNet == null)
			throw new RuntimeException("SendThread constructor params can not be null");
		this.tcpNet = tcpNet;
		sendPacketQueue = tcpNet.getSendQueue();
		dataOutputStream=tcpNet.getOutputStream();
		isRun = true;
	}

	public void stopThread() {
		isRun = false;
		this.interrupt();
		tcpNet.disConnect();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (isRun && !Thread.interrupted() && tcpNet.isConnecting()) {

				SendPacket sendPacket = sendPacketQueue.poll(200, TimeUnit.MILLISECONDS);
				if (sendPacket == null)
					continue;
				MessageType msgType = sendPacket.getMsgType();
				switch (msgType) {
				case MOUSE_RIGHT_CLICK:
					dataOutputStream.writeByte(msgType.getValue());
					break;
				default:
					break;
				}

			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
