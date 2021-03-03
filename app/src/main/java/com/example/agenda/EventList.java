package com.example.agenda;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.Toolbar;

public class EventList extends AppCompatActivity {

    public DBAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // déclaration de la barre d'activité
        Toolbar toolbar = findViewById(R.id.maBarreOutils2);
        super.setActionBar(toolbar);

        dbHelper = new DBAdapter(this);
        dbHelper.open();

        dbHelper.deleteAllEvents();
        dbHelper.insertSomeEvents();

        DisplayEventListView();
    }

    /*
    public static final String KEY_NOMEVENT = "nomEvent"; EventName
    public static final String KEY_DATE = "Date"; EventDate
    public static final String KEY_HEUREDEB = "heureDebut"; EventStart
    public static final String KEY_HEUREFIN = "heureFin"; EventEnd
    */
    private void DisplayEventListView() {

        Cursor cursor = dbHelper.fetchAllEvents();

        String[] Eventcolumns = new String[] {
                DBAdapter.KEY_NOMEVENT,
                DBAdapter.KEY_DATE,
                DBAdapter.KEY_HEUREDEB,
                DBAdapter.KEY_HEUREFIN,
        };

        int[] tp = new int[] {
                R.id.EventName,
                R.id.EventDate,
                R.id.EventStart,
                R.id.EventEnd,
        };

        dataAdapter2 = new SimpleCursorAdapter( this, R.layout.event_info,
                cursor, Eventcolumns, tp, 0);

        ListView eventListview = (ListView) findViewById(R.id.listView2);
        eventListview.setAdapter(dataAdapter2);

        /* Afficher le noms des participants ? JSP si possible
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> listView, View view,
                                   int position, long id) {
               //get the cursor, positioned to corresponding row in the result set
               Cursor cursor = (Cursor)
                       listView.getItemAtPosition(position);
               // Get the person's name from the row in the database
               String personName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
               Toast.makeText(getApplicationContext(),
                       personName, Toast.LENGTH_SHORT).show();
           }
       });*/


        ImageView versAjoutEvent = findViewById(R.id.imageView3);
        versAjoutEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventList.this, AjoutEvent.class);
                startActivityForResult(intent,0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            //displayListView();
            dataAdapter2.notifyDataSetChanged();
        }
    }
}