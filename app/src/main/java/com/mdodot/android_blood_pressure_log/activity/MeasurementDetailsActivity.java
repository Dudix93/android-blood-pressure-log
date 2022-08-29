package com.mdodot.android_blood_pressure_log.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;

public class MeasurementDetailsActivity extends AppCompatActivity {

    private TextView measurementDetailsDateTextView;
    private TextView measurementDetailsTextView;
    private TextView measurementDetailsPulseTextView;
    private TextView measurementDetailsNoteTextView;
    private MaterialButton editMeasurementButton;
    private MaterialButton deleteMeasurementButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.measurement_details_activity);

        measurementDetailsDateTextView = findViewById(R.id.measurement_details_date);
        measurementDetailsTextView = findViewById(R.id.measurement_details_blood_pressure);
        measurementDetailsNoteTextView = findViewById(R.id.measurement_details_note);
        measurementDetailsPulseTextView = findViewById(R.id.measurement_details_pulse);
        editMeasurementButton = findViewById(R.id.edit_measurement_material_button);
        deleteMeasurementButton = findViewById(R.id.delete_measurement_material_button);

        fillMeasurementDetails();
    }

    public void fillMeasurementDetails() {
        if (getIntent().getSerializableExtra("measurement") != null && getIntent().getSerializableExtra("measurement") instanceof MeasurementEntity) {
            MeasurementEntity measurementEntity = (MeasurementEntity) getIntent().getSerializableExtra("measurement");
            measurementDetailsDateTextView.setText(measurementEntity.getTime() + " " + measurementEntity.getDate());
            measurementDetailsPulseTextView.setText(String.valueOf(measurementEntity.getPulse()));
            measurementDetailsTextView.setText(measurementEntity.getSystolic() + "/" + measurementEntity.getDiastolic());
            if (measurementEntity.getNote().toString().isEmpty()) {
                measurementDetailsNoteTextView.setText(getString(R.string.no_description));
            } else {
                measurementDetailsNoteTextView.setText(measurementEntity.getNote());
            }
        }
    }
}
