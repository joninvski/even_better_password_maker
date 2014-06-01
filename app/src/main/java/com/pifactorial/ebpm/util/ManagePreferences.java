package com.pifactorial.ebpm.util;

import android.annotation.TargetApi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.pifactorial.ebpm.core.Constants;

import android.os.Build;

public class ManagePreferences {

    private SharedPreferences mPrefs;
    private Context c;
    private final int sdk_version = android.os.Build.VERSION.SDK_INT;

    public ManagePreferences(Context context) {
        c = context;
        mPrefs = c.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setLastSelectedProfile(int last_selected) {
        final Editor editor = mPrefs.edit();
        editor.putInt(Constants.LAST_SELECTED_PROFILE, last_selected);
        if(sdk_version < Build.VERSION_CODES.GINGERBREAD) {
            commit(editor);
        } else {
            apply(editor);
        }
    }

    public void setLastURL(String lastUrl) {
        final Editor editor = mPrefs.edit();
        editor.putString(Constants.LAST_URL, lastUrl);
        if(sdk_version < Build.VERSION_CODES.GINGERBREAD) {
            commit(editor);
        } else {
            apply(editor);
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private void commit(Editor e) {
        e.commit();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void apply(Editor e) {
        e.apply();
    }

    public int getLastSelectedProfile() {
        final int last_profile_selected = mPrefs.getInt(Constants.LAST_SELECTED_PROFILE, 0);
        return last_profile_selected;
    }

    public String getLastURL() {
        final String lastURL = mPrefs.getString(Constants.LAST_URL, "http://");
        return lastURL;
    }
}
