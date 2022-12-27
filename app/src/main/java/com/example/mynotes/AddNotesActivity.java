package com.example.mynotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import cn.iwgang.countdownview.CountdownView;

public class AddNotesActivity extends  AppCompatActivity  {

    EditText title, description,time,date,days,colours;
    Button addNote;
    int hour1,minite2;
    int year1,month1,day1;
    ImageButton buttond;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        createNotificationChannel();

        buttond = findViewById(R.id.done);
        RadioGroup rg = (RadioGroup) findViewById(R.id.radio);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
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






        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        time = findViewById(R.id.time1);
        date = findViewById(R.id.date1);
        days = findViewById(R.id.days);
        colours = findViewById(R.id.colour1);

        addNote = findViewById(R.id.addNote);

        Calendar calendar = Calendar.getInstance();


        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DATE);

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minite = calendar.get(Calendar.MINUTE);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddNotesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year, month, dayOfMonth);
                                year1 =year;
                                month1 =month;
                                day1 =dayOfMonth;


                                date.setText(DateFormat.format(" yyyy-MM-dd ", calendar));
                                days.setText(DateFormat.format(" EEE dd ", calendar));


                            }

                        }, year, month, day
                );
                datePickerDialog.show();



            }

        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddNotesActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hour1 = hourOfDay;
                                minite2 = minute;
                                Calendar calendar1 = Calendar.getInstance();


                                calendar1.set(0, 0, 0, hour1, minite2);




                                time.setText(DateFormat.format(" hh:mm aa ", calendar1));

                            }


                        }, 12, 0, false
                );

                timePickerDialog.show();
                timePickerDialog.updateTime(hour1, minite2);
                final Calendar c = Calendar.getInstance();
               


            }
        });



        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence potentialUrl = null;
                if (Patterns.WEB_URL.matcher(description.getText().toString()).matches()){
                    if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString()) && !TextUtils.isEmpty(time.getText().toString()) && !TextUtils.isEmpty(date.getText().toString())) {
                        DatabaseClass db = new DatabaseClass(AddNotesActivity.this);
                        db.addNotes(title.getText().toString(), description.getText().toString(), time.getText().toString(), date.getText().toString(), days.getText().toString(), colours.getText().toString(),noti);



                        setAlarm();
                        Intent intent = new Intent(AddNotesActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(AddNotesActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    Toast.makeText(AddNotesActivity.this, "Wrong Meeting Link", Toast.LENGTH_SHORT).show();
                    description.setBackgroundColor(Color.parseColor("#F6B4C0"));




                }






            }
        });

        buttond.setOnClickListener(new View.OnClickListener() {
            private HttpCookie HttpUrl;

            @Override
            public void onClick(View v) {



                CharSequence potentialUrl = null;
                if (Patterns.WEB_URL.matcher(description.getText().toString()).matches()){
                    if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString()) && !TextUtils.isEmpty(time.getText().toString()) && !TextUtils.isEmpty(date.getText().toString())) {
                        DatabaseClass db = new DatabaseClass(AddNotesActivity.this);
                        db.addNotes(title.getText().toString(), description.getText().toString(), time.getText().toString(), date.getText().toString(), days.getText().toString(), colours.getText().toString(),noti);


                        setAlarm();
                        Intent intent = new Intent(AddNotesActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();



                    } else {
                        Toast.makeText(AddNotesActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    Toast.makeText(AddNotesActivity.this, "Wrong Meeting Link", Toast.LENGTH_SHORT).show();
                    description.setBackgroundColor(Color.parseColor("#F6B4C0"));

                }



            }




        });
    }

    Long tsLong = System.currentTimeMillis()/10000;
    String noti = tsLong.toString();
    int notifiid =Integer.parseInt(noti);

    private void setAlarm() {
        alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent1 =new Intent(getApplicationContext(),AlarmReceiver.class);
        intent1.putExtra("notitext",title.getText().toString());
        intent1.putExtra("notidate", time.getText().toString());
        intent1.putExtra("notiid",noti.toString());



        PendingIntent pendingIntent= PendingIntent.getBroadcast(getApplicationContext(),notifiid,intent1,PendingIntent.FLAG_ONE_SHOT);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(0, 0, 0, hour1, minite2);

        Calendar calendar3 =Calendar.getInstance();
        calendar3.set(year1,month1,day1,hour1,minite2);




        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar3.getTimeInMillis()-1000*60*5,AlarmManager
                .INTERVAL_DAY,pendingIntent);

        Toast.makeText(this,"Set Reminder",Toast.LENGTH_SHORT).show();

        



    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
           CharSequence name ="foxandroidReminderChannel";
            String description="channel for Alam manneger";
            int importance = NotificationManager.IMPORTANCE_HIGH;
           NotificationChannel channel =new NotificationChannel("Meeting Reminder",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager =getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    }
