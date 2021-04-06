package com.example.damsphojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "TAG";
    private EditText mFullName, mEmail, mPassword;
    private Button register;
    private TextView banner;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.etFirstName);
        mEmail = findViewById(R.id.etEmail);
        mPassword = findViewById(R.id.etPassword);

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        register = findViewById(R.id.btnRegister);
        register.setOnClickListener(this);

        banner = findViewById(R.id.title);
        banner.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title:
                startActivity(new Intent(this, Login.class));
            case R.id.btnRegister:
                registerUser();
                break;
        }
    }

    public void registerUser() {
        String fullName = mFullName.getText().toString();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (fullName.isEmpty()) {
            mFullName.setError("Full name is required.");
            mFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            mEmail.setError("Email is required.");
            mEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Must be a valid email.");
            mEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mPassword.setError("Password is required.");
            return;
        }

        if (password.length() < 6) {
            mPassword.setError("Password must be >= 6 characters.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Create a new user with their full name and email.
                Map<String, Object> user = new HashMap<>();
                user.put("fname", fullName);
                user.put("email", email);

                // Add a new document with a generated ID
                mStore.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Register.this, MainActivity.class));
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(Register.this, "Registration Failed. Please try again.", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });
    }
}