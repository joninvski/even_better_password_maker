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
    private static final String TAG = ProfileDataSource.class.getName();

    // Database fields
    private SQLiteDatabase database;
    private ProfileSqLiteHelper dbHelper;
    private String[] allColumns = { ProfileSqLiteHelper.COLUMN_ID,
        ProfileSqLiteHelper.COLUMN_NAME,
        ProfileSqLiteHelper.COLUMN_USERNAME,
        ProfileSqLiteHelper.COLUMN_ALGORITHM,
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
        ProfileSqLiteHelper.COLUMN_CHAR_SET_COSTUM };

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
        // Prepare the contentor to insert values
        ContentValues values = new ContentValues();

        values.put(ProfileSqLiteHelper.COLUMN_NAME, profile.getName());
        values.put(ProfileSqLiteHelper.COLUMN_USERNAME, profile.getUsername());
        values.put(ProfileSqLiteHelper.COLUMN_ALGORITHM, profile.getAlgorithm()
                .toString());
        values.put(ProfileSqLiteHelper.COLUMN_LENGTH,
                Integer.toString(profile.getLength()));
        values.put(ProfileSqLiteHelper.COLUMN_LEET_TYPE, profile.getLeetType()
                .toString());
        values.put(ProfileSqLiteHelper.COLUMN_LEET_LEVEL, profile
                .getLeetLevel().toString());
        values.put(ProfileSqLiteHelper.COLUMN_MODIFIER, profile.getModifier());
        values.put(ProfileSqLiteHelper.COLUMN_PREFIX, profile.getPrefix());
        values.put(ProfileSqLiteHelper.COLUMN_SUFFIX, profile.getSuffix());
        values.put(ProfileSqLiteHelper.COLUMN_URL_COMPONENT_PROTOCOL, profile
                .getUrlCompomentProtocol().toString());
        values.put(ProfileSqLiteHelper.COLUMN_URL_COMPONENT_SUBDOMAIN, profile
                .getUrlComponentSubDomain().toString());
        values.put(ProfileSqLiteHelper.COLUMN_URL_COMPONENT_DOMAIN, profile
                .getUrlComponentDomain().toString());
        values.put(ProfileSqLiteHelper.COLUMN_URL_COMPONENT_PORT_PARAMETERS,
                profile.getUrlComponentPortParameters().toString());
        values.put(ProfileSqLiteHelper.COLUMN_CHAR_SET_UPPERCASE, profile
                .getCharSetUppercase().toString());
        values.put(ProfileSqLiteHelper.COLUMN_CHAR_SET_LOWERCASE, profile
                .getCharSetLowercase().toString());
        values.put(ProfileSqLiteHelper.COLUMN_CHAR_SET_NUMBERS, profile
                .getCharSetNumbers().toString());
        values.put(ProfileSqLiteHelper.COLUMN_CHAR_SET_SYMBOLS, profile
                .getCharSetSymbols().toString());
        values.put(ProfileSqLiteHelper.COLUMN_CHAR_SET_COSTUM, profile
                .getCharSerCostum().toString());

        long insertId = database.insert(ProfileSqLiteHelper.TABLE_PROFILES,
                null, values);

        Cursor cursor = database.query(ProfileSqLiteHelper.TABLE_PROFILES,
                allColumns, ProfileSqLiteHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        Profile newAccount = cursorToAccount(cursor);
        cursor.close();

        return newAccount;
    }

    public void deleteProfile(Profile account) {
        long id = Long.parseLong(account.getName());
        System.out.println("Profile deleted with id: " + id);
        database.delete(ProfileSqLiteHelper.TABLE_PROFILES,
                ProfileSqLiteHelper.COLUMN_ID + " = " + id, null);
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
            Profile profile = cursorToAccount(cursor);
            profiles.add(profile);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return profiles;
    }

    public Profile cursorToAccount(Cursor cursor) {
        Profile account = new Profile();

        // values.put(ProfileSqLiteHelper.COLUMN_URL_COMPONENT_PROTOCOL,         profile.getUrlCompomentProtocol().toString());
        // values.put(ProfileSqLiteHelper.COLUMN_URL_COMPONENT_SUBDOMAIN,        profile.getUrlComponentSubDomain().toString());

        try {
            account.setName(cursor.getString(1));
            account.setUsername(cursor.getString(2));
            account.setAlgorithm(cursor.getString(3));
            account.setLength(cursor.getInt(4));
            account.setLeetType(cursor.getString(5));
            account.setLeetLevel(cursor.getInt(6));
            account.setModifier(cursor.getString(7));
            account.setPrefix(cursor.getString(8));
            account.setSuffix(cursor.getString(9));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return account;
    }

    public Profile getProfileByName(String profileName) throws ProfileNotFound {
        List<Profile> listProfiles = getAllProfiles();

        for(Profile p : listProfiles)
        {
            if(p.getName().equalsIgnoreCase(profileName)){
                return p;
            }
        }
        throw new ProfileNotFound("Profile with name " + profileName + " was not found");
    }
} 
