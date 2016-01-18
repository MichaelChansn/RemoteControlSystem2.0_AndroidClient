package com.ks.streamline;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.ks.activitys.MainActivity;
import com.ks.activitys.MainActivity.MainHandler;
import com.ks.application.KSApplication;
import com.ks.myexceptions.FileLogger;
import com.ks.net.TcpNet;
import com.ks.streamline.Recpacket.BitmapType;
import com.ks.streamline.Recpacket.PacketType;
import com.ks.testndk.JNIBtmProcess;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.view.SurfaceHolder;

public class RecoverAndDisplayThread extends Thread {
	private MainHandler hander;
	private LinkedBlockingQueue<BitmapWithCursor> difBtmQueue;
	private TcpNet tcpNet;
	private static Bitmap globalBtm;
	private static Matrix matrix = new Matrix();
	private static Matrix globalMatrix = new Matrix();
	private static Matrix displayMatrix = new Matrix();
	private JNIBtmProcess jNIProcess = new JNIBtmProcess();
	private boolean isRun = false;
	private static float scale = (float) 1.0;
	private static int btmHeight = 0;
	private static int btmWidth = 0;
	private static ShortPoint curPoint;

	public float getScale() {
		return scale;
	}

	public RecoverAndDisplayThread(TcpNet tcpNet, MainHandler hander) {
		if (tcpNet == null || hander == null)
			throw new RuntimeException(
					"RecoverAndDisplayThread constructor params can not be null except displayQueue");
		this.hander = hander;
		this.difBtmQueue = tcpNet.getDifBtmQueue();
		this.tcpNet = tcpNet;
		isRun = true;
	}

	public void stopThread() {
		isRun = false;
		this.interrupt();
	}

	public void setMatrix(Matrix m) {
		RecoverAndDisplayThread.globalMatrix.set(m);
	}

	public int getGlobalBtmWidth() {
		return btmWidth;
	}

	public int getGlobalBtmHeight() {
		return btmHeight;
	}

	public Point getMatrixBtmPoint() {
		float[] f = new float[9];
		displayMatrix.set(matrix);
		displayMatrix.postConcat(globalMatrix);
		displayMatrix.getValues(f);
		// 图片4个顶点的坐标
		float x1 = f[0] * 0 + f[1] * 0 + f[2];
		float y1 = f[3] * 0 + f[4] * 0 + f[5];
		return new Point((int) x1, (int) y1);
	}

	public float[] getMatrixBtmAllPoints() {
		float[] f = new float[9];
		displayMatrix.set(matrix);
		displayMatrix.postConcat(globalMatrix);
		displayMatrix.getValues(f);
		int width = getGlobalBtmWidth();
		int height = getGlobalBtmHeight();
		float x1 = f[0] * 0 + f[1] * 0 + f[2];
		float y1 = f[3] * 0 + f[4] * 0 + f[5];
		float x2 = f[0] * width + f[1] * 0 + f[2];
		float y2 = f[3] * width + f[4] * 0 + f[5];
		float x3 = f[0] * 0 + f[1] * height + f[2];
		float y3 = f[3] * 0 + f[4] * height + f[5];
		float x4 = f[0] * width + f[1] * height + f[2];
		float y4 = f[3] * width + f[4] * height + f[5];
		float[] retF = new float[8];
		retF[0] = x1;
		retF[1] = y1;
		retF[2] = x2;
		retF[3] = y2;
		retF[4] = x3;
		retF[5] = y3;
		retF[6] = x4;
		retF[7] = y4;
		return retF;
	}

	public float getMatrixBtmScale() {
		float[] f = new float[9];
		displayMatrix.set(matrix);
		displayMatrix.postConcat(globalMatrix);
		displayMatrix.getValues(f);
		int height = getGlobalBtmHeight();
		float x1 = f[0] * 0 + f[1] * 0 + f[2];
		float y1 = f[3] * 0 + f[4] * 0 + f[5];
		float x3 = f[0] * 0 + f[1] * height + f[2];
		float y3 = f[3] * 0 + f[4] * height + f[5];
		double nowHeight = Math.sqrt((x1 - x3) * (x1 - x3) + (y1 - y3) * (y1 - y3));
		return (float) (nowHeight / height);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (isRun && !Thread.interrupted() && tcpNet.isConnecting()) {
			BitmapWithCursor btmDif;
			try {
				// System.out.println("displayThread*********************");
				btmDif = difBtmQueue.poll(60, TimeUnit.MILLISECONDS);
				if (btmDif != null) {
					PacketType packetType = btmDif.getPacketType();
					switch (packetType) {
					case BITMAP:
						curPoint = btmDif.getCursorPoint();
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
							scale = ((MainActivity) hander.getContext()).getSVShow().getHeight()
									/ (float) btm.getHeight();
							RecoverAndDisplayThread.matrix.setScale(scale, scale);
							btmWidth = btm.getWidth();
							btmHeight = btm.getHeight();
							globalBtm = btm;
							break;
						default:
							break;
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

				}
				if (globalBtm != null) {
					displayMatrix.set(matrix);
					displayMatrix.postConcat(globalMatrix);
					Point position = getMatrixBtmPoint();
					final MainActivity context = (MainActivity) hander.getContext();
					if (context != null) {
						SurfaceHolder holder = context.getSVHolder();
						if (holder != null) {
							Canvas canvs = holder.lockCanvas();
							if (canvs != null) {
								canvs.drawBitmap(globalBtm, displayMatrix, null);
								canvs.drawBitmap(KSApplication.btmCursor,
										(curPoint.getXPoint() * getMatrixBtmScale()) + position.x,
										(curPoint.getYPoint() * getMatrixBtmScale()) + position.y, null);
								holder.unlockCanvasAndPost(canvs);
							}

						}

					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				isRun = false;
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
