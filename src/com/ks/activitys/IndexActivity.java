package com.ks.activitys;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.ks.net.NUMCODES.NETSTATE;
import com.ks.net.UDPScanThread;
import com.ks.testndk.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class IndexActivity extends Activity {

	private Button btnScanServer;
	private Button btnConnectServer;
	private ListView lvServerList;
	private EditText edServerIP;
	private EditText edServerPort;
	private ProgressDialog dialog;
	private ArrayList<String> servers;
	private UDPScanThread udpScanThread;
	private MyHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_index);
		findViews();
		initFuns();

	}

	private void findViews() {
		btnScanServer = (Button) findViewById(R.id.buttonScanServer);
		btnConnectServer = (Button) findViewById(R.id.buttonConnectServer);
		lvServerList = (ListView) findViewById(R.id.listViewServerList);
		edServerIP = (EditText) findViewById(R.id.editTextServerIP);
		edServerPort = (EditText) findViewById(R.id.editTextServerPort);
		servers = new ArrayList<String>();
		handler = new MyHandler(IndexActivity.this);
		udpScanThread = new UDPScanThread(servers, handler);
	}

	private void initFuns() {
		MyButtonListener btnListener = new MyButtonListener();
		btnScanServer.setOnClickListener(btnListener);
		btnConnectServer.setOnClickListener(btnListener);
	}

	private class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.buttonScanServer:
				dialog = ProgressDialog.show(IndexActivity.this, "正在扫描", "请稍后...");
				dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							udpScanThread.stopUDP();
							lvServerList.setAdapter(new ArrayAdapter<String>(IndexActivity.this,
									android.R.layout.simple_list_item_1, (String[]) servers.toArray()));
							dialog.dismiss();
						}
						return false;
					}
				});
				udpScanThread.start();

				break;
			case R.id.buttonConnectServer:
				break;

			}
		}

	}

	/** 自定义handler 采用弱引用，防止内存泄露 */
	public static class MyHandler extends Handler {
		private WeakReference<Activity> mActivity;

		public MyHandler(Activity activity) {
			mActivity = new WeakReference<Activity>(activity);
		}

		public Activity getContext() {
			return mActivity.get();
		}

		@Override
		public void handleMessage(Message msg) {
			Activity theActivity = mActivity.get(); // 是否还存在
			if (theActivity == null)
				return;
			switch (NETSTATE.getNETSTATEByValue(msg.what)) {
			case UDPSCANOK:
				if(((IndexActivity)theActivity).dialog!=null)
				{
					((IndexActivity)theActivity).dialog.dismiss();
					((IndexActivity)theActivity).dialog=null;
					((IndexActivity)theActivity).lvServerList.setAdapter(new ArrayAdapter<String>(((IndexActivity)theActivity),
							android.R.layout.simple_list_item_1, (String[]) ((IndexActivity)theActivity).servers.toArray()));
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);

		}
	}
}
