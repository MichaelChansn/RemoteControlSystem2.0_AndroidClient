package com.ks.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.LinkedBlockingQueue;

import com.ks.application.R;
import com.ks.myexceptions.FileLogger;
import com.ks.myexceptions.NetExceptions;
import com.ks.net.enums.MessageEnums.MessageType;
import com.ks.streamline.BitmapWithCursor;
import com.ks.streamline.ReceiveThread;
import com.ks.streamline.Recpacket;
import com.ks.streamline.SendPacket;
import com.ks.streamline.SendThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class TcpNet {

	private Socket client = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	private boolean isConnecting = false;
	private static TcpNet instance = null;
	private Thread recThread;
	private Thread sendThread;
	private LinkedBlockingQueue<Recpacket> recPacketQueue = new LinkedBlockingQueue<Recpacket>(10);
	private LinkedBlockingQueue<BitmapWithCursor> difBtmQueue = new LinkedBlockingQueue<BitmapWithCursor>(10);
	private LinkedBlockingQueue<SendPacket> sendPacketQueue = new LinkedBlockingQueue<SendPacket>(50);

	private TcpNet() {
	};

	public LinkedBlockingQueue<Recpacket> getRecPacketQueue() {
		return recPacketQueue;
	}

	public LinkedBlockingQueue<BitmapWithCursor> getDifBtmQueue() {
		return difBtmQueue;
	}

	public LinkedBlockingQueue<SendPacket> getSendQueue() {
		return sendPacketQueue;
	}

	public static TcpNet getInstance() {
		if (instance == null) {
			synchronized (TcpNet.class) {
				if (instance == null) {
					instance = new TcpNet();
				}

			}
		}
		return instance;

	}

	public boolean sendMessage(SendPacket sendPacket) {
		boolean retBool = false;
		if (sendPacket != null) {
			retBool = this.sendPacketQueue.offer(sendPacket);
		}
		return retBool;
	}

	public void cancelConnecting() {
		if (client != null) {
			try {
				isConnecting = false;
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean connect2Server(SocketAddress address, int timeOut) throws NetExceptions {
		resetTcpNet();// clear old clients
		client = new Socket();
		try {
			client.setReuseAddress(true);
			client.connect(address, timeOut);
			dataInputStream = new DataInputStream(client.getInputStream());
			dataOutputStream = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			client = null;
			throw new NetExceptions(e.getMessage());
		}
		if (!client.isClosed() && client.isConnected()) {
			isConnecting = true;
			recThread = new ReceiveThread(instance, recPacketQueue);
			recThread.setDaemon(true);
			recThread.start();
			sendThread = new SendThread(instance);
			sendThread.setDaemon(true);
			sendThread.start();

		}
		return isConnecting;
	}

	private void resetTcpNet() {
		this.disConnect();
	}

	public DataInputStream getInputStream() {
		return this.dataInputStream;
	}

	public DataOutputStream getOutputStream() {
		return this.dataOutputStream;
	}

	public boolean isConnecting() {
		return this.isConnecting;
	}

	public void sendHostMessage() {
		String host = android.os.Build.BRAND + "_" + android.os.Build.MODEL;
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.HOST_NANME);
		senPacket.setStrValue(host);
		this.sendMessage(senPacket);
	}

	public void sendExitMeaasge() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.EXIT);
		this.sendMessage(senPacket);
	}

	public void ExitApp(final Context context) {
		new AlertDialog.Builder(context).setTitle("Exit...").setMessage(context.getString(R.string.app_exit_message))
				.setIcon(R.drawable.eva_icon)
				.setPositiveButton(context.getString(R.string.app_confirm), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						TcpNet.getInstance().sendExitMeaasge();
						((Activity) context).finish();
						TcpNet.getInstance().disConnect();
						// 退出后台线程,以及销毁静态变量
						System.exit(0);
					}
				}).setNeutralButton(context.getString(R.string.app_cancel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}

				}).show();

	}

	public void disConnect() {
		System.out.println("connection is closed");
		if (isConnecting) {
			try {
				isConnecting = false;
				if (dataInputStream != null) {
					dataInputStream.close();

				}
				if (dataOutputStream != null) {
					dataOutputStream.close();

				}
				if (client != null) {
					client.close();

				}
				if (recThread != null) {
					((ReceiveThread) recThread).stopThread();
					recThread.join();
				}
				if (sendThread != null) {
					((SendThread) sendThread).stopThread();
					sendThread.join();
				}

			} catch (IOException e) {
				e.printStackTrace();
				FileLogger.getLogger().write(e.getMessage());
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			} finally {
				dataInputStream = null;
				dataOutputStream = null;
				client = null;
				recThread = null;
				sendThread = null;
				recPacketQueue.clear();
				difBtmQueue.clear();
				sendPacketQueue.clear();
			}
		}
	}
}
