/*
 * Copyright 2010: K.-M. Hansche
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, which
 * you can find at http://www.opensource.org/licenses/gpl-3.0.html
 * 
 */

package com.pifactorial;

import org.daveware.passwordmaker.Account;
import org.daveware.passwordmaker.AlgorithmType;
import org.daveware.passwordmaker.CharacterSets;
import org.daveware.passwordmaker.LeetLevel;
import org.daveware.passwordmaker.LeetType;
import org.daveware.passwordmaker.PasswordMaker;
import org.daveware.passwordmaker.SecureCharArray;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;

public class EntryActivity extends Activity implements View.OnClickListener {

	private static final String TAG = "EvenBetterPassMaker";

	private Boolean visible;

	protected EditText etURL;
	protected EditText etMasterPass;
	protected TextView textOutputPass;

	/* TODO - Clean these buttons */
	protected Button btnGo;
	protected Button btnUpdate;
	protected Button btnCopy;
	protected CheckBox cbEmptyFields;

	/* What the hell is this? */
	protected Dialog dialog;
	protected PasswordMaker pwc;
	protected Account account;
	protected SecureCharArray master;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, "Creating Entry Activity");

		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String text = intent.getStringExtra(Intent.EXTRA_TEXT);
		setContentView(R.layout.main);

		textOutputPass = (TextView) findViewById(R.id.textResultPass);
		etMasterPass = (EditText) findViewById(R.id.etMasterPass);

		pwc = new PasswordMaker();

		visible = false;
		try {
			account = new Account("", "", "google.com", "",
					AlgorithmType.SHA256, false, true, 12,
					CharacterSets.ALPHANUMERIC, LeetType.NONE,
					LeetLevel.LEVEL1, "", "", "", false);
		} catch (Exception e) {

		}

		CookieSyncManager.createInstance(this);

		etURL = (EditText) findViewById(R.id.etURL);
		etMasterPass = (EditText) findViewById(R.id.etMasterPass);

		btnGo = (Button) findViewById(R.id.btnGo);
		btnGo.setOnClickListener(this);

		btnCopy = (Button) findViewById(R.id.btnShow);
		btnCopy.setOnClickListener(this);

		// cbEmptyFields = (CheckBox) findViewById(R.id.emptyFieldsCB);
		// findViewById(R.id.btnProfile).setOnClickListener(this);

		if (text != null) {
			etURL.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
			etMasterPass.requestFocus();
		}

		/*
		 * SharedPreferences settings = getPreferences(MODE_PRIVATE);
		 * cbEmptyFields.setChecked(settings.getBoolean("emptyFields", true));
		 */
	}

	public void onClick(View v) {

		switch (v.getId()) {
		/*
		 * case R.id.btnProfile: Log.i(TAG, "Button profile");
		 * 
		 * Intent myIntent = new Intent(EntryActivity.this,
		 * UpdateActivity.class); EntryActivity.this.startActivity(myIntent);
		 * break;
		 */
		/*
		 * case R.id.btnUpdate: try { if (etMasterPass.getText().length() > 0) {
		 * Log.i(TAG, "Got the password: " + etMasterPass.getText().toString());
		 * master = new SecureCharArray(etMasterPass.getText() .toString());
		 * SecureCharArray result = PasswordMaker.makePassword(master, account);
		 * textOutputPass.setText(new String(result.getData())); } } catch
		 * (Exception e) { }
		 * 
		 * break;
		 */
		case R.id.btnShow:
			Log.i(TAG, "Button copy");
			break;

		case R.id.btnGo:
			Log.i(TAG, "Button go");
			break;
		case R.id.textResultPass:

			Log.i(TAG, "Button output password");
			Log.i(TAG, "Current password: " + etMasterPass.getText().toString());
			account.setUrl(etURL.getText().toString());
			Log.i(TAG, "Current url: " + account.getUrl());

			// Toggle the visible variable
			visible = !visible;

			if (visible) {
				try {
					master = new SecureCharArray(etMasterPass.getText()
							.toString());
					SecureCharArray result = PasswordMaker.makePassword(master,
							account);
					textOutputPass.setText(new String(result.getData()));
				} catch (Exception e) {
				}
			} else {
				textOutputPass.setText("");
			}
			break;
		}
	}

	// private void copyPwToClipboard(String pw) {
	// ClipboardManager cb = (ClipboardManager)
	// getSystemService(CLIPBOARD_SERVICE);
	// cb.setText(pw);
	// Toast.makeText(this, R.string.copiedtoclip, Toast.LENGTH_SHORT).show();
	// }

	@Override
	protected void onResume() {
		CookieSyncManager.getInstance().startSync();
		super.onResume();
	}

	@Override
	protected void onPause() {
		CookieSyncManager.getInstance().startSync();
		super.onPause();
	}

	@Override
	protected void onStop() {
		/*
		 * SharedPreferences settings = getPreferences(MODE_PRIVATE);
		 * SharedPreferences.Editor editor = settings.edit();
		 * editor.putBoolean("emptyFields", cbEmptyFields.isChecked());
		 * editor.commit();
		 */	
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
}
