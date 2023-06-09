package com.example.vaccap.ui.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vaccap.admin.AdminMainActivity;
import com.example.vaccap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ClinicLoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;

    private TextView resetPasswordButton;
    private TextView signupLink;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_login);

        emailInput = findViewById(R.id.emailSignin);
        passwordInput = findViewById(R.id.passwordSignin);
        loginButton = findViewById(R.id.signup_btn);
        signupLink = findViewById(R.id.tv_signupLink);
        resetPasswordButton = findViewById(R.id.reset_password);

        mAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(view -> loginUser());
        signupLink.setOnClickListener(view -> startActivity(new Intent(ClinicLoginActivity.this, ClinicRegistrationActivity.class)));
        resetPasswordButton.setOnClickListener(view ->startActivity(new Intent(ClinicLoginActivity.this, PasswordResetActivity.class)));
    }
    private void loginUser() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (validateInput(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ClinicLoginActivity.this, "Clinic logged successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ClinicLoginActivity.this, AdminMainActivity.class));
                    }
                    else {
                        Toast.makeText(ClinicLoginActivity.this, "Log in failed." ,Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
    private boolean validateInput(String email, String password) {
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email address.", Toast.LENGTH_SHORT).show();
            emailInput.requestFocus();
            return false;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            emailInput.requestFocus();

            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter a password.", Toast.LENGTH_SHORT).show();
            passwordInput.requestFocus();
            return false;
        }
        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password must be at least 8 characters long.", Toast.LENGTH_SHORT).show();
            passwordInput.requestFocus();
            return false;
        }

        // Add additional validation rules if necessary

        return true;
    }

    private boolean isValidEmail(String email) {
        // Add your email validation logic here
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // Add your password validation logic here
        return !TextUtils.isEmpty(password) && password.length() >= 8;
    }
}