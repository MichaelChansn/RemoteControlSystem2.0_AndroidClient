package com.ks.activitys;

import java.lang.ref.WeakReference;

import com.ks.net.NUMCODES.NETSTATE;
import com.ks.net.TcpNet;
import com.ks.net.enums.MessageEnums.MessageType;
import com.ks.streamline.DecompressThread;
import com.ks.streamline.RecoverAndDisplayThread;
import com.ks.streamline.SendPacket;
import com.ks.testndk.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
	private PointF startPoint = new PointF();
	private PointF movePoint = new PointF();
	private PointF midPoint = new PointF();
	private static float lx; // 记录上次鼠标的位置
	private static float ly;
	private float oldDist;
	private Thread decompreThread;
	private RecoverAndDisplayThread showThread;

	// 手势识别
	private GestureDetector gestureDetector;
	private Matrix globalMatrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private Matrix tmpMatrix = new Matrix();
	private Point pointHit = new Point();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		findViews();
		createFloatMenu();
	}

	private void findViews() {
		svShow = (SurfaceView) findViewById(R.id.surfaceViewShow);
		svHolder = svShow.getHolder();
		svShow.setOnTouchListener(new svShowOnTouchListener());
		svHolder.addCallback(new SurfaceViewCallback());
		handler = new MainHandler(MainActivity.this);
		tcpNet = TcpNet.getInstance();
		gestureDetector = new GestureDetector(this, new MyGestureListener());
		gestureDetector.setOnDoubleTapListener(new MyOnDoubleTapListener());

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

	private class svShowOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			gestureDetector.onTouchEvent(event);
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				System.out.println("onTouch ACTION_DOWN");
				mode = DRAG;
				startPoint.set(event.getX(), event.getY());
				movePoint.set(event.getX(), event.getY());
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			}
			// true,处理完毕，不在向下传送，false，继续向下传送消息
			return true;
		}

	}

	private Matrix tmp = new Matrix();

	public void limitMatrix(Matrix matrix) {
		tmp.reset();
		tmp.setScale(showThread.getScale(), showThread.getScale());
		tmp.postConcat(matrix);
		int width = showThread.getGlobalBtmWidth();
		int height = showThread.getGlobalBtmHeight();
		float[] f = new float[9];
		tmp.getValues(f);
		// 图片4个顶点的坐标
		float x1 = f[0] * 0 + f[1] * 0 + f[2];
		float y1 = f[3] * 0 + f[4] * 0 + f[5];
		float x2 = f[0] * width + f[1] * 0 + f[2];
		float y2 = f[3] * width + f[4] * 0 + f[5];
		float x3 = f[0] * 0 + f[1] * height + f[2];
		float y3 = f[3] * 0 + f[4] * height + f[5];
		float x4 = f[0] * width + f[1] * height + f[2];
		float y4 = f[3] * width + f[4] * height + f[5];
		// 打印图片现在参数
		/*
		 * System.out.println("坐标1："+x1+":"+y1);
		 * System.out.println("坐标2："+x2+":"+y2);
		 * System.out.println("坐标3："+x3+":"+y3);
		 * System.out.println("坐标4："+x4+":"+y4);
		 */
		if (x1 > 0 || x3 > 0) {
			matrix.postTranslate(-x1, 0);
		}
		if (x2 < svShow.getWidth() || x4 < svShow.getWidth()) {
			matrix.postTranslate(-(x2 - svShow.getWidth()), 0);
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

	private class MyGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			//System.out.println("onDown Pressed" + ":" + e.getX() + ":" + e.getY());

			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			//System.out.println("OnFling Pressed" + ":" + velocityX + ":" + velocityY);
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			//System.out.println("OnLongPressed Pressed");
			clickRightMouse();
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			//System.out.println("OnScroll Pressed" + ":" + distanceX + ":" + distanceY);
			if (e2.getPointerCount()==1) {
				tmpMatrix.postTranslate(-distanceX, 0);
				limitMatrix(tmpMatrix);
				showThread.setMatrix(tmpMatrix);
			}
			if(e2.getPointerCount()>1)
			{
				onMiddleButtonMove(-(int)distanceY);
			}
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			//System.out.println("onShowPress Pressed");
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			//System.out.println("onSingleTapUp Pressed");
			return true;
		}

	}

	private class MyOnDoubleTapListener implements OnDoubleTapListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			//System.out.println("onSingleTapConfirmed...");
			if (e.getPointerCount() == 1) {
				setMousePos((int) ((e.getX() - showThread.getMatrixBtmXPoint()) / showThread.getScale()),
						(int) (e.getY() / showThread.getScale()));
				clickLeftMouse();
			}
			return true;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			//System.out.println("onDoubleTapEvent...");
			return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			//System.out.println("onDoubleTap...");
			if (e.getPointerCount() == 1) {
				setMousePos((int) ((e.getX() - showThread.getMatrixBtmXPoint()) / showThread.getScale()),
						(int) (e.getY() / showThread.getScale()));
				doubleClickLeftMouse();
			}
			return true;
		}

	}

	
	private void createFloatMenu()
	{
		// Set up the white button on the lower right corner
				// more or less with default parameter
				final ImageView fabIconNew = new ImageView(this);
				fabIconNew.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_new_light));
				int bigActionButtonSize = getResources().getDimensionPixelSize(R.dimen.big_action_button_size);
				int bigActionButtonMargin = getResources().getDimensionPixelOffset(R.dimen.big_action_button_margin);
				int bigActionButtonContentSize = getResources().getDimensionPixelSize(R.dimen.big_action_button_content_size);
				int bigActionButtonContentMargin = getResources()
						.getDimensionPixelSize(R.dimen.big_action_button_content_margin);
				int bigActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.big_action_menu_radius);
				int smallSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.small_sub_action_button_size);
				int smallSubActionButtonContentMargin = getResources()
						.getDimensionPixelSize(R.dimen.small_sub_action_button_content_margin);

				FloatingActionButton.LayoutParams bigParams = new FloatingActionButton.LayoutParams(bigActionButtonSize,
						bigActionButtonSize);
				bigParams.setMargins(bigActionButtonMargin, bigActionButtonMargin, bigActionButtonMargin,
						bigActionButtonMargin);
				fabIconNew.setLayoutParams(bigParams);

				FloatingActionButton.LayoutParams fabIconNewParams = new FloatingActionButton.LayoutParams(
						bigActionButtonContentSize, bigActionButtonContentSize);
				fabIconNewParams.setMargins(bigActionButtonContentMargin, bigActionButtonContentMargin,
						bigActionButtonContentMargin, bigActionButtonContentMargin);
				final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(this)
						.setContentView(fabIconNew, fabIconNewParams)
						.setLayoutParams(bigParams)/*
													 * .setBackgroundDrawable(R.drawable
													 * .button_action_red_selector)
													 * .setPosition(FloatingActionButton
													 * .POSITION_BOTTOM_CENTER)
													 */
						.build();

				SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
				/*
				 * rLSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.
				 * drawable.button_action_blue_selector));
				 */

				FrameLayout.LayoutParams smallContentParams = new FrameLayout.LayoutParams(
						FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
				smallContentParams.setMargins(smallSubActionButtonContentMargin, smallSubActionButtonContentMargin,
						smallSubActionButtonContentMargin, smallSubActionButtonContentMargin);
				rLSubBuilder.setLayoutParams(smallContentParams);
				FrameLayout.LayoutParams smallParams = new FrameLayout.LayoutParams(smallSubActionButtonSize,
						smallSubActionButtonSize);
				rLSubBuilder.setLayoutParams(smallParams);

				ImageView rlIcon1 = new ImageView(this);
				ImageView rlIcon2 = new ImageView(this);
				ImageView rlIcon3 = new ImageView(this);
				ImageView rlIcon4 = new ImageView(this);

				rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_chat_light));
				rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_camera_light));
				rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_video_light));
				rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_place_light));

				
				// Build the menu with default options: light theme, 90 degrees, 72dp
				// radius.
				// Set 4 default SubActionButtons
				final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(this)
						.addSubActionView(rLSubBuilder.setContentView(rlIcon1, smallContentParams).build())
						.addSubActionView(rLSubBuilder.setContentView(rlIcon2, smallContentParams).build())
						.addSubActionView(rLSubBuilder.setContentView(rlIcon3, smallContentParams).build())
						.addSubActionView(rLSubBuilder.setContentView(rlIcon4, smallContentParams).build())
						.setRadius(bigActionMenuRadius)
						/*.setStartAngle(-20)
						.setEndAngle(-160)*/
						.attachTo(rightLowerButton)
						.build();

				rlIcon1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MainActivity.this, MouseActivity.class);
						MainActivity.this.startActivity(intent);
						
					}
				});
				// Listen menu open and close events to animate the button content view
				rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
					@Override
					public void onMenuOpened(FloatingActionMenu menu) {
						// Rotate the icon of rightLowerButton 45 degrees clockwise
						fabIconNew.setRotation(0);
						PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
						ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
						animation.start();
					}

					@Override
					public void onMenuClosed(FloatingActionMenu menu) {
						// Rotate the icon of rightLowerButton 45 degrees
						// counter-clockwise
						fabIconNew.setRotation(45);
						PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
						ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
						animation.start();
					}
				});

	}
	
	private void clickRightMouse() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_RIGHT_CLICK);
		tcpNet.sendMessage(senPacket);
	}

	private void setMousePos(int xPoint, int yPoint) {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_SET);
		senPacket.setIntValue1(xPoint);
		senPacket.setIntValue2(yPoint);
		tcpNet.sendMessage(senPacket);
	}

	private void clickLeftMouse() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_LEFT_CLICK);
		tcpNet.sendMessage(senPacket);
	}

	private void doubleClickLeftMouse() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_LEFT_DOUBLE_CLICK);
		tcpNet.sendMessage(senPacket);
	}

	private void onMiddleButtonDown(MotionEvent ev) {
		ly = ev.getY();

	}

	private void onMiddleButtonMove(int my) {
		if (my > 3 || my < -3) { // 减少发送次数 滑轮移动慢点
			SendPacket senPacket = new SendPacket();
			senPacket.setMsgType(MessageType.MOUSE_WHEEL);
			senPacket.setIntValue1( my);
			tcpNet.sendMessage(senPacket);
		}

	}

	private void onMouseMove(MotionEvent ev) {

		float x = ev.getX();
		int mx = (int) (x - movePoint.x); // 当前鼠标位置 - 上次鼠标的位置
		movePoint.x = x; // 把当前鼠标的位置付给lx 以备下次使用
		float y = ev.getY();
		int my = (int) (y - movePoint.y);
		movePoint.y = y;
		if ((Math.abs(mx) > 1 && my != 0) || (mx != 0 && Math.abs(my) > 1)) {
			SendPacket senPacket = new SendPacket();
			senPacket.setMsgType(MessageType.MOUSE_MOVE);
			senPacket.setIntValue1(mx);
			senPacket.setIntValue2(my);
			tcpNet.sendMessage(senPacket);
		}

	}

}
