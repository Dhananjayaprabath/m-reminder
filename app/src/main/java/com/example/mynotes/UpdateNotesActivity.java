package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateNotesActivity extends AppCompatActivity {

    EditText title,description,time,date,days,colours;
    Button updateNotes;
    String id, noti;
    DatePickerDialog.OnDateSetListener setListener;
    int hour1,minite2;
    ImageButton buttond;
    int year1,month1,day1;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notes);

        buttond =findViewById(R.id.done1);



        RadioGroup rg = (RadioGroup) findViewById(R.id.radio);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio1:
                        colours.setText("#3DDC84");
                        break;
                    case R.id.radio2:
                        colours.setText("#A18DFC");
                        break;
                    case R.id.radio3:
                        colours.setText("#F6618F");

                        break;
                }
            }
        });



        title=findViewById(R.id.title);
        description=findViewById(R.id.description);
        time=findViewById(R.id.time);
        date=findViewById(R.id.date2);
        colours=findViewById(R.id.colour1);
        days=findViewById(R.id.days);

        updateNotes=findViewById(R.id.updateNote);

        Calendar calendar = Calendar.getInstance();
        final int year =calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DATE);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateNotesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year, month, dayOfMonth);
                                year1 =year;
                                month1 =month;
                                day1 =dayOfMonth;

                                date.setText(DateFormat.format(" yyyy-MM-dd ", calendar));
                                days.setText(DateFormat.format(" EEE\ndd ", calendar));
                            }
                        },year, month, day
                );
                datePickerDialog.show();




            }

        });


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog= new TimePickerDialog(UpdateNotesActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hour1 = hourOfDay;
                                minite2 = minute;
                                Calendar calendar1 = Calendar.getInstance();

                                calendar1.set(0, 0, 0, hour1, minite2);
                                time.setText(DateFormat.format(" hh:mm aa ", calendar1));

                            }
                        },12,0,false
                );
                timePickerDialog.show();
                timePickerDialog.updateTime(hour1,minite2);

            }
        });


        Intent i =getIntent();
        title.setText(i.getStringExtra("title"));
        description.setText(i.getStringExtra("description"));
        time.setText(i.getStringExtra("time"));
        date.setText(i.getStringExtra("date"));
        days.setText(i.getStringExtra("day"));
        colours.setText(i.getStringExtra("colours"));
        id=i.getStringExtra("id");
        noti=i.getStringExtra("noti");

        updateNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence potentialUrl = null;
                if (Patterns.WEB_URL.matcher(description.getText().toString()).matches()) {

                    if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString())) {

                        DatabaseClass db = new DatabaseClass(UpdateNotesActivity.this);
                        db.updateNotes(title.getText().toString(), description.getText().toString(), time.getText().toString(), date.getText().toString(), id, days.getText().toString(), colours.getText().toString(),noti);

                        setAlarm();
                        Intent i = new Intent(UpdateNotesActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();


                    } else {
                        Toast.makeText(UpdateNotesActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    Toast.makeText(UpdateNotesActivity.this, "Wrong Meeting Link", Toast.LENGTH_SHORT).show();
                    description.setBackgroundColor(Color.parseColor("#F6B4C0"));
                }

            }
        });
buttond.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        CharSequence potentialUrl = null;
        if (Patterns.WEB_URL.matcher(description.getText().toString()).matches()) {

            if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString())) {

                DatabaseClass db = new DatabaseClass(UpdateNotesActivity.this);
                db.updateNotes(title.getText().toString(), description.getText().toString(), time.getText().toString(), date.getText().toString(), id, days.getText().toString(), colours.getText().toString(),noti);

                setAlarm();
                Intent i = new Intent(UpdateNotesActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();


            } else {
                Toast.makeText(UpdateNotesActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
            }
        }else {
            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(100);
            Toast.makeText(UpdateNotesActivity.this, "Wrong Meeting Link", Toast.LENGTH_SHORT).show();
            description.setBackgroundColor(Color.parseColor("#F6B4C0"));
        }

    }
});

    }
    private void setAlarm() {
        alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent1 =new Intent(getApplicationContext(),AlarmReceiver.class);
        intent1.putExtra("notitext",title.getText().toString());
        intent1.putExtra("notidate", time.getText().toString());
        intent1.putExtra("notiid",noti.toString());
        int notifiid =Integer.parseInt(noti);

        PendingIntent pendingIntent= PendingIntent.getBroadcast(getApplicationContext(),notifiid,intent1,PendingIntent.FLAG_ONE_SHOT);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(0, 0, 0, hour1, minite2);

        Calendar calendar3 =Calendar.getInstance();
        calendar3.set(year1,month1,day1,hour1,minite2);




        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar3.getTimeInMillis()-1000*60*5,AlarmManager
                .INTERVAL_DAY,pendingIntent);

        Toast.makeText(this,"Update Remind",Toast.LENGTH_SHORT).show();





    }




}

