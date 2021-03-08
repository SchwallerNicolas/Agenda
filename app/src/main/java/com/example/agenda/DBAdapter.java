package com.example.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class DBAdapter {



    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";

    public static final String KEY_ROWIDEVENT = "_id";
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
    // version 1 : création de la table People dans la db
    // version 2 : ajout de la table Events dans la db
    // version 4 : modif table Event, suppression col idParticipant
    // version 5 : modif table Event, ajout col idParticipant
    private static final int DATABASE_VERSION = 5;

    private final Context mCtx;

    private static final String CREATE_TABLE_PEOPLE =
            "CREATE TABLE if not exists " + SQLITE_TABLE_USERS + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_NAME + "," +
                    KEY_SURNAME + "," +
                    " UNIQUE (" + KEY_ROWID +"));";

    private static final String CREATE_TABLE_EVENTS =
            "CREATE TABLE if not exists " + SQLITE_TABLE_EVENTS + " (" +
                    KEY_ROWIDEVENT + " integer PRIMARY KEY autoincrement," +
                    KEY_NOMEVENT + "," +
                    KEY_DATE + "," +
                    KEY_HEUREDEB + "," +
                    KEY_HEUREFIN + "," +
                    KEY_IDPARTICIPANT + "," +
                    " UNIQUE (" + KEY_ROWIDEVENT +"));";


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

    //---------------------------USERS

    // Ajout d'un utilisateur à la bdd
    public long createPerson(Person person) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, person.getName());
        initialValues.put(KEY_SURNAME, person.getSurname());

        return mDb.insert(SQLITE_TABLE_USERS, null, initialValues);
    }

    // Supprimer un utilisateur spécifique
    public void deletePerson(String NameToDelete) {
        mDb.delete(SQLITE_TABLE_USERS, "name=?", new String[]{NameToDelete});
    }

    /*public boolean deleteAllPersons() {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE_USERS, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }*/

    // Chercher un utilisatuer par son prénom
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

    // Chercher * de la table
    public Cursor fetchAllPersons() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_USERS, new String[] {KEY_ROWID,
                        KEY_NAME, KEY_SURNAME},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //
    public ArrayList<String> getAllPersonne() {
        ArrayList<String> list=new ArrayList<String>();
        Cursor mCursor = mDb.query(SQLITE_TABLE_USERS, new String[] {KEY_ROWID,
                        KEY_NAME, KEY_SURNAME},
                null, null, null, null, null);

        if (mCursor.getCount()>0) {
            while (mCursor.moveToNext()){
                String idParticipant=mCursor.getString(mCursor.getColumnIndex("_id"));
                String nameParticipant=mCursor.getString(mCursor.getColumnIndex("name"));
                String surnameParticipant=mCursor.getString(mCursor.getColumnIndex("surname"));
                list.add(idParticipant+" "+nameParticipant+" "+surnameParticipant);
            }
        }
        return list;
    }

    //---------------------------EVENTS

    // Ajout d'un évènement à la table Event
    public long createEvent(Event event) {

        ContentValues eventValues = new ContentValues();
        eventValues.put(KEY_NOMEVENT , event.getEventName());
        eventValues.put(KEY_DATE , event.getEventDate());
        eventValues.put(KEY_HEUREDEB  , event.getEventStart());
        eventValues.put(KEY_HEUREFIN  , event.getEventEnd());
        eventValues.put(KEY_IDPARTICIPANT  , event.getIdParticipant());
        return mDb.insert(SQLITE_TABLE_EVENTS, null, eventValues);
    }

    // Supprimer un évènement
    public void deleteEvent(String eventToDelete) {
        mDb.delete(SQLITE_TABLE_EVENTS,  "nomEvent=?", new String[]{eventToDelete});
    }

    // Checher tous les évènements (cf. EventListActivity)
    public Cursor fetchAllEvents() {

        Cursor mCursor = mDb.query(SQLITE_TABLE_EVENTS, new String[] {KEY_ROWIDEVENT,
                        KEY_NOMEVENT, KEY_DATE, KEY_HEUREDEB, KEY_HEUREFIN},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // Chercher un évènement spécifique à partir d'un paramètre
    // Utilisé l'id de l'utilisateur cliqué
    // intent.putExtra() dans ItemOnClick() (cf. HomePageActivity)
    public Cursor fetchYourEvents(String whoseEvent) throws SQLException {
        Log.w(TAG, whoseEvent);
        Cursor mCursor = null;
        if (whoseEvent == null  ||  whoseEvent.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_EVENTS, new String[] {KEY_ROWIDEVENT,
                        KEY_NOMEVENT, KEY_DATE, KEY_HEUREDEB, KEY_HEUREFIN, KEY_IDPARTICIPANT},
                    null, null, null, null, null);

        } else {
            String where = KEY_IDPARTICIPANT + "=?";
            String[] whereArgs = {whoseEvent};
            mCursor = mDb.query(true, SQLITE_TABLE_EVENTS, new String[] {KEY_ROWIDEVENT,
                            KEY_NOMEVENT, KEY_DATE, KEY_HEUREDEB, KEY_HEUREFIN, KEY_IDPARTICIPANT},
                    where, whereArgs,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public ArrayList<String> CompareEvents(String compareDate) throws SQLException {
        Log.w(TAG, compareDate);
        ArrayList<String> list=new ArrayList<String>();
        Cursor mCursor = null;
        if (compareDate == null  ||  compareDate.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE_EVENTS, new String[] {KEY_ROWIDEVENT,
                            KEY_NOMEVENT, KEY_DATE, KEY_HEUREDEB, KEY_HEUREFIN, KEY_IDPARTICIPANT},
                    null, null, null, null, null);

        } else {
            String where = KEY_DATE + "=?";
            String[] whereArgs = {compareDate};
            mCursor = mDb.query(true, SQLITE_TABLE_EVENTS, new String[] {KEY_ROWIDEVENT,
                             KEY_DATE, KEY_HEUREDEB, KEY_HEUREFIN},
                    where, whereArgs,
                    null, null, null, null);
        }
        if (mCursor.getCount()>0) {
            while (mCursor.moveToNext()){
                String idEvent=mCursor.getString(mCursor.getColumnIndex("_id"));
                String Date=mCursor.getString(mCursor.getColumnIndex("Date"));
                String heureDebut=mCursor.getString(mCursor.getColumnIndex("heureDebut"));
                String heureFin=mCursor.getString(mCursor.getColumnIndex("heureFin"));
                list.add(idEvent+" "+Date+" "+heureDebut+" "+heureFin);
            }
        }
        return list;

    }

    public ArrayList<String> fetchYourEvent(String whoseEventId) throws SQLException {
        Log.w(TAG, whoseEventId);
        ArrayList<String> list = new ArrayList<String>();
        Cursor mCursor = null;
        if (whoseEventId == null || whoseEventId.length() == 0) {
            mCursor = mDb.query(SQLITE_TABLE_EVENTS, new String[]{KEY_ROWIDEVENT,
                            KEY_NOMEVENT, KEY_DATE, KEY_HEUREDEB, KEY_HEUREFIN, KEY_IDPARTICIPANT},
                    null, null, null, null, null);

        } else {
            String where = KEY_IDPARTICIPANT + "=?";
            String[] whereArgs = {whoseEventId};
            mCursor = mDb.query(true, SQLITE_TABLE_EVENTS, new String[]{KEY_ROWIDEVENT,
                            KEY_NOMEVENT, KEY_DATE, KEY_HEUREDEB, KEY_HEUREFIN, KEY_IDPARTICIPANT},
                    where, whereArgs,
                    null, null, null, null);
        }
        if (mCursor.getCount() > 0) {
            while (mCursor.moveToNext()) {
                String idEvenement = mCursor.getString(mCursor.getColumnIndex("_id"));
                String nomEvenement = mCursor.getString(mCursor.getColumnIndex("nonEvent"));
                String dateEvenement = mCursor.getString(mCursor.getColumnIndex("Date"));
                String debutEvenement = mCursor.getString(mCursor.getColumnIndex("heureDebut"));
                String finEvenement = mCursor.getString(mCursor.getColumnIndex("heureFin"));
                String idMit = mCursor.getString(mCursor.getColumnIndex("idParticipant"));
                list.add(idEvenement + " " + nomEvenement + " " + dateEvenement + " " + debutEvenement + " " + finEvenement + " " + idMit);
            }
        }
        return list;
    }

}

