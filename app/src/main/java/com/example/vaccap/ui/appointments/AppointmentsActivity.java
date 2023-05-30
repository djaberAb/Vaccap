package com.example.vaccap.ui.appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vaccap.R;
import com.example.vaccap.databinding.ActivityAppointmentsBinding;
import com.example.vaccap.ui.DrawerBaseActivity;

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