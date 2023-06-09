package com.example.vaccap.ui.appointments;

import static android.content.ContentValues.TAG;

import static com.example.vaccap.models.User.fromSnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccap.R;
import com.example.vaccap.models.Appointment;
import com.example.vaccap.models.AppointmentAdapterPatient;
import com.example.vaccap.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PendingFragment extends Fragment implements AppointmentAdapterPatient.OnItemClickListener {

    private RecyclerView appointmentsRecyclerView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private AppointmentAdapterPatient appointmentAdapterPatient;
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
            getAppointmentsByStatusAndPatient(patientUsername);
        }

        return view;
    }

    private void getAppointmentsByStatusAndPatient(String patientUsername) {
        db.collection("appointments")
                .whereEqualTo("status", "pending")
                .whereEqualTo("patient", patientUsername)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Appointment> appointments = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Appointment appointment = document.toObject(Appointment.class);
                        appointment.setId(document.getId());
                        appointments.add(appointment);
                    }
                    displayAppointments(appointments);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting appointments for patient");
                    Toast.makeText(getContext(), "Failed to get appointments", Toast.LENGTH_SHORT).show();
                });
    }

    private void displayAppointments(List<Appointment> appointments) {
        // Set up the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        appointmentsRecyclerView.setLayoutManager(layoutManager);
        appointmentAdapterPatient = new AppointmentAdapterPatient(appointments, this);
        appointmentsRecyclerView.setAdapter(appointmentAdapterPatient);
    }

    @Override
    public void onEditClick(Appointment appointment) {
        Intent intent = new Intent(getActivity(), EditAppointmentActivity.class);
        intent.putExtra("appointmentId", appointment.getId());
        startActivity(intent);
    }

    @Override
    public void onCancelClick(Appointment appointment) {
        db.collection("appointments")
                .document(appointment.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Appointment deleted", Toast.LENGTH_SHORT).show();
                    appointmentAdapterPatient.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting appointment from Firestore", e);
                    // TODO: Handle the error case
                });
    }

    public void getUserName(){
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid(); // Get the current user's ID

        DocumentReference userDocRef = db.collection("/users/Patients/patients").document(userID); // Get the reference to the user's document

        userDocRef.get().addOnCompleteListener(task -> {
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
        });
    }
}