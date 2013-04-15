package com.pifactorial;

import org.daveware.passwordmaker.Profile;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailProfileFrag extends Fragment {

	private static final String TAG = "DetailProfileFrag";

    protected EditText et_profileName;
    protected CheckBox cb_urlProtocol;
    protected CheckBox cb_urlSubdomain;
    protected CheckBox cb_urlDomain;
    protected CheckBox cb_urlPort;
    protected Spinner  sp_hash_alg;
    protected EditText et_password_lenght;
    protected CheckBox cb_charLower;
    protected CheckBox cb_charUpper;
    protected CheckBox cb_charNumber;
    protected CheckBox cb_charSymbols;

    private static final String TAG = DetailProfileFrag.class.getName();

    private ProfileDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Detail Profile creation started");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        datasource = new ProfileDataSource(getActivity());
        datasource.open();

        try {
            loadProfile();
        } catch (ProfileNotFound e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Detail Profile created");
    }

    private void loadProfile() throws ProfileNotFound {
        Log.v(TAG, "Going to load profile");
        Profile p = datasource.getProfileByName(etProfileName.getText().toString());

        ArrayAdapter spAdap = (ArrayAdapter) spAlgorithms.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = spAdap.getPosition(p.getName());

        //set the default according to value
        spAlgorithms.setSelection(spinnerPosition);

        Log.v(TAG, "Profile loaded");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.v(TAG, "Starting on CreateView");
        View view = inflater.inflate(R.layout.detail_profile, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        et_profileName     = (EditText) view.findViewById(R.id.profile_name);
        cb_urlProtocol     = (CheckBox) view.findViewById(R.id.cb_url_protocol);
        cb_urlSubdomain    = (CheckBox) view.findViewById(R.id.cb_url_subdomain);
        cb_urlDomain       = (CheckBox) view.findViewById(R.id.cb_url_domain);
        cb_urlPort         = (CheckBox) view.findViewById(R.id.cb_url_port);
        sp_hash_alg        = (Spinner) view.findViewById(R.id.sp_hash_alg);
        et_password_lenght = (EditText) view.findViewById(R.id.password_size);
        cb_charLower       = (CheckBox) view.findViewById(R.id.cb_char_lower);
        cb_charUpper       = (CheckBox) view.findViewById(R.id.cb_char_upper);
        cb_charNumber      = (CheckBox) view.findViewById(R.id.cb_char_number);
        cb_charSymbols     = (CheckBox) view.findViewById(R.id.cb_char_symbols);

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
        // TODO Auto-generated method stub

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
