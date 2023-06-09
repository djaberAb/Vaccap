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
import com.example.vaccap.models.Clinic;
import com.example.vaccap.models.adapters.ClinicAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClinicsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ClinicAdapter clinicAdapter;
    private List<Clinic> clinicList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clinics, container, false);

        recyclerView = view.findViewById(R.id.clinicsad_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        clinicList = new ArrayList<>();
        clinicAdapter = new ClinicAdapter(getActivity(), clinicList);
        recyclerView.setAdapter(clinicAdapter);

        getClinicList();

        return view;
    }

    private void getClinicList() {
        // Retrieve the list of clinics from the database and pass it to the clinicAdapter
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference clinicsRef = db.collection("users/Clinics/clinics");

        clinicsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Clinic> clinics = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Clinic clinic = documentSnapshot.toObject(Clinic.class);
                        clinics.add(clinic);
                    }

                    clinicAdapter.updateClinicList(clinics);
                })
                .addOnFailureListener(e -> Log.d(TAG, "Error getting clinics list from database", e));
    }
}