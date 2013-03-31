package com.pifactorial;

import org.daveware.passwordmaker.Profile;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "Creating Entry Activity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Log.i(TAG, "View created");

		// Let's get the window controls
		textOutputPass = (TextView) findViewById(R.id.textResultPass);
		etMasterPass = (EditText) findViewById(R.id.etMasterPass);

		visible = false;

		datasource = new ProfileDataSource(this);
		datasource.open();

		Log.i(TAG, "Creating Entry Activity2.5");
		Cursor cursor = datasource.getAllProfilesCursor();

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor, 
                new String[] { ProfileSqLiteHelper.COLUMN_NAME }, new int[] { android.R.id.text1 }, 0);

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		Log.i(TAG, "Creating Entry Activity2.7");

		this.getActionBar().setDisplayShowTitleEnabled(false);

		Log.i(TAG, "Creating Entry Activity4");
		etURL = (EditText) findViewById(R.id.etURL);
		etMasterPass = (EditText) findViewById(R.id.etMasterPass);

		Log.i(TAG, "Creating Entry Activity6");
	}

	private void createDefaultProfile() {
		/* Create with enums */
		/*
		 * Profile account = new Profile("", "", "", AlgorithmType.SHA256,
		 * false, true, 12, CharacterSets.ALPHANUMERIC, LeetType.NONE,
		 * LeetLevel.LEVEL1, "", "", "", false); datasource.createAccount(name,
		 * algorithm);
		 */
	}

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.textResultPass:

			Log.i(TAG, "Button output password");
			Log.i(TAG, "Current password: " + etMasterPass.getText().toString());

			// Toggle the visible variable
			visible = !visible;

			if (visible) {
				try {
					Log.i(TAG, "Creating Entry Activity3");

					/*
					 * master = new SecureCharArray(etMasterPass.getText()
					 * .toString()); SecureCharArray result =
					 * PasswordMaker.makePassword(master, account);
					 * textOutputPass.setText(new String(result.getData()));
					 */
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
			datasource.insertProfile(new Profile("JAKIM"));
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
