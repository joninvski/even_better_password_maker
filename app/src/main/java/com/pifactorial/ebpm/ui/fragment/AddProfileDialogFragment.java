package com.pifactorial.ebpm.ui.fragment;

import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import android.util.Log;

import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.pifactorial.ebpm.core.Constants;
import com.pifactorial.R;

import java.util.Locale;

public class AddProfileDialogFragment extends DialogFragment implements
    OnEditorActionListener {

    private EditText mEditText;
    private DialogFragment mAddProfileDialogFragment;

    public interface AddProfileDialogListener {
        void onFinishEditDialog(String inputText);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddProfileDialogFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_dialog_add_profile, container, false);
        mEditText = (EditText) v.findViewById(R.id.txt_profile_name);

        getDialog().setTitle(getString(R.string.new_profile));

        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        // Let's get the button and insert the callback
        final Button buttonOk = (Button) v.findViewById(R.id.ok_add_profile);
        buttonOk.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewProfileAndDismissDialog();
                mAddProfileDialogFragment.dismiss();
            }
        });

        final Button buttonCancel = (Button) v.findViewById(R.id.cancel_add_profile);
        buttonCancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddProfileDialogFragment.dismiss();
            }
        });

        return v;
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            Log.d(Constants.LOG, "Finished dialog");

            createNewProfileAndDismissDialog();
            this.dismiss();

            return true;
        }
        return false;
    }

    private void createNewProfileAndDismissDialog() {
        final FragmentManager fm = getFragmentManager();
        final DetailProfileFrag fragmentToCallback = (DetailProfileFrag) fm.findFragmentById(R.id.frag_update_detail);
        final String profileName = mEditText.getText().toString();
        fragmentToCallback.onFinishEditDialog(profileName);

        final String msg = String.format(Locale.US, "%s %s", profileName, getString(R.string.toast_profile_created));
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

        //Lets close the alert box
        this.dismiss();
    }
}
