package com.example.personal.project_android;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Notes extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private int counter = 0;
    private ImageButton ib;
    LinearLayout linearlayout;
    private String s;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> myDataset;
    Notes_DbAdapter helper;
    String r;
    Spinner sp1,sp2;
    int count=0;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_bar);
        Intent i = getIntent();;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDataset = new ArrayList<String>();
        getSupportActionBar().setTitle("Notes");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setItemPrefetchEnabled(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        helper = new Notes_DbAdapter(this);
        r = helper.getData();
        if (!r.isEmpty()) {
            mAdapter = new MyAdapter(Notes.this, Arrays.asList(r.split(";;")));
            mRecyclerView.setAdapter(mAdapter);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
        // specify an adapter (see also next example)
        ib = (ImageButton) findViewById(R.id.edit_text_notes);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(Notes.this, Edit_Notes.class);
                startActivityForResult(p, 2);

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        linearlayout = (LinearLayout) findViewById(R.id.ll1);
        switch (item.getItemId()) {
            case R.id.about_app:
                Intent j= new Intent(this,About_App.class);
                startActivity(j);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,3);
            // Handle the camera action
        } else if (id == R.id.nav_reminder) {
            Intent p=new Intent(Notes.this,Reminder.class);
            startActivity(p);


        } else if (id == R.id.nav_record) {
            promptSpeechInput();

        } else if (id == R.id.nav_manage) {
            //Toast.makeText(Notes.this,"anan", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(Notes.this);
            final View myView = getLayoutInflater().inflate(R.layout.fonts_and_color, null);
            builder.setTitle("Edit Font and Color");
            sp1 = (Spinner) myView.findViewById(R.id.spinner_font);
            ArrayAdapter<String> ad = new ArrayAdapter<String>(Notes.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Set_Font));
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp1.setAdapter(ad);
            sp2 = (Spinner) myView.findViewById(R.id.spinner_color);
            ArrayAdapter<String> ad1 = new ArrayAdapter<String>(Notes.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Set_Color));
            ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp2.setAdapter(ad1);
            sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    final TextView mTextView = (TextView) findViewById(R.id.info_text);
                    if (selectedItem.equals("Normal"))
                        mTextView.setTypeface(Typeface.SANS_SERIF);

                    if (selectedItem.equals("Monospace"))
                        mTextView.setTypeface(Typeface.MONOSPACE);

                    if (selectedItem.equals("Italica"))
                        mTextView.setTypeface(mTextView.getTypeface(), Typeface.ITALIC);
                    if (selectedItem.equals("Bold"))
                        mTextView.setTypeface(mTextView.getTypeface(), Typeface.BOLD);
                    if (selectedItem.equals("Bold_italic"))
                        mTextView.setTypeface(mTextView.getTypeface(), Typeface.BOLD_ITALIC);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    final TextView mTextView = (TextView) findViewById(R.id.info_text);
                    if (selectedItem.equals("Blue"))
                        mTextView.setBackgroundColor(Color.parseColor("#26f1fb"));

                    if (selectedItem.equals("Green"))
                        mTextView.setBackgroundColor(Color.parseColor("#1ee224"));

                    if (selectedItem.equals("Orange"))
                        mTextView.setBackgroundColor(Color.parseColor("#FFEC6212"));

                    if (selectedItem.equals("Red"))
                        mTextView.setBackgroundColor(Color.parseColor("#FFF20B1A"));

                    if (selectedItem.equals("White"))
                        mTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    if (selectedItem.equals("Yellow"))
                        mTextView.setBackgroundColor(Color.parseColor("#FFEFDC12"));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });


            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                                    }
                    });

            builder.setView(myView);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (id == R.id.nav_share) {
            final TextView mTextView = (TextView) findViewById(R.id.info_text);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT,"Your body is here");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, mTextView.getText().toString());
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent,"Share Using"));

                }
            });


        }
        else if(id==R.id.logout)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Notes.this);
            builder1.setMessage("Logout ?");
            builder1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth fAuth = FirebaseAuth.getInstance();
                    fAuth.signOut();
                    startActivity(new Intent(Notes.this, Login.class)); //Go back to home page
                    finish();
                }
            });
            builder1.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Notes.this, "Logout cancelled", Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog dialog1=builder1.create();
            dialog1.show();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            s = data.getStringExtra("string");
            if (!s.isEmpty()) {
                myDataset.add(s);
                helper = new Notes_DbAdapter(this);
                r = helper.getData();
            }
            mAdapter = new MyAdapter(Notes.this, Arrays.asList(r.split(";;")));
            mRecyclerView.setAdapter(mAdapter);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            s=results.get(0).toString();
            Intent p = new Intent(Notes.this, Edit_Notes.class);
            p.putExtra("string",s);
            startActivityForResult(p, 2);

        }
        if (requestCode == 3 && resultCode == RESULT_OK) {
            Bitmap bm=(Bitmap)data.getExtras().get("data");
            /*myDataset.add(bm.toString());
            helper = new Notes_DbAdapter(this);
            r = helper.getData();
             mAdapter = new MyAdapter(Notes.this, Arrays.asList(r.split(";;")));
             mRecyclerView.setAdapter(mAdapter);
             NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
             navigationView.setNavigationItemSelectedListener(this);
        */
        }
    }

    public void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something!");
// Start the activity, the intent will be populated with the speech text
        try {
             count=0;
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(Notes.this, "Sorry your device does not support speech language", Toast.LENGTH_LONG).show();
        }
    }
}

