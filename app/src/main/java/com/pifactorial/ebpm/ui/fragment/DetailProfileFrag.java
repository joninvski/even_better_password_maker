package com.pifactorial.ebpm.ui.fragment;

import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SimpleCursorAdapter;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class DetailProfileFrag extends Fragment implements
    OnItemSelectedListener, AddProfileDialogListener {

    protected Spinner mProfiles;
    protected CheckBox mIsHMAC;
    protected CheckBox mUrlProtocol;
    protected CheckBox mUrlSubdomain;
    protected CheckBox mUrlDomain;
    protected CheckBox mUrlPort;
    protected Spinner mHashAlg;
    protected EditText mPasswordLenght;
    protected CheckBox mCharLower;
    protected CheckBox mCharUpper;
    protected CheckBox mCharNumber;
    protected CheckBox mCharSymbols;
    protected Button mProfileAdd;

    private ProfileDataSource datasource;
    private ManagePreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(Constants.LOG, "Detail Profile creation started");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        datasource = new ProfileDataSource(getActivity());
        datasource.open();
        mPrefs = new ManagePreferences(getActivity());
    }

    private void updateProfileOnGui() throws ProfileNotFound {
        final int last_profile_selected = mPrefs.getLastSelectedProfile();
        mProfiles.setSelection(last_profile_selected);

        SQLiteCursor profileCursor = (SQLiteCursor) mProfiles.getSelectedItem();
        final String profileName = datasource.getProfileName(profileCursor);
        final Profile p = datasource.getProfileByName(profileName);

        mUrlProtocol.setChecked(p.getUrlCompomentProtocol());
        mUrlSubdomain.setChecked(p.getUrlComponentSubDomain());
        mUrlDomain.setChecked(p.getUrlComponentDomain());
        mUrlPort.setChecked(p.getUrlComponentPortParameters());
        mCharLower.setChecked(p.hasCharSetLowercase());
        mCharUpper.setChecked(p.hasCharSetUppercase());
        mCharNumber.setChecked(p.hasCharSetNumbers());
        mCharSymbols.setChecked(p.hasCharSetSymbols());
        mPasswordLenght.setText(Integer.toString(p.getLength()));

        mIsHMAC.setChecked(p.isHMAC());

        String[] androidStrings = getResources().getStringArray(R.array.hash_algorithms_string_array);
        mHashAlg.setSelection(java.util.Arrays.asList(androidStrings).indexOf(p.getAlgorithm().getName()));
        Log.d(Constants.LOG, "Profile loaded");
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail_profile, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        mProfiles = (Spinner) view.findViewById(R.id.spProfiles);
        mProfileAdd = (Button) view.findViewById(R.id.btAddProfile);
        mUrlProtocol = (CheckBox) view.findViewById(R.id.cb_url_protocol);
        mIsHMAC = (CheckBox) view.findViewById(R.id.cb_isHMAC);
        mUrlSubdomain = (CheckBox) view.findViewById(R.id.cb_url_subdomain);
        mUrlDomain = (CheckBox) view.findViewById(R.id.cb_url_domain);
        mUrlPort = (CheckBox) view.findViewById(R.id.cb_url_port);
        mHashAlg = (Spinner) view.findViewById(R.id.sp_hash_alg);
        mPasswordLenght = (EditText) view.findViewById(R.id.etPassLenght);
        mCharLower = (CheckBox) view.findViewById(R.id.cb_char_lower);
        mCharUpper = (CheckBox) view.findViewById(R.id.cb_char_upper);
        mCharNumber = (CheckBox) view.findViewById(R.id.cb_char_number);
        mCharSymbols = (CheckBox) view.findViewById(R.id.cb_char_symbols);

        mProfileAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(Constants.LOG, "Clicked add profile button");
                // Watch for button clicks.
                final FragmentManager fm = getFragmentManager();
                final AddProfileDialogFragment editNameDialog = new AddProfileDialogFragment();
                editNameDialog.show(fm, "fragment_edit_name");
            }
        });

        mProfiles.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                Log.d(Constants.LOG, "Choosen a new profile");
                try {
                    final int last_selected = mProfiles.getSelectedItemPosition();
                    mPrefs.setLastSelectedProfile(last_selected);
                    updateProfileOnGui();
                } catch (ProfileNotFound e) {
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                Log.i(Constants.LOG, "Nothing was selected on the profile spinner");
            }
        });

        updateProfileSpinner();


        try {
            updateProfileOnGui();
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
        Log.v(Constants.LOG, "Get shown index = ");

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
            Log.d(Constants.LOG, "Clicked the save button");

            if(!validateProfileInput()) {
                break;
            }

            saveProfile();

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
            Log.d(Constants.LOG, "Clicked the delete button");

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

        Log.d(Constants.LOG, "Deleting the profile with name " + profile.getName());

        if(profile.getName().equals(Profile.DEFAULT_NAME)) {
            Log.i(Constants.LOG, "Could not delete as it was the default");
            return false;
        }

        else {
            datasource.deleteProfile(profile);
            return true;
        }
    }

    private void saveProfile() {
        // set the default according to value
        Log.d(Constants.LOG, "Clicked the save button");
        Profile p = new Profile();

        SQLiteCursor cursor = (SQLiteCursor) mProfiles.getSelectedItem();
        String profileName = datasource.createProfileFromCursor(cursor).getName();

        p.setName(profileName);
        p.setUrlCompomentProtocol(mUrlProtocol.isChecked());
        p.setUrlComponentSubDomain(mUrlSubdomain.isChecked());
        p.setUrlComponentDomain(mUrlDomain.isChecked());
        p.setUrlComponentPortParameters(mUrlPort.isChecked());
        p.setIsHMAC(mIsHMAC.isChecked());
        p.setCharSetLowercase(mCharLower.isChecked());
        p.setCharSetUppercase(mCharUpper.isChecked());
        p.setCharSetNumbers(mCharNumber.isChecked());
        p.setCharSetSymbols(mCharSymbols.isChecked());
        p.setLength(Integer.parseInt(mPasswordLenght.getText().toString()));

        final String algorithm_string = mHashAlg.getSelectedItem().toString();
        try {
            p.setAlgorithm(AlgorithmType.fromRdfString(algorithm_string));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(Constants.LOG, "Saving profile: " + p);
        if (datasource.profileExists(p.getName())) {
            Log.i(Constants.LOG, "Profile already exists. Replacing it");
            datasource.replaceProfile(p);
        } else {
            Log.d(Constants.LOG, "Inserting new profile");
            datasource.insertProfile(p);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
}