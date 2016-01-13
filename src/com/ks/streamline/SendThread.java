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
				//System.out.println("on "+msgType+":"+msgType.getValue());
				switch (msgType) {
				case MOUSE_RIGHT_CLICK:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case MOUSE_LEFT_CLICK:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case MOUSE_LEFT_DOUBLE_CLICK:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case MOUSE_SET:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.writeInt((int)sendPacket.getIntValue1());
					dataOutputStream.writeInt((int)sendPacket.getIntValue2());
					dataOutputStream.flush();
					break;
				case MOUSE_MOVE:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.writeInt((int)sendPacket.getIntValue1());
					dataOutputStream.writeInt((int)sendPacket.getIntValue2());
					dataOutputStream.flush();
					break;
				case MOUSE_WHEEL:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.writeInt((int)sendPacket.getIntValue1());
					dataOutputStream.flush();
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
