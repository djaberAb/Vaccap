package com.example.vaccap.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vaccap.R;
import com.example.vaccap.databinding.ActivitySearchBinding;

public class SearchActivity extends DrawerBaseActivity {

    ActivitySearchBinding activitySearchBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(activitySearchBinding.getRoot());
        allocateActivityTitles("Search");
    }
}