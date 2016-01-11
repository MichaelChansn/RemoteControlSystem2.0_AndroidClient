package com.ks.streamline;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.ks.myexceptions.FileLogger;
import com.ks.net.TcpNet;
import com.ks.streamline.Recpacket.BitmapType;
import com.ks.streamline.Recpacket.PacketType;
import com.ks.testndk.JNITest;
import com.ks.testndk.MainActivity;
import com.ks.testndk.MainActivity.MyHandler;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class RecoverAndDisplayThread extends Thread {
	private MyHandler hander;
	private LinkedBlockingQueue<BitmapWithCursor> difBtmQueue;
	private LinkedBlockingQueue<BitmapWithCursor> displayQueue;
	private TcpNet tcpNet;
	private Bitmap globalBtm = null;
	private Matrix matrix = new Matrix();
	private JNITest jNITest = new JNITest();

	public RecoverAndDisplayThread(TcpNet tcpNet, MyHandler hander, LinkedBlockingQueue<BitmapWithCursor> difBtmQueue,
			LinkedBlockingQueue<BitmapWithCursor> displayQueue) {
		if (tcpNet == null || difBtmQueue == null || hander == null)
			throw new RuntimeException(
					"RecoverAndDisplayThread constructor params can not be null except displayQueue");
		this.hander = hander;
		this.difBtmQueue = difBtmQueue;
		this.displayQueue = displayQueue;
		this.tcpNet = tcpNet;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (!isInterrupted() && tcpNet.isConnecting()) {
			BitmapWithCursor btmDif;
			try {
				btmDif = difBtmQueue.poll(200, TimeUnit.MILLISECONDS);
				if (btmDif == null)
					continue;
				PacketType packetType = btmDif.getPacketType();
				switch (packetType) {
				case BITMAP:
					ShortPoint curPoint = btmDif.getCursorPoint();
					List<ShortRec> recs = btmDif.getDifPointsList();
					BitmapType type = btmDif.getType();
					Bitmap btm = btmDif.getDifBitmap();
					switch (type) {
					case BLOCK:
						jNITest.getBitmapOrlBtm(recs, globalBtm, btm);
						break;
					case COMPLETE:
						globalBtm = btm;
						break;
					default:
						break;
					}
					final MainActivity context = (MainActivity) hander.getContext();
					if (context != null) {
						matrix.setScale((float) 1.0, (float) 1.0);
						context.ivShow.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								context.ivShow.setImageMatrix(matrix);
								context.ivShow.setImageBitmap(globalBtm);
							}
						});

					}
					break;
				case TEXT:
					//TODO
					String valueFromServer=btmDif.getStringValue();
					System.out.println(valueFromServer);
					break;
				default:
					break;
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				FileLogger.getLogger().write(e.getMessage());
				tcpNet.disConnect();
			}

		}
	}
}
