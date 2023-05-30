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

import com.example.vaccap.R;
import com.example.vaccap.models.Clinic;
import com.example.vaccap.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class ClinicRegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText emailInput, passwordInput, clinicNameInput, phoneNumberInput, nameBabyInput, birthDayBabyInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_registration);


        emailInput = findViewById(R.id.emailSignup);
        passwordInput = findViewById(R.id.passwordSignup);
        clinicNameInput = findViewById(R.id.clinicNameSignup);
        phoneNumberInput = findViewById(R.id.phoneNumberSignup);
        TextView signinLink = findViewById(R.id.signin_link);
        Button signup_Btn = findViewById(R.id.signup_btn);

        mAuth = FirebaseAuth.getInstance();

        signinLink.setOnClickListener(view-> startActivity(new Intent(ClinicRegistrationActivity.this, ClinicLoginActivity.class)));

        signup_Btn.setOnClickListener(view-> CreateUser());

    }

    private void CreateUser() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String phone = phoneNumberInput.getText().toString();
        String clinicname = clinicNameInput.getText().toString();

        if (validateInput(email, password)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Get the user's ID
                                String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                                // Create a new User object with null values
                                Clinic clinic = new Clinic(email, clinicname, phone);
                                db.collection("/users/Clinics/clinics").document(userID)
                                        .set(clinic.toMap())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ClinicRegistrationActivity.this, "Clinic registered successfully", Toast.LENGTH_SHORT).show();
                                                // Proceed to the next fragment or activity
                                                startActivity(new Intent(ClinicRegistrationActivity.this, ClinicLoginActivity.class));
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // If sign up fails, display a message to the user.
                                                Toast.makeText(ClinicRegistrationActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                Toast.makeText(ClinicRegistrationActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                // Proceed to the next fragment or activity
                                startActivity(new Intent(ClinicRegistrationActivity.this, LoginActivity.class));
                            } else {
                                // If sign up fails, display a message to the user.
                                Toast.makeText(ClinicRegistrationActivity.this, "Registration failed." ,Toast.LENGTH_SHORT).show();
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