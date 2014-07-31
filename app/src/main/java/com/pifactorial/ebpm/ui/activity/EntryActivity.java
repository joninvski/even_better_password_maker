package com.pifactorial.ebpm.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.pifactorial.ebpm.ui.fragment.MainFragment;
import com.pifactorial.R;

import java.security.Security;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import timber.log.Timber;

public class EntryActivity extends ActionBarActivity {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private int sdk_version;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_entry);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, MainFragment.newInstance()).commit();

        sdk_version = android.os.Build.VERSION.SDK_INT;
    }

    @Override
    protected void onStop() {
        Timber.d("on Stopping");
        super.onStop();
    }
}

