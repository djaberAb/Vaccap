package com.example.vaccap;

import static android.content.ContentValues.TAG;
import static com.example.vaccap.models.Clinic.fromSnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccap.models.Appointment;
import com.example.vaccap.models.AppointmentAdapterClinic;
import com.example.vaccap.models.Clinic;
import com.example.vaccap.ui.authentication.PatientORClinicActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClinicMainActivity extends AppCompatActivity {

    private RecyclerView appointmentsRecyclerView;
    private List<Appointment> appointments;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String clinicName;
    private Button lougout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        appointmentsRecyclerView = findViewById(R.id.appointments_recycler_view);

        String userID = mAuth.getUid();
        getClinicName(userID);

        lougout = findViewById(R.id.logoutClinic);
        lougout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(ClinicMainActivity.this, PatientORClinicActivity.class));
        });
    }

    private void getClinicName(String userID) {
        DocumentReference docRef = db.collection("users/Clinics/clinics/").document(userID);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Clinic clinic = fromSnapshot(document);
                    clinicName = clinic.getClinicName();
                    getAppointmentsByClinicName(clinicName);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    private void getAppointmentsByClinicName(String clinicName) {
        db.collection("appointments")
                .whereEqualTo("clinic", clinicName).whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(onCompleteListener -> {
                    appointments = new ArrayList<>();
                    for (QueryDocumentSnapshot document : onCompleteListener) {
                        Appointment appointment = document.toObject(Appointment.class);
                        appointment.setId(document.getId());
                        appointments.add(appointment);
                    }
                    displayAppointments(appointments);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting appointments for clinic " + clinicName, e);
                    Toast.makeText(this, "Failed to get appointments", Toast.LENGTH_SHORT).show();
                });
    }

    private void displayAppointments(List<Appointment> appointments) {
        // Set up the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        appointmentsRecyclerView.setLayoutManager(layoutManager);
        AppointmentAdapterClinic adapter = new AppointmentAdapterClinic(appointments);
        appointmentsRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(ClinicMainActivity.this, PatientORClinicActivity.class));
        }
    }
}