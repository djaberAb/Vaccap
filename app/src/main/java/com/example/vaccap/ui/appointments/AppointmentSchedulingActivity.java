package com.example.vaccap.ui.appointments;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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

        // create and print the PDF document
        createAndPrintPdf(appointment);

        // add the appointment to Firestore
        addAppointmentToFirestore(appointment);
    }

    private void addAppointmentToFirestore(Appointment appointment) {
        db.collection("appointments").add(appointment)
                .addOnSuccessListener(documentReference -> {
                    appointment.setId(documentReference.getId()); // Set the appointment ID
                    appointmentDocRef = db.collection("appointments").document(appointment.getId()); // Use the appointment ID to get the document reference
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
                    startActivity(new Intent(AppointmentSchedulingActivity.this, Appointment.class));
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
            PdfWriter.getInstance(document, new FileOutputStream("appointment.pdf"));

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

            // print the document
            printPdf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printPdf() {
        // get the file path of the PDF document
        String filePath = getFilesDir() + "/appointment.pdf";
        File file = new File(filePath);

        // create a new print job name
        String jobName = getString(R.string.app_name) + " Appointment";

        // create a new print job
        PrintJob printJob = getSystemService(PrintManager.class).print(jobName, new PrintDocumentAdapter() {
            @Override
            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
                if (cancellationSignal.isCanceled()) {
                    callback.onLayoutCancelled();
                    return;
                }

                PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder("document.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(1);

                PrintDocumentInfo info = builder.build();
                callback.onLayoutFinished(info, true);
            }

            @Override
            public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
                try {
                    FileInputStream input = new FileInputStream(file);
                    OutputStream output = new FileOutputStream(destination.getFileDescriptor());

                    byte[] buf = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = input.read(buf)) > 0) {
                        output.write(buf, 0, bytesRead);
                    }

                    callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                    input.close();
                    output.close();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "File not found: " + e.getMessage());
                } catch (Exception e) {
                    Log.e(TAG, "Error printing PDF: " + e.getMessage());
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();

                // delete the PDF file after printing
                File file = new File(getFilesDir() + "/appointment.pdf");
                file.delete();
            }
        }, null);

        // notify the user if the print job was unsuccessful
        if (printJob == null) {
            Toast.makeText(this, "Unable to print appointment", Toast.LENGTH_SHORT).show();
        }
    }

}