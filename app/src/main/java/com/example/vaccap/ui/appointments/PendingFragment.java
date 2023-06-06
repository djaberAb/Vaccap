package com.example.vaccap.ui.appointments;

import static android.content.ContentValues.TAG;

import static com.example.vaccap.models.User.fromSnapshot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccap.R;
import com.example.vaccap.models.Appointment;
import com.example.vaccap.models.AppointmentAdapterPatient;
import com.example.vaccap.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PendingFragment extends Fragment {

    private RecyclerView appointmentsRecyclerView;
    private List<Appointment> appointments;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String patientUsername;

    public PendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        appointmentsRecyclerView = view.findViewById(R.id.pending_appointments_recycler_view);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            getUserName();
            getAppointmentsByStatusAndPatient("pending", patientUsername);
        }

        return view;
    }

    private void getAppointmentsByStatusAndPatient(String status, String patientUsername) {
        db.collection("appointments")
                .whereEqualTo("status", status)
                .whereEqualTo("patient", patientUsername)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    appointments = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Appointment appointment = document.toObject(Appointment.class);
                        appointment.setId(document.getId());
                        appointments.add(appointment);
                    }
                    displayAppointments(appointments);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting appointments for clinic");
                    Toast.makeText(getContext(), "Failed to get appointments", Toast.LENGTH_SHORT).show();
                });
    }

    private void displayAppointments(List<Appointment> appointments) {
        // Set up the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        appointmentsRecyclerView.setLayoutManager(layoutManager);
        AppointmentAdapterPatient adapter = new AppointmentAdapterPatient(appointments);
        appointmentsRecyclerView.setAdapter(adapter);
    }

    public void getUserName(){
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid(); // Get the current user's ID

        DocumentReference userDocRef = db.collection("/users/Patients/patients").document(userID); // Get the reference to the user's document

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Convert the document data to a User object
                        User user = fromSnapshot(document);
                        patientUsername= user.getUserName();
                    } else {
                        Log.w(TAG, "No such document");
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
}