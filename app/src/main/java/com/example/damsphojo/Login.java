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

/**
 * Login
 * A class employed for logging in
 * the user. For first time registered users
 * it sends users an eamil so that they verify
 * their account.
 *
 * @author danallewellyn
 */

public class Login extends AppCompatActivity implements View.OnClickListener {

    /**
     * Private variables
     */

    private EditText mEmail, mPassword;
    private Button login;
    private TextView createAccount, mForgotPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    /**
     * onCreate
     * Builds the necessary objects so the user
     * can log in.
     * @param savedInstanceState
     */

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

    /**
     * onClick
     * Listens for button clicks and
     * sends the user to the right
     * activity.
     * @param v
     */

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

    /**
     * validateSignIn
     * Validates what the user inputs into
     * the edit texts.
     */

    private void validateSignIn() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        //checks to make sure the user didn't forget to put in their email.
        if(email.isEmpty()){
            mEmail.setError("Email is required.");
            mEmail.requestFocus();
            return;
        }

        //checks to make sure the user inputted a valid email
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Must be a valid email.");
            mEmail.requestFocus();
            return;
        }

        //checks to make sure the user didn't forget to put in their password.
        if(password.isEmpty()){
            mPassword.setError("Password is required.");
            return;
        }

        //checks to make sure the password is at least six characters long.
        if(password.length() < 6){
            mPassword.setError("Password must be >= 6 characters.");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        //Makes sure the user is already registered and exists in the database.
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //Checks to make sure the user verified their email. If not it sends them an email.
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