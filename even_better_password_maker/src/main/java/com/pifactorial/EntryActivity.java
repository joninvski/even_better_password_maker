/*
 * Copyright 2010: K.-M. Hansche
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, which
 * you can find at http://www.opensource.org/licenses/gpl-3.0.html
 * 
 */

package com.pifactorial;

import java.util.List;

import org.daveware.passwordmaker.Account;
import org.daveware.passwordmaker.AlgorithmType;
import org.daveware.passwordmaker.CharacterSets;
import org.daveware.passwordmaker.LeetLevel;
import org.daveware.passwordmaker.LeetType;
import org.daveware.passwordmaker.PasswordMaker;
import org.daveware.passwordmaker.SecureCharArray;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class EntryActivity extends Activity implements View.OnClickListener {

	private static final String TAG = "EvenBetterPassMaker";

	private ProfileDataSource datasource;
	
	private Boolean visible;

	protected EditText etURL;
	protected EditText etMasterPass;
	protected TextView textOutputPass;

	/* TODO - Clean these buttons */
	protected Item btnGo;
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
		setContentView(R.layout.main);
		Log.i(TAG, "Creating Entry Activity2");

		textOutputPass = (TextView) findViewById(R.id.textResultPass);
		etMasterPass = (EditText) findViewById(R.id.etMasterPass);

		pwc = new PasswordMaker();

		visible = false;

		datasource = new ProfileDataSource(this);
		datasource.open();

		Log.i(TAG, "Creating Entry Activity2.5");
		Cursor cursor = datasource.getAllCommentsCursor();
		
		for(String s : cursor.getColumnNames()){
			Log.i(TAG, s);			
		}
		SimpleCursorAdapter adapter =
				  new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor, new String[]{"name"}, new int[] {android.R.id.text1}, 0);
				
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		Log.i(TAG, "Creating Entry Activity2.7");

		/*
		try {
			account = new Account("", "", "google.com", "",
					AlgorithmType.SHA256, false, true, 12,
					CharacterSets.ALPHANUMERIC, LeetType.NONE,
					LeetLevel.LEVEL1, "", "", "", false);
		} catch (Exception e) {

		}
		Log.i(TAG, "Creating Entry Activity3");

		this.getActionBar().setDisplayShowTitleEnabled(false);

		
		Log.i(TAG, "Creating Entry Activity4");
		etURL = (EditText) findViewById(R.id.etURL);
		etMasterPass = (EditText) findViewById(R.id.etMasterPass);

		Log.i(TAG, "Creating Entry Activity6");
		if (text != null) {
			etURL.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
			etMasterPass.requestFocus();
		}

		Log.i(TAG, "Creating Entry Activity7");
		 */
	}

	public void onClick(View v) {

		switch (v.getId()) {

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

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.actionBtnGo:
			Log.i(TAG, "Button go");
			datasource.createAccount("HONAS " + Integer.toString(datasource.getAllComments().size()), "MD5");
			break;
			
		case R.id.actionBtnCopy:
			Log.i(TAG, "Clicked item Copy");
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("Copied Text",
					textOutputPass.getText());
			clipboard.setPrimaryClip(clip);
			break;
		case R.id.actionBtnAbout:
			Log.i(TAG, "Clicked item 4");
			break;
		case R.id.actionBtnProfiles:
			Log.i(TAG, "Clicked item 4");

			Intent myIntent = new Intent(EntryActivity.this,
					UpdateActivity.class);
			EntryActivity.this.startActivity(myIntent);
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "on Pausing");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "on Stopping");
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
