package com.ks.streamline;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.ks.myexceptions.FileLogger;
import com.ks.net.TcpNet;
import com.ks.streamline.Recpacket.BitmapType;
import com.ks.streamline.Recpacket.PacketType;

public class ReceiveThread extends Thread {

	private LinkedBlockingQueue<Recpacket> recPacketQueue;
	private TcpNet tcpNet;

	public ReceiveThread(TcpNet tcpNet, LinkedBlockingQueue<Recpacket> recPacketQueue) {
		if (tcpNet == null | recPacketQueue == null)
			throw new RuntimeException("ReceiveThread constructor params can not be null");
		this.tcpNet = tcpNet;
		this.recPacketQueue = recPacketQueue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (!Thread.currentThread().isInterrupted() && tcpNet.isConnecting()) {
			Recpacket recpacket = new Recpacket();
			try {

				PacketType packetType = PacketType.getPacketType(tcpNet.getInputStream().readByte());
				recpacket.setPacketType(packetType);
				switch (packetType) {
				case BITMAP:
					int btmByteLen = conventSmall2Big(tcpNet.getInputStream().readInt());
					BitmapType type = BitmapType.getBitmapType(tcpNet.getInputStream().readByte());
					short cursorX = conventSmall2Big(tcpNet.getInputStream().readShort());
					short cursorY = conventSmall2Big(tcpNet.getInputStream().readShort());
					short difNums = conventSmall2Big(tcpNet.getInputStream().readShort());
					if (difNums > 0) {
						List<ShortRec> difPoints = new ArrayList<ShortRec>();
						for (int i = 0; i < difNums; i++) {
							short xpoint = conventSmall2Big(tcpNet.getInputStream().readShort());
							short ypoint = conventSmall2Big(tcpNet.getInputStream().readShort());
							short width = conventSmall2Big(tcpNet.getInputStream().readShort());
							short height = conventSmall2Big(tcpNet.getInputStream().readShort());
							ShortRec difPoint = new ShortRec(xpoint, ypoint, width, height);
							difPoints.add(difPoint);

						}
						recpacket.setDifPointsList(difPoints);
					}
					byte[] btmBytes = new byte[btmByteLen];
					int lenget = 0;
					while (lenget < btmByteLen) {
						try {
							lenget += tcpNet.getInputStream().read(btmBytes, lenget, btmByteLen - lenget);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							FileLogger.getLogger().write(e.getMessage());
							tcpNet.disConnect();
						}
					}

					/** 组装数据 */
					recpacket.setBitByts(btmBytes);
					recpacket.setBitmapBytesLength(btmByteLen);
					recpacket.setBitmapType(type);
					recpacket.setCursorPoint(new ShortPoint(cursorX, cursorY));
					// MessageBox.Show(getBitmapBytes.Length+"");
					/** 添加到接收队列 */
					recPacketQueue.put(recpacket);
					break;
				case TEXT:
					int textLen=conventSmall2Big(tcpNet.getInputStream().readInt());
					byte[] textBytes = new byte[textLen];
					int lentext = 0;
					while (lentext < textLen) {
						try {
							lentext += tcpNet.getInputStream().read(textBytes, lentext, textLen - lentext);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							FileLogger.getLogger().write(e.getMessage());
							tcpNet.disConnect();
						}
					}
					recpacket.setStringValue(new String(textBytes));//UTF-8
					recPacketQueue.put(recpacket);
					break;
				default:
					break;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				FileLogger.getLogger().write(e.getMessage());
				tcpNet.disConnect();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				FileLogger.getLogger().write(e.getMessage());
				tcpNet.disConnect();
			}
		}

	}

	/** 数据转换过程中一定要注意符号位扩展问题，自动转型问题,因为&符号两边默认都是int型 */
	private int conventSmall2Big(int small) {
		byte[] bits = new byte[4];
		bits[0] = (byte) (small & 0x000000ff);
		bits[1] = (byte) ((small >>> 8) & 0x000000ff);
		bits[2] = (byte) ((small >>> 16) & 0x000000ff);
		bits[3] = (byte) ((small >>> 24) & 0x000000ff);
		int ret = (bits[0] & 0x000000ff) << 24 | (bits[1] & 0x000000ff) << 16 | (bits[2] & 0x000000ff) << 8
				| (bits[3] & 0x000000ff);
		return ret;
	}

	private short conventSmall2Big(short small) {
		byte[] bits = new byte[2];
		bits[0] = (byte) (small & 0x000000ff);
		bits[1] = (byte) ((small >> 8) & 0x000000ff);
		short ret = (short) ((bits[0] & 0x000000ff) << 8 | (bits[1] & 0x000000ff));
		return ret;
	}
}
