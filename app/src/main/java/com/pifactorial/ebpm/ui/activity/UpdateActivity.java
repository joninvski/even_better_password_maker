package com.pifactorial.ebpm.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.pifactorial.R;

public class UpdateActivity extends FragmentActivity {

    private static final String TAG = UpdateActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_update);
    }
}
