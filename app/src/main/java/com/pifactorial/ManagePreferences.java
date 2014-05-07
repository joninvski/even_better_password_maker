package com.pifactorial;

import android.annotation.TargetApi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.Build;

public class ManagePreferences {

    private SharedPreferences mPrefs;
    private Context c;
    private final int sdk_version = android.os.Build.VERSION.SDK_INT;

    public ManagePreferences(Context context) {
        c = context;
        mPrefs = c.getSharedPreferences(c.getString(R.string.SharedPreferencesName), Context.MODE_PRIVATE);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void setLastSelectedProfile(int last_selected) {
        final Editor editor = mPrefs.edit();
        editor.putInt(c.getString(R.string.LastSelectedProfile), last_selected);
        if(sdk_version < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void setLastURL(String lastUrl) {
        final Editor editor = mPrefs.edit();
        editor.putString("LastURL", lastUrl);
        if(sdk_version < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public int getLastSelectedProfile() {
        final int last_profile_selected = mPrefs.getInt(c.getString(R.string.LastSelectedProfile), 0);
        return last_profile_selected;
    }

    public String getLastURL() {
        final String lastURL = mPrefs.getString("LastURL", c.getString(R.string.http));
        return lastURL;
    }
}
