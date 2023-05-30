package com.example.vaccap.ui.appointments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vaccap.R;
import com.example.vaccap.models.Appointment;
import com.example.vaccap.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AppointmentSchedulingActivity extends AppCompatActivity {

    private EditText dateEditText;
    private EditText timeEditText;
    private EditText typeEditText;
    private EditText clinicNameEditText;
    private Button requestAppointmentButton;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private String patientName;
    private String clinicName;
    private String appointmentId;

    private DocumentReference appointmentDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_scheduling);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        dateEditText = findViewById(R.id.date_input);
        timeEditText = findViewById(R.id.time_input);
        typeEditText = findViewById(R.id.type_vaccine_input);
        clinicNameEditText = findViewById(R.id.clinicName);
        requestAppointmentButton = findViewById(R.id.request_appointment_button);

        getPatientName();

        requestAppointmentButton.setOnClickListener(v -> {
            String date = dateEditText.getText().toString().trim();
            String time = timeEditText.getText().toString().trim();
            String type = typeEditText.getText().toString().trim();
            String clinic = clinicNameEditText.getText().toString().trim();

            Appointment appointment = new Appointment(date, time, type, clinic, patientName, "pending");

            addAppointmentToFirestore(appointment);
        });
    }

    private void addAppointmentToFirestore(Appointment appointment) {
        db.collection("appointments").add(appointment)
                .addOnSuccessListener(documentReference -> {
                    appointmentId = documentReference.getId(); // Set the appointmentId variable to the ID of the added document
                    appointmentDocRef = db.collection("appointments").document(appointmentId);
                    appointmentDocRef.addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            Log.e(TAG, "Error listening for appointment updates", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            String status = snapshot.getString("status");
                            if (status != null) {
                                appointment.setStatus(status);
                                updateAppointmentStatus(appointment);
                            }
                        }
                    });
                    Toast.makeText(this, "Appointment requested successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding appointment", e);
                    Toast.makeText(this, "Failed to request appointment", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateAppointmentStatus(Appointment appointment) {
        appointmentDocRef.update("status", appointment.getStatus())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Appointment status updated"))
                .addOnFailureListener(e -> Log.e(TAG, "Error updating appointment status", e));
    }
    private void getPatientName() {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DocumentReference userDocRef = db.collection("/users/Patients/patients").document(userId);
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            if (user != null) {
                patientName = user.getUserName();
            }
        }).addOnFailureListener(e -> Log.e(TAG, "Error getting patient name", e));
    }
}