package com.example.vaccap.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vaccap.R;
import com.example.vaccap.databinding.ActivitySettingsBinding;

public class SettingsActivity extends DrawerBaseActivity {

    ActivitySettingsBinding activitySettingsBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(activitySettingsBinding.getRoot());
        allocateActivityTitles("Settings");
    }
}