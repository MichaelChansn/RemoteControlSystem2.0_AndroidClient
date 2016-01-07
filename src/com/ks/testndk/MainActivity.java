package com.ks.testndk;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.ks.net.NUMCODES.NETSTATE;
import com.ks.net.NetConnectThread;
import com.ks.net.TcpNet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btnTestJNI;
	private Button btnConnect;
	private EditText edIP;
	private EditText edPort;
	private JNITest jNITest;
	private ImageView ivShow;
	private Matrix matrix=new Matrix();
	private TcpNet tcpNet;
	private MyHandler handler;
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		inits();
	}
	private void findViews()
	{
		btnTestJNI=(Button) findViewById(R.id.buttonJNITest);
		btnConnect=(Button) findViewById(R.id.buttonConnect);
		edIP=(EditText) findViewById(R.id.editTextIP);
		edPort=(EditText) findViewById(R.id.editTextPort);
		ivShow=(ImageView)findViewById(R.id.imageViewShow);
		jNITest=new JNITest();
		handler=new MyHandler(this);
	}
	private void inits()
	{
		btnTestJNI.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bitmap btm1=Bitmap.createBitmap(1920,1080,Config.ARGB_8888);
				btm1.eraseColor(Color.GREEN);
				Bitmap btm2=Bitmap.createBitmap(1920, 1080, Config.ARGB_8888);
				btm2.eraseColor(Color.BLUE);
				List<Rect> res=new ArrayList<Rect>();
				for(int i=0;i<1000;i++)
				{
					Rect r=new Rect(i, i, 30+i,30+i);
					res.add(r);
				}
				long start=System.nanoTime();
				jNITest.getBitmapOrlBtm(res, btm1, btm2);
				//Bitmap btm3=Bitmap.createBitmap(1920,1080,Config.ARGB_8888);//这句话耗时30ms
				//Bitmap btm3=Bitmap.createBitmap(btm1);//这句话耗时30ms
				
				matrix.setScale((float)1.0,(float) 1.0);
				ivShow.setImageMatrix(matrix);
				ivShow.setImageBitmap(btm1);
				/**冲突测试,结果正常，不会产生冲突
				btm2.eraseColor(Color.CYAN);
			    jNITest.getBitmapOrlBtm(res, btm1, btm2);
				matrix.setScale((float)1.0,(float) 1.0);
				ivShow.setImageMatrix(matrix);
				ivShow.setImageBitmap(btm1);
				OutputStream op=new DataOutputStream(System.out);
				btm1.compress(CompressFormat.JPEG, 100, op);
				*/
				long stop=System.nanoTime();
				/*
				Bitmap btm1=Bitmap.createBitmap(1920,1080,Config.ARGB_8888);
				btm1.eraseColor(Color.GREEN);
				Bitmap btm2=Bitmap.createBitmap(1920, 1080, Config.ARGB_8888);
				btm2.eraseColor(Color.BLUE);
				Canvas cv=new Canvas(btm1);
				long start=System.nanoTime();
				for(int i=0;i<1000;i++)
				{
					cv.drawBitmap(btm2, new Rect(i, i, i+30,i+30), new Rect(i, i, i+80,i+80), null);
				}
				cv.save(Canvas.ALL_SAVE_FLAG);
				cv.restore();
				long stop=System.nanoTime();
				ivShow.setImageBitmap(btm1);*/
				
				Toast.makeText(getApplication(), "time:"+(stop-start)/1000000+"ms", Toast.LENGTH_SHORT).show();
			}
		});
		btnConnect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
				tcpNet=TcpNet.getInstance();
				String IP=edIP.getText().toString();
				int port=Integer.valueOf(edPort.getText().toString());
				new NetConnectThread(tcpNet, IP, port, handler);
				}
				catch(RuntimeException e)
				{
					
				}
			}
		});
		
	}

	
	
	/**自定义handler 采用弱引用，防止内存泄露*/
	static class MyHandler extends Handler {  
        WeakReference<Activity> mActivity;  
        
        MyHandler(Activity activity) {  
                mActivity = new WeakReference<Activity>(activity);  
        }  

        @Override
		public void handleMessage(Message msg) 
		{
			// TODO Auto-generated method stub
        	Activity theActivity = mActivity.get(); // 是否还存在
        	if(theActivity==null)
        		return;
			switch(NETSTATE.getNETSTATEByValue(msg.what))
			{
			case CONNECTING:
				((MainActivity)theActivity).dialog=ProgressDialog.show(theActivity, "正在连接", "请稍后...");
				((MainActivity)theActivity).dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
		            
		            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		                // TODO Auto-generated method stub
		                // Cancel task.
		                if (keyCode == KeyEvent.KEYCODE_BACK) {
		                	dialog.dismiss();
		                	
		                }
		                return false;
		            }
		        });
				break;
			case CONNECTOK:
				if(((MainActivity)theActivity).dialog!=null)
				{
					((MainActivity)theActivity).dialog.dismiss();
					((MainActivity)theActivity).dialog=null;
				}
				Toast.makeText(theActivity, "连接成功", Toast.LENGTH_SHORT).show();
				break;
			case CONNECTFAILE:
				new AlertDialog
				 .Builder(theActivity)
				 .setIcon(android.R.drawable.stat_notify_error)
				 .setTitle("连接错误！")
				 .setMessage("请检查网络和IP地址是否输入错误！")
				 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					
					}
				}).create().show();
				break;
			case WRONGCODE:
				break;
			}
			super.handleMessage(msg);
			
		}
}  

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		jNITest.release();
	}
}

