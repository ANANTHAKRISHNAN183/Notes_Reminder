package com.example.personal.project_android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Reminder extends AppCompatActivity {

    static int date,year,month;
    static int min,hour;
    myDbAdapter helper;
    Calendar cal;
    RelativeLayout rl;
    EditText et,et1;
    Button bt;
    String s,text,day,time;
    Spinner sp1,sp2;
    int click_times=0;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder);
        Intent in= getIntent();
        getSupportActionBar().setTitle("Reminder");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reminder_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        rl = (RelativeLayout) findViewById(R.id.rl);
        switch (item.getItemId()) {
            case R.id.show_reminders:
                Intent j= new Intent(Reminder.this,Show_Reminders.class);
                startActivity(j);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void voiceClick(View view)
    {
        if(view.getId()==R.id.imageButton)
        {
            promptSpeechInput();
        }
    }

    public void promptSpeechInput()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something!");
// Start the activity, the intent will be populated with the speech text
        try {
            count=0;
            startActivityForResult(intent,100);
        }
        catch(ActivityNotFoundException a)
        {
            Toast.makeText(Reminder.this,"Sorry your device does not support speech language",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK)
        {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            bt=(Button) findViewById(R.id.button1);
            s=results.get(0).toString();
            if(count==0)
            {
                bt.setVisibility(View.VISIBLE); // hide it
                bt.setClickable(true);
            }
            bt.setText(s);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                    public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Reminder.this);
                            final View myView = getLayoutInflater().inflate(R.layout.edit_reminder, null);
                            builder.setTitle("Edit Reminder");
                            et = (EditText) myView.findViewById(R.id.edit_text);
                            et.setText(s);
                            sp1 = (Spinner) myView.findViewById(R.id.spinner1);
                            ArrayAdapter<String> ad = new ArrayAdapter<String>(Reminder.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Set_Date));
                            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp1.setAdapter(ad);
                            sp2 = (Spinner) myView.findViewById(R.id.spinner2);
                            ArrayAdapter<String> ad1 = new ArrayAdapter<String>(Reminder.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Set_Time));
                            ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp2.setAdapter(ad1);
                            sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedItem = parent.getItemAtPosition(position).toString();
                                    if (selectedItem.equals("Today")) {
                                        Calendar cal = Calendar.getInstance();
                                        year = cal.get(Calendar.YEAR);
                                        month = cal.get(Calendar.MONTH);
                                        date = cal.get(Calendar.DAY_OF_MONTH);
                                    }
                                    if (selectedItem.equals("Tomorrow")) {
                                        Calendar cal = Calendar.getInstance();
                                        year = cal.get(Calendar.YEAR);
                                        month = cal.get(Calendar.MONTH);
                                        date = cal.get(Calendar.DAY_OF_MONTH) + 1;
                                    }
                                    if (selectedItem.equals("Set date...")) {
                                        year = 0;
                                        month = 0;
                                        date = 0;
                                        Intent myIntent = new Intent(view.getContext(), Date_Set.class);
                                        myIntent.putExtra("year", year);
                                        myIntent.putExtra("month", month);
                                        myIntent.putExtra("date", date);
                                        startActivityForResult(myIntent, 2);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedItem = parent.getItemAtPosition(position).toString();
                                    if (selectedItem.equals("Morning")) {
                                        hour = 8;
                                        min = 0;
                                    }
                                    if (selectedItem.equals("Afternoon")) {
                                        hour = 12;
                                        min = 0;
                                    }
                                    if (selectedItem.equals("Evening")) {
                                        hour = 17;
                                        min = 00;
                                    }
                                    if (selectedItem.equals("Night")) {
                                        hour = 21;
                                        min = 0;
                                    }
                                    if (selectedItem.equals("Set time")) {
                                        hour = 0;
                                        min = 0;
                                        Intent myIntent = new Intent(view.getContext(), Time_Set.class);
                                        myIntent.putExtra("hour", hour);
                                        myIntent.putExtra("min", min);
                                        startActivityForResult(myIntent, 1);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }

                            });

                            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    et1 = (EditText) myView.findViewById(R.id.edit_text);
                                    s = et1.getText().toString();
                                    helper = new myDbAdapter(Reminder.this);
                                    long id = helper.insertData(s);
                                    bt = (Button) findViewById(R.id.button1);
                                    bt.setText(s);
                                    Toast.makeText(Reminder.this, "saving reminder...", Toast.LENGTH_LONG).show();

                                    cal = Calendar.getInstance();
                                    cal.set(Calendar.MONTH, month);
                                    cal.set(Calendar.YEAR, year);
                                    cal.set(Calendar.DAY_OF_MONTH, date);
                                    cal.set(Calendar.HOUR_OF_DAY, hour);
                                    cal.set(Calendar.MINUTE, min);
                                    cal.set(Calendar.SECOND, 0);
                                    Intent myIntent1 = new Intent(Reminder.this, AlarmBroadCustReciver.class);
                                    myIntent1.putExtra("Notify", s);
                                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(Reminder.this, 100, myIntent1,
                                            PendingIntent.FLAG_UPDATE_CURRENT);
                                    AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
                                    alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);

                                }
                            });

                            builder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Reminder.this);
                                    final View myView1 = getLayoutInflater().inflate(R.layout.delete_confirm, null);
                                    builder1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(Reminder.this, "reminder deleted", Toast.LENGTH_LONG).show();
                                            bt = (Button) findViewById(R.id.button1);
                                            bt.setText("");
                                        }
                                    });
                                    builder1.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(Reminder.this, "reminder deletion cancelled", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    builder1.setView(myView1);
                                    AlertDialog dialog1 = builder1.create();
                                    dialog1.show();
                                }
                            });
                        builder.setView(myView);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        bt.setVisibility(View.GONE); // hide it
                        bt.setClickable(false);
                        count=1;
                    }
                });

        }
        if(requestCode==1&&resultCode==RESULT_OK)
        {
            hour = data.getIntExtra("hour",0);
            min=data.getIntExtra("min",0);
        }
        if(requestCode==2&&resultCode==RESULT_OK)
        {
            year = data.getIntExtra("year",0);
            month=data.getIntExtra("month",0);
            date=data.getIntExtra("date",0);
        }
    }
}