/*
 * Copyright 2010: K.-M. Hansche
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, which
 * you can find at http://www.opensource.org/licenses/gpl-3.0.html
 * 
 */

package com.pifactorial;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.webkit.CookieSyncManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import org.daveware.passwordmaker.PasswordMaker;
import org.daveware.passwordmaker.Account;
import org.daveware.passwordmaker.AlgorithmType;
import org.daveware.passwordmaker.CharacterSets;
import org.daveware.passwordmaker.LeetType;
import org.daveware.passwordmaker.LeetLevel;
import org.daveware.passwordmaker.SecureCharArray;

public class PwMaker extends Activity implements View.OnClickListener {
	protected EditText edDomain;
	protected EditText edPW;
	protected Button btnGo;
	protected Button btnUpdate;
	protected Button btnCopy;
	protected CheckBox cbEmptyFields;
	protected TextView tvPW;
	protected Dialog dialog;
	protected PasswordMaker pwc;
	protected Account account;
	protected SecureCharArray master;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String text = intent.getStringExtra(Intent.EXTRA_TEXT);

		setContentView(R.layout.main);

		tvPW = (TextView) findViewById(R.id.textPW);
		edPW = (EditText) findViewById(R.id.editPW);

		pwc = new PasswordMaker();

		Log.i("PwMaker", "Here");
		Log.i("PwMaker", "edpPW: " + edPW.getText());

		try {
			account = new Account("", "", "google.com", "",
					AlgorithmType.SHA256, false, true, 12,
					CharacterSets.ALPHANUMERIC, LeetType.NONE,
					LeetLevel.LEVEL1, "", "", "", false);
		} catch (Exception e) {

		}

		CookieSyncManager.createInstance(this);

		edDomain = (EditText) findViewById(R.id.editDomain);
		edPW = (EditText) findViewById(R.id.editPW);
		btnGo = (Button) findViewById(R.id.btnGo);
		btnGo.setOnClickListener(this);
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnUpdate.setOnClickListener(this);
		btnCopy = (Button) findViewById(R.id.btnCopy);
		btnCopy.setOnClickListener(this);
		cbEmptyFields = (CheckBox) findViewById(R.id.emptyFieldsCB);
		findViewById(R.id.btnProfile).setOnClickListener(this);

		if (text != null) {
			edDomain.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
			edPW.requestFocus();
		}

		SharedPreferences settings = getPreferences(MODE_PRIVATE);
		cbEmptyFields.setChecked(settings.getBoolean("emptyFields", true));
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnProfile:
			Log.i("PwMaker", "Clicket the btnProfile");
			Intent myIntent = new Intent(PwMaker.this, PwUpdate.class);
			PwMaker.this.startActivity(myIntent);
			break;
		case R.id.btnUpdate:
			try {
				if (edPW.getText().length() > 0) {
					Log.i("PwMaker", "Got the password: "
							+ edPW.getText().toString());
					master = new SecureCharArray(edPW.getText().toString());
					SecureCharArray result = PasswordMaker.makePassword(master,
							account);
					tvPW.setText(new String(result.getData()));
				}
			} catch (Exception e) {
			}
			break;
		case R.id.btnCopy:
			break;
		case R.id.btnGo:
			Log.i("PwMaker", "Button go");
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
		SharedPreferences settings = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("emptyFields", cbEmptyFields.isChecked());
		editor.commit();

		super.onStop();
	}
}
