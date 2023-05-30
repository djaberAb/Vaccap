package com.example.vaccap.ui.profile;

import static android.content.ContentValues.TAG;

import static com.example.vaccap.models.User.fromSnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vaccap.R;
import com.example.vaccap.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
public class EditProfileActivity extends AppCompatActivity {


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    private TextView userName, fullName, phoneNumber, babyName, babyBirthDay, email;
    private Button updateBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userName = findViewById(R.id.edituserNamepr);
        fullName = findViewById(R.id.editFullNamepr);
        phoneNumber = findViewById(R.id.editPhoneNumberpr);
        babyName = findViewById(R.id.editNameBabypr);
        babyBirthDay = findViewById(R.id.editDateOfBirthpr);
        showUserData();

        updateBtn = findViewById(R.id.updateBtn);

        updateBtn.setOnClickListener(v->{
            updateUserData();
        });
    }

    public void updateUserData() {
        String userID = mAuth.getCurrentUser().getUid(); // Get the current user's ID

        DocumentReference userDocRef = mDatabase.collection("/users/Patients/patients").document(userID); // Get the reference to the user's document

        String username = userName.getText().toString().trim();
        String fullname = fullName.getText().toString().trim();
        String phonenumber = phoneNumber.getText().toString().trim();
        String babyname = babyName.getText().toString().trim();
        String babyBD= babyBirthDay.getText().toString().trim();
        String email = mAuth.getCurrentUser().getEmail();

        User user = new User(email, username, fullname, phonenumber, babyname, babyBD);

        // Convert the user object to a map using the toMap method you added earlier
        Map<String, Object> userMap = user.toMap();

        userDocRef.update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                    Log.d(TAG, "User data updated successfully");
                } else {
                    Log.w(TAG, "Error updating user data", task.getException());
                }
            }
        });
    }

    public void showUserData(){
        String userID = mAuth.getCurrentUser().getUid(); // Get the current user's ID

        DocumentReference userDocRef = mDatabase.collection("/users/Patients/patients").document(userID); // Get the reference to the user's document

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