package com.example.personal.project_android;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class Time_Set extends AppCompatActivity {
    TimePicker tp;
    Button bv1;
    public int hour, min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock);
        Intent intent = new Intent();
        hour = intent.getIntExtra("hour", 0);
        min = intent.getIntExtra("min", 0);
        getSupportActionBar().setTitle("Time set");
        time_set();
    }

    public void time_set() {
        tp = (TimePicker) findViewById(R.id.timePicker);
        bv1 = (Button) findViewById(R.id.clock_ok_button);
        tp.setIs24HourView(true);
        bv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent();
                if (Build.VERSION.SDK_INT >= 23) {
                    hour = tp.getHour();
                    min = tp.getMinute();
                } else {
                    hour = tp.getCurrentHour();
                    min = tp.getCurrentMinute();
                }
                intent1.putExtra("hour",hour);
                intent1.putExtra("min",min) ;
                setResult(RESULT_OK, intent1);
                finish();
            }
        });
    }
}