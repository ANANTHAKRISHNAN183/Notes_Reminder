package com.example.personal.project_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class Date_Set extends AppCompatActivity {

    DatePicker dp;
    Button bv;
    int date,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date);
        Intent intent = new Intent();
        date = intent.getIntExtra("date", 0);
        month = intent.getIntExtra("month", 0);
        year = intent.getIntExtra("year", 0);
        getSupportActionBar().setTitle("Date set");
        calender_set();
    }
    public void calender_set() {
        dp=(DatePicker)findViewById(R.id.datePicker);
        bv=(Button)findViewById(R.id.date_button);
        bv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent();
                date=dp.getDayOfMonth();
                month=dp.getMonth();
                year=dp.getYear();
                intent2.putExtra("date",date);
                intent2.putExtra("month",month) ;
                intent2.putExtra("year",year);
                setResult(RESULT_OK, intent2);
                finish();
            }
        });
    }
}
