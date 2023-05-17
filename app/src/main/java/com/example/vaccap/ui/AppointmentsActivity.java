package com.example.vaccap.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vaccap.R;
import com.example.vaccap.databinding.ActivityAppointmentsBinding;

public class AppointmentsActivity extends DrawerBaseActivity {

    ActivityAppointmentsBinding activityAppointmentsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAppointmentsBinding = ActivityAppointmentsBinding.inflate(getLayoutInflater());
        setContentView(activityAppointmentsBinding.getRoot());
        allocateActivityTitles("Appointments");
    }
}