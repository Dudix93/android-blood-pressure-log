package com.mdodot.android_blood_pressure_log.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.gson.Gson;
import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;
import com.mdodot.android_blood_pressure_log.fragment.NewEntryFragment;

public class EditMeasurementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_measurement_activity);

        if (getIntent().getSerializableExtra("edit_measurement") != null) {
            MeasurementEntity measurementEntity = (MeasurementEntity) getIntent().getSerializableExtra("edit_measurement");
            Fragment newEntryFragment = new NewEntryFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("measurement_edit_data", new Gson().toJson(measurementEntity));
            newEntryFragment.setArguments(bundle);
            transaction.add(R.id.fragment_container_view, newEntryFragment).commit();
        }
    }
}