package com.example.vaccap.ui.profile;

import static android.content.ContentValues.TAG;

import static com.example.vaccap.models.User.fromSnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.vaccap.R;
import com.example.vaccap.databinding.ActivityProfileBinding;
import com.example.vaccap.models.User;
import com.example.vaccap.ui.DrawerBaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends DrawerBaseActivity {

    FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private TextView userName, fullName, phoneNumber, babyName, babyBirthDay;
    private TextView titleuserName, titleFullName;
    private Button editProfileBtn;

    ActivityProfileBinding activityProfileBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitles("Profile");

        userName = findViewById(R.id.edituserNamepr);
        fullName = findViewById(R.id.fullNameSignup);
        phoneNumber = findViewById(R.id.fullNamesignup);
        babyName = findViewById(R.id.nameBabySignup);
        babyBirthDay = findViewById(R.id.dateOfBirthSignup);

        titleuserName = findViewById(R.id.titleUserName);
        titleFullName = findViewById(R.id.titleLFullName);

        editProfileBtn = findViewById(R.id.editProfileBtn);
        showUserData();

        editProfileBtn.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });

    }


    public void showUserData(){
        String userID = mAuth.getCurrentUser().getUid(); // Get the current user's ID

        DocumentReference userDocRef = mDatabase.collection("users").document(userID); // Get the reference to the user's document

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Convert the document data to a User object
                        User user = fromSnapshot(document);

                        // Update the UI with the user data

                        userName.setText(user.getUserName());
                        fullName.setText(user.getFullName());
                        phoneNumber.setText(user.getPhoneNumber());
                        babyName.setText(user.getNameBaby());
                        babyBirthDay.setText(user.getbDayBaby());
                        titleFullName.setText(user.fullName);
                        titleuserName.setText(user.userName);
                    } else {
                        Log.w(TAG, "No such document");
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }

}