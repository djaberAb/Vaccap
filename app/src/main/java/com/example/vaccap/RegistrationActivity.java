package com.example.vaccap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailInput;
    EditText passwordInput;
    TextView signinLink;
    EditText usernameInput;

    Button signup_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        usernameInput = findViewById(R.id.name);
        signinLink = findViewById(R.id.signin_link);
        signup_Btn = findViewById(R.id.signup_btn);

        mAuth = FirebaseAuth.getInstance();

        signinLink.setOnClickListener(view-> startActivity(new Intent(RegistrationActivity.this, LoginActivity.class)));

        signup_Btn.setOnClickListener(view-> CreateUser());
    }

    private void CreateUser() {
        String username = usernameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (validateInput(username, email, password)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                // Proceed to the next fragment or activity
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                            } else {
                                // If sign up fails, display a message to the user.
                                Toast.makeText(RegistrationActivity.this, "Registration failed." ,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    private boolean validateInput(String username, String email, String password) {
        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter your username.", Toast.LENGTH_SHORT).show();
            usernameInput.requestFocus();
            return false;
        }
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
