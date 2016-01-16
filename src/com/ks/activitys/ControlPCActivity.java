package com.ks.activitys;

import java.util.Calendar;

import com.ks.application.KSApplication;
import com.ks.application.R;
import com.ks.net.TcpNet;
import com.ks.net.enums.MessageEnums.MessageType;
import com.ks.streamline.SendPacket;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class ControlPCActivity extends Activity {

	private ImageButton shutDown;
	private ImageButton reboot;
	private ImageButton logout;
	private ImageButton sleep;
	private ImageButton lockUp;
	private ImageButton taskManager;
	private ImageButton select;
	private ImageButton confirm;
	private EditText shutdownTime;

	private Calendar calender;
	private Calendar lastCalender;
	private long shutdowntime;
	private FunButtonOnClickListener funButtonOnClickListener;

	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controlpc);

		findViews();
		initFuns();

	}

	private void findViews() {

		shutDown = (ImageButton) findViewById(R.id.imageButton_shutdown);
		reboot = (ImageButton) findViewById(R.id.imageButton_reboot);
		logout = (ImageButton) findViewById(R.id.imageButton_logout);
		sleep = (ImageButton) findViewById(R.id.imageButton_sleep);
		lockUp = (ImageButton) findViewById(R.id.imageButton_lockwindows);
		taskManager = (ImageButton) findViewById(R.id.imageButton_taskmanager);

		select = (ImageButton) findViewById(R.id.imageButton_picktime);
		confirm = (ImageButton) findViewById(R.id.imageButton_confirm);
		shutdownTime = (EditText) findViewById(R.id.editText_shutdowntime);

		calender = Calendar.getInstance();
		lastCalender = Calendar.getInstance();

		shutdownTime.setText(KSApplication.controlpcinfo);
		if (KSApplication.isSure == false) {
			confirm.setImageResource(R.drawable.cancelshutdown);
		}
		funButtonOnClickListener = new FunButtonOnClickListener();
	}

	private void initFuns() {
		shutDown.setOnClickListener(funButtonOnClickListener);

		reboot.setOnClickListener(funButtonOnClickListener);

		logout.setOnClickListener(funButtonOnClickListener);

		sleep.setOnClickListener(funButtonOnClickListener);

		lockUp.setOnClickListener(funButtonOnClickListener);

		taskManager.setOnClickListener(funButtonOnClickListener);

		select.setOnClickListener(funButtonOnClickListener);

		confirm.setOnClickListener(funButtonOnClickListener);

	}

	private class FunButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			KSApplication.Vibrate(ControlPCActivity.this,KSApplication.VIBRATETIME);
			switch (v.getId()) {
			case R.id.imageButton_shutdown:
				sendFunMessage(MessageType.FUN_SHUTDOWN);
				break;
			case R.id.imageButton_reboot:
				sendFunMessage(MessageType.FUN_RESTART);
				break;
			case R.id.imageButton_lockwindows:
				sendFunMessage(MessageType.FUN_LOCK);
				break;
			case R.id.imageButton_logout:
				sendFunMessage(MessageType.FUN_LOGOUT);
				break;
			case R.id.imageButton_sleep:
				sendFunMessage(MessageType.FUN_SLEEP);
				break;
			case R.id.imageButton_taskmanager:
				sendFunMessage(MessageType.FUN_MANAGER);
				break;
			case R.id.imageButton_picktime:
				calender.setTimeInMillis(System.currentTimeMillis());
				int hour = calender.get(Calendar.HOUR_OF_DAY);
				int minute = calender.get(Calendar.MINUTE);
				new TimePickerDialog(ControlPCActivity.this, new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						lastCalender.setTimeInMillis(System.currentTimeMillis());
						lastCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
						lastCalender.set(Calendar.MINUTE, minute);
						lastCalender.set(Calendar.SECOND, 0);
						lastCalender.set(Calendar.MILLISECOND, 0);
						shutdownTime.setText("关机时机:" + lastCalender.get(Calendar.HOUR_OF_DAY) + ":"
								+ lastCalender.get(Calendar.MINUTE));
						KSApplication.controlpcinfo = shutdownTime.getText().toString();
						shutdowntime = (lastCalender.getTimeInMillis() - calender.getTimeInMillis()) / 1000;
					}

				}, hour, minute, true).show();
				break;
			case R.id.imageButton_confirm:
				if (KSApplication.isSure) {
					if (shutdowntime >= 0) {
						sendFunMessage(MessageType.FUN_SHUTDOWN_TIME, (int) shutdowntime);
						shutdownTime.setText("关机时机:" + lastCalender.get(Calendar.HOUR_OF_DAY) + ":"
								+ lastCalender.get(Calendar.MINUTE) + "  总秒数:" + shutdowntime);
						KSApplication.controlpcinfo = shutdownTime.getText().toString();
						confirm.setImageResource(R.drawable.cancelshutdown);
						KSApplication.isSure = false;
					} else {
						confirm.setImageResource(R.drawable.confirmshutdown);
						Toast.makeText(ControlPCActivity.this, "不能早于当前时间，秒数未负数了", Toast.LENGTH_SHORT).show();
						KSApplication.isSure = true;
					}
				} else {

					sendFunMessage(MessageType.FUN_SHUTDOWN_CANCEL);
					shutdownTime.setText("定时关机已取消");
					KSApplication.controlpcinfo = shutdownTime.getText().toString();
					confirm.setImageResource(R.drawable.confirmshutdown);
					KSApplication.isSure = true;
				}
				break;
			default:
				break;

			}

		}

	}

	private void sendFunMessage(MessageType type) {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(type);
		TcpNet.getInstance().sendMessage(senPacket);
	}

	private void sendFunMessage(MessageType type, int time) {
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(type);
		senPacket.setIntValue1(time);
		TcpNet.getInstance().sendMessage(senPacket);
	}

}
