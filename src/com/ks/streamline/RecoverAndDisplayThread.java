package com.ks.streamline;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.ks.activitys.MainActivity;
import com.ks.activitys.MainActivity.MainHandler;
import com.ks.application.KSApplication;
import com.ks.myexceptions.FileLogger;
import com.ks.net.NUMCODES;
import com.ks.net.TcpNet;
import com.ks.streamline.Recpacket.BitmapType;
import com.ks.streamline.Recpacket.PacketType;
import com.ks.testndk.JNIBtmProcess;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Message;
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
	private static boolean isPrepareOK=false;//init globalBitmap OK ? as long as globalbtm is OK, the points and scale can get or you will get zero.

	public boolean isPrepareOK()
	{
		return isPrepareOK;
	}
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
		float[] retF = new float[8];
		displayMatrix.set(matrix);
		displayMatrix.postConcat(globalMatrix);
		displayMatrix.getValues(f);
		retF[0] = f[0] * 0 + f[1] * 0 + f[2];
		retF[1] = f[3] * 0 + f[4] * 0 + f[5];
		retF[2] = f[0] * btmWidth + f[1] * 0 + f[2];
		retF[3] = f[3] * btmWidth + f[4] * 0 + f[5];
		retF[4] = f[0] * 0 + f[1] * btmHeight + f[2];
		retF[5] = f[3] * 0 + f[4] * btmHeight + f[5];
		retF[6] = f[0] * btmWidth + f[1] * btmHeight + f[2];
		retF[7] = f[3] * btmWidth + f[4] * btmHeight + f[5];
		return retF;
	}

	public float getMatrixBtmScale() {
		float[] f = new float[9];
		displayMatrix.set(matrix);
		displayMatrix.postConcat(globalMatrix);
		displayMatrix.getValues(f);
		return f[Matrix.MSCALE_X];
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
							isPrepareOK=true;
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
						Message msg=hander.obtainMessage();
						msg.what=NUMCODES.NETSTATE.SERVERMESSAGE.getValue();
						msg.obj=valueFromServer;
						hander.sendMessage(msg);
						break;
					default:
						break;
					}

				}
				if (globalBtm != null) {
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
