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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

/**
 * ForgotPassword
 * Allows the user to reset their password
 * if forgotten.
 * @author Dana llewellyn
 */
public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    /**
     * Private variables
     */
    private EditText editPassword;
    private TextView title;
    private Button reset;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    /**
     * onCreate
     * Builds the necessary objects so the user
     * can reset their password.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editPassword = findViewById(R.id.tvEditPassword);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress);

        reset = findViewById(R.id.btnResetPassword);
        reset.setOnClickListener(this);

        title = findViewById(R.id.title);

    }

    /**
     * onClick
     * Listens for button clicks and
     * either sends the user to the new activity
     * or calls the resetPassword method.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnResetPassword:
                resetPassword();
                return;
            case R.id.title:
                startActivity(new Intent(this, Login.class));
        }
    }

    /**
     * resetPassword
     * Allows the user to reset their password.
     * the edit texts.
     */
    public void resetPassword(){
        String email = editPassword.getText().toString().trim();
        if(email.isEmpty()){
            editPassword.setError("Email is required.");
            editPassword.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editPassword.setError("Must be a valid email.");
            editPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            //If the user exists in the database it sends the user an email to change their password.
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Request is successful. Please check your email to reset your password.", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(ForgotPassword.this, "Sorry, something went wrong. Please try again..", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}