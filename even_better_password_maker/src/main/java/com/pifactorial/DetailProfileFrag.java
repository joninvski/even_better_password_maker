package com.pifactorial;

import org.daveware.passwordmaker.AlgorithmType;
import org.daveware.passwordmaker.Profile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.pifactorial.AddProfileDialogFragment.AddProfileDialogListener;

public class DetailProfileFrag extends Fragment implements
		OnItemSelectedListener, AddProfileDialogListener {

	private static final String TAG = DetailProfileFrag.class.getName();

	protected Spinner sp_profiles;
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
	protected Button bt_profileAdd;

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

		SQLiteCursor profileName = (SQLiteCursor) sp_profiles.getSelectedItem();
        if(profileName == null)
            sp_profiles.setSelection(0);

		Profile profile = datasource.cursorToAccount(profileName);

		Profile p = datasource.getProfileByName(profile.getName());

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

	public void updateProfileSpinner() {
		Log.i(TAG, "Populating spinner with stored profiles");
		// Populate a spinner with the profiles
		Cursor cursor = datasource.getAllProfilesCursor();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
				this.getActivity(), android.R.layout.simple_spinner_item,
				cursor, new String[] { ProfileSqLiteHelper.COLUMN_NAME },
				new int[] { android.R.id.text1 }, 0);

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		sp_profiles.setAdapter(adapter);

		Log.i(TAG, "Finished creating entry activity");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v(TAG, "Starting on CreateView");
		View view = inflater.inflate(R.layout.detail_profile, container, false);
		super.onCreateView(inflater, container, savedInstanceState);

		sp_profiles = (Spinner) view.findViewById(R.id.spProfiles);
		bt_profileAdd = (Button) view.findViewById(R.id.btAddProfile);
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

		bt_profileAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i(TAG, "Clicked add profile button");
				// Watch for button clicks.
				FragmentManager fm = getFragmentManager();
				Log.i(TAG, "Got fragment manager");
				AddProfileDialogFragment editNameDialog = new AddProfileDialogFragment();
				Log.i(TAG, "Got dialog");
				editNameDialog.show(fm, "fragment_edit_name");
				Log.i(TAG, "Showing");
			}
		});

		sp_profiles.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				Log.i(TAG, "Choosen a new profile");
				try {
					updateProfileOnGui();
				} catch (ProfileNotFound e) {
					e.printStackTrace();
				}
			}

			public void onNothingSelected(AdapterView<?> parentView) {
				Log.i(TAG, "Nothing was selected on the profile spinner");
			}

		});

		Log.i(TAG, "Going to update spinner");
		updateProfileSpinner();
		Log.i(TAG, "Updated Spinner");

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
		TextView view = (TextView) getView().findViewById(R.id.spProfiles);
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
		// set the default according to value
		Log.d(TAG, "Clicked the save button");
		Profile p = new Profile();

		SQLiteCursor profileName = (SQLiteCursor) sp_profiles.getSelectedItem();
		Profile profile = datasource.cursorToAccount(profileName);

		p.setName(profile.getName());
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
			e.printStackTrace();
		}

		Log.d(TAG, "Saving profile: " + p);
		if (datasource.profileExists(p.getName())) {
			Log.d(TAG, "Profile already exists. Replacing it");
			datasource.replaceProfile(p);
		} else {
			Log.d(TAG, "Inserting new profile");
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

	public void onFinishEditDialog(String profileName) {
		Log.i(TAG, "Dialog text was: " + profileName);

		Profile defaultProfile = Profile.getDefaultProfile();
		defaultProfile.setName(profileName);

		ProfileDataSource datasource = new ProfileDataSource(getActivity());
		datasource.open();
		datasource.insertProfile(defaultProfile);
		datasource.close();
		updateProfileSpinner();
	}
}
