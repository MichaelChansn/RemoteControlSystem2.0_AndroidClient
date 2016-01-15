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
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class ControlPCActivity extends Activity {

	private ImageButton guanji;
	private ImageButton chongqi;
	private ImageButton zhuxiao;
	private ImageButton daiji;
	private ImageButton suoding;
	private ImageButton renwu;
	private ImageButton xuanze;
	private ImageButton queding;
	private EditText shijian;

	private Calendar c;
	private Calendar last;
	private long shutdowntime;

	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controlpc);
		this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);// 屏蔽Home键

		guanji = (ImageButton) findViewById(R.id.imageButton_guanji);
		chongqi = (ImageButton) findViewById(R.id.imageButton_chongqi);
		zhuxiao = (ImageButton) findViewById(R.id.imageButton_zhuxiao);
		daiji = (ImageButton) findViewById(R.id.imageButton_daiji);
		suoding = (ImageButton) findViewById(R.id.imageButton_suoding);
		renwu = (ImageButton) findViewById(R.id.imageButton_renwuguanli);

		xuanze = (ImageButton) findViewById(R.id.imageButton_xuanzeshijian);
		queding = (ImageButton) findViewById(R.id.imageButton_quedingguanji);
		shijian = (EditText) findViewById(R.id.editText_guanjishike);

		c = Calendar.getInstance();
		last = Calendar.getInstance();

		shijian.setText(KSApplication.controlpcinfo);

		if (KSApplication.issure == false) {
			queding.setImageResource(R.drawable.quxiaoguanjiweian);
		}

		guanji.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					guanji.setImageResource(R.drawable.guanjianxia);
					KSApplication.Vibrate(ControlPCActivity.this, KSApplication.VIBRATETIME);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					sendFunMessage(MessageType.FUN_SHUTDOWN);
					guanji.setImageResource(R.drawable.guanjiweian);

				}
				return false;
			}
		});

		chongqi.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					chongqi.setImageResource(R.drawable.chongqianxia);
					KSApplication.Vibrate(ControlPCActivity.this, KSApplication.VIBRATETIME);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					sendFunMessage(MessageType.FUN_RESTART);
					chongqi.setImageResource(R.drawable.chongqiweian);

				}
				return false;
			}
		});

		zhuxiao.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					zhuxiao.setImageResource(R.drawable.zhuxiaoanxia);
					KSApplication.Vibrate(ControlPCActivity.this, KSApplication.VIBRATETIME);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					sendFunMessage(MessageType.FUN_LOGOUT);
					zhuxiao.setImageResource(R.drawable.zhuxiaoweian);

				}
				return false;
			}
		});

		daiji.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					daiji.setImageResource(R.drawable.daijianxia);
					KSApplication.Vibrate(ControlPCActivity.this, KSApplication.VIBRATETIME);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					sendFunMessage(MessageType.FUN_SLEEP);
					daiji.setImageResource(R.drawable.daijiweian);

				}
				return false;
			}
		});

		suoding.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					suoding.setImageResource(R.drawable.suodinganxia);
					KSApplication.Vibrate(ControlPCActivity.this, KSApplication.VIBRATETIME);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					sendFunMessage(MessageType.FUN_LOCK);
					suoding.setImageResource(R.drawable.suodingweian);

				}
				return false;
			}
		});

		renwu.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.performClick();
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					renwu.setImageResource(R.drawable.renwuguanlianxia);
					KSApplication.Vibrate(ControlPCActivity.this, KSApplication.VIBRATETIME);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					sendFunMessage(MessageType.FUN_MANAGER);
					renwu.setImageResource(R.drawable.renwuguanliweian);

				}
				return false;
			}
		});

		xuanze.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					xuanze.setImageResource(R.drawable.xuanzeshijiananxia);
					KSApplication.Vibrate(ControlPCActivity.this, KSApplication.VIBRATETIME);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					// sendMessage("FUN+MANGER+OK+3");
					xuanze.setImageResource(R.drawable.xuanzeshijianweian);

					c.setTimeInMillis(System.currentTimeMillis());

					int hour = c.get(Calendar.HOUR_OF_DAY);

					int minute = c.get(Calendar.MINUTE);

					new TimePickerDialog(ControlPCActivity.this, new OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute)

						{

							last.setTimeInMillis(System.currentTimeMillis());

							last.set(Calendar.HOUR_OF_DAY, hourOfDay);

							last.set(Calendar.MINUTE, minute);

							last.set(Calendar.SECOND, 0);

							last.set(Calendar.MILLISECOND, 0);

							shijian.setText("关机时机:" + last.get(Calendar.HOUR_OF_DAY) + ":" + last.get(Calendar.MINUTE));
							KSApplication.controlpcinfo = shijian.getText().toString();
							shutdowntime = (last.getTimeInMillis() - c.getTimeInMillis()) / 1000;

						}

					}, hour, minute, true).show();

				}
				return false;
			}
		});

		queding.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (KSApplication.issure) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						queding.setImageResource(R.drawable.quedingguanjianxia);
						KSApplication.Vibrate(ControlPCActivity.this, KSApplication.VIBRATETIME);
					}
					if (event.getAction() == MotionEvent.ACTION_UP) {
						// sendMessage("FUN+MANGER+OK+3");
						if (shutdowntime >= 0) {
							queding.setImageResource(R.drawable.quedingguangjiweian);
							sendFunMessage(MessageType.FUN_SHUTDOWN_TIME,(int)shutdowntime);
							shijian.setText("关机时机:" + last.get(Calendar.HOUR_OF_DAY) + ":" + last.get(Calendar.MINUTE)
									+ "  总秒数:" + shutdowntime);
							KSApplication.controlpcinfo = shijian.getText().toString();
							queding.setImageResource(R.drawable.quxiaoguanjiweian);
							KSApplication.issure = false;
						} else {
							queding.setImageResource(R.drawable.quedingguangjiweian);
							Toast.makeText(ControlPCActivity.this, "不能早于当前时间，秒数未负数了", Toast.LENGTH_SHORT).show();
							KSApplication.issure = true;
						}
					}
				} else {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						queding.setImageResource(R.drawable.quxiaoguanjianxia);
						KSApplication.Vibrate(ControlPCActivity.this, 100);
					}
					if (event.getAction() == MotionEvent.ACTION_UP) {
						// sendMessage("FUN+MANGER+OK+3");

						queding.setImageResource(R.drawable.quxiaoguanjiweian);
						sendFunMessage(MessageType.FUN_SHUTDOWN_CANCEL);
						shijian.setText("定时关机已取消");
						KSApplication.controlpcinfo = shijian.getText().toString();
						queding.setImageResource(R.drawable.quedingguangjiweian);
						KSApplication.issure = true;

					}
				}
				return false;
			}
		});

	}

	private void sendFunMessage(MessageType type)
	{
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(type);
		TcpNet.getInstance().sendMessage(senPacket);
	}
	private void sendFunMessage(MessageType type,int time)
	{
		SendPacket senPacket = new SendPacket();
		senPacket.setMsgType(type);
		senPacket.setIntValue1(time);
		TcpNet.getInstance().sendMessage(senPacket);
	}

}
