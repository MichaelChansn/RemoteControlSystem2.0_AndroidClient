package com.ks.activitys;

import java.lang.ref.WeakReference;

import com.ks.application.R;
import com.ks.net.NUMCODES.NETSTATE;
import com.ks.net.TcpNet;
import com.ks.net.enums.MessageEnums.MessageType;
import com.ks.net.enums.MessageEnums.SpecialKeys;
import com.ks.streamline.DecompressThread;
import com.ks.streamline.RecoverAndDisplayThread;
import com.ks.streamline.SendPacket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity {

	private SurfaceView svShow;
	private SurfaceHolder svHolder;
	private MainHandler handler;
	private TcpNet tcpNet;
	private PointF startPoint = new PointF();
	private PointF movePoint = new PointF();
	private Thread decompreThread;
	private RecoverAndDisplayThread showThread;
	private EditText edSendText;
	private Button specalKeyClose;
	private RelativeLayout speicalKeys;

	private Button buttonOK;
	private Button backspace;
	private Button enter;

	private Button space;
	private Button esc;
	private Button shift;
	private Button ctrl;
	private Button alt;
	private Button tab;
	private Button win;
	private Button f1;
	private Button f2;
	private Button f3;
	private Button f4;
	private Button f5;
	private Button f6;
	private Button f7;
	private Button f8;
	private Button f9;
	private Button f10;
	private Button f11;
	private Button f12;
	private Button end;
	private Button home;
	private Button delete;
	private Button prtsc;
	private Button insert;
	private Button numlock;
	private Button pageup;
	private Button pagedown;
	private Button upkey;
	private Button downkey;
	private Button leftkey;
	private Button rightkey;
	private Button capslock;
	// 手势识别
	private GestureDetector gestureDetector;
	private Matrix tmpMatrix = new Matrix();
	private SpecialKeyLinstener specialKeyLinstener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		findViews();
		initFuns();
	}

	private void findViews() {
		svShow = (SurfaceView) findViewById(R.id.surfaceViewShow);
		edSendText = (EditText) findViewById(R.id.editTextSendText);
		edSendText.setVisibility(View.INVISIBLE);
		specalKeyClose = (Button) findViewById(R.id.buttonSpecalKeyClose);
		speicalKeys = (RelativeLayout) findViewById(R.id.relativeLayoutSpelKeys);
		svHolder = svShow.getHolder();
		svShow.setOnTouchListener(new svShowOnTouchListener());
		svHolder.addCallback(new SurfaceViewCallback());
		handler = new MainHandler(MainActivity.this);
		tcpNet = TcpNet.getInstance();
		gestureDetector = new GestureDetector(this, new MyGestureListener());
		gestureDetector.setOnDoubleTapListener(new MyOnDoubleTapListener());
		new FloatMenu(MainActivity.this).createFloatMenu();
		findSpeialKeyViews();
		specialKeyLinstener = new SpecialKeyLinstener();

	}

	private void findSpeialKeyViews() {
		buttonOK = (Button) findViewById(R.id.button_OK);
		backspace = (Button) findViewById(R.id.button_Backspace);
		enter = (Button) findViewById(R.id.button_Enter);

		space = (Button) findViewById(R.id.button_Space);
		esc = (Button) findViewById(R.id.button_Esc);
		shift = (Button) findViewById(R.id.button_Shift);
		ctrl = (Button) findViewById(R.id.button_Ctrl);
		alt = (Button) findViewById(R.id.button_Alt);
		tab = (Button) findViewById(R.id.button_Tab);

		win = (Button) findViewById(R.id.button_Windows);
		f1 = (Button) findViewById(R.id.button_F1);
		f2 = (Button) findViewById(R.id.button_F2);
		f3 = (Button) findViewById(R.id.button_F3);
		f4 = (Button) findViewById(R.id.button_F4);
		f5 = (Button) findViewById(R.id.button_F5);
		f6 = (Button) findViewById(R.id.button_F6);
		f7 = (Button) findViewById(R.id.button_F7);
		f8 = (Button) findViewById(R.id.button_F8);
		f9 = (Button) findViewById(R.id.button_F9);
		f10 = (Button) findViewById(R.id.button_F10);
		f11 = (Button) findViewById(R.id.button_F11);
		f12 = (Button) findViewById(R.id.button_F12);

		end = (Button) findViewById(R.id.button_End);
		home = (Button) findViewById(R.id.button_Home);
		delete = (Button) findViewById(R.id.button_Delete);
		prtsc = (Button) findViewById(R.id.button_PrtSc);
		insert = (Button) findViewById(R.id.button_Insert);
		numlock = (Button) findViewById(R.id.button_NumLock);
		pageup = (Button) findViewById(R.id.button_PageUp);
		pagedown = (Button) findViewById(R.id.button_PageDown);
		upkey = (Button) findViewById(R.id.button_Up);
		downkey = (Button) findViewById(R.id.button_Down);
		leftkey = (Button) findViewById(R.id.button_Left);
		rightkey = (Button) findViewById(R.id.button_Right);
		capslock = (Button) findViewById(R.id.button_CapsLock);
	}

	private void initFuns() {
		specalKeyClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closeSpecialKeys();

			}
		});
		edSendText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_DEL) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						sendKeyMessage(MessageType.KEY_DOWN, SpecialKeys.BACKSPACE);
						sendKeyMessage(MessageType.KEY_UP, SpecialKeys.BACKSPACE);
					}

				}

				return false;
			}
		});
		edSendText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				System.out.println("onTextChanged得到的数据是：——>" + edSendText.getText().toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				System.out.println("beforeTextChanged得到的数据是：——>" + edSendText.getText().toString());

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				System.out.println("afterTextChanged得到的数据是：——>" + edSendText.getText().toString());

				String getText = edSendText.getText().toString();
				if (!getText.isEmpty()) {
					sendTextMessage(getText);
					edSendText.getText().clear();// setText("");
				}

			}
		});

		edSendText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub

				if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
					// System.out.println("这个是回车键按下了哦J克隆空款就考了利率J");
					sendKeyMessage(MessageType.KEY_DOWN, SpecialKeys.ENTER);
					sendKeyMessage(MessageType.KEY_UP, SpecialKeys.ENTER);
				}

				return true;
			}
		});
		buttonOK.setOnClickListener(specialKeyLinstener);
		backspace.setOnClickListener(specialKeyLinstener);
		enter.setOnClickListener(specialKeyLinstener);

		space.setOnClickListener(specialKeyLinstener);
		esc.setOnClickListener(specialKeyLinstener);
		shift.setOnClickListener(specialKeyLinstener);
		ctrl.setOnClickListener(specialKeyLinstener);
		alt.setOnClickListener(specialKeyLinstener);
		tab.setOnClickListener(specialKeyLinstener);

		win.setOnClickListener(specialKeyLinstener);
		f1.setOnClickListener(specialKeyLinstener);
		f2.setOnClickListener(specialKeyLinstener);
		f3.setOnClickListener(specialKeyLinstener);
		f4.setOnClickListener(specialKeyLinstener);
		f5.setOnClickListener(specialKeyLinstener);
		f6.setOnClickListener(specialKeyLinstener);
		f7.setOnClickListener(specialKeyLinstener);
		f8.setOnClickListener(specialKeyLinstener);
		f9.setOnClickListener(specialKeyLinstener);
		f10.setOnClickListener(specialKeyLinstener);
		f11.setOnClickListener(specialKeyLinstener);
		f12.setOnClickListener(specialKeyLinstener);

		end.setOnClickListener(specialKeyLinstener);
		home.setOnClickListener(specialKeyLinstener);
		delete.setOnClickListener(specialKeyLinstener);
		prtsc.setOnClickListener(specialKeyLinstener);
		insert.setOnClickListener(specialKeyLinstener);
		numlock.setOnClickListener(specialKeyLinstener);
		pageup.setOnClickListener(specialKeyLinstener);
		pagedown.setOnClickListener(specialKeyLinstener);
		upkey.setOnClickListener(specialKeyLinstener);
		downkey.setOnClickListener(specialKeyLinstener);
		leftkey.setOnClickListener(specialKeyLinstener);
		rightkey.setOnClickListener(specialKeyLinstener);
		capslock.setOnClickListener(specialKeyLinstener);
	}

	public boolean isSpecialShow() {
		return speicalKeys.getVisibility() == View.VISIBLE;
	}

	public void showIME() {

		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		edSendText.setVisibility(View.VISIBLE);
		edSendText.setFocusable(true);
		// edSendText.setFocusableInTouchMode(true);
		edSendText.requestFocus();
		// edSendText.requestFocusFromTouch();
	}

	public void showSpecialKeys() {
		System.out.println("onshow");
		speicalKeys.setAlpha(0);
		speicalKeys.setVisibility(View.VISIBLE);
		
		speicalKeys.animate().alpha(1).setDuration(500).setListener(null).start();
	}

	public void closeSpecialKeys() {
		System.out.println("onclose");
		speicalKeys.setAlpha(1);
		speicalKeys.animate().alpha(0).setDuration(500).setListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				speicalKeys.setVisibility(View.GONE);
			}

		}).start();

	}

	public SurfaceHolder getSVHolder() {
		return svHolder;
	}

	public SurfaceView getSVShow() {
		return svShow;
	}

	private void inits() {
		startRecPic();
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
		stopRecPic();
		((DecompressThread) decompreThread).stopThread();
		((RecoverAndDisplayThread) showThread).stopThread();
		try {
			decompreThread.join();
			showThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			decompreThread = null;
			showThread = null;
		}

	}

	private class svShowOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			gestureDetector.onTouchEvent(event);
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				if (event.getPointerCount() == 3) {
					MouseLeftDown();
				}
				startPoint.set(event.getX(), event.getY());
				movePoint.set(event.getX(), event.getY());
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				if (event.getPointerCount() == 3) {
					MouseLeftUp();
				}
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

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {

			System.out.println("created");
			inits();
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
			// System.out.println("onDown Pressed" + ":" + e.getX() + ":" +
			// e.getY());

			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			// System.out.println("OnFling Pressed" + ":" + velocityX + ":" +
			// velocityY);
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// System.out.println("OnLongPressed Pressed");
			setMousePos((int) ((e.getX() - showThread.getMatrixBtmXPoint()) / showThread.getScale()),
					(int) (e.getY() / showThread.getScale()));
			clickRightMouse();
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// System.out.println("OnScroll Pressed" + ":" + distanceX + ":" +
			// distanceY);
			if (e2.getPointerCount() == 1) {
				tmpMatrix.postTranslate(-distanceX, 0);
				limitMatrix(tmpMatrix);
				showThread.setMatrix(tmpMatrix);
			}
			if (e2.getPointerCount() == 2) {
				onMiddleButtonMove(-(int) distanceY);
			}
			if (e2.getPointerCount() == 3) {

				onMouseMove(distanceX, distanceY);

			}
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// System.out.println("onShowPress Pressed");
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// System.out.println("onSingleTapUp Pressed");
			return true;
		}

	}

	private class MyOnDoubleTapListener implements OnDoubleTapListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// System.out.println("onSingleTapConfirmed...");
			if (e.getPointerCount() == 1) {
				setMousePos((int) ((e.getX() - showThread.getMatrixBtmXPoint()) / showThread.getScale()),
						(int) (e.getY() / showThread.getScale()));
				clickLeftMouse();
			}
			return true;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			// System.out.println("onDoubleTapEvent...");
			return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// System.out.println("onDoubleTap...");
			if (e.getPointerCount() == 1) {
				setMousePos((int) ((e.getX() - showThread.getMatrixBtmXPoint()) / showThread.getScale()),
						(int) (e.getY() / showThread.getScale()));
				doubleClickLeftMouse();
			}
			return true;
		}

	}

	private void sendKeyMessage(MessageType keyType, SpecialKeys key) {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(keyType);
		senPacket.setSplKeys(key);
		tcpNet.sendMessage(senPacket);
	}

	private void sendTextMessage(String message) {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.TEXT);
		senPacket.setStrValue(message);
		tcpNet.sendMessage(senPacket);
	}

	private void startRecPic() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.START_PIC);
		tcpNet.sendMessage(senPacket);
	}

	private void stopRecPic() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.STOP_PIC);
		tcpNet.sendMessage(senPacket);
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

	private void onMiddleButtonMove(int my) {
		if (my > 3 || my < -3) { // 减少发送次数 滑轮移动慢点
			SendPacket senPacket = new SendPacket();
			senPacket.setMsgType(MessageType.MOUSE_WHEEL);
			senPacket.setIntValue1(my);
			tcpNet.sendMessage(senPacket);
		}

	}

	private void MouseLeftDown() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_LEFT_DOWN);
		TcpNet.getInstance().sendMessage(senPacket);
	}

	private void MouseLeftUp() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_LEFT_UP);
		TcpNet.getInstance().sendMessage(senPacket);
	}

	private void onMouseMove(float mx, float my) {

		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_MOVE);
		senPacket.setIntValue1(-(int) mx);
		senPacket.setIntValue2(-(int) my);
		TcpNet.getInstance().sendMessage(senPacket);

	}

}
