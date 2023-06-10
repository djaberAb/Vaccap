package com.example.vaccap.admin;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vaccap.R;

public class VaccineActivity extends AppCompatActivity {

    private CheckBox hepatitisBCheckBox, rotavirusCheckBox, dtapCheckBox, hibCheckBox, pcv13CheckBox,
            polioCheckBox, rotavirus2CheckBox, dtap2CheckBox, hib2CheckBox, pcv13_2CheckBox,
            rotavirus3CheckBox, dtap3CheckBox, hib3CheckBox, pcv13_3CheckBox, polio2CheckBox,
            mmrCheckBox, varicellaCheckBox, hibBoosterCheckBox, pcv13BoosterCheckBox, hepatitisACheckBox,
            dtapBoosterCheckBox, dtapBooster2CheckBox, mmrBoosterCheckBox, varicellaBoosterCheckBox, polioBoosterCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccines);

        // Find all the checkboxes in the layout
        hepatitisBCheckBox = findViewById(R.id.hepatitis_b_checkbox);
        rotavirusCheckBox = findViewById(R.id.rotavirus_checkbox);
        dtapCheckBox = findViewById(R.id.dtap_checkbox);
        hibCheckBox = findViewById(R.id.hib_checkbox);
        pcv13CheckBox = findViewById(R.id.pcv13_checkbox);
        polioCheckBox = findViewById(R.id.polio_checkbox);
        rotavirus2CheckBox = findViewById(R.id.rotavirus_2_checkbox);
        dtap2CheckBox = findViewById(R.id.dtap_2_checkbox);
        hib2CheckBox = findViewById(R.id.hib_2_checkbox);
        pcv13_2CheckBox = findViewById(R.id.pcv13_2_checkbox);
        rotavirus3CheckBox = findViewById(R.id.rotavirus_3_checkbox);
        dtap3CheckBox = findViewById(R.id.dtap_3_checkbox);
        hib3CheckBox = findViewById(R.id.hib_3_checkbox);
        pcv13_3CheckBox = findViewById(R.id.pcv13_3_checkbox);
        polio2CheckBox = findViewById(R.id.polio_2_checkbox);
        mmrCheckBox = findViewById(R.id.mmr_checkbox);
        varicellaCheckBox = findViewById(R.id.varicella_checkbox);
        hibBoosterCheckBox = findViewById(R.id.hib_booster_checkbox);
        pcv13BoosterCheckBox = findViewById(R.id.pcv13_booster_checkbox);
        hepatitisACheckBox = findViewById(R.id.hepatitis_a_checkbox);
        dtapBoosterCheckBox = findViewById(R.id.dtap_booster_checkbox);
        dtapBooster2CheckBox = findViewById(R.id.dtap_booster_2_checkbox);
        mmrBoosterCheckBox = findViewById(R.id.mmr_booster_checkbox);
        varicellaBoosterCheckBox = findViewById(R.id.varicella_booster_checkbox);
        polioBoosterCheckBox = findViewById(R.id.polio_booster_checkbox);

        // Set the clinic name in the layout to a custom value
        String clinicName = getIntent().getStringExtra("clinicName");
        TextView clinicNameTextView = findViewById(R.id.clinic_name);
        clinicNameTextView.setText(clinicName);

        // Set the checked state of all the checkboxes to true
        hepatitisBCheckBox.setChecked(true);
        rotavirusCheckBox.setChecked(true);
        dtapCheckBox.setChecked(true);
        hibCheckBox.setChecked(true);
        pcv13CheckBox.setChecked(true);
        polioCheckBox.setChecked(true);
        rotavirus2CheckBox.setChecked(true);
        dtap2CheckBox.setChecked(true);
        hib2CheckBox.setChecked(true);
        pcv13_2CheckBox.setChecked(true);
        rotavirus3CheckBox.setChecked(true);
        dtap3CheckBox.setChecked(true);
        hib3CheckBox.setChecked(true);
        pcv13_3CheckBox.setChecked(true);
        polio2CheckBox.setChecked(true);
        mmrCheckBox.setChecked(true);
        varicellaCheckBox.setChecked(true);
        hibBoosterCheckBox.setChecked(true);
        pcv13BoosterCheckBox.setChecked(true);
        hepatitisACheckBox.setChecked(true);
        dtapBoosterCheckBox.setChecked(true);
        dtapBooster2CheckBox.setChecked(true);
        mmrBoosterCheckBox.setChecked(true);
        varicellaBoosterCheckBox.setChecked(true);
        polioBoosterCheckBox.setChecked(true);

        // Add logic to handle when a checkbox is checked or unchecked
        hepatitisBCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // do something when the checkbox is checked or unchecked
        });

        // Repeat the above setChecked() and setOnCheckedChangeListener() calls for each checkbox in the layout
    }
}