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

public class HomePageActivity extends AppCompatActivity {

    public static DBAdapter dbHelper;
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

        /*//"Nettoyer" la bdd
        dbHelper.deleteAllPersons();
        //Ajouter des données à la bdd
        dbHelper.insertSomePersons();*/

        // Générer une ListView à partir des éléments de la BDD
        displayUserListView();

        ImageView versAddUser = findViewById(R.id.imageView2);
        versAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, AddUserActivity.class);
                //intent.putExtra("DB", (Serializable) dbHelper);
                startActivityForResult(intent,1);
            }
        });
    }

    private void displayUserListView() {
        // cf DBAdapter
        Cursor cursor = dbHelper.fetchAllPersons();

        //Colonnes à lier
        String[] columns = new String[] {
                DBAdapter.KEY_NAME,
                DBAdapter.KEY_SURNAME,
                DBAdapter.KEY_ROWID,
        };

        // Éléments définis dans le Layout XML person_info
        int[] boundTo = new int[] {
                R.id.name,
                R.id.surname,
        };

        // Créer l'adapter avec un cursor pointant vers la donnée désirée
        //avec l'information du layout
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.person_info,
                cursor,
                columns,
                boundTo,
                0);

        ListView listView = (ListView) findViewById(R.id.listView1);
        // Adapter vers ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                //Avoir le cursor lié à la ligne qui lui correspond
                Cursor cursor = (Cursor)
                        listView.getItemAtPosition(position);
                String belongsTo = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                Toast.makeText(HomePageActivity.this, "Longer click to delete", Toast.LENGTH_SHORT).show();
                Intent intentEvent = new Intent(HomePageActivity.this, EventListActivity.class);
                intentEvent.putExtra("belongs", belongsTo);
                startActivity(intentEvent);
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
                displayUserListView();
                dataAdapter.notifyDataSetChanged();
                return false;
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }

        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchPersonsByName(constraint.toString());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            displayUserListView();
            dataAdapter.notifyDataSetChanged();
        }
    }
}



