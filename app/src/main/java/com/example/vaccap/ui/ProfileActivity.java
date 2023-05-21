package com.example.vaccap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.vaccap.R;
import com.example.vaccap.databinding.ActivityProfileBinding;

public class ProfileActivity extends DrawerBaseActivity {

    private TextView userName, fullName, phoneNumber, babyName, babyBirthDay;
    private TextView titleFName, titleLName;
    private Button editProfileBtn;

    ActivityProfileBinding activityProfileBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitles("Profile");

        userName = findViewById(R.id.userNameSignup);
        fullName = findViewById(R.id.fullNameSignup);
        phoneNumber = findViewById(R.id.fullNamesignup);
        babyName = findViewById(R.id.nameBabySignup);
        babyBirthDay = findViewById(R.id.dateOfBirthSignup);

        titleFName = findViewById(R.id.titleUserName);
        titleLName = findViewById(R.id.titleLFullName);

    }

    public void showUserData(){
        Intent intent = getIntent();

        String userNameUser = intent.getStringExtra("username");
        String fullNameUser = intent.getStringExtra("firstname");
        String phoneNumberUser = intent.getStringExtra("phonenumber");
        String nameBaby = intent.getStringExtra("namebaby");
        String bDayBaby = intent.getStringExtra("birthdaybaby");

    }
}