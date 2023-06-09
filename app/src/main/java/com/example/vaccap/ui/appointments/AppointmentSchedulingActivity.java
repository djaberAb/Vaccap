package com.example.vaccap.ui.appointments;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.vaccap.MainActivity;
import com.example.vaccap.R;
import com.example.vaccap.models.Appointment;
import com.example.vaccap.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AppointmentSchedulingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView date;
    private TextView time;
    private Spinner type, clinicName;
    private Button requestAppointmentButton;

    private List<String> clinicNames = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private String patientName;

    private DocumentReference appointmentDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_scheduling);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        date = findViewById(R.id.date_input);
        time = findViewById(R.id.time_input);
        type = findViewById(R.id.type_vaccine_input);
        clinicName = findViewById(R.id.clinicName);
        requestAppointmentButton = findViewById(R.id.request_appointment_button);
        date.setOnClickListener(view -> showDatePickerDialog());
        time.setOnClickListener(view -> showTimePickerDialog());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.vaccines, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        populateClinicSpinner(clinicName);

        requestAppointmentButton.setOnClickListener(v -> requestAppointment());
        getPatientName();
    }

    private void requestAppointment() {
        String date = this.date.getText().toString();
        String time = this.time.getText().toString();
        String type = this.type.getSelectedItem().toString();
        String clinic = clinicName.getSelectedItem().toString();

        Appointment appointment = new Appointment(date, time, type, clinic, patientName, "pending");

        // add the appointment to Firestore
        addAppointmentToFirestore(appointment);

        // create and print the PDF document
        downloadAndPrintPdf(appointment);
    }

    private void addAppointmentToFirestore(Appointment appointment) {
        // Generate a unique ID for the appointment document
        String appointmentId = db.collection("appointments").document().getId();
        appointment.setId(appointmentId);

        // Add the appointment to Firestore using the generated ID
        db.collection("appointments").document(appointmentId)
                .set(appointment)
                .addOnSuccessListener(aVoid -> {
                    // Listen for updates to the appointment status
                    DocumentReference appointmentDocRef = db.collection("appointments").document(appointmentId);
                    appointmentDocRef.addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            Log.e(TAG, "Error listening for appointment updates", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            String status = snapshot.getString("status");
                            if (status != null) {
                                appointment.setStatus(status);
                                updateAppointmentStatus(appointment);
                            }
                        }
                    });

                    Toast.makeText(this, "Appointment requested successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AppointmentSchedulingActivity.this, MainActivity.class));
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding appointment", e);
                    Toast.makeText(this, "Failed to request appointment", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateAppointmentStatus(Appointment appointment) {
        appointmentDocRef.update("status", appointment.getStatus())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Appointment status updated"))
                .addOnFailureListener(e -> Log.e(TAG, "Error updating appointment status", e));
    }

    private void getPatientName() {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        DocumentReference userDocRef = db.collection("/users/Patients/patients").document(userId);
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            if (user != null) {
                patientName = user.getUserName();
            }
        }).addOnFailureListener(e -> Log.e(TAG, "Error getting patient name", e));
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
                    date.setText(selectedDate);
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
                    time.setText(selectedTime);
                },
                hour,
                minute,
                DateFormat.is24HourFormat(this)
        );

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void populateClinicSpinner(Spinner spinner) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> clinicNames = new ArrayList<>();

        db.collection("/users/Clinics/clinics")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String clinicName = document.getString("clinicName");
                            clinicNames.add(clinicName);
                        }

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clinicNames);
                        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(spinnerAdapter);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void createAndPrintPdf(Appointment appointment) {
        // create a new PDF document
        Document document = new Document();

        try {
            // create a new pdf writer
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(getExternalFilesDir(null) + "/appointment.pdf")));
            }

            // open the document for writing
            document.open();

            // add the appointment details to the document
            Paragraph heading = new Paragraph("Appointment Details");
            heading.setAlignment(Element.ALIGN_CENTER);
            document.add(heading);

            document.add(new Paragraph("Date: " + appointment.getDate()));
            document.add(new Paragraph("Time: " + appointment.getTime()));
            document.add(new Paragraph("Type: " + appointment.getType()));
            document.add(new Paragraph("Clinic: " + appointment.getClinic()));
            document.add(new Paragraph("Patient Name: " + appointment.getPatient()));

            // close the document
            document.close();

            // show a Toast message to notify the user that the PDF has been saved
            Toast.makeText(this, "Appointment PDF saved to device storage", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadAndPrintPdf(Appointment appointment) {
        createAndPrintPdf(appointment);

        // Get the external storage directory
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Create the Appointments directory if it doesn't exist
        File appointmentsDir = new File(downloadsDir, "Appointments");
        if (!appointmentsDir.exists()) {
            appointmentsDir.mkdirs();
        }

        // Create the appointment PDF file
        File appointmentFile = new File(appointmentsDir, "appointment.pdf");

        try {
            // Copy the generated PDF file to the appointment file
            InputStream in = new FileInputStream(getExternalFilesDir(null) + "/appointment.pdf");
            OutputStream out = new FileOutputStream(appointmentFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();

            // create an Intent to open the PDF file with a PDF viewer app
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", appointmentFile);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } catch (IOException e) {
            Log.e(TAG, "Error copying PDF file to appointment directory", e);
            Toast.makeText(this, "Failed to download appointment", Toast.LENGTH_SHORT).show();
        }
    }

}