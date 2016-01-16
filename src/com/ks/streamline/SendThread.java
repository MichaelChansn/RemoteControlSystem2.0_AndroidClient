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
				case EXIT:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case START_PIC:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case STOP_PIC:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
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
				case MOUSE_LEFT_DOWN:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case MOUSE_LEFT_UP:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case MOUSE_RIGHT_DOWN:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case MOUSE_RIGHT_UP:
					dataOutputStream.writeByte((byte)msgType.getValue());
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
				case KEY_DOWN:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.writeByte((byte)sendPacket.getSplKeys().getValue());
					dataOutputStream.flush();
					break;
				case KEY_UP:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.writeByte((byte)sendPacket.getSplKeys().getValue());
					dataOutputStream.flush();
					break;
				case TEXT:
					dataOutputStream.writeByte((byte)msgType.getValue());
					byte[] strByte=sendPacket.getStrValue().getBytes();
					int len=strByte.length;
					dataOutputStream.writeInt(len);
					dataOutputStream.write(strByte,0,len);
					dataOutputStream.flush();
					break;
				case HOST_NANME:
					dataOutputStream.writeByte((byte)msgType.getValue());
					byte[] strByte2=sendPacket.getStrValue().getBytes();
					int len2=strByte2.length;
					dataOutputStream.writeInt(len2);
					dataOutputStream.write(strByte2,0,len2);
					dataOutputStream.flush();
					break;
				case FUN_LOCK:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case FUN_LOGOUT:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case FUN_MANAGER:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case FUN_RESTART:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case FUN_SHOW_DESKTOP:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case FUN_SHUTDOWN:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case FUN_SHUTDOWN_CANCEL:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case FUN_SLEEP:
					dataOutputStream.writeByte((byte)msgType.getValue());
					dataOutputStream.flush();
					break;
				case FUN_SHUTDOWN_TIME:
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
