package com.ks.testndk;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btn;
	private EditText edNum1;
	private EditText edNum2;
	private EditText edNum3;
	private TextView tvHello;
	private JNITest jNITest;
	private ImageView ivShow;
	private Matrix matrix=new Matrix();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		inits();
	}
	private void findViews()
	{
		btn=(Button) findViewById(R.id.buttonJNITest);
		edNum1=(EditText) findViewById(R.id.editTextNum1);
		edNum2=(EditText) findViewById(R.id.editTextNum2);
		edNum3=(EditText) findViewById(R.id.editTextNum3);
		tvHello=(TextView) findViewById(R.id.textViewHello);
		ivShow=(ImageView)findViewById(R.id.imageViewShow);
		jNITest=new JNITest();
	}
	private void inits()
	{
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int result=jNITest.add(Integer.valueOf(edNum1.getText().toString()).intValue(),Integer.valueOf(edNum2.getText().toString()).intValue());
				edNum3.setText(result+"");
				tvHello.setText(jNITest.getHello());
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
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		jNITest.release();
	}
}

