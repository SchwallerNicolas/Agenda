package com.example.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";

    public static final String KEY_NOMEVENT = "nomEvent";
    public static final String KEY_DATE = "Date";
    public static final String KEY_HEUREDEB = "heureDebut";
    public static final String KEY_HEUREFIN = "heureFin";
    public static final String KEY_IDPARTICIPANT = "idParticipant";

    private static final String TAG = "DbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Agenda";
    private static final String SQLITE_TABLE_USERS = "People";
    private static final String SQLITE_TABLE_EVENTS = "Events";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String CREATE_TABLE_PEOPLE =
            "CREATE TABLE if not exists " + SQLITE_TABLE_USERS + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_NAME + "," +
                    KEY_SURNAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String CREATE_TABLE_EVENTS =
            "CREATE TABLE if not exists " + SQLITE_TABLE_EVENTS + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_NOMEVENT + "," +
                    KEY_DATE + "," +
                    KEY_HEUREDEB + "," +
                    KEY_HEUREFIN + "," +
                    KEY_IDPARTICIPANT + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    public void deletePerson(String NameToDelete) {
        mDb.delete(SQLITE_TABLE_USERS,  "name=?", new String[]{NameToDelete});
        mDb.delete(SQLITE_TABLE_EVENTS,  "name=?", new String[]{NameToDelete});
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, CREATE_TABLE_PEOPLE);
            db.execSQL(CREATE_TABLE_PEOPLE);
            Log.w(TAG, CREATE_TABLE_EVENTS);
            db.execSQL(CREATE_TABLE_EVENTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS '" + SQLITE_TABLE_USERS+"'");
            db.execSQL("DROP TABLE IF EXISTS '" + SQLITE_TABLE_EVENTS+"'");
            onCreate(db);
        }
    }


    public DBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    public long createPerson(String name, String surname) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_SURNAME, surname);

        return mDb.insert(SQLITE_TABLE_USERS, null, initialValues);
    }

    /*public boolean deleteAllPersons() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_USERS, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }*/

    public Cursor fetchPersonsByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_USERS, new String[] {KEY_ROWID,
                            KEY_NAME, KEY_SURNAME},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE_USERS, new String[] {KEY_ROWID,
                            KEY_NAME, KEY_SURNAME},
                    KEY_NAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllPersons() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_USERS, new String[] {KEY_ROWID,
                        KEY_NAME, KEY_SURNAME},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /*public void insertSomePersons() {

        createPerson("Nicolas", "SCHWALLER");
        createPerson("Jochen", "LEMMENS");
        createPerson("Evan", "FROUIN");
        createPerson("Lm", "MOUSSY");
        createPerson("Richard", "CRESSOL");

    }*/

    public long createEvent(String nomEvent, String Date, String heureDebut, String heureFin) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOMEVENT , nomEvent);
        initialValues.put(KEY_DATE , Date);
        initialValues.put(KEY_HEUREDEB  , heureDebut);
        initialValues.put(KEY_HEUREFIN  , heureFin);
        //initialValues.put(KEY_IDPARTICIPANT  , idParticipant);

        return mDb.insert(SQLITE_TABLE_EVENTS, null, initialValues);
    }

    /*public boolean deleteAllEvents() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_EVENTS, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }*/

    public Cursor fetchAllEvents() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_EVENTS, new String[] {KEY_IDPARTICIPANT,
                        KEY_NOMEVENT, KEY_DATE, KEY_HEUREDEB, KEY_HEUREFIN},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeEvents() {
        createEvent("cours","02/03/2021", "8h30", "16h30");
    }

}

