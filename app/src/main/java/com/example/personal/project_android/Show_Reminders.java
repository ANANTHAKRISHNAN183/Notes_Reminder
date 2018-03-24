package com.example.personal.project_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Show_Reminders extends AppCompatActivity {

    myDbAdapter helper;
    EditText et,et1;
    ListView lv;
    Button bv;
    String p,st,st1;
    ArrayList<String>arraylist;
    ArrayAdapter<String>adapter;
    Spinner sp1,sp2,sp3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_reminders);
        Intent i=getIntent();
        lv=(ListView)findViewById(R.id.list_view);
        helper = new myDbAdapter(this);
        p=helper.getData();
        final String[] items=p.split("\\r?\\n");
        arraylist = new ArrayList<String>(Arrays.asList(items));
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.text_view, arraylist);
        lv.setAdapter(adapter);
        //arraylist.add(items);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Show_Reminders.this);
                final View myView = getLayoutInflater().inflate(R.layout.edit_reminder, null);
                builder.setTitle("Edit Reminder");
                et = (EditText) myView.findViewById(R.id.edit_text);
                st=(String)lv.getItemAtPosition(position);
                et.setText(st);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arraylist.remove(st);
                        st1=et.getText().toString();
                        boolean v=helper.updateName(st,st1);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(Show_Reminders.this, "saving reminder...", Toast.LENGTH_LONG).show();
                        arraylist.add(st1);
                    }
                });
                builder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Show_Reminders.this);
                        final View myView1 = getLayoutInflater().inflate(R.layout.delete_confirm, null);
                        builder1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Show_Reminders.this, "reminder deleted", Toast.LENGTH_LONG).show();
                                boolean k=helper.delete(st);
                                arraylist.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        builder1.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Show_Reminders.this, "reminder deletion cancelled", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder1.setView(myView1);
                        AlertDialog dialog1=builder1.create();
                        dialog1.show();
                    }
                });
                builder.setView(myView);
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

    }
}
