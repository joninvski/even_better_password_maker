package com.pifactorial;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import android.util.Log;

public class ListProfileFrag extends ListFragment {

    private static final String TAG = "MyActivity";
    boolean mDualPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v(TAG, "mDualPane= " + mDualPane);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.v(TAG, "Set Text = " + position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_profile, container, false);
        return view;
    }

}
