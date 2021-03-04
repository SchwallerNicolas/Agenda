package com.example.agenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AjoutEvent extends AppCompatActivity {

    TextView tvTimer1, tvTimer2;
    int t1Hour, t1Minute, t2Hour, t2Minute;
    private CalendarView calendarView;
    private SQLiteDatabase sqLiteDatabase;
    private DBAdapter dbAdapter;
    private EditText editText;
    private Button buttonEvent;

    String selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_event);

        tvTimer1=findViewById(R.id.tv_timer1);
        tvTimer2=findViewById(R.id.tv_timer2);
        calendarView=findViewById(R.id.calendarView);
        editText= findViewById(R.id.idTextNameEvent);
        buttonEvent=findViewById(R.id.buttonEvent);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate=Integer.toString(year)+Integer.toString(month)+Integer.toString(dayOfMonth);
            }
        });



        tvTimer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(
                    AjoutEvent.this,
                    new TimePickerDialog.OnTimeSetListener(){
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            t1Hour = hourOfDay;
                            t1Minute = minute;

                            String time = t1Hour + ":" + t1Minute;
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
                    AjoutEvent.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    t2Hour = hourOfDay;
                                    t2Minute = minute;

                                    String time = t2Hour + ":" + t2Minute;
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
                String nomEvent= editText.getText().toString();
                //Date dateCalendar=

                if(nomEvent.isEmpty()|| selectedDate.isEmpty())
                {
                    Toast.makeText(AjoutEvent.this, "Please enter all the details correctly!", Toast.LENGTH_SHORT).show();
                }else {
                    //dbHelper.createEvent(nomEvent,);
                    Toast.makeText(AjoutEvent.this, "Event ajouté", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
            }
        });
    }
}