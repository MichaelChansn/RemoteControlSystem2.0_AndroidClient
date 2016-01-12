package com.ks.streamline;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.ks.activitys.MainActivity;
import com.ks.activitys.MainActivity.MainHandler;
import com.ks.myexceptions.FileLogger;
import com.ks.net.TcpNet;
import com.ks.streamline.Recpacket.BitmapType;
import com.ks.streamline.Recpacket.PacketType;
import com.ks.testndk.JNIBtmProcess;
import com.ks.tools.ScreenTools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.SurfaceHolder;

public class RecoverAndDisplayThread extends Thread {
	private MainHandler hander;
	private ScreenTools scTool;
	private LinkedBlockingQueue<BitmapWithCursor> difBtmQueue;
	private TcpNet tcpNet;
	private static Bitmap globalBtm = null;
	private Matrix matrix = new Matrix();
	private JNIBtmProcess jNIProcess = new JNIBtmProcess();
	private boolean isRun = false;
	private boolean isInitMatrix = false;

	public RecoverAndDisplayThread(TcpNet tcpNet, MainHandler hander) {
		if (tcpNet == null || hander == null)
			throw new RuntimeException(
					"RecoverAndDisplayThread constructor params can not be null except displayQueue");
		this.hander = hander;
		this.difBtmQueue = tcpNet.getDifBtmQueue();
		this.tcpNet = tcpNet;
		isRun = true;
		scTool = new ScreenTools(hander.getContext());
	}

	public void stopThread() {
		isRun = false;
		this.interrupt();
	}

	public void setMatrix(Matrix m) {
		this.matrix.postConcat(m);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (isRun && !Thread.interrupted()&& tcpNet.isConnecting()) {
			BitmapWithCursor btmDif;
			try {
				//System.out.println("displayThread*********************");
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
						jNIProcess.getBitmapOrlBtm(recs, globalBtm, btm);
						btm.recycle();
						btm = null;
						break;
					case COMPLETE:
						if (globalBtm != null) {
							globalBtm.recycle();
						}

						globalBtm = btm;
						break;
					default:
						break;
					}
					final MainActivity context = (MainActivity) hander.getContext();
					if (context != null) {
						if(!isInitMatrix)
						{
							isInitMatrix=true;
							float scale=context.getSVShow().getHeight()/(float)globalBtm.getHeight();
							this.matrix.postScale(scale, scale);
						}
						SurfaceHolder holder = context.getSVHolder();
						if (holder != null) {
							Canvas canvs = holder.lockCanvas();
							canvs.drawBitmap(globalBtm, matrix, null);
							holder.unlockCanvasAndPost(canvs);

						}

					}
					break;
				case TEXT:
					// TODO
					String valueFromServer = btmDif.getStringValue();
					System.out.println(valueFromServer);
					break;
				default:
					break;
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				FileLogger.getLogger().write(e.getMessage());
				break;
				// tcpNet.disConnect();
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}

		}
		jNIProcess.release();
	}
}
