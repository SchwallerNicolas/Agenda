package com.example.agenda;
//Activity
import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddUserActivity extends HomePageActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Button monBouton = findViewById(R.id.buttonAdd);
        TextView nameEntered = findViewById(R.id.editTextTextPersonName);
        TextView surnameEntered = findViewById(R.id.editTextTextPersonSurname);

        monBouton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String monPrenom = nameEntered.getText().toString();
                String monNom = surnameEntered.getText().toString().toUpperCase();

                if(monPrenom.isEmpty() || monNom.isEmpty())
                {
                    Toast.makeText(AddUserActivity.this, "Veuillez rentrer les informations correctement !", Toast.LENGTH_SHORT).show();
                }else {
                    dbHelper.createPerson(new Person(monPrenom, monNom));
                    Toast.makeText(AddUserActivity.this, "Utilisateur ajouté", Toast.LENGTH_SHORT).show();

                }
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
            }
        });
    }



}