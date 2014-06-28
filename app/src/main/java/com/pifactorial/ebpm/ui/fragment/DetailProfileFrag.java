package com.pifactorial.ebpm.ui.fragment;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;

import android.graphics.Color;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.ButterKnife;

import butterknife.InjectView;

import butterknife.OnCheckedChanged;

import butterknife.OnClick;

import butterknife.OnItemSelected;

import butterknife.OnTextChanged;

import com.pifactorial.ebpm.core.Constants;
import com.pifactorial.ebpm.data.ProfileDataSource;
import com.pifactorial.ebpm.data.ProfileSqLiteHelper;
import com.pifactorial.ebpm.exception.ProfileNotFound;
import com.pifactorial.ebpm.ui.activity.EntryActivity;
import com.pifactorial.ebpm.ui.fragment.AddProfileDialogFragment.AddProfileDialogListener;
import com.pifactorial.ebpm.util.ManagePreferences;
import com.pifactorial.R;

import org.daveware.passwordmaker.AlgorithmType;
import org.daveware.passwordmaker.Profile;

import timber.log.Timber;

public class DetailProfileFrag extends Fragment implements AddProfileDialogListener {

    @InjectView(R.id.spProfiles) Spinner mProfiles;
    @InjectView(R.id.btAddProfile) Button mProfileAdd;
    @InjectView(R.id.cb_url_protocol) CheckBox mUrlProtocol;
    @InjectView(R.id.cb_isHMAC) CheckBox mIsHMAC;
    @InjectView(R.id.cb_url_subdomain) CheckBox mUrlSubdomain;
    @InjectView(R.id.cb_url_domain) CheckBox mUrlDomain;
    @InjectView(R.id.cb_url_port) CheckBox mUrlPort;
    @InjectView(R.id.sp_hash_alg) Spinner mHashAlg;
    @InjectView(R.id.etPassLenght) EditText mPasswordLenght;
    @InjectView(R.id.cb_char_lower) CheckBox mCharLower;
    @InjectView(R.id.cb_char_upper) CheckBox mCharUpper;
    @InjectView(R.id.cb_char_number) CheckBox mCharNumber;
    @InjectView(R.id.cb_char_symbols) CheckBox mCharSymbols;
    @InjectView(R.id.et_custom_symbols_input) EditText mCustomChars;
    @InjectView(R.id.cb_custom_symbols_active) CheckBox mCustomCharsActive;
    @InjectView(R.id.cb_top_domains) CheckBox mJoinTopLevelDomain;

