package com.example.vaccap.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vaccap.R;
import com.example.vaccap.databinding.ActivityAboutBinding;

public class AboutActivity extends DrawerBaseActivity {

    ActivityAboutBinding activityAboutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAboutBinding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(activityAboutBinding.getRoot());
        allocateActivityTitles("About");
    }
}