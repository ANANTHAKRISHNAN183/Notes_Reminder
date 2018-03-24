package com.example.personal.project_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Edit_Notes extends AppCompatActivity {

    EditText et;
    Button b;
    String s;
    Notes_DbAdapter helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_notes);
        Intent i=getIntent();
        s= i.getStringExtra("string");
        et=(EditText)findViewById(R.id.editing_text);
        et.setText(s);
        b=(Button)findViewById(R.id.save);
        b.setOnClickListener(new View.OnClickListener() {
            Intent intent1=new Intent();
            @Override
            public void onClick(View v) {
                s=et.getText().toString();
                if(!s.isEmpty())
                {
                    helper = new Notes_DbAdapter(Edit_Notes.this);
                    long id = helper.insertData(s);
                }
                intent1.putExtra("string",s);
                setResult(RESULT_OK, intent1);
                finish();
            }
        });

    }
}
