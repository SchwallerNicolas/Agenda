package com.example.agenda;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.Toolbar;

public class HomePageActivity extends AppCompatActivity {

    //
    public DBAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // déclaration de la barre d'activité
        Toolbar toolbar = findViewById(R.id.maBarreOutils);
        super.setActionBar(toolbar);

        dbHelper = new DBAdapter(this);
        dbHelper.open();

        // "Nettoyer" la bdd
        dbHelper.deleteAllPersons();
        // Ajouter des données à la bdd
        dbHelper.insertSomePersons();

        // Générer une ListView de SQLite DB
        displayListView();
    }

    // Montrer les personnes de la bdd dans une ListView
    private void displayListView() {
        Cursor cursor = dbHelper.fetchAllPersons();

        //Colonnes à lier
        String[] columns = new String[] {
                DBAdapter.KEY_NAME,
                DBAdapter.KEY_SURNAME,
        };

        // XML views définis avec lesquels est lié la donnée
        int[] to = new int[] {
                R.id.name,
                R.id.surname,
        };

        // Créer l'adapter avec un cursor pointant vers la donnée désirée
        //avec l'information du layout aussi (forme dans la liste)
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.person_info,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listView1);
        // Adapter vers ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                //get the cursor, positioned to corresponding row in the result set
                Cursor cursor = (Cursor)
                        listView.getItemAtPosition(position);

                /*Get the person's name from the row in the database
                String personName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                Toast.makeText(getApplicationContext(),
                        personName, Toast.LENGTH_SHORT).show();*/

                Toast.makeText(HomePageActivity.this, "Long click to delete", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePageActivity.this, EventList.class);
                startActivityForResult(intent,1);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //get the cursor, positioned to corresponding row in the result set
                Cursor cursor = (Cursor)
                        listView.getItemAtPosition(position);
                // Get the person's name from the row in the database
                String NameToDelete = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                dbHelper.deletePerson(NameToDelete);
                Toast.makeText(HomePageActivity.this, "Delete "+parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                displayListView();
                dataAdapter.notifyDataSetChanged();
                return false;
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchPersonsByName(constraint.toString());
            }
        });

        ImageView versAddUser = findViewById(R.id.imageView2);
        versAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, AddUser.class);
                //intent.putExtra("DB", (Serializable) dbHelper);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            displayListView();
            dataAdapter.notifyDataSetChanged();
        }
    }
}



