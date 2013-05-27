package com.pifactorial;

import android.app.DialogFragment;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AddProfileDialogFragment extends DialogFragment {
    int mNum;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static AddProfileDialogFragment newInstance(int num) {
        AddProfileDialogFragment f = new AddProfileDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_add_profile, container, false);
        // View tv = v.findViewById(R.id.text);
        // ((TextView) tv).setText("Dialog #" + mNum + ": using style " + getNameForNum(mNum));

        // // Watch for button clicks.
        // Button button = (Button)v.findViewById(R.id.show);
        // button.setOnClickListener(new OnClickListener() {
        //     public void onClick(View v) {
        //         // When button is clicked, call up to owning activity.
        //         ((FragmentDialog)getActivity()).showDialog();
        //     }
        // });

        return v;
    }
}
