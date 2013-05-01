package com.pifactorial;

import java.util.List;

import org.daveware.passwordmaker.AlgorithmType;
import org.daveware.passwordmaker.Profile;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailProfileFrag extends Fragment implements
        OnItemSelectedListener {

    private static final String TAG = DetailProfileFrag.class.getName();

    protected EditText et_profileName;
    protected CheckBox cb_urlProtocol;
    protected CheckBox cb_urlSubdomain;
    protected CheckBox cb_urlDomain;
    protected CheckBox cb_urlPort;
    protected Spinner sp_hash_alg;
    protected EditText et_password_lenght;
    protected CheckBox cb_charLower;
    protected CheckBox cb_charUpper;
    protected CheckBox cb_charNumber;
    protected CheckBox cb_charSymbols;

    private ProfileDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Detail Profile creation started");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        datasource = new ProfileDataSource(getActivity());
        datasource.open();

    }

    private void updateProfileOnGui() throws ProfileNotFound {
        Log.v(TAG, "Going to load profile");

        Profile p = datasource.getProfileByName(et_profileName.getText()
                .toString());

        Log.v(TAG, "Profile fetched : " + p.toString());

        cb_urlProtocol.setChecked(p.getUrlCompomentProtocol());
        cb_urlSubdomain.setChecked(p.getUrlComponentSubDomain());
        cb_urlDomain.setChecked(p.getUrlComponentDomain());
        cb_urlPort.setChecked(p.getUrlComponentPortParameters());
        cb_charLower.setChecked(p.hasCharSetLowercase());
        cb_charUpper.setChecked(p.hasCharSetUppercase());
        cb_charNumber.setChecked(p.hasCharSetNumbers());
        cb_charSymbols.setChecked(p.hasCharSetSymbols());
        et_password_lenght.setText(Integer.toString(p.getLength()));
        // int spinnerPosition = spAdap.getItemViewType(p.getName());
        String[] androidStrings = getResources().getStringArray(
                R.array.hash_algorithms_string_array);

        sp_hash_alg.setSelection(java.util.Arrays.asList(androidStrings)
                .indexOf(p.getAlgorithm().getName()));

        Log.v(TAG, "Profile loaded");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.v(TAG, "Starting on CreateView");
        View view = inflater.inflate(R.layout.detail_profile, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        et_profileName = (EditText) view.findViewById(R.id.etProfileName);
        cb_urlProtocol = (CheckBox) view.findViewById(R.id.cb_url_protocol);
        cb_urlSubdomain = (CheckBox) view.findViewById(R.id.cb_url_subdomain);
        cb_urlDomain = (CheckBox) view.findViewById(R.id.cb_url_domain);
        cb_urlPort = (CheckBox) view.findViewById(R.id.cb_url_port);
        sp_hash_alg = (Spinner) view.findViewById(R.id.sp_hash_alg);
        et_password_lenght = (EditText) view.findViewById(R.id.etPassLenght);
        cb_charLower = (CheckBox) view.findViewById(R.id.cb_char_lower);
        cb_charUpper = (CheckBox) view.findViewById(R.id.cb_char_upper);
        cb_charNumber = (CheckBox) view.findViewById(R.id.cb_char_number);
        cb_charSymbols = (CheckBox) view.findViewById(R.id.cb_char_symbols);

        // loadSpinnerDataHama(getActivity());
        try {
            updateProfileOnGui();
        } catch (ProfileNotFound e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Detail Profile created");

        Log.v(TAG, "View created");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v(TAG, "Going to create activity");
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "Activity Created");
    }

    public void setText(String item) {
        Log.v(TAG, "Set Text = " + item);
        TextView view = (TextView) getView().findViewById(R.id.etProfileName);
        view.setText(item);
    }

    @Override
    public void onStop() {
        super.onStop();
    };

    public int getShownIndex() {
        Log.v(TAG, "Get shown index = ");

        return getArguments().getInt("index", 0);
    }

    public static DetailProfileFrag newInstance(int index) {
        Log.v(TAG, "New instance = ");

        DetailProfileFrag f = new DetailProfileFrag();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "Some button was pressed");
        switch (item.getItemId()) {
        case R.id.actionBtnSave:
            saveProfile();
            break;
        default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void saveProfile() {
        //set the default according to value
        Log.d(TAG, "Clicked the save button");
        Profile p = new Profile();

        p.setName(et_profileName.getText().toString());
        p.setUrlCompomentProtocol(cb_urlProtocol.isChecked());
        p.setUrlComponentSubDomain(cb_urlSubdomain.isChecked());
        p.setUrlComponentDomain(cb_urlDomain.isChecked());
        p.setUrlComponentPortParameters(cb_urlPort.isChecked());
        p.setCharSetLowercase(cb_charLower.isChecked());
        p.setCharSetUppercase(cb_charUpper.isChecked());
        p.setCharSetNumbers(cb_charNumber.isChecked());
        p.setCharSetSymbols(cb_charSymbols.isChecked());
        p.setLength(Integer.parseInt(et_password_lenght.getText().toString()));

        String algorithm_string = sp_hash_alg.getSelectedItem().toString();
        try {
            p.setAlgorithm(AlgorithmType.fromRdfString(algorithm_string));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d(TAG, "Saving profile: " + p);

        if(datasource.profileExists(p.getName()))
        {
            datasource.replaceProfile(p);
        }
        else{
            datasource.insertProfile(p);
        }

        Log.d(TAG, "Profile saved");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
            long arg3) {
        // TODO Auto-generated method stub

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
    private void loadSpinnerAlgorithm(Context context) {
        // database handler
        List<SpinnerProfile> labels = datasource.getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<SpinnerProfile> dataAdapter = new ArrayAdapter<SpinnerProfile>(context, android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp_hash_alg.setAdapter(dataAdapter);
    }
}
