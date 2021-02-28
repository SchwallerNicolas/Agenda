package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText eName;
    private EditText ePassword;
    private Button eLogin;
    private TextView eAttemptsInfo;

    private final String Username="Admin";
    private final String Password="123";

    boolean isValid=false;
    private int counter=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eName=findViewById(R.id.idName);
        ePassword=findViewById(R.id.idPassword);
        eLogin=findViewById(R.id.buttonLogin);
        eAttemptsInfo=findViewById(R.id.tvAttempsInfo);

        eLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
             String imputName=eName.getText().toString();
             String imputPassword=ePassword.getText().toString();

             if(imputName.isEmpty() || imputPassword.isEmpty())
             {
                 Toast.makeText(MainActivity.this, "Please enter all the details correctly!", Toast.LENGTH_SHORT).show();
             }else{
                 isValid=validate(imputName, imputPassword);
                 if(!isValid){
                     counter--;
                     Toast.makeText(MainActivity.this, "Incorrect credentials entered!", Toast.LENGTH_SHORT).show();
                     eAttemptsInfo.setText("Nb of attemps remaining: "+counter);

                     if(counter==0){
                         eLogin.setEnabled(false);
                     }
                 }else{
                     Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                     //add the code to the new activity
                     Intent intent=new Intent(MainActivity.this, HomePageActivity.class);
                     startActivity(intent);
                 }
             }
            }
        });
    }

    private boolean validate(String name, String password){
        boolean res=false;
        if(name.equals(Username) && password.equals(Password)){
            res= true;
        }
        return res;
    }
}