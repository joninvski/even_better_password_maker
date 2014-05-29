package com.pifactorial.ebpm.ui.activity;

import android.annotation.TargetApi;

import android.app.Activity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;

import android.net.Uri;

import android.os.Build;
import android.os.Bundle;

import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;

import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pifactorial.ebpm.core.Constants;
import com.pifactorial.ebpm.data.ProfileDataSource;
import com.pifactorial.ebpm.data.ProfileSqLiteHelper;
import com.pifactorial.ebpm.util.ManagePreferences;
import com.pifactorial.R;

import java.security.Security;

import org.daveware.passwordmaker.PasswordGenerationException;
import org.daveware.passwordmaker.PasswordMaker;
import org.daveware.passwordmaker.Profile;
import org.daveware.passwordmaker.SecureCharArray;

import org.michaelevans.chromahashview.ChromaHashView;

import org.spongycastle.jce.provider.BouncyCastleProvider;

public class EntryActivity extends ActionBarActivity implements View.OnClickListener {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // Views
    private EditText etURL;
    private ChromaHashView etMasterPass;
    private TextView textOutputPass;
    private Spinner spProfiles;

    // Other
    private ProfileDataSource datasource;
    private Boolean mPassVisible;
    private ManagePreferences mPrefs;
    private int sdk_version;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_entry);

        // Let's get the window controls
        textOutputPass = (TextView) findViewById(R.id.tvResultPass);
        etURL          = (EditText) findViewById(R.id.etURL);
        etMasterPass   = (ChromaHashView) findViewById(R.id.etMasterPass);
        spProfiles     = (Spinner) findViewById(R.id.spProfiles);

        sdk_version = android.os.Build.VERSION.SDK_INT;

        setActionBar();

        // Master password should always start as not mPassVisible
        mPassVisible = false;

        // Create a data source to get profiles
        datasource = new ProfileDataSource(this);
        datasource.open();

        mPrefs = new ManagePreferences(this);

        // Let's create the callback when the spinner changes.
        // We just want to store in the last selected preference the selected value
        spProfiles.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {

                final int last_selected = spProfiles.getSelectedItemPosition();
                mPrefs.setLastSelectedProfile(last_selected);
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                Log.i(Constants.LOG,
                      "Strange, nothing was selected on the profile spinner");
            }
        });


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

    private void setActionBar() {
        if( sdk_version < Build.VERSION_CODES.HONEYCOMB) {
            setActionBarForSeven();
        } else {
            setActionBarForEleven();
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    private void setActionBarForSeven() {
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setDisplayShowTitleEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setActionBarForEleven() {
        android.app.ActionBar bar2 = getActionBar();
        bar2.setDisplayShowTitleEnabled(true);
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
        final int last_selected = mPrefs.getLastSelectedProfile();
        spProfiles.setSelection(last_selected);
    }

    public void onClick(View v) {
        switch (v.getId()) {

        case R.id.tvResultPass:
            mPassVisible = !mPassVisible;
            updatePassword();

            break;

        default:
            Log.e(Constants.LOG, "Some unexpected view was pressed");
            break;
        }
    }

    private void updatePassword() {
        if (mPassVisible) {
            try {
                final SecureCharArray result = getPassword();
                textOutputPass.setText(new String(result.getData()));     // Show the generated password
            } catch (PasswordGenerationException e) {
                Log.e(Constants.LOG, "Error in generating the new password" + e.getMessage());
            }
        } else {
            textOutputPass.setText("");
        }
    }

    private SecureCharArray getPassword() throws PasswordGenerationException {
        // Get spinner profile
        final SQLiteCursor profileCursor = (SQLiteCursor) spProfiles.getSelectedItem();
        final Profile profile = datasource.createProfileFromCursor(profileCursor);
        Log.d(Constants.LOG, "Profile fetched \n" + profile.toString());

        // Use the profile and master password to get the generated password
        final SecureCharArray master = new SecureCharArray(etMasterPass.getText().toString());
        final SecureCharArray result = PasswordMaker.makePassword(master, profile, etURL.getText().toString());
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if there is no profile
        if (datasource.getAllProfiles().size() < 1) {
            Log.d(Constants.LOG, "No profile in DB was found");
            final Profile defaultProfile = Profile.getDefaultProfile();
            Log.d(Constants.LOG, "Inserting default profile: " + defaultProfile);
            datasource.insertProfile(defaultProfile);
        }

        updateProfileSpinner();

        // Get intent, action and MIME type
        final Intent intent = getIntent();
        final String action = intent.getAction();
        final String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    etURL.setText(sharedText);
                }
            }
        } else {
            String lastURL = mPrefs.getLastURL();

            etURL.setText(lastURL);
        }

        etURL.setSelection(etURL.getText().length()); // Puts the cursor at the end of the string
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

        case R.id.actionBtnGo:
            Log.d(Constants.LOG, "Pressed the go button");
            final Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);

            try {
                Uri uri = Uri.parse(etURL.getText().toString());
                myWebLink.setData(uri);
                startActivity(myWebLink);
            }
            catch (NullPointerException e) {
                Log.e(Constants.LOG, "Url string is empty" + e.getMessage());
                Toast.makeText(getApplicationContext(), "Url string is empty", Toast.LENGTH_SHORT).show();
            }
            catch (android.content.ActivityNotFoundException e) {
                Log.e(Constants.LOG, "Unable to generate uri" + e.getMessage());
                Toast.makeText(getApplicationContext(), "Invalid URI", Toast.LENGTH_SHORT).show();
            }
            break;

        case R.id.actionBtnCopy:
            Log.d(Constants.LOG, "Clicked item Copy");

            try {
                final SecureCharArray generatedPassword = getPassword();
                String pass = new String(generatedPassword.getData());
                copyPassToClipBoard(pass);
            } catch (PasswordGenerationException e) {
                Log.e(Constants.LOG, "Error in generating the new password" + e.getMessage());
                Toast.makeText(getApplicationContext(), "Error generating password", Toast.LENGTH_SHORT).show();
            }

            break;

        case R.id.actionBtnProfiles:
            Log.d(Constants.LOG, "Clicked Profiles");
            final Intent myIntent = new Intent(EntryActivity.this, UpdateActivity.class);
            EntryActivity.this.startActivity(myIntent);
            break;

        default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void copyPassToClipBoard(String pass) {
        if( sdk_version < android.os.Build.VERSION_CODES.HONEYCOMB ) {
            copyClipBoardBeforeHoneyComb(pass);
        } else {
            copyClipboardAfterHoneyComb(pass);
        }

        Toast.makeText(getApplicationContext(), R.string.password_copy_to_clipboard, Toast.LENGTH_SHORT).show();
    }

    @TargetApi(android.os.Build.VERSION_CODES.ECLAIR_MR1)
    private void copyClipBoardBeforeHoneyComb(String pass) {
        final android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setText(pass);
    }

    @TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    private void copyClipboardAfterHoneyComb(String pass) {
        final android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText("Copied Text", pass);
        clipboard.setPrimaryClip(clip);
    }


    @Override
    protected void onPause() {
        // Save the last URL
        mPrefs.setLastURL(etURL.getText().toString());
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

        if( sdk_version >= 11) {
            inflater.inflate(R.menu.main_menu, menu);
        } else {
            inflater.inflate(R.menu.main_menu_old, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }
}