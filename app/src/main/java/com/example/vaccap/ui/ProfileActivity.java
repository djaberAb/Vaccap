package com.example.vaccap.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vaccap.R;
import com.example.vaccap.databinding.ActivityProfileBinding;

public class ProfileActivity extends DrawerBaseActivity {

    ActivityProfileBinding activityProfileBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitles("Profile");
    }
}