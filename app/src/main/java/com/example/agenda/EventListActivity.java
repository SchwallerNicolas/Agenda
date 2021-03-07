package com.example.agenda;
//Activity
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.Toolbar;


public class EventListActivity extends AppCompatActivity {

    static SimpleCursorAdapter dataAdapter2;

    private String belongsTo;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // déclaration de la barre d'activité
        Toolbar toolbar = findViewById(R.id.maBarreOutils2);
        super.setActionBar(toolbar);

        belongsTo = getIntent().getStringExtra("belongs");
        HomePageActivity.dbHelper.open();


        DisplayEventListView();

        ImageView versAjoutEvent = findViewById(R.id.imageView3);
        versAjoutEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(EventListActivity.this, AjoutEventActivity.class);
                intent2.putExtra("belongs2", belongsTo);
                startActivityForResult(intent2,2);
            }
        });
    }

    private void DisplayEventListView() {

        //Cursor cursor = HomePageActivity.dbHelper.fetchAllEvents();
        Cursor cursor = HomePageActivity.dbHelper.fetchYourEvents(belongsTo);

            String[] Eventcolumns = new String[]{
                    DBAdapter.KEY_NOMEVENT,
                    DBAdapter.KEY_DATE,
                    DBAdapter.KEY_HEUREDEB,
                    DBAdapter.KEY_HEUREFIN,
            };

            int[] boundTo2 = new int[]{
                    R.id.EventName,
                    R.id.EventDate,
                    R.id.EventStart,
                    R.id.EventEnd,
            };

            dataAdapter2 = new SimpleCursorAdapter(this, R.layout.event_info,
                    cursor,
                    Eventcolumns,
                    boundTo2,
                    0);

        ListView eventListview = (ListView) findViewById(R.id.listView2);
        eventListview.setAdapter(dataAdapter2);

        eventListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                //Avoir le cursor lié à la ligne qui lui correspond
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                Toast.makeText(EventListActivity.this, "Longer click to delete", Toast.LENGTH_SHORT).show();
            }
        });

        eventListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //get the cursor, positioned to corresponding row in the result set
                Cursor cursor = (Cursor) eventListview.getItemAtPosition(position);
                String EventoDelete = cursor.getString(cursor.getColumnIndexOrThrow("nomEvent"));
                HomePageActivity.dbHelper.deleteEvent(EventoDelete);
                Toast.makeText(EventListActivity.this, "Deleted "+parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                DisplayEventListView();
                dataAdapter2.notifyDataSetChanged();
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            DisplayEventListView();
            dataAdapter2.notifyDataSetChanged();
        }
    }
}