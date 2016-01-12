package com.ks.activitys;

import java.lang.ref.WeakReference;
import java.util.concurrent.LinkedBlockingQueue;

import com.ks.net.NUMCODES.NETSTATE;
import com.ks.net.TcpNet;
import com.ks.net.enums.MessageEnums.MessageType;
import com.ks.streamline.BitmapWithCursor;
import com.ks.streamline.DecompressThread;
import com.ks.streamline.ReceiveThread;
import com.ks.streamline.RecoverAndDisplayThread;
import com.ks.streamline.Recpacket;
import com.ks.streamline.SendPacket;
import com.ks.testndk.R;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;

public class MainActivity extends Activity {

	private SurfaceView svShow;
	private SurfaceHolder svHolder;
	private MainHandler handler;
	private TcpNet tcpNet;
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;
	private PointF start = new PointF();
	private PointF mid = new PointF();
	private float oldDist;
	private Thread decompreThread;
	private Thread showThread;

	private Matrix globalMatrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private Matrix tmpMatrix=new Matrix();
	private Point pointHit=new Point();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		findViews();
	}

	private void findViews() {
		svShow = (SurfaceView) findViewById(R.id.surfaceViewShow);
		svHolder = svShow.getHolder();
		svShow.setOnTouchListener(new svShowOnTouchListener());
		svShow.setOnLongClickListener(new svOnLongClickListener());
		svHolder.addCallback(new SurfaceViewCallback());
		handler = new MainHandler(MainActivity.this);
		tcpNet = TcpNet.getInstance();

	}

	public SurfaceHolder getSVHolder() {
		return svHolder;
	}

	public SurfaceView getSVShow() {
		return svShow;
	}

	private void inits() {
		initThreads(tcpNet, handler);
	}

	/** 自定义handler 采用弱引用，防止内存泄露 */
	public static class MainHandler extends Handler {
		private WeakReference<Activity> mActivity;

		public MainHandler(Activity activity) {
			mActivity = new WeakReference<Activity>(activity);
		}

		public Activity getContext() {
			return mActivity.get();
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Activity theActivity = mActivity.get(); // 是否还存在
			if (theActivity == null)
				return;
			switch (NETSTATE.getNETSTATEByValue(msg.what)) {
			case WRONGCODE:
				break;
			default:
				break;
			}
			super.handleMessage(msg);

		}
	}

	private void initThreads(TcpNet tcpNet, MainHandler handler) {
		decompreThread = new DecompressThread(tcpNet);
		showThread = new RecoverAndDisplayThread(tcpNet, handler);
		decompreThread.setDaemon(true);
		showThread.setDaemon(true);
		decompreThread.start();
		showThread.start();
	}

	private void stopShowThreads() {
		((DecompressThread) decompreThread).stopThread();
		((RecoverAndDisplayThread) showThread).stopThread();

	}

	private class svOnLongClickListener implements OnLongClickListener
	{

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			SendPacket senPacket=new SendPacket();
			senPacket.setMsgType(MessageType.MOUSE_RIGHT_CLICK);
			tcpNet.sendMessage(senPacket);
			return false;
		}
		
	}
	private class svShowOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			}
			return false;
		}
	

	}

	private class SurfaceViewCallback implements Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

			System.out.println("changed");
			inits();
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {

			System.out.println("created");
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {

			System.out.println("destoryed");
			stopShowThreads();

		}

	}

}
