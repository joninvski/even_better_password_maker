package com.pifactorial;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProfileSqLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_PROFILES                       = "profiles";
    public static final String COLUMN_ID                            = "_id";
    public static final String COLUMN_NAME                          = "name";
    public static final String COLUMN_USERNAME                      = "username";
    public static final String COLUMN_ALGORITHM                     = "algorithm";
    public static final String COLUMN_LENGTH                        = "length";
    public static final String COLUMN_LEET_TYPE                     = "leet_type";
    public static final String COLUMN_LEET_LEVEL                    = "leet_level";
    public static final String COLUMN_MODIFIER                      = "modifiers";
    public static final String COLUMN_PREFIX                        = "prefix";
    public static final String COLUMN_SUFFIX                        = "suffix";

    // URL COMPONENTS
    public static final String COLUMN_URL_COMPONENT_PROTOCOL        = "url_comp_protocol";
    public static final String COLUMN_URL_COMPONENT_SUBDOMAIN       = "url_comp_subdomain";
    public static final String COLUMN_URL_COMPONENT_DOMAIN          = "url_comp_domain";
    public static final String COLUMN_URL_COMPONENT_PORT_PARAMETERS = "url_comp_port";

    // CHARACTER SET
    public static final String COLUMN_CHAR_SET_UPPERCASE            = "char_set_upper";
    public static final String COLUMN_CHAR_SET_LOWERCASE            = "char_set_lower";
    public static final String COLUMN_CHAR_SET_NUMBERS              = "char_set_numbers";
    public static final String COLUMN_CHAR_SET_SYMBOLS              = "char_set_symbols";
    public static final String COLUMN_CHAR_SET_COSTUM               = "char_set_costum";

    private static final String DATABASE_NAME = "profiles.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PROFILES                       + "("
            + COLUMN_ID                            + " integer primary key autoincrement, "
            + COLUMN_NAME                          + " text not null, "
            + COLUMN_USERNAME                      + " text not null, "
            + COLUMN_ALGORITHM                     + " text not null, "
            + COLUMN_LENGTH                        + " integer not null, "
            + COLUMN_LEET_TYPE                     + " text not null, "
            + COLUMN_LEET_LEVEL                    + " integer not null, "
            + COLUMN_MODIFIER                      + " text not null, "
            + COLUMN_PREFIX                        + " text not null, "
            + COLUMN_SUFFIX                        + " text not null, "
            + COLUMN_URL_COMPONENT_PROTOCOL        + " boolean not null, "
            + COLUMN_URL_COMPONENT_SUBDOMAIN       + " boolean not null, "
            + COLUMN_URL_COMPONENT_DOMAIN          + " boolean not null, "
            + COLUMN_URL_COMPONENT_PORT_PARAMETERS + " boolean not null, "
            + COLUMN_CHAR_SET_UPPERCASE            + " boolean not null, "
            + COLUMN_CHAR_SET_LOWERCASE            + " boolean not null, "
            + COLUMN_CHAR_SET_NUMBERS              + " boolean not null, "
            + COLUMN_CHAR_SET_SYMBOLS              + " boolean not null, "
            + COLUMN_CHAR_SET_COSTUM               + " text not null "
            + ");";

    public ProfileSqLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(Constants.LOG, "Creating database: " + DATABASE_CREATE);
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ProfileSqLiteHelper.class.getName(),
              "Upgrading database from version " + oldVersion + " to "
              + newVersion + ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        //onCreate(db);
    }
}
