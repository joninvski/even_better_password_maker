package com.pifactorial;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;


public class DetailProfileFrag extends Fragment {

    private static final String TAG = "DetailProfileFrag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.v(TAG, "Detail Profile created");
    	super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "ViewCreated");
    	View view = inflater.inflate(R.layout.detail_profile, container, false);
        return view;
    }

    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "Activity");
    }

  
    public void setText(String item) {
        Log.v(TAG, "Set Text = " + item);
        TextView view = (TextView) getView().findViewById(R.id.profile_name);
        view.setText(item);
    }

    @Override
    public void onStop() {
    	super.onStop();
    };
    
    public int getShownIndex() {
        Log.v(TAG, "Get shown index = ");

    	return getArguments().getInt("index", 0);
    }

    public static DetailProfileFrag newInstance(int index) {
        Log.v(TAG, "New instance = ");

    	DetailProfileFrag f = new DetailProfileFrag();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }    
}
