package com.pifactorial;

import java.util.ArrayList;
import java.util.List;

import org.daveware.passwordmaker.Profile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ProfileDataSource {

    // Database fields
    private SQLiteDatabase database;
    private ProfileSqLiteHelper dbHelper;
    private final String[] allColumns = { ProfileSqLiteHelper.COLUMN_ID,
                                          ProfileSqLiteHelper.COLUMN_NAME,
                                          ProfileSqLiteHelper.COLUMN_USERNAME,
                                          ProfileSqLiteHelper.COLUMN_ALGORITHM,
                                          ProfileSqLiteHelper.COLUMN_ISHMAC,
                                          ProfileSqLiteHelper.COLUMN_LENGTH,
                                          ProfileSqLiteHelper.COLUMN_LEET_TYPE,
                                          ProfileSqLiteHelper.COLUMN_LEET_LEVEL,
                                          ProfileSqLiteHelper.COLUMN_MODIFIER,
                                          ProfileSqLiteHelper.COLUMN_PREFIX,
                                          ProfileSqLiteHelper.COLUMN_SUFFIX,
                                          ProfileSqLiteHelper.COLUMN_URL_COMPONENT_PROTOCOL,
                                          ProfileSqLiteHelper.COLUMN_URL_COMPONENT_SUBDOMAIN,
                                          ProfileSqLiteHelper.COLUMN_URL_COMPONENT_DOMAIN,
                                          ProfileSqLiteHelper.COLUMN_URL_COMPONENT_PORT_PARAMETERS,
                                          ProfileSqLiteHelper.COLUMN_CHAR_SET_UPPERCASE,
                                          ProfileSqLiteHelper.COLUMN_CHAR_SET_LOWERCASE,
                                          ProfileSqLiteHelper.COLUMN_CHAR_SET_NUMBERS,
                                          ProfileSqLiteHelper.COLUMN_CHAR_SET_SYMBOLS,
                                          ProfileSqLiteHelper.COLUMN_CHAR_SET_COSTUM
                                        };

    public ProfileDataSource(Context context) {
        dbHelper = new ProfileSqLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Profile insertProfile(Profile profile) {
        Log.d(Constants.LOG, "Inserting new profile");

        // Prepare the contentor to insert values
        ContentValues values = new ContentValues();

        values.put(ProfileSqLiteHelper.COLUMN_NAME, profile.getName());
        values.put(ProfileSqLiteHelper.COLUMN_USERNAME, profile.getUsername());
        values.put(ProfileSqLiteHelper.COLUMN_ALGORITHM, profile.getAlgorithm().toString());
        values.put(ProfileSqLiteHelper.COLUMN_ISHMAC, profile.isHMAC().toString());
        values.put(ProfileSqLiteHelper.COLUMN_LENGTH, Integer.toString(profile.getLength()));
        values.put(ProfileSqLiteHelper.COLUMN_LEET_TYPE, profile.getLeetType().toString());
        values.put(ProfileSqLiteHelper.COLUMN_LEET_LEVEL, profile.getLeetLevel().toString());
        values.put(ProfileSqLiteHelper.COLUMN_MODIFIER, profile.getModifier());
        values.put(ProfileSqLiteHelper.COLUMN_PREFIX, profile.getPrefix());
        values.put(ProfileSqLiteHelper.COLUMN_SUFFIX, profile.getSuffix());
        values.put(ProfileSqLiteHelper.COLUMN_URL_COMPONENT_PROTOCOL, profile.getUrlCompomentProtocol().toString());
        values.put(ProfileSqLiteHelper.COLUMN_URL_COMPONENT_SUBDOMAIN, profile.getUrlComponentSubDomain().toString());
        values.put(ProfileSqLiteHelper.COLUMN_URL_COMPONENT_DOMAIN, profile.getUrlComponentDomain().toString());
        values.put(ProfileSqLiteHelper.COLUMN_URL_COMPONENT_PORT_PARAMETERS, profile.getUrlComponentPortParameters().toString());
        values.put(ProfileSqLiteHelper.COLUMN_CHAR_SET_UPPERCASE, profile.hasCharSetUppercase().toString());
        values.put(ProfileSqLiteHelper.COLUMN_CHAR_SET_LOWERCASE, profile.hasCharSetLowercase().toString());
        values.put(ProfileSqLiteHelper.COLUMN_CHAR_SET_NUMBERS, profile.hasCharSetNumbers().toString());
        values.put(ProfileSqLiteHelper.COLUMN_CHAR_SET_SYMBOLS, profile.hasCharSetSymbols().toString());
        values.put(ProfileSqLiteHelper.COLUMN_CHAR_SET_COSTUM, profile.getCharSetCostum().toString());

        long insertId = database.insert(ProfileSqLiteHelper.TABLE_PROFILES, null, values);

        Cursor cursor = database.query(ProfileSqLiteHelper.TABLE_PROFILES,
                                       allColumns, ProfileSqLiteHelper.COLUMN_ID + " = " + insertId,
                                       null, null, null, null);
        cursor.moveToFirst();
        final Profile newAccount = createProfileFromCursor(cursor);
        cursor.close();

        return newAccount;
    }

    public void deleteProfile(Profile profile) {
        Log.i(Constants.LOG, "Deleting profile: " + profile);
        String name = profile.getName();

        database.delete(ProfileSqLiteHelper.TABLE_PROFILES,
                        ProfileSqLiteHelper.COLUMN_NAME + " = '" + name + "'", null);
    }

    public Cursor getAllProfilesCursor() {
        return database.query(ProfileSqLiteHelper.TABLE_PROFILES, allColumns,
                              null, null, null, null, null);
    }

    public List<Profile> getAllProfiles() {
        List<Profile> profiles = new ArrayList<Profile>();

        Cursor cursor = database.query(ProfileSqLiteHelper.TABLE_PROFILES,
                                       allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Profile profile = createProfileFromCursor(cursor);
            profiles.add(profile);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return profiles;
    }

    public String getProfileName(Cursor cursor) {
        return cursor.getString(1);
    }

    public Profile createProfileFromCursor(Cursor cursor) {
        Profile profile = new Profile();

        try {
            profile.setName(cursor.getString(1));
            profile.setUsername(cursor.getString(2));
            profile.setAlgorithm(cursor.getString(3));
            profile.setIsHMAC(Boolean.parseBoolean(cursor.getString(4)));
            profile.setLength(cursor.getInt(5));
            profile.setLeetType(cursor.getString(6));
            profile.setLeetLevel(cursor.getInt(7));
            profile.setModifier(cursor.getString(8));
            profile.setPrefix(cursor.getString(9));
            profile.setSuffix(cursor.getString(10));

            profile.setUrlCompomentProtocol(Boolean.parseBoolean(cursor.getString(11)));
            profile.setUrlComponentSubDomain(Boolean.parseBoolean(cursor.getString(12)));
            profile.setUrlComponentDomain(Boolean.parseBoolean(cursor.getString(13)));
            profile.setUrlComponentPortParameters(Boolean.parseBoolean(cursor.getString(14)));

            profile.setCharSetUppercase(Boolean.parseBoolean(cursor.getString(15)));
            profile.setCharSetLowercase(Boolean.parseBoolean(cursor.getString(16)));
            profile.setCharSetNumbers(Boolean.parseBoolean(cursor.getString(17)));
            profile.setCharSetSymbols(Boolean.parseBoolean(cursor.getString(18)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return profile;
    }

    public Profile getProfileByName(String profileName) throws ProfileNotFound {
        Log.d(Constants.LOG, "Searching for profile by name: " + profileName);
        List<Profile> listProfiles = getAllProfiles();

        for(Profile p : listProfiles) {

            if(p.getName().equals(profileName)) {
                Log.d(Constants.LOG, "Found profile " + p.getName());
                return p;
            }
        }
        throw new ProfileNotFound("Profile with name " + profileName + " was not found");
    }

    public List<SpinnerProfile> getAllLabels() {
        List<SpinnerProfile> labels = new ArrayList<SpinnerProfile>();
        Cursor cursor = database.query(ProfileSqLiteHelper.TABLE_PROFILES,
                                       allColumns, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                labels.add ( new SpinnerProfile ( cursor.getString(1) , cursor.getString(2) ) );
            } while (cursor.moveToNext());
        }

        // Make sure to close the cursor
        cursor.close();
        return labels;
    }

    public boolean profileExists(String name) {
        try {
            getProfileByName(name);
        } catch (ProfileNotFound e) {
            return false;
        }
        return true;
    }

    public void replaceProfile(Profile p) {
        deleteProfile(p);
        insertProfile(p);
    }
}
