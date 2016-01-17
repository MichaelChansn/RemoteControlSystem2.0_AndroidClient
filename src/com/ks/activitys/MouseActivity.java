package com.ks.activitys;

import com.ks.application.R;
import com.ks.net.TcpNet;
import com.ks.net.enums.MessageEnums.MessageType;
import com.ks.streamline.SendPacket;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class MouseActivity extends Activity {

	private FrameLayout touch;
	private Button mouseLeft;
	private Button mouseRight;
	// 手势识别
	private GestureDetector gestureDetector;
	private MouseListener mouseListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mouse);
		findViews();
		inits();
	}

	private void findViews() {
		touch = (FrameLayout) findViewById(R.id.touch);
		touch.setLongClickable(false);
		mouseLeft = (Button) findViewById(R.id.buttonLeft);
		mouseRight = (Button) findViewById(R.id.buttonRight);

	}

	private void inits() {
		gestureDetector = new GestureDetector(this, new MyGestureListener());
		gestureDetector.setOnDoubleTapListener(new MyOnDoubleTapListener());
		touch.setOnTouchListener(new MyOnTouchListener());
		mouseListener=new MouseListener();
		mouseLeft.setOnTouchListener(mouseListener);
		mouseRight.setOnTouchListener(mouseListener);
	}

	private class MouseListener implements OnTouchListener {

		private boolean isLeft=true;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.buttonLeft:
				isLeft=true;
				break;
			case R.id.buttonRight:
				isLeft=false;
				break;
			default:
				break;
			}
			
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				//if(event.getPointerCount()==1)
				if(isLeft)
				{
					MouseLeftDown();
				}
				else
				{
					MouseRightDown();
				}
				break;
			case MotionEvent.ACTION_UP:
				//if(event.getPointerCount()==1)
				if(isLeft)
				{
					MouseLeftUp();
				}
				else
				{
					MouseRightUp();
				}
				break;
			}
			return false;
		}

	}

	private PointF firstPoint=new PointF();
	private PointF secondPoint=new PointF();
	private class MyOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			// TODO Auto-generated method stub
			gestureDetector.onTouchEvent(event);
			// true,处理完毕，不在向下传送，false，继续向下传送消息
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				v.setBackgroundColor(getResources().getColor(R.color.mouseDown));
				if (event.getPointerCount() == 3) {
					MouseLeftDown();
				}
				if(event.getPointerCount()==2)
				{
					firstPoint.set(event.getX(0),event.getY(0));
					secondPoint.set(event.getX(1), event.getY(1));
					
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				v.setBackgroundColor(getResources().getColor(R.color.mouseUp));
				if (event.getPointerCount() == 3) {
					MouseLeftUp();
				}
				if(event.getPointerCount()==2)
				{
					if(firstPoint.equals(event.getX(0), event.getY(0)) && secondPoint.equals(event.getX(1), event.getY(1)))
					{
						clickRightMouse();
					}
				}
				break;
			}
			return true;
		}

	}

	private class MyGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			// System.out.println("onDown Pressed" + ":" + e.getX() + ":" +
			// e.getY());
			if (e.getPointerCount() == 3) {

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
			 System.out.println("OnLongPressed Pressed");
			//clickRightMouse();
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// System.out.println("OnScroll Pressed" + ":" + distanceX + ":" +
			// distanceY);
			if (e2.getPointerCount() == 1) {
				onMouseMove(distanceX, distanceY);
			}
			if (e2.getPointerCount() == 2) {
				onMiddleButtonMove(-(int) (distanceY*1.4));
			}
			if (e2.getPointerCount() == 3) {

				onMouseMove(distanceX, distanceY);

			}
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			//System.out.println("onShowPress Pressed");

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
			System.out.println("onDoubleTapEvent...");
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

	private void MouseRightDown() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_RIGHT_DOWN);
		TcpNet.getInstance().sendMessage(senPacket);
	}

	private void MouseRightUp() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_RIGHT_UP);
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
