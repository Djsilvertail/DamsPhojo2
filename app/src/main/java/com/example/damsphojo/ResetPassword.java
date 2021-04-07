package com.example.damsphojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Register
 * Allows the user to register a new account.
 * @author Dana llewellyn
 */

public class ResetPassword extends AppCompatActivity {
    /**
     * Private variables
     */
    private EditText psword, cpsword;
    private Button resetPassBtn;

    FirebaseUser user;
    ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        psword = findViewById(R.id.etPassword);
        cpsword = findViewById(R.id.cPassword);
        pBar = findViewById(R.id.progressBar);
        user = FirebaseAuth.getInstance().getCurrentUser();

        resetPassBtn = findViewById(R.id.btnReset);
        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPass = psword.getText().toString().trim();
                String cPass = cpsword.getText().toString().trim();

                if (newPass.isEmpty()) {
                    psword.setError("Password is required.");
                    psword.requestFocus();
                    return;
                }

                //checks to make sure the password is at least six characters long.
                if (newPass .length() < 6) {
                    psword.setError("Password must be >= 6 characters.");
                    psword.requestFocus();
                    return;
                }

                if(cPass.isEmpty()){
                    cpsword.setError("Please make sure you confirm your password.");
                    cpsword.requestFocus();
                    return;
                }

                if(!newPass.equals(cPass)) {
                    cpsword.setError("Passwords don't match. Please try again.");
                    cpsword.requestFocus();
                    return;
                }

                pBar.setVisibility(View.VISIBLE);

                user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ResetPassword.this, "Password updated successfully.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ResetPassword.this, MainActivity.class));
                        finish();
                        pBar.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPassword.this, "Password could not be updated", Toast.LENGTH_LONG).show();
                        pBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
   }
}