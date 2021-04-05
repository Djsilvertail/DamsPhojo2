package com.example.damsphojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmail, mPassword;
    private Button login;
    private TextView createAccount, mForgotPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.etEmail);
        mPassword = findViewById(R.id.etPassword);

        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        login = findViewById(R.id.btnLogin);
        login.setOnClickListener(this);

        createAccount = findViewById(R.id.tvRegister);
        createAccount.setOnClickListener(this);

        mForgotPassword = findViewById(R.id.tvForgotPassword);
        mForgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnLogin:
                validateSignIn();
                return;
            case R.id.tvRegister:
                startActivity(new Intent(this, Register.class));
                return;
            case R.id.tvForgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                return;
        }
    }

    private void validateSignIn() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if(email.isEmpty()){
            mEmail.setError("Email is required.");
            mEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Must be a valid email.");
            mEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            mPassword.setError("Password is required.");
            return;
        }

        if(password.length() < 6){
            mPassword.setError("Password must be >= 6 characters.");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    if(user.isEmailVerified()){
                        Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Login.this, MainActivity.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Please check your email to verify your account.", Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(Login.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}