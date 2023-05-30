package com.example.vaccap.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.vaccap.R;

public class PatientORClinicActivity extends AppCompatActivity {

    private Button clinic, patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_orclinic);

        clinic = findViewById(R.id.clinic);
        patient = findViewById(R.id.patient);

        clinic.setOnClickListener(v -> {
            startActivity(new Intent(PatientORClinicActivity.this, ClinicLoginActivity.class));
        });

        patient.setOnClickListener(v -> {
            startActivity(new Intent(PatientORClinicActivity.this, LoginActivity.class));
        });
    }
}