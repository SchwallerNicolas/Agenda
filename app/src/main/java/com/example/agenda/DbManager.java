package com.example.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager extends SQLiteOpenHelper
{
    private static final String dbname="Agenda.db";

    public DbManager(Context context){
        super(context,dbname, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String qry="create table personne (id integer primary key autoincrement, name, firstname)";
        db.execSQL(qry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS personne");
        onCreate(db);
    }

    public String addRecord(String p1, String p2)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("name",p1);
        cv.put("firstname",p2);

        long res=db.insert("agenda", null, cv);
        if(res==-1)
            return "Failed";
        else
            return "Successfully inserted";
    }
}
