package com.mdodot.android_blood_pressure_log.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;
import com.mdodot.android_blood_pressure_log.fragment.NewEntryFragment;

public class MeasurementDetailsActivity extends AppCompatActivity {

    private TextView measurementDetailsDateTextView;
    private TextView measurementDetailsTextView;
    private TextView measurementDetailsPulseTextView;
    private TextView measurementDetailsNoteTextView;
    private MaterialButton editMeasurementButton;
    private MaterialButton deleteMeasurementButton;
    private MeasurementEntity measurementEntity;
    private ActivityResultLauncher<Intent> editMeasurementActivityResultLauncher;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.measurement_details_activity);

        this.mContext = this;

        measurementDetailsDateTextView = findViewById(R.id.measurement_details_date);
        measurementDetailsTextView = findViewById(R.id.measurement_details_blood_pressure);
        measurementDetailsNoteTextView = findViewById(R.id.measurement_details_note);
        measurementDetailsPulseTextView = findViewById(R.id.measurement_details_pulse);
        editMeasurementButton = findViewById(R.id.edit_measurement_material_button);
        deleteMeasurementButton = findViewById(R.id.delete_measurement_material_button);

        if (getIntent().getSerializableExtra("measurement") != null && getIntent().getSerializableExtra("measurement") instanceof MeasurementEntity) {
            measurementEntity = (MeasurementEntity) getIntent().getSerializableExtra("measurement");
            fillMeasurementDetails();
            setOnDeleteClickListener();
            setOnEditClickListener();
            registerEditMeasurementActivityResultLauncher();
        }
    }

    public void setOnEditClickListener() {
        editMeasurementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditMeasurementActivity.class);
                intent.putExtra("edit_measurement", measurementEntity);
                editMeasurementActivityResultLauncher.launch(intent);
            }
        });
    }

    public void setOnDeleteClickListener() {
        deleteMeasurementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setTitle(getString(R.string.delete_measurment))
                        .setMessage(getString(R.string.delete_entry_message))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.putExtra("measurement_to_delete", measurementEntity);
                                setResult(RESULT_OK, intent );
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_delete)
                        .show();
            }
        });
    }

    public void fillMeasurementDetails() {
        measurementDetailsDateTextView.setText(measurementEntity.getTime() + " " + measurementEntity.getDate());
        measurementDetailsPulseTextView.setText(String.valueOf(measurementEntity.getPulse()));
        measurementDetailsTextView.setText(measurementEntity.getSystolic() + "/" + measurementEntity.getDiastolic());
        if (measurementEntity.getNote().toString().isEmpty()) {
            measurementDetailsNoteTextView.setText(getString(R.string.no_description));
        } else {
            measurementDetailsNoteTextView.setText(measurementEntity.getNote());
        }
    }

    public void registerEditMeasurementActivityResultLauncher() {
        editMeasurementActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data.getSerializableExtra("new_measurement_values") != null && data.getSerializableExtra("new_measurement_values") instanceof MeasurementEntity) {
                                measurementEntity = (MeasurementEntity) data.getSerializableExtra("new_measurement_values");
                                fillMeasurementDetails();
                            }
                        }
                    }
                });
    }
}
