package com.example.vaccap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccap.databinding.ActivityMainBinding;
import com.example.vaccap.models.adapters.VaccineAdapter;
import com.example.vaccap.models.VaccineInfo;
import com.example.vaccap.ui.DrawerBaseActivity;
import com.example.vaccap.ui.appointments.AppointmentSchedulingActivity;
import com.example.vaccap.ui.authentication.PatientORAdminActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends DrawerBaseActivity{
    ActivityMainBinding activityMainBinding;
    FirebaseAuth mAuth;
    private List<VaccineInfo> vaccines;
    Button makeaAPP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        allocateActivityTitles("Home");

        mAuth = FirebaseAuth.getInstance();
        onStart();
        RecyclerView vaccineRecyclerView = findViewById(R.id.vaccine_recycler_view);
        vaccineRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        makeaAPP = findViewById(R.id.makeAppointment);

        makeaAPP.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AppointmentSchedulingActivity.class));
        });

        vaccines = new ArrayList<>();
        vaccines.add(new VaccineInfo("At Birth", "HepB", true));
        vaccines.add(new VaccineInfo("2 months", "HepB, DTaP, PCV, Hib, Polio, RV", true));
        vaccines.add(new VaccineInfo("4 months", "HepB, DTaP, PCV, Hib, Polio, RV", true));
        vaccines.add(new VaccineInfo("6 months", "HepB, DTaP, PCV, Hib³, Polio, RV⁴, Influenza⁵, COVID⁶", true));
        vaccines.add(new VaccineInfo("8 months", "HepB, DTaP, PCV, Hib³, Polio, RV⁴, Influenza⁵, COVID⁶", true));
        vaccines.add(new VaccineInfo("12 months and Older", "MMR, DTaP, PCV, Hib, Chickenpox, HepA⁷, Influenza⁵", true));
        // Add more vaccines here

        VaccineAdapter vaccineAdapter = new VaccineAdapter(vaccines);
        vaccineRecyclerView.setAdapter(vaccineAdapter);

        vaccineAdapter.setOnItemClickListener(new VaccineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                VaccineInfo clickedVaccine = vaccines.get(position);
                Toast.makeText(MainActivity.this, "Clicked: " + clickedVaccine.getAgeRange(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, PatientORAdminActivity.class));
        }
//        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }


}