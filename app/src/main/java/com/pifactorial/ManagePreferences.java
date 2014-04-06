package com.pifactorial;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ManagePreferences {

    private SharedPreferences mPrefs;
    private Context c;

    public ManagePreferences(Context context) {
        c = context;
        mPrefs = c.getSharedPreferences(c.getString(R.string.SharedPreferencesName), Context.MODE_PRIVATE);
    }

    public void setLastSelectedProfile(int last_selected) {
        final Editor editor = mPrefs.edit();
        editor.putInt(c.getString(R.string.LastSelectedProfile), last_selected);
        editor.apply(); // TODO - Check the return value
    }

    public void setLastURL(String lastUrl) {
        final Editor editor = mPrefs.edit();
        editor.putString("LastURL", lastUrl);
        editor.apply(); // TODO - Check the return value
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
