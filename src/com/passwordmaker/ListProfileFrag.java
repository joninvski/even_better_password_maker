package com.passwordmaker;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.util.Log;
import android.app.FragmentTransaction;

public class ListProfileFrag extends ListFragment {

    private static final String TAG = "MyActivity";
    boolean mDualPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        String[] values = new String[] { "Enterprise", "Star Trek", "Next Generation", "Deep Space 9", "Voyager"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        // Check to see if we have a frame in which to embed the details
        //         // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.frag_update_detail);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        Log.v(TAG, "mDualPane= " + mDualPane);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.v(TAG, "Set Text = " + position);
        String item = (String) getListAdapter().getItem(position);
        DetailProfileFrag frag = (DetailProfileFrag) getFragmentManager().findFragmentById(R.id.frag_update_detail);
        if (frag != null && frag.isInLayout()) {
            frag.setText(getCapt(item));

        }

        if (mDualPane) {
            // We can display everything in-place with fragments.
            // Have the list highlight this item and show the data.
            getListView().setItemChecked(position, true);

            // Check what fragment is shown, replace if needed.
            DetailProfileFrag details = (DetailProfileFrag)
                getFragmentManager().findFragmentById(R.id.frag_update_detail);
            if (details == null) {
                Log.v(TAG, "Details is null");
                // Make new fragment to show this selection.
                details = DetailProfileFrag.newInstance(position);

                // Execute a transaction, replacing any existing
                // fragment with this one inside the frame.
                FragmentTransaction ft
                    = getFragmentManager().beginTransaction();
                ft.replace(R.id.frag_update_detail, details);
                ft.setTransition(
                        FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_profile, container, false);
        return view;
    }

    private String getCapt(String ship) {
        if (ship.toLowerCase().contains("enterprise")) {
            return "Johnathan Archer";
        }
        if (ship.toLowerCase().contains("star trek")) {
            return "James T. Kirk";
        }
        if (ship.toLowerCase().contains("next generation")) {
            return "Jean-Luc Picard";
        }
        if (ship.toLowerCase().contains("deep space 9")) {
            return "Benjamin Sisko";
        }
        if (ship.toLowerCase().contains("voyager")) {
            return "Kathryn Janeway";
        }
        return "???";
    }
}
