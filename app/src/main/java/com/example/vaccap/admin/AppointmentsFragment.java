package com.example.vaccap.admin;

import static android.content.ContentValues.TAG;

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
import com.example.vaccap.models.adapters.AppointmentAdapterClinic;
import com.example.vaccap.ui.appointments.EditAppointmentActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsFragment extends Fragment implements AppointmentAdapterClinic.OnAppointmentClickListener {
    private RecyclerView recyclerView;
    private AppointmentAdapterClinic adapter;
    private List<Appointment> appointments;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        recyclerView = view.findViewById(R.id.appointmentsad_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db = FirebaseFirestore.getInstance();

        getAppointmentsFromFirestore();

        return view;
    }

    private void getAppointmentsFromFirestore() {
        db.collection("appointments")
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    appointments = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Appointment appointment = documentSnapshot.toObject(Appointment.class);
                        appointments.add(appointment);
                    }
                    adapter = new AppointmentAdapterClinic(appointments, this);
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting appointments from Firestore", e);
                    // TODO: Handle the error case
                });
    }

    @Override
    public void onAcceptButtonClick(Appointment appointment) {
        appointment.setStatus("accepted");
        updateAppointmentInFirestore(appointment);
    }

    @Override
    public void onDeclineButtonClick(Appointment appointment) {
        appointment.setStatus("declined");
        updateAppointmentInFirestore(appointment);
    }

    @Override
    public void onEditButtonClick(Appointment appointment) {
        Intent intent = new Intent(getActivity(), EditAppointmentActivity.class);
        intent.putExtra("appointmentId", appointment.getId());
        startActivity(intent);
    }

    @Override
    public void onCancelButtonClick(Appointment appointment) {
        db.collection("appointments")
                .document(appointment.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Appointment deleted", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting appointment from Firestore", e);
                    // TODO: Handle the error case
                });
    }

    private void updateAppointmentInFirestore(Appointment appointment) {
        db.collection("appointments")
                .document(appointment.getId())
                .set(appointment)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Appointment updated", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating appointment in Firestore", e);
                    // TODO: Handle the error case
                });
    }
}