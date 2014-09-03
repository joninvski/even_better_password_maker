package com.pifactorial.ebpm.util;

import android.annotation.TargetApi;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.os.Build;

public class ManagePreferences {

    private static final String LAST_SELECTED_PROFILE = "LAST_PROFILE";
    private static final String LAST_URL = "LAST_URL";
    private static final String MASTER_PASS = "MASTER_PASS";
    private static final String SHARED_PREFERENCES_NAME = "com.pifactorial.com.config";

    private SharedPreferences mPrefs;
    private Context c;
    private final int sdk_version = android.os.Build.VERSION.SDK_INT;

    public ManagePreferences(Context context) {
        c = context;
        mPrefs = c.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setLastSelectedProfile(int last_selected) {
        final Editor editor = mPrefs.edit();
        editor.putInt(LAST_SELECTED_PROFILE, last_selected);
        if(sdk_version < Build.VERSION_CODES.GINGERBREAD) {
            commit(editor);
        } else {
            apply(editor);
        }
    }

    public void setMasterPass(String master_password) {
        final Editor editor = mPrefs.edit();
        editor.putString(MASTER_PASS, master_password);
        if(sdk_version < Build.VERSION_CODES.GINGERBREAD) {
            commit(editor);
        } else {
            apply(editor);
        }
    }

    public void eraseMasterPass() {
        final Editor editor = mPrefs.edit();
        editor.putString(MASTER_PASS, "");
        if(sdk_version < Build.VERSION_CODES.GINGERBREAD) {
            commit(editor);
        } else {
            apply(editor);
        }
    }

    public void setLastURL(String lastUrl) {
        final Editor editor = mPrefs.edit();
        editor.putString(LAST_URL, lastUrl);
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
        final int last_profile_selected = mPrefs.getInt(LAST_SELECTED_PROFILE, 0);
        return last_profile_selected;
    }

    public String getLastURL() {
        final String lastURL = mPrefs.getString(LAST_URL, "http://");
        return lastURL;
    }

    public String getMasterPassword() {
        final String masterPass = mPrefs.getString(MASTER_PASS, "");
        return masterPass;
    }
}
