package com.example.vaccap.ui.appointments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccap.R;
import com.example.vaccap.models.Appointment;
import com.example.vaccap.models.AppointmentAdapterPatient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AcceptedFragment extends Fragment implements AppointmentAdapterPatient.OnItemClickListener {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private RecyclerView appointmentsRecyclerView;
    private AppointmentAdapterPatient appointmentAdapterPatient;
    private String patientUsername;

    public AcceptedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accepted, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            getUserName();
            getAppointmentsByStatusAndPatient(patientUsername);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appointmentsRecyclerView = view.findViewById(R.id.acceptedd_appointments_recycler_view);

        // Set up the RecyclerView with a fixed height and an empty adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        appointmentsRecyclerView.setLayoutManager(layoutManager);
        appointmentsRecyclerView.setHasFixedSize(true);
        appointmentAdapterPatient = new AppointmentAdapterPatient(new ArrayList<>(), this);
        appointmentsRecyclerView.setAdapter(appointmentAdapterPatient);
    }

    private void displayAppointments(List<Appointment> appointments) {
        // Update the adapter with the new list of appointments
        appointmentAdapterPatient.getAppointments().clear();
        appointmentAdapterPatient.getAppointments().addAll(appointments);
        appointmentAdapterPatient.notifyDataSetChanged();

        // Force the RecyclerView to redraw itself
        appointmentsRecyclerView.invalidate();
    }

    private void getAppointmentsByStatusAndPatient(String patientUsername) {
        db.collection("appointments")
                .whereEqualTo("patientUsername", patientUsername)
                .whereEqualTo("status", "Accepted")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Appointment> appointments = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Appointment appointment = documentSnapshot.toObject(Appointment.class);
                        appointments.add(appointment);
                    }
                    displayAppointments(appointments);
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error getting accepted appointments: " + e.getMessage()));
    }

    private void getUserName() {
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    patientUsername = documentSnapshot.getString("username");
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error getting patient username: " + e.getMessage()));
    }

    @Override
    public void onEditClick(Appointment appointment) {
        // Handle edit click
    }

    @Override
    public void onCancelClick(Appointment appointment) {
        // Handle cancel click
    }

}