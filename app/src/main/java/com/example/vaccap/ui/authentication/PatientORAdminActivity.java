package com.example.vaccap.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vaccap.R;

public class PatientORAdminActivity extends AppCompatActivity {

    private Button clinic, patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_orclinic);

        clinic = findViewById(R.id.clinic);
        patient = findViewById(R.id.patient);

        clinic.setOnClickListener(v -> {
            startActivity(new Intent(PatientORAdminActivity.this, AdminLoginActivity.class));
        });

        patient.setOnClickListener(v -> {
            startActivity(new Intent(PatientORAdminActivity.this, LoginActivity.class));
        });
    }
}