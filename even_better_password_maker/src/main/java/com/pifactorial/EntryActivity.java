package com.pifactorial;

import org.daveware.passwordmaker.PasswordGenerationException;
import org.daveware.passwordmaker.PasswordMaker;
import org.daveware.passwordmaker.Profile;
import org.daveware.passwordmaker.SecureCharArray;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class EntryActivity extends Activity implements View.OnClickListener {

    private static final String TAG = EntryActivity.class.getName();

    private ProfileDataSource datasource;

    private Boolean visible;

    protected EditText etURL;
    protected EditText etMasterPass;
    protected TextView textOutputPass;
    protected Spinner spinner;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Creating Entry Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i(TAG, "Activity created");

        Log.i(TAG, "Fetching views");
        // Let's get the window controls
        textOutputPass = (TextView) findViewById(R.id.textResultPass);
        etURL = (EditText) findViewById(R.id.etURL);
        etMasterPass = (EditText) findViewById(R.id.etMasterPass);
        Log.i(TAG, "Fetched all views");

        // Set the action bar to don't show the app title
        this.getActionBar().setDisplayShowTitleEnabled(false);

        // Master password should always start as not visible
        visible = false;

        // Create a data source to get profiles
        Log.i(TAG, "Creating data source");
        datasource = new ProfileDataSource(this);
        datasource.open();
        Log.i(TAG, "Created data source");

        // Check if there is no profile
        if(datasource.getAllProfiles().size() < 1) {
            Log.i(TAG, "No profile in DB was found");
            Profile defaultProfile = Profile.getDefaultProfile();
            Log.i(TAG, "Inserting default profile: " + defaultProfile);
            datasource.insertProfile(defaultProfile);
        }

        updateProfileSpinner();
    }

    public void updateProfileSpinner()
    {
        Log.i(TAG, "Populating spinner with stored profiles");
        // Populate a spinner with the profiles
        Cursor cursor = datasource.getAllProfilesCursor();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor, 
                new String[] { ProfileSqLiteHelper.COLUMN_NAME }, new int[] { android.R.id.text1 }, 0);
        spinner = (Spinner) findViewById(R.id.spAlgorithms);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Log.i(TAG, "Finished creating entry activity");
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.textResultPass:

                Log.i(TAG, "Clicked on text output password");

                // Toggle the visible variable
                visible = !visible;

                if (visible) {
                    try {
                        // Get spinner profile
                        SQLiteCursor profileName = (SQLiteCursor) spinner.getSelectedItem();
                        Profile profile = datasource.cursorToAccount(profileName);
                        Log.i(TAG, "Profile fetched \n" + profile.toString());

                        // Use the profile and master password to get the generated password
                        SecureCharArray master = new SecureCharArray(etMasterPass.getText().toString());
                        Log.e(TAG, "Generating password now --> " + master.toString());
                        SecureCharArray result = PasswordMaker.makePassword(master, profile, etURL.getText().toString());
                        Log.e(TAG, "Password generated --> " + result);

                        // Show the generated password
                        textOutputPass.setText(new String(result.getData()));

                    } catch (PasswordGenerationException e) {
                        Log.e(TAG, "Error in generating the new password" + e.getMessage());
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
        updateProfileSpinner();
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
                ClipData clip = ClipData.newPlainText("Copied Text", textOutputPass.getText());
                clipboard.setPrimaryClip(clip);
                break;
            case R.id.actionBtnAbout:
                Log.i(TAG, "Clicked About");
                break;
            case R.id.actionBtnProfiles:
                Log.i(TAG, "Clicked Profiles");

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
