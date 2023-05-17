package com.example.vaccap;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vaccap.databinding.ActivityMainBinding;
import com.example.vaccap.ui.DrawerBaseActivity;
import com.example.vaccap.ui.authentication.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends DrawerBaseActivity{
    ActivityMainBinding activityMainBinding;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        allocateActivityTitles("Home");



        mAuth = FirebaseAuth.getInstance();

    }
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
//        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}