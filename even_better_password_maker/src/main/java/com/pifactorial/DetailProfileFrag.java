package com.pifactorial;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;


public class DetailProfileFrag extends Fragment {

    private static final String TAG = "MyActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "Simple create");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "Activity");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_profile, container, false);
        Log.v(TAG, "ViewCreated");
        return view;
    }

    public void setText(String item) {
        Log.v(TAG, "Set Text = " + item);
        TextView view = (TextView) getView().findViewById(R.id.profile_name);
        view.setText(item);

    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    public static DetailProfileFrag newInstance(int index) {
        DetailProfileFrag f = new DetailProfileFrag();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }
}
