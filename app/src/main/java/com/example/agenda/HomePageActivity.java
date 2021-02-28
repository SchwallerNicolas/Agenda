package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePageActivity extends AppCompatActivity {

    private Button eAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        new DbManager(this);

    }

    public void startdbapp(View v)
    {
        new DbManager(this);
        startActivity(new Intent(HomePageActivity.this, InsertData.class));
    }

    public void startdbappEvent(View v)
    {
        startActivity(new Intent(HomePageActivity.this, AjoutEvent.class));
    }


}