package com.ks.activitys;

import com.ks.testndk.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				  Intent intent=new Intent();
				 intent.setClass(WelcomeActivity.this, IndexActivity.class);
				 WelcomeActivity.this.startActivity(intent);
				 WelcomeActivity.this.finish();
				 WelcomeActivity.this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			}
		}, 1000);
	}

}
