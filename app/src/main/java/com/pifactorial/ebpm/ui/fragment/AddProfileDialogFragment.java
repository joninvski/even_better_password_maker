package com.pifactorial.ebpm.ui.fragment;

import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

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

import butterknife.ButterKnife;

import butterknife.InjectView;

import butterknife.OnClick;

import com.pifactorial.R;

import java.util.Locale;

import timber.log.Timber;

public class AddProfileDialogFragment extends DialogFragment implements
    OnEditorActionListener {

    @InjectView(R.id.txt_profile_name) EditText mEditText;
    @InjectView(R.id.ok_add_profile) Button buttonOk;

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
        final View v = inflater.inflate(R.layout.frag_add_profile_dialog, container, false);
        ButterKnife.inject(this, v);

        getDialog().setTitle(getString(R.string.new_profile));

        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        return v;
    }

    @OnClick(R.id.ok_add_profile)
    public void onClickOk() {
        createNewProfileAndDismissDialog();
        mAddProfileDialogFragment.dismiss();
    }

    @OnClick(R.id.cancel_add_profile)
    public void onClickCancel() {
        mAddProfileDialogFragment.dismiss();
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            Timber.d("Finished dialog");

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
