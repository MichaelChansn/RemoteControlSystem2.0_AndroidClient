package com.ks.activitys;

import com.ks.net.TcpNet;
import com.ks.net.enums.MessageEnums.MessageType;
import com.ks.streamline.SendPacket;
import com.ks.testndk.R;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

public class MouseActivity extends Activity {

	private FrameLayout touch;
	// 手势识别
	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mouse);
		findViews();
		inits();
	}

	private void findViews() {
		touch = (FrameLayout) findViewById(R.id.touch);
	}

	private void inits() {
		gestureDetector = new GestureDetector(this, new MyGestureListener());
		gestureDetector.setOnDoubleTapListener(new MyOnDoubleTapListener());
		touch.setOnTouchListener(new MyOnTouchListener());
	}

	private class MyOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			gestureDetector.onTouchEvent(event);
			// true,处理完毕，不在向下传送，false，继续向下传送消息
			return true;
		}

	}

	private class MyGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			// System.out.println("onDown Pressed" + ":" + e.getX() + ":" +
			// e.getY());
			if (e.getPointerCount() == 1) {
			}
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
			clickRightMouse();
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// System.out.println("OnScroll Pressed" + ":" + distanceX + ":" +
			// distanceY);
			if (e2.getPointerCount() == 1) {
				onMouseMove(distanceX,distanceY);
			}
			if (e2.getPointerCount() > 1) {
				onMiddleButtonMove(-(int) distanceY);
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
				doubleClickLeftMouse();
			}
			return true;
		}

	}

	private void clickLeftMouse() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_LEFT_CLICK);
		TcpNet.getInstance().sendMessage(senPacket);
	}

	private void doubleClickLeftMouse() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_LEFT_DOUBLE_CLICK);
		TcpNet.getInstance().sendMessage(senPacket);
	}

	private void onMiddleButtonMove(int my) {
		if (my > 3 || my < -3) { // 减少发送次数 滑轮移动慢点
			SendPacket senPacket = new SendPacket();
			senPacket.setMsgType(MessageType.MOUSE_WHEEL);
			senPacket.setIntValue1(my);
			TcpNet.getInstance().sendMessage(senPacket);
		}

	}

	private void clickRightMouse() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_RIGHT_CLICK);
		TcpNet.getInstance().sendMessage(senPacket);
	}

	private void onMouseMove(float mx,float my) {

			SendPacket senPacket = new SendPacket();
			senPacket.setMsgType(MessageType.MOUSE_MOVE);
			senPacket.setIntValue1(-(int)mx);
			senPacket.setIntValue2(-(int)my);
			TcpNet.getInstance().sendMessage(senPacket);

	}
}
