package com.pifactorial.ebpm.ui.activity;

import android.annotation.TargetApi;

import android.content.ClipData;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;

import android.net.Uri;

import android.os.Build;
import android.os.Bundle;

import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;

import butterknife.InjectView;

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

import timber.log.Timber;

public class EntryActivity extends ActionBarActivity implements View.OnClickListener {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // Views
    @InjectView(R.id.etURL) EditText etURL;
    @InjectView(R.id.etMasterPass) ChromaHashView etMasterPass;
    @InjectView(R.id.tvResultPass) TextView textOutputPass;
    @InjectView(R.id.spProfiles) Spinner spProfiles;

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

        sdk_version = android.os.Build.VERSION.SDK_INT;

        ButterKnife.inject(this);

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
                Timber.i("Strange, nothing was selected on the profile spinner");
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

        // Now let's get the last selected profile
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
            Timber.e("Some unexpected view was pressed");
            break;
        }
    }

    private void updatePassword() {
        if (mPassVisible) {
            try {
                final SecureCharArray result = getPassword();
                textOutputPass.setText(new String(result.getData()));     // Show the generated password
            } catch (PasswordGenerationException e) {
                Timber.e("Error in generating the new password: %s", e);
            }
        } else {
            textOutputPass.setText("");
        }
    }

    private SecureCharArray getPassword() throws PasswordGenerationException {
        // Get spinner profile
        final SQLiteCursor profileCursor = (SQLiteCursor) spProfiles.getSelectedItem();
        final Profile profile = datasource.createProfileFromCursor(profileCursor);
        Timber.d("Profile fetched: %s \n", profile.toString());

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
            Timber.d("No profile in DB was found");
            final Profile defaultProfile = Profile.getDefaultProfile();
            Timber.d("Inserting default profile: %s", defaultProfile);
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
            Timber.d("Pressed the go button");
            final Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);

            try {
                Uri uri = Uri.parse(etURL.getText().toString());
                myWebLink.setData(uri);
                startActivity(myWebLink);
            }
            catch (NullPointerException e) {
                Timber.e("Url string is empty %s", e.getMessage());
                Toast.makeText(getApplicationContext(), "Url string is empty", Toast.LENGTH_SHORT).show();
            }
            catch (android.content.ActivityNotFoundException e) {
                Timber.e("Unable to generate uri %s", e.getMessage());
                Toast.makeText(getApplicationContext(), "Invalid URI", Toast.LENGTH_SHORT).show();
            }
            break;

        case R.id.actionBtnCopy:
            Timber.d("Clicked item Copy");

            try {
                final SecureCharArray generatedPassword = getPassword();
                String pass = new String(generatedPassword.getData());
                copyPassToClipBoard(pass);
            } catch (PasswordGenerationException e) {
                Timber.e("Error in generating the new password %s", e.getMessage());
                Toast.makeText(getApplicationContext(), "Error generating password", Toast.LENGTH_SHORT).show();
            }

            break;

        case R.id.actionBtnProfiles:
            Timber.d("Clicked Profiles");
            final Intent myIntent = new Intent(EntryActivity.this, UpdateActivity.class);
            EntryActivity.this.startActivity(myIntent);
            break;

        case R.id.about:
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","trindade.joao@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[evenbetterpassmaker] - Feedback");
            emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.feedback_text_body));
            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_chooser)));
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
        Timber.d("on Stopping");
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
