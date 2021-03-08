package com.example.agenda;
//Activity
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AjoutEventActivity extends AppCompatActivity {

    TextView tvTimer1, tvTimer2;
    int t1Hour, t1Minute, t2Hour, t2Minute;
    private CalendarView calendarView;
    private SQLiteDatabase sqLiteDatabase;
    private EditText editText;
    private Button buttonEvent;
    private Spinner spinnerParticipant;
    private Button buttonParticipant;

    ArrayList<String> listParticipant;
    ArrayList<String> listParticipantSelec=new ArrayList<String>();
    String selectedDate=null;
    String nameEvent=null;
    String heureD=null;
    String heureF=null;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_event);

        tvTimer1=findViewById(R.id.tv_timer1);
        tvTimer2=findViewById(R.id.tv_timer2);
        calendarView=findViewById(R.id.calendarView);
        editText= findViewById(R.id.idTextNameEvent);
        buttonEvent=findViewById(R.id.buttonEvent);
        spinnerParticipant=findViewById(R.id.spinner);
        buttonParticipant=findViewById(R.id.buttonParticipant);

        listParticipant=HomePageActivity.dbHelper.getAllPersonne();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, listParticipant);
        spinnerParticipant.setAdapter(adapter);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = year+"/"+(month+1)+"/"+dayOfMonth;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameEvent=editText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvTimer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(
                    AjoutEventActivity.this,
                    new TimePickerDialog.OnTimeSetListener(){
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            t1Hour = hourOfDay;
                            t1Minute = minute;
                            String time = t1Hour + ":" + t1Minute;
                            heureD=time;
                            SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                            try {
                                Date date = f24Hours.parse(time);
                                SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 12, 0, false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(t1Hour,t1Minute);
                timePickerDialog.show();
            }
        });

        tvTimer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(
                    AjoutEventActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    t2Hour = hourOfDay;
                                    t2Minute = minute;
                                    String time = t2Hour + ":" + t2Minute;
                                    heureF=time;
                                    SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                                    try {
                                        Date date = f24Hours.parse(time);
                                        SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 12, 0, false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(t2Hour,t2Minute);
                timePickerDialog.show();
            }
        });

        buttonEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(AjoutEventActivity.this, ""+selectedDate, Toast.LENGTH_SHORT).show();
                if(nameEvent==null || selectedDate==null || heureF == null || heureD == null || listParticipantSelec.isEmpty())
                {
                    Toast.makeText(AjoutEventActivity.this, "Please enter all the details correctly!", Toast.LENGTH_SHORT).show();
                }
                else if (CheckHour(heureD, heureF) == false){
                    Toast.makeText(AjoutEventActivity.this, "horaires impossibles", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(AjoutEventActivity.this, Integer.parseInt(heureD.toString().substring(0,heureD.toString().indexOf(":"))), Toast.LENGTH_SHORT).show();
                    //SimpleCursorAdapter dataAdapter3 = EventListActivity.dataAdapter2;
                    //DBAdapter.compareEvents(selectedDate);

                    /*if(CheckHour()){
                        //Toast.makeText(AjoutEventActivity.this, "horaires impossibles", Toast.LENGTH_SHORT).show();
                    }
                    else {*/
                        for(int i=0;i<listParticipantSelec.size();i++){
                            HomePageActivity.dbHelper.createEvent(new Event(nameEvent,selectedDate,heureD,heureF,listParticipantSelec.get(i)));
                        }
                        Toast.makeText(AjoutEventActivity.this, "Event ajouté", Toast.LENGTH_SHORT).show();
                    }
               // }
                Intent intent = new Intent();
                setResult(2, intent);
                finish();
            }

            public boolean CheckHour(String heureD, String heureF){
                boolean res= true;
                int h1=Integer.parseInt(heureD.substring(0,heureD.indexOf(":")));
                int m1=Integer.parseInt(heureD.substring(1,heureD.indexOf(":")));
                int h2=Integer.parseInt(heureF.substring(0,heureF.indexOf(":")));
                int m2=Integer.parseInt(heureF.substring(1,heureF.indexOf(":")));
                int heureDebut=(60*60*h1)+(60*m1);
                int heureFin=(60*60*h2)+(60*m2);
                if(heureDebut>heureFin){
                    res=false;
                }
                return res;
            }
        });

        buttonParticipant.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(listParticipantSelec.contains(spinnerParticipant.getSelectedItem().toString().substring(0,spinnerParticipant.getSelectedItem().toString().indexOf(' ')))==true){
                    Toast.makeText(AjoutEventActivity.this, "Participant déjà ajouté", Toast.LENGTH_SHORT).show();
                }
                else {
                    listParticipantSelec.add(spinnerParticipant.getSelectedItem().toString().substring(0,spinnerParticipant.getSelectedItem().toString().indexOf(' ')));
                    Toast.makeText(AjoutEventActivity.this, "Participant ajouté", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}