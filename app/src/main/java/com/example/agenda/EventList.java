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
    private SimpleCursorAdapter dataAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // déclaration de la barre d'activité
        Toolbar toolbar = findViewById(R.id.maBarreOutils2);
        super.setActionBar(toolbar);
    }

    private void DisplayListViev() {

        ImageView versAjoutEvent = findViewById(R.id.imageView3);
        versAjoutEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventList.this, AjoutEvent.class);
                //intent.putExtra("DB", (Serializable) dbHelper);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            //displayListView();
            dataAdapter.notifyDataSetChanged();
        }
    }
}