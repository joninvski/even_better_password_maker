package com.pifactorial;

import android.app.Activity;

import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;

import android.net.Uri;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.daveware.passwordmaker.PasswordGenerationException;
import org.daveware.passwordmaker.PasswordMaker;
import org.daveware.passwordmaker.Profile;
import org.daveware.passwordmaker.SecureCharArray;

public class EntryActivity extends Activity implements View.OnClickListener {

    private ProfileDataSource datasource;
    private Boolean visible;
    protected EditText etURL;
    protected EditText etMasterPass;
    protected TextView textOutputPass;
    protected Spinner spProfiles;

    private SharedPreferences mPrefs;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(Constants.LOG, "Creating Entry Activity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Let's get the window controls
        textOutputPass = (TextView) findViewById(R.id.tvResultPass);
        etURL = (EditText) findViewById(R.id.etURL);

        etMasterPass = (EditText) findViewById(R.id.etMasterPass);
        spProfiles = (Spinner) findViewById(R.id.spProfiles);
        Log.d(Constants.LOG, "Fetched all views");

        // Set the action bar to show the app title
        this.getActionBar().setDisplayShowTitleEnabled(true);

        // Master password should always start as not visible
        visible = false;

        // Create a data source to get profiles
        Log.d(Constants.LOG, "Creating data source");
        datasource = new ProfileDataSource(this);
        datasource.open();
        Log.d(Constants.LOG, "Created data source");

        mPrefs = getSharedPreferences(getString(R.string.SharedPreferencesName), Context.MODE_PRIVATE);

        // Let's create the callback when the spinner changes.
        // We just want to store in the last selected preference the selected
        // value
        spProfiles.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView,
                View selectedItemView, int position, long id) {
                Log.d(Constants.LOG, "Saving shared preferences");

                int last_selected = spProfiles.getSelectedItemPosition();
                Editor editor = mPrefs.edit();
                editor.putInt(getString(R.string.LastSelectedProfile), last_selected);
                editor.apply(); // TODO - Check the return value
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                Log.i(Constants.LOG,
                    "Strange, nothing was selected on the profile spinner");
            }

        });

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    etURL.setText(sharedText);
                }
            }
        }

        // Check if there is no profile
        if (datasource.getAllProfiles().size() < 1) {
            Log.d(Constants.LOG, "No profile in DB was found");
            Profile defaultProfile = Profile.getDefaultProfile();
            Log.d(Constants.LOG, "Inserting default profile: " + defaultProfile);
            datasource.insertProfile(defaultProfile);
        }

        updateProfileSpinner();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePassword();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                return; // Do nothing

            }

            @Override
            public void afterTextChanged(Editable s) {
                return; // Do nothing
            }
        };

        // Change the passwords as the text views are changed
        etURL.addTextChangedListener(watcher);
        etMasterPass.addTextChangedListener(watcher);
    }

    public void updateProfileSpinner() {
        // Populate a spinner with the profiles
        Cursor cursor = datasource.getAllProfilesCursor();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item, cursor,
                new String[] { ProfileSqLiteHelper.COLUMN_NAME },
                new int[] { android.R.id.text1 }, 0);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spProfiles.setAdapter(adapter);

        // Now let's get the default profile
        int last_selected = mPrefs.getInt(getString(R.string.LastSelectedProfile), 0);
        spProfiles.setSelection(last_selected);
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvResultPass:

                Log.d(Constants.LOG, "Clicked on text output password");
                // Toggle the visible variable
                visible = !visible;
                updatePassword();

                break;

            default:
                Log.e(Constants.LOG, "Some unexpected view was pressed");
                break;
        }
    }

    private void updatePassword() {
        if (visible) {
            try {
                SecureCharArray result = getPassword();
                textOutputPass.setText(new String(result.getData()));     // Show the generated password
            } catch (PasswordGenerationException e) {
                Log.e(Constants.LOG, "Error in generating the new password"
                        + e.getMessage());
            }
        } else {
            textOutputPass.setText("");
        }
    }

    private SecureCharArray getPassword() throws PasswordGenerationException {
        // Get spinner profile
        SQLiteCursor profileName = (SQLiteCursor) spProfiles.getSelectedItem();
        Profile profile = datasource.cursorToAccount(profileName);
        Log.d(Constants.LOG, "Profile fetched \n" + profile.toString());

        // Use the profile and master password to get the generated password
        SecureCharArray master = new SecureCharArray(etMasterPass.getText()
                .toString());
        SecureCharArray result = PasswordMaker.makePassword(master, profile,
                etURL.getText().toString());
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProfileSpinner();

        String lastURL = mPrefs.getString("LastURL", getString(R.string.http));
        etURL.setText(lastURL);
        etURL.setSelection(etURL.getText().length()); // Puts the cursor at the end of the string
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.actionBtnGo:
                Log.d(Constants.LOG, "Pressed the go button");
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                myWebLink.setData(Uri.parse(etURL.getText().toString()));
                startActivity(myWebLink);
                break;

            case R.id.actionBtnCopy:
                Log.d(Constants.LOG, "Clicked item Copy");
                try {
                    SecureCharArray generatedPassword = getPassword();

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text", new String(generatedPassword.getData()));
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(getApplicationContext(), R.string.password_copy_to_clipboard, Toast.LENGTH_SHORT).show();
                } catch (PasswordGenerationException e) {
                    Log.e(Constants.LOG, "Error in generating the new password" + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Error generating password", Toast.LENGTH_SHORT).show();
                }

                break;

                // case R.id.actionBtnAbout:
                // Log.i(Constants.LOG, "Clicked About");
                // break;

            case R.id.actionBtnProfiles:
                Log.d(Constants.LOG, "Clicked Profiles");
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
        Log.d(Constants.LOG, "on Pausing");

        // Save the last URL
        Editor editor = mPrefs.edit();
        editor.putString("LastURL", etURL.getText().toString());
        editor.apply(); // TODO - Check the return value

        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(Constants.LOG, "on Stopping");
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
