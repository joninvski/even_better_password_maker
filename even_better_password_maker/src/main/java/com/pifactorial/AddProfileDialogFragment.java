package com.pifactorial;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class AddProfileDialogFragment extends DialogFragment implements
		OnEditorActionListener, View.OnClickListener {

	private static final String TAG = DetailProfileFrag.class.getName();

	private EditText mEditText;

	public interface AddProfileDialogListener {
		void onFinishEditDialog(String inputText);
	}

	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an
	 * argument.
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
		View v = inflater.inflate(R.layout.fragment_dialog_add_profile,
				container, false);
		mEditText = (EditText) v.findViewById(R.id.txt_profile_name);

		getDialog().setTitle("New Profile");

		// Show soft keyboard automatically
		mEditText.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		mEditText.setOnEditorActionListener(this);

		// Let's get the button and insert the callback
		Button button = (Button) v.findViewById(R.id.button1);
		button.setOnClickListener(this);
		

		return v;
	}

	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (EditorInfo.IME_ACTION_DONE == actionId) {
			// this.dismiss();
			Log.i(TAG, "Finished dialog");

            createNewProfileAndDismissDialog();
			this.dismiss();

			return true;
		}
		return false;
	}

	public void onClick(View v) {
		Log.i(TAG, "Button clicked with text ");
        createNewProfileAndDismissDialog();
		this.dismiss();
	}

	private void createNewProfileAndDismissDialog()
	{
		FragmentManager fm = getFragmentManager();
		DetailProfileFrag fragmentToCallback = (DetailProfileFrag) fm
				.findFragmentById(R.id.frag_update_detail);
		fragmentToCallback.onFinishEditDialog(mEditText.getText()
				.toString());
        this.dismiss();


	}
}
