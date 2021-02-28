package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InsertData extends AppCompatActivity {
    private EditText et1;
    private EditText et2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        et1=(EditText)findViewById(R.id.t1);
        et2=(EditText)findViewById(R.id.t2);
    }

    public void addRecord(View view)
    {
        DbManager db=new DbManager(this);
        String imputName=et1.getText().toString();
        String imputFirstname=et2.getText().toString();

        if(imputName.isEmpty() || imputFirstname.isEmpty())
        {
            Toast.makeText(InsertData.this, "Please enter all the details correctly!", Toast.LENGTH_SHORT).show();
        }else {
            String res = db.addRecord(et1.getText().toString(), et2.getText().toString());
            Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
            et1.setText("");
            et2.setText("");
        }
        Intent intent=new Intent(InsertData.this, HomePageActivity.class);
        startActivity(intent);
    }
}