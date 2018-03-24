package com.example.personal.project_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class Login extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private ProgressDialog progressDialog;
    private TextView signin;
    private FirebaseAuth firebaseAuth;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        tv=(TextView)findViewById(R.id.textView_email);
        tv.setSelected(true);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPassword = (EditText) findViewById(R.id.editText_password);

        buttonSignup = (Button) findViewById(R.id.login_button);

        progressDialog = new ProgressDialog(this);
        signin=(TextView)findViewById(R.id.already_signed);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        signin.setOnClickListener(this);
    }

    private void registerUser(){

        //getting email and password from edit texts
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here
                            Toast.makeText(Login.this,"Successfully registered",Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Login.this, Notes.class);
                            startActivity(i);
                        }else{
                            //display some message here
                            Toast.makeText(Login.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }
    private boolean checkAccountEmailExistInFirebase(String email) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final boolean[] b = new boolean[1];
        mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                b[0] = !task.getResult().getProviders().isEmpty();
            }
        });
        return b[0];
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignup)
        //calling register method on click
        registerUser();
        else{
            String mailid=editTextEmail.getText().toString().trim();
            if(mailid!=null&&!mailid.isEmpty()) {
                boolean b = checkAccountEmailExistInFirebase(mailid);
                if (b == false) {
                    Toast.makeText(Login.this, "Successfully signed", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Login.this, Notes.class);
                    startActivity(i);
                } else {
                    Toast.makeText(Login.this, "Entered email not registered", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_LONG).show();
            }
        }


    }
}
