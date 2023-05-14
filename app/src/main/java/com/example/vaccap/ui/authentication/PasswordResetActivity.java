package com.example.vaccap.ui.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vaccap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private EditText editTextTextEmailAddress;
    private ProgressBar progressBar;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passord_reset);

        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        Button buttonPasswordReset = findViewById(R.id.resetPassordBtn);
        progressBar= findViewById(R.id.pBar);
        TextView backtoLogin = findViewById(R.id.backtoLogin);

        mAuth = FirebaseAuth.getInstance();



        buttonPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextTextEmailAddress.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(PasswordResetActivity.this, "Please enter your registered Email", Toast.LENGTH_SHORT).show();
                    editTextTextEmailAddress.setError("Email is required");
                    editTextTextEmailAddress.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(PasswordResetActivity.this, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
                    editTextTextEmailAddress.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    resetPassword(email);
                }
            }
        });

        backtoLogin.setOnClickListener(view-> startActivity(new Intent(PasswordResetActivity.this, LoginActivity.class)));

    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PasswordResetActivity.this, "Check your Email please", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PasswordResetActivity.this, LoginActivity.class);

                    //Clear Stack so user don't go back to Password reset activty
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(PasswordResetActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}