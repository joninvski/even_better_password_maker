package com.pifactorial;

import java.util.ArrayList;
import java.util.List;

import org.daveware.passwordmaker.Profile;
import org.daveware.passwordmaker.AlgorithmType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ProfileDataSource {
    private static final String TAG = "ProfileDataSource";

    // Database fields
    private SQLiteDatabase database;
    private ProfileSqLiteHelper dbHelper;
    private String[] allColumns = { ProfileSqLiteHelper.COLUMN_ID,
        ProfileSqLiteHelper.COLUMN_ALGORITHM, ProfileSqLiteHelper.COLUMN_NAME };

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

        values.put(ProfileSqLiteHelper.COLUMN_NAME,        profile.getName());
        values.put(ProfileSqLiteHelper.COLUMN_USERNAME,    profile.getUsername());
        values.put(ProfileSqLiteHelper.COLUMN_ALGORITHM,   profile.getAlgorithm().toString());
        values.put(ProfileSqLiteHelper.COLUMN_LENGTH,      Integer.toString(profile.getLength()));
        values.put(ProfileSqLiteHelper.COLUMN_LEET_TYPE,   profile.getLeetType().toString());
        values.put(ProfileSqLiteHelper.COLUMN_LEET_LEVEL,  profile.getLeetLevel().toString());
        values.put(ProfileSqLiteHelper.COLUMN_MODIFIER,    profile.getModifier());
        values.put(ProfileSqLiteHelper.COLUMN_PREFIX,      profile.getPrefix());
        values.put(ProfileSqLiteHelper.COLUMN_SUFFIX,      profile.getSuffix());
        values.put(ProfileSqLiteHelper.COLUMN_ALGORITHM,   profile.getAlgorithm().toString());

        long insertId = database.insert(ProfileSqLiteHelper.TABLE_PROFILES, null,
                values);

        Cursor cursor = database.query(ProfileSqLiteHelper.TABLE_PROFILES,
                allColumns, ProfileSqLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Profile newAccount = cursorToAccount(cursor);
        cursor.close();

        return newAccount;
    }

    public void deleteProfile(Profile account) {
        long id = Long.parseLong(account.getName());
        System.out.println("Profile deleted with id: " + id);
        database.delete(ProfileSqLiteHelper.TABLE_PROFILES, ProfileSqLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public Cursor getAllProfilesCursor() {
        return database.query(ProfileSqLiteHelper.TABLE_PROFILES,
                allColumns, null, null, null, null, null);
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

    private Profile cursorToAccount(Cursor cursor) {
        Profile account = new Profile();
        account.setName(Long.toString(cursor.getLong(0)));

        try {
            AlgorithmType algorithmType = AlgorithmType.fromRdfString(cursor.getString(1));
            account.setAlgorithm(algorithmType);
        } catch (Exception e) {
            Log.i(TAG, "Algorithm type does not exits with name: " + cursor.getString(1));
            e.printStackTrace();
        }       
        return account;
    }
} 
