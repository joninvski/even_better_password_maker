package com.pifactorial;

import android.util.Log;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class UpdateActivity extends Activity {

    private static final String TAG = UpdateActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
	}

}
