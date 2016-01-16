package com.ks.activitys;

import com.ks.application.KSApplication;
import com.ks.application.R;
import com.ks.net.TcpNet;
import com.ks.net.enums.MessageEnums.MessageType;
import com.ks.net.enums.MessageEnums.SpecialKeys;
import com.ks.streamline.SendPacket;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class GameControlActivity extends Activity {

	private ImageButton up;
	private ImageButton down;
	private ImageButton left;
	private ImageButton right;
	private ImageButton leftup;
	private ImageButton leftdown;
	private ImageButton rightup;
	private ImageButton rightdown;
	private ImageButton zhongjian;
	private ImageButton space;
	private ImageButton enter;

	private ImageButton A;
	private ImageButton B;
	private ImageButton C;
	private ImageButton D;
	private ImageButton leftmouse;

	private ImageButton start;
	private ImageButton stop;

	private ImageButton jiasudu;

	
	private static float mx = 0; // 发送的鼠标移动的差值
	private static float my = 0;
	private static float lx; // 记录上次鼠标的位置
	private static float ly;
	private static float fx; // 手指第一次接触屏幕时的坐标
	private static float fy;

	private SensorManager sensorManager;
	private MySensorEventListener mySensorEventListener;
	private ButtonListener touchListener;

	private boolean isopen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game_control);
		findViews();
		setUpFunctions();
	}

	private void findViews() {
		up = (ImageButton) findViewById(R.id.imageButton_up);
		down = (ImageButton) findViewById(R.id.imageButton_down);
		left = (ImageButton) findViewById(R.id.imageButton_left);
		right = (ImageButton) findViewById(R.id.imageButton_right);
		leftup = (ImageButton) findViewById(R.id.imageButton_leftup);
		leftdown = (ImageButton) findViewById(R.id.imageButton_leftdown);
		rightup = (ImageButton) findViewById(R.id.imageButton_rightup);
		rightdown = (ImageButton) findViewById(R.id.imageButton_rightdown);
		zhongjian = (ImageButton) findViewById(R.id.imageButton_leftmiddle);
		A = (ImageButton) findViewById(R.id.imageButton_A);
		B = (ImageButton) findViewById(R.id.imageButton_B);
		C = (ImageButton) findViewById(R.id.imageButton_C);
		D = (ImageButton) findViewById(R.id.imageButton_D);
		leftmouse = (ImageButton) findViewById(R.id.imageButton_rightMiddle);
		start = (ImageButton) findViewById(R.id.imageButton_start);
		stop = (ImageButton) findViewById(R.id.imageButton_stop);

		space = (ImageButton) findViewById(R.id.imageButton_space);
		enter = (ImageButton) findViewById(R.id.ImageButton_enter);

		jiasudu = (ImageButton) findViewById(R.id.imageButton_gravity);
		mySensorEventListener = new MySensorEventListener();// 这个监听器当然是我们自己定义的，在重力感
															// 应器感应到手机位置有变化的时候，我们可以采取相应的操作，这里紧紧是将x,y,z的值打印出来
		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		touchListener = new ButtonListener();

	}

	private void setUpFunctions() {
		jiasudu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				KSApplication.Vibrate(GameControlActivity.this, KSApplication.VIBRATETIME);
				if (isopen == false) {
					isopen = true;
					jiasudu.setBackgroundResource(R.drawable.button_close);
				} else {
					isopen = false;
					jiasudu.setBackgroundResource(R.drawable.button_open);
				}

			}
		});
		up.setOnTouchListener(touchListener);
		down.setOnTouchListener(touchListener);
		left.setOnTouchListener(touchListener);
		right.setOnTouchListener(touchListener);
		leftup.setOnTouchListener(touchListener);
		leftdown.setOnTouchListener(touchListener);
		rightup.setOnTouchListener(touchListener);
		rightdown.setOnTouchListener(touchListener);
		A.setOnTouchListener(touchListener);
		B.setOnTouchListener(touchListener);
		C.setOnTouchListener(touchListener);
		D.setOnTouchListener(touchListener);

		zhongjian.setOnTouchListener(touchListener);
		leftmouse.setOnTouchListener(touchListener);
		space.setOnTouchListener(touchListener);
		enter.setOnTouchListener(touchListener);
		start.setOnTouchListener(touchListener);
		stop.setOnTouchListener(touchListener);

	}

	private class ButtonListener implements OnTouchListener {

		public boolean onTouch(View v, MotionEvent event) {
			v.performClick();
			SpecialKeys key1 = SpecialKeys.NONE;
			SpecialKeys key2 = SpecialKeys.NONE;
			boolean isMouse = false;
			switch (v.getId()) {
			case R.id.imageButton_up:
				key1 = SpecialKeys.GAME_UP;
				break;
			case R.id.imageButton_down:
				key1 = SpecialKeys.GAME_DOWN;
				break;
			case R.id.imageButton_left:
				key1 = SpecialKeys.GAME_LEFT;
				break;
			case R.id.imageButton_right:
				key1 = SpecialKeys.GAME_RIGHT;
				break;
			case R.id.imageButton_leftup:
				key1 = SpecialKeys.GAME_LEFT;
				key2 = SpecialKeys.GAME_UP;
				break;
			case R.id.imageButton_leftdown:
				key1 = SpecialKeys.GAME_LEFT;
				key2 = SpecialKeys.GAME_DOWN;
				break;
			case R.id.imageButton_rightup:
				key1 = SpecialKeys.GAME_RIGHT;
				key2 = SpecialKeys.GAME_UP;
				break;
			case R.id.imageButton_rightdown:
				key1 = SpecialKeys.GAME_RIGHT;
				key2 = SpecialKeys.GAME_DOWN;
				break;
			case R.id.imageButton_A:
				key1 = SpecialKeys.GAME_A;
				break;
			case R.id.imageButton_B:
				key1 = SpecialKeys.GAME_B;
				break;
			case R.id.imageButton_C:
				key1 = SpecialKeys.GAME_C;
				break;
			case R.id.imageButton_D:
				key1 = SpecialKeys.GAME_D;
				break;
			case R.id.imageButton_leftmiddle:
				// TODO
				isMouse = true;

				if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
					KSApplication.Vibrate(GameControlActivity.this, KSApplication.VIBRATETIME);
					MouseRightDown();
				} else if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
					MouseRightUp();
				}

				break;
			case R.id.imageButton_rightMiddle:
				// TODO
				isMouse = true;
				if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
					onMouseMove(event);
				}
				if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
					KSApplication.Vibrate(GameControlActivity.this, KSApplication.VIBRATETIME);
					onMouseDown(event);

				}
				if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
					onMouseUp(event);
				}

				break;
			case R.id.imageButton_space:
				key1 = SpecialKeys.SPACE;
				break;
			case R.id.ImageButton_enter:
				key1 = SpecialKeys.ENTER;
				break;
			case R.id.imageButton_start:
				key1 = SpecialKeys.GAME_START;
				break;
			case R.id.imageButton_stop:
				key1 = SpecialKeys.GAME_STOP;
				break;
			default:
				break;

			}

			if (!isMouse) {
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					KSApplication.Vibrate(GameControlActivity.this,  KSApplication.VIBRATETIME);
					if (key1 != SpecialKeys.NONE)
						sendGameKeys(MessageType.KEY_DOWN, key1);
					if (key2 != SpecialKeys.NONE)
						sendGameKeys(MessageType.KEY_DOWN, key2);
					break;
				case MotionEvent.ACTION_UP:
					if (key1 != SpecialKeys.NONE)
						sendGameKeys(MessageType.KEY_UP, key1);
					if (key2 != SpecialKeys.NONE)
						sendGameKeys(MessageType.KEY_UP, key2);
					break;
				}
			}
			//normally,you should not return true,if you return true ,button selector is not work.you should let event gos next.
			return false;
		}

	}

	private void sendGameKeys(MessageType type, SpecialKeys key) {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(type);
		senPacket.setSplKeys(key);
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

	private void clickLeftMouse() {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_LEFT_CLICK);
		TcpNet.getInstance().sendMessage(senPacket);
	}

	private void onMouseMove(MotionEvent ev) {
		float x = ev.getX();
		mx = x - lx; // 当前鼠标位置 - 上次鼠标的位置
		lx = x; // 把当前鼠标的位置付给lx 以备下次使用
		float y = ev.getY();
		my = y - ly;
		ly = y;
		if (mx != 0 && my != 0)
			onMouseMove(-my, mx);

	}

	private void onMouseMove(float mx, float my) {

		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(MessageType.MOUSE_MOVE);
		senPacket.setIntValue1(-(int) mx);
		senPacket.setIntValue2(-(int) my);
		TcpNet.getInstance().sendMessage(senPacket);

	}

	private void onMouseDown(MotionEvent ev) {
		lx = ev.getX(); // 当手机第一放入时 把当前坐标付给lx
		ly = ev.getY();
		fx = ev.getX();
		fy = ev.getY();
	}

	private void onMouseUp(MotionEvent ev) {
		if (fx == ev.getX() && fy == ev.getY()) {// 单击左键
			clickLeftMouse();

		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		registerSensor();
		super.onStart();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		unregisterSensor();
		super.onStop();
	}

	void registerSensor() {
		Sensor sensor_accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(mySensorEventListener, sensor_accelerometer, SensorManager.SENSOR_DELAY_GAME);
	}

	void unregisterSensor() {
		sensorManager.unregisterListener(mySensorEventListener);
	}

	private boolean isup = false;
	private boolean isdown = false;
	private boolean isleft = false;
	private boolean isright = false;
	private String TAG = "HandActivity";

	private final class MySensorEventListener implements SensorEventListener {
		@Override
		public void onSensorChanged(SensorEvent event) {
			// 重力传感器
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				if (isopen) {
					float x = event.values[SensorManager.DATA_X];
					float y = event.values[SensorManager.DATA_Y];
					float z = event.values[SensorManager.DATA_Z];
					// tv_accelerometer是界面上的一个TextView标签，不再赘述
					Log.e(TAG, "Orientation:" + x + "," + y + "," + z);
					/**
					 * 手机竖着放
					 */
					if (z > 5)// 上
					{
						if (isup == false) {
							isup = true;
							sendGameKeys(MessageType.KEY_DOWN, SpecialKeys.GAME_UP);
						}
					}
					if (z < -5)// 下
					{
						if (isdown == false) {
							isdown = true;
							sendGameKeys(MessageType.KEY_DOWN, SpecialKeys.GAME_DOWN);
						}
					}

					if (z >= -5 && z <= 5)// 上下恢复
					{
						if (isup == true) {
							isup = false;
							sendGameKeys(MessageType.KEY_UP, SpecialKeys.GAME_UP);
						}
						if (isdown == true) {
							isdown = false;
							sendGameKeys(MessageType.KEY_UP, SpecialKeys.GAME_DOWN);
						}
					}

					if (y < -5)// 横屏左
					{
						if (isleft == false) {
							sendGameKeys(MessageType.KEY_DOWN, SpecialKeys.GAME_LEFT);
							isleft = true;
						}
					}
					if (y > 5)// 横屏右
					{
						if (isright == false) {
							isright = true;
							sendGameKeys(MessageType.KEY_DOWN, SpecialKeys.GAME_RIGHT);
						}
					}
					if (y >= -5 && y <= 5) {

						if (isleft == true) {
							isleft = false;
							sendGameKeys(MessageType.KEY_UP, SpecialKeys.GAME_LEFT);
						}
						if (isright == true) {
							isright = false;
							sendGameKeys(MessageType.KEY_UP, SpecialKeys.GAME_RIGHT);
						}

					}

				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	}
}
