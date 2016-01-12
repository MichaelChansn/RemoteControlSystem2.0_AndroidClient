package com.ks.activitys;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.ks.net.NUMCODES.NETSTATE;
import com.ks.net.NetConnectThread;
import com.ks.net.TcpNet;
import com.ks.net.UDPScanThread;
import com.ks.net.enums.MessageEnums;
import com.ks.testndk.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class IndexActivity extends Activity {

	private Button btnScanServer;
	private Button btnConnectServer;
	private ListView lvServerList;
	private EditText edServerIP;
	private EditText edServerPort;
	private ProgressDialog dialog;
	private ArrayList<String> servers;
	private UDPScanThread udpScanThread;
	private IndexHandler handler;
	private TextView tvIndexTitle;
	private static TcpNet tcpNet;
	private static NetConnectThread netConnectThread;

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
		tvIndexTitle = (TextView) findViewById(R.id.textViewIndexTitle);
		servers = new ArrayList<String>();
		handler = new IndexHandler(IndexActivity.this);
		tcpNet=TcpNet.getInstance();

	}

	private void initFuns() {
		MyButtonListener btnListener = new MyButtonListener();
		btnScanServer.setOnClickListener(btnListener);
		btnConnectServer.setOnClickListener(btnListener);
		lvServerList.setOnItemClickListener(btnListener);
	}

	private class MyButtonListener implements OnClickListener, OnItemClickListener {

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
							if (servers.size() > 0) {
								String[] lists = new String[servers.size()];
								servers.toArray(lists);
								lvServerList.setAdapter(new ArrayAdapter<String>(IndexActivity.this,
										android.R.layout.simple_list_item_1, lists));
							} else {
								lvServerList.setAdapter(new ArrayAdapter<String>(IndexActivity.this,
										android.R.layout.simple_list_item_1,
										new String[] { MessageEnums.FINDNOSERVER }));
							}
							if (dialog != null)
								dialog.dismiss();
						}
						return false;
					}
				});
				servers.clear();
				udpScanThread = new UDPScanThread(servers, handler);
				udpScanThread.setDaemon(true);
				udpScanThread.start();

				break;
			case R.id.buttonConnectServer:
				String serverIP = edServerIP.getText().toString();
				String serverPort = edServerPort.getText().toString();
				if (serverIP != null && serverPort != null && serverIP.trim().length() > 0
						&& serverPort.trim().length() > 0) {
					int port = Integer.valueOf(serverPort);
					netConnectThread = new NetConnectThread(tcpNet, serverIP, port, handler);
					netConnectThread.setDaemon(true);
					netConnectThread.start();

				} else {
					
					new AlertDialog.Builder(IndexActivity.this).setIcon(android.R.drawable.stat_notify_error)
							.setTitle("Server IP Is Wrong!")
							.setMessage("Please Make Sure Server IP And Port is Right, Can Not Be Empty!")
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).create().show();
				}
				break;

			}
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			if (arg2 >= 0 && servers.size() > 0) {
				String listText = servers.get(arg2);
				if (listText.equals(MessageEnums.FINDNOSERVER))
					return;
				String[] serverInfos = listText.split(MessageEnums.UDPSEPARATOR);
				if (serverInfos != null && serverInfos.length == 2) {
					String serverName = serverInfos[0];
					String[] serverIPPort = serverInfos[1].split(":");
					if (serverIPPort != null && serverIPPort.length == 2) {
						String serverIP = serverIPPort[0];
						String serverPort = serverIPPort[1];
						tvIndexTitle.setText(serverName);
						edServerIP.setText(serverIP);
						edServerPort.setText(serverPort);
					}
				}
			}
		}

	}

	/** 自定义handler 采用弱引用，防止内存泄露 */
	public static class IndexHandler extends Handler {
		private WeakReference<Activity> mActivity;

		public IndexHandler(Activity activity) {
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
				if (((IndexActivity) theActivity).dialog != null) {
					((IndexActivity) theActivity).dialog.dismiss();
					((IndexActivity) theActivity).dialog = null;
					if (((IndexActivity) theActivity).servers.size() > 0) {
						String[] lists = new String[(((IndexActivity) theActivity).servers.size())];
						((IndexActivity) theActivity).servers.toArray(lists);
						((IndexActivity) theActivity).lvServerList.setAdapter(new ArrayAdapter<String>(
								((IndexActivity) theActivity), android.R.layout.simple_list_item_1, lists));
					} else {
						((IndexActivity) theActivity).lvServerList.setAdapter(new ArrayAdapter<String>(
								((IndexActivity) theActivity), android.R.layout.simple_list_item_1,
								new String[] { MessageEnums.FINDNOSERVER }));
					}
				}
				break;
			case CONNECTING:
				((IndexActivity) theActivity).dialog = ProgressDialog.show(theActivity, "正在连接", "请稍后...");
				((IndexActivity) theActivity).dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						// Cancel task.
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							netConnectThread.cancelConnecting();
							dialog.dismiss();

						}
						return false;
					}
				});
				break;
			case CONNECTOK:
				if (((IndexActivity) theActivity).dialog != null) {
					((IndexActivity) theActivity).dialog.dismiss();
					((IndexActivity) theActivity).dialog = null;
				}
				Toast.makeText(theActivity, "连接成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent((IndexActivity) theActivity,MainActivity.class);
				((IndexActivity) theActivity).startActivity(intent);
				((IndexActivity) theActivity).finish();
				break;
			case CONNECTFAILE:
				if (((IndexActivity) theActivity).dialog != null) {
					((IndexActivity) theActivity).dialog.dismiss();
					((IndexActivity) theActivity).dialog = null;
				}
				new AlertDialog.Builder(theActivity).setIcon(android.R.drawable.stat_notify_error).setTitle("连接失败！")
						.setMessage("请检查网络和IP地址是否输入错误！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();

							}
						}).create().show();
				break;
			case CONNECTTIMEOUT:
				if (((IndexActivity) theActivity).dialog != null) {
					((IndexActivity) theActivity).dialog.dismiss();
					((IndexActivity) theActivity).dialog = null;
				}
				new AlertDialog.Builder(theActivity).setIcon(android.R.drawable.stat_notify_error).setTitle("连接超时！")
						.setMessage("网络貌似不通畅，稍后再试！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();

							}
						}).create().show();
				break;
			case WRONGCODE:
				break;
			default:
				break;
			}
			super.handleMessage(msg);

		}
	}
}
