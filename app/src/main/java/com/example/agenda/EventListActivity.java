package com.example.agenda;
//Activity
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.Toolbar;

import static com.example.agenda.HomePageActivity.*;


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
        dbHelper.open();


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
        Cursor cursor = dbHelper.fetchYourEvents(belongsTo);

        String[] Eventcolumns = new String[]{
                DBAdapter.KEY_NOMEVENT,
                DBAdapter.KEY_DATE,
                DBAdapter.KEY_HEUREDEB,
                DBAdapter.KEY_HEUREFIN,
                DBAdapter.KEY_ISRAPPEL,
        };

        int[] boundTo2 = new int[]{
                R.id.EventName,
                R.id.EventDate,
                R.id.EventStart,
                R.id.EventEnd,
                R.id.checkBox,
        };

        dataAdapter2 = new MyCursorAdapter(this, R.layout.event_info,
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
        }

        );

        eventListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //get the cursor, positioned to corresponding row in the result set
                Cursor cursor = (Cursor) eventListview.getItemAtPosition(position);
                String EventoDelete = cursor.getString(cursor.getColumnIndexOrThrow("nomEvent"));
                dbHelper.deleteEvent(EventoDelete);
                Toast.makeText(EventListActivity.this, "Deleted "+parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                DisplayEventListView();
                dataAdapter2.notifyDataSetChanged();
                return false;
            }
        });


    }

    private class MyCursorAdapter extends SimpleCursorAdapter{

        public MyCursorAdapter(Context context, int layout, Cursor c,
                               String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //get reference to the row
            View view = super.getView(position, convertView, parent);
            //check for odd or even to set alternate colors to the row background
            /*Cursor cursor = (Cursor) eventListview.getItemAtPosition(position);
            String reminder = cursor.getString(cursor.getColumnIndexOrThrow("rappelEvent"));
            if(reminder == "1"){
                view.setBackgroundColor(Color.rgb(238, 233, 233));
            }*/
            if(position % 2 == 0){
                view.setBackgroundColor(Color.rgb(238, 233, 233));
            }
            else {
                view.setBackgroundColor(Color.rgb(255, 255, 255));
            }
            return view;
        }
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