    private ProfileDataSource datasource;
    private ManagePreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Timber.v("Detail Profile creation started");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        datasource = new ProfileDataSource(getActivity());
        datasource.open();
        mPrefs = new ManagePreferences(getActivity());
    }

    private void updateLastSelectedProfileSpinner() {
        final int last_profile_selected = mPrefs.getLastSelectedProfile();
        mProfiles.setSelection(last_profile_selected);
    }

    private Profile getProfileInSpinner() {
        SQLiteCursor profileCursor = (SQLiteCursor) mProfiles.getSelectedItem();
        final String profileName = datasource.getNameFromProfileCursor(profileCursor);

        try {
            Profile p = datasource.getProfileByName(profileName);
            return p;
        }
        catch (ProfileNotFound e) {
            Timber.e("Profile displayed in spinner does not exist", e);
            return Profile.getDefaultProfile();
        }
    }

    private void updateProfileOnGui(Profile p) throws ProfileNotFound {

        mUrlProtocol.setChecked(p.getUrlComponentProtocol());
        mUrlSubdomain.setChecked(p.getUrlComponentSubDomain());
        mUrlDomain.setChecked(p.getUrlComponentDomain());
        mUrlPort.setChecked(p.getUrlComponentPortParameters());
        mCharLower.setChecked(p.hasCharSetLowercase());
        mCharUpper.setChecked(p.hasCharSetUppercase());
        mCharNumber.setChecked(p.hasCharSetNumbers());
        mCharSymbols.setChecked(p.hasCharSetSymbols());
        mCustomChars.setText(p.getCustomCharset());
        mPasswordLenght.setText(Integer.toString(p.getLength()));
        mJoinTopLevelDomain.setChecked(p.getJoinTopLevel());
        mCustomCharsActive.setChecked(p.isCustomCharsetActive());

        mIsHMAC.setChecked(p.isHMAC());

        String[] androidStrings = getResources().getStringArray(R.array.hash_algorithms_string_array);
        mHashAlg.setSelection(java.util.Arrays.asList(androidStrings).indexOf(p.getAlgorithm().getName()));
        Timber.d("Profile loaded");
    }

    public void updateProfileSpinner() {
        // Populate a spinner with the profiles
        Cursor cursor = datasource.getAllProfilesCursor();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this.getActivity(), android.R.layout.simple_spinner_item,
                cursor, new String[] { ProfileSqLiteHelper.COLUMN_NAME },
                new int[] { android.R.id.text1 }, 0);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mProfiles.setAdapter(adapter);

        // Now let's get the last selected profile
        final int last_selected = mPrefs.getLastSelectedProfile();
        mProfiles.setSelection(last_selected);
    }

    @OnClick(R.id.btAddProfile)
    public void onClickAddProfile() {
        Timber.i("Clicked add profile button");
        final FragmentManager fm = getFragmentManager();
        final AddProfileDialogFragment editNameDialog = new AddProfileDialogFragment();
        editNameDialog.show(fm, "fragment_edit_name");
    }

    @OnItemSelected(R.id.spProfiles)
    protected void onProfileItemSpinnerChanged() {
        Timber.d("Choosen a new profile");
        try {
            // The profile in spinner has changed, save the last selected profile
            final int last_selected = mProfiles.getSelectedItemPosition();
            mPrefs.setLastSelectedProfile(last_selected);

            // Then update the various views
            final Profile p = getProfileInSpinner();
            updateLastSelectedProfileSpinner();
            updateProfileOnGui(p);
        } catch (ProfileNotFound e) {
            e.printStackTrace();
        }
    }


    @OnCheckedChanged(R.id.cb_custom_symbols_active)
    protected void onCustumCheckboxChecked(boolean isChecked) {
        if ( isChecked ) {
            mCustomChars.setTextColor(Color.WHITE);
        }

        else {
            mCustomChars.setTextColor(Color.DKGRAY);
            // Hides the keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mCustomChars.getWindowToken(), 0);
        }
    }

    @OnTextChanged(R.id.et_custom_symbols_input)
    void onTextChanged(CharSequence text) {
        mCustomCharsActive.setChecked(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_detail_profile, container, false);
        ButterKnife.inject(this, view);

        super.onCreateView(inflater, container, savedInstanceState);

        updateProfileSpinner();

        Profile p;
        if ((savedInstanceState != null) && (savedInstanceState.getSerializable("profile") != null)) {
            p = (Profile) savedInstanceState.getSerializable("profile");
            Timber.v("Instance saved");
        }
        else{
            p = getProfileInSpinner();
            updateLastSelectedProfileSpinner();
        }

        try {
            updateProfileOnGui(p);
        } catch (ProfileNotFound e) {
            e.printStackTrace();
        }
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
    };

    public int getShownIndex() {
        Timber.v("Get shown index = ");

        return getArguments().getInt("index", 0);
    }

    public boolean validateProfileInput() {
        try {
            int value = Integer.parseInt(mPasswordLenght.getText().toString());
            if (value <= 0) {
                Toast.makeText(getActivity(), "Password length cannot be less than 1", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Invalid password length", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Context context = getActivity();
        AlertDialog.Builder alertDialogBuilder;
        AlertDialog alertDialog;

        switch (item.getItemId()) {

            case R.id.actionBtnSave:
                Timber.d("Clicked the save button");

                if(!validateProfileInput()) {
                    break;
                }

                Profile p = getProfileConfiguration();
                saveProfile(p);

                // Now let's show an alert box
                alertDialogBuilder = new AlertDialog.Builder(context);
                // set title
                alertDialogBuilder.setTitle("Profile Saved");
                // alertDialogBuilder.setMessage("Profile has been saved");
                alertDialogBuilder.setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent myIntent = new Intent(context, EntryActivity.class);
                                DetailProfileFrag.this.startActivity(myIntent);
                            }
                        });
                // create alert dialog
                alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
                break;

            case R.id.actionBtnDelete:
                Timber.d("Clicked the delete button");

                alertDialogBuilder = new AlertDialog.Builder(context);

                if(deleteProfile())
                    alertDialogBuilder.setTitle("Profile deleted");
                else
                    alertDialogBuilder.setTitle("Default profile cannot be deleted");

                alertDialog = alertDialogBuilder.create();
                mPrefs.setLastSelectedProfile(0);
                updateProfileSpinner();

                // show it
                alertDialog.show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private boolean deleteProfile() {
        SQLiteCursor cursor = (SQLiteCursor) mProfiles.getSelectedItem();
        Profile profile = datasource.createProfileFromCursor(cursor);

        Timber.d("Deleting the profile with name %s", profile.getName());

        if(profile.getName().equals(Profile.DEFAULT_NAME)) {
            Timber.i("Could not delete as it was the default");
            return false;
        }

        else {
            datasource.deleteProfile(profile);
            return true;
        }
    }

    private Profile getProfileConfiguration() {

        SQLiteCursor cursor = (SQLiteCursor) mProfiles.getSelectedItem();
        String profileName = datasource.getNameFromProfileCursor(cursor);

        Profile p = new Profile();

        p.setName(profileName);
        p.setUrlComponentProtocol(mUrlProtocol.isChecked());
        p.setUrlComponentSubDomain(mUrlSubdomain.isChecked());
        p.setUrlComponentDomain(mUrlDomain.isChecked());
        p.setUrlComponentPortParameters(mUrlPort.isChecked());
        p.setIsHMAC(mIsHMAC.isChecked());
        p.setCharSetLowercase(mCharLower.isChecked());
        p.setCharSetUppercase(mCharUpper.isChecked());
        p.setCharSetNumbers(mCharNumber.isChecked());
        p.setCharSetSymbols(mCharSymbols.isChecked());
        p.setJoinTopLevel(mJoinTopLevelDomain.isChecked());
        p.setLength(Integer.parseInt(mPasswordLenght.getText().toString()));
        p.setCustomCharset(mCustomChars.getText().toString());
        p.setCharSetCustomActive(mCustomCharsActive.isChecked());

        final String algorithm_string = mHashAlg.getSelectedItem().toString();
        try {
            p.setAlgorithm(AlgorithmType.fromRdfString(algorithm_string));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    private void saveProfile(Profile p) {
        Timber.d("Saving profile %s", p);
        if (datasource.profileExists(p.getName())) {
            Timber.i("Profile already exists. Replacing it");
            datasource.replaceProfile(p);
        } else {
            Timber.d("Inserting new profile");
            datasource.insertProfile(p);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onFinishEditDialog(String profileName) {
        final Profile defaultProfile = Profile.getDefaultProfile();
        defaultProfile.setName(profileName);

        mPrefs.setLastSelectedProfile(mProfiles.getAdapter().getCount());

        final ProfileDataSource datasource = new ProfileDataSource(getActivity());
        datasource.open();
        datasource.insertProfile(defaultProfile);
        datasource.close();
        updateProfileSpinner();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("profile", getProfileConfiguration());
    }
}
