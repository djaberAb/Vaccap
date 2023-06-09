package com.example.vaccap.admin;


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
import com.example.vaccap.models.User;
import com.example.vaccap.models.adapters.PatientsAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class PatientsFragment extends Fragment {
    private PatientsAdapter patientsAdapter;

    public PatientsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patients, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.patientsad_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        patientsAdapter = new PatientsAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(patientsAdapter);

        getPatientList();

        return view;
    }

    private void getPatientList() {
        // Retrieve the list of patients from the database and pass it to the patientAdapter
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference patientsRef = db.collection("users/Patients/patients");

        patientsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<User> patients = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        User patient = documentSnapshot.toObject(User.class);
                        patients.add(patient);
                    }

                    patientsAdapter.updatePatientList(patients);
                })
                .addOnFailureListener(e -> Log.d(TAG, "Error getting patients list from database", e));
    }
}