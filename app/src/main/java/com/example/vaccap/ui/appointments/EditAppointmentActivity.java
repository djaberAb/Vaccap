package com.example.vaccap.ui.appointments;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vaccap.R;
import com.example.vaccap.models.Appointment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Locale;

public class EditAppointmentActivity extends AppCompatActivity {
    private TextView dateTV;
    private TextView timeTV;
    private Button updateButton;
    private Button cancelButton;
    private String appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);

        dateTV = findViewById(R.id.date_tv);
        timeTV = findViewById(R.id.time_edit_text);
        updateButton = findViewById(R.id.update_button);
        cancelButton = findViewById(R.id.cancel_button);

        appointmentId = getIntent().getStringExtra("appointmentId");

        // Retrieve the appointment from Firestore and pre-fill the date and time fields
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference appointmentRef = db.collection("appointments").document(appointmentId);

        appointmentRef.get().addOnSuccessListener(documentSnapshot -> {
                    Appointment appointment = documentSnapshot.toObject(Appointment.class);
                    dateTV.setText(appointment.getDate());
                    timeTV.setText(appointment.getTime());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting appointment from Firestore", e);
                    // TODO: Handle the error case
                });

        updateButton.setOnClickListener(v -> {
            // Update the appointment date and time in Firestore
            String newDate = dateTV.getText().toString();
            String newTime = timeTV.getText().toString();

            appointmentRef.update("date", newDate, "time", newTime)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Appointment date and time updated successfully");
                        // TODO: Perform any additional actions after the appointment date and time are updated
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error updating appointment date and time", e);
                        // TODO: Handle the error case
                    });
        });

        cancelButton.setOnClickListener(v -> finish());
        dateTV.setOnClickListener(v -> showDatePickerDialog());
        timeTV.setOnClickListener(v -> showTimePickerDialog());
    }


    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new DatePickerDialog instance
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    // Update the EditText field with the selected date
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                    dateTV.setText(selectedDate);
                },
                year,
                month,
                dayOfMonth
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a new TimePickerDialog instance
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    // Update the EditText field with the selected time
                    String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    timeTV.setText(selectedTime);
                },
                hour,
                minute,
                DateFormat.is24HourFormat(this)
        );

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

}