package com.mdodot.android_blood_pressure_log.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.adapter.MeasurementsAdapter;
import com.mdodot.android_blood_pressure_log.database.RoomDB;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;

import java.util.List;

public class MeasurementsListFragment extends Fragment implements NewEntryFragment.OnRefreshMeasurementsListListener {

    private View layoutView;
    private MeasurementsAdapter measurementsAdapter;
    private List<MeasurementEntity> measurementsList;
    private RecyclerView measurementsRecyclerView;
    private RoomDB roomDB;
    private NewEntryFragment newEntryFragment;
    private ActivityResultLauncher<Intent> measurementDetailsActivityResultLauncher;
    private MeasurementEntity measurementEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.layoutView = inflater.inflate(R.layout.measurements_list_fragment, container, false);
        this.newEntryFragment = new NewEntryFragment();
        this.newEntryFragment.registerListener(this);
        loadMesurements();
        registerMeasurementDetailsActivityResultLauncher();
        return layoutView;
    }

    public void registerMeasurementDetailsActivityResultLauncher() {
        measurementDetailsActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data.getSerializableExtra("measurement_to_delete") != null && data.getSerializableExtra("measurement_to_delete") instanceof MeasurementEntity) {
                            measurementEntity = (MeasurementEntity) data.getSerializableExtra("measurement_to_delete");
                            deleteMeasurement();
                            }
                        }
                    }
                });
    }

    public void deleteMeasurement() {
        roomDB.measurementDao().delete(measurementEntity);
        measurementsList = roomDB.measurementDao().getAll();
        this.measurementsAdapter = new MeasurementsAdapter(measurementsList, getContext(), MeasurementsListFragment.this);
        this.measurementsRecyclerView.setAdapter(measurementsAdapter);
    }

    public void launchMeasurementDetailsActivity(Intent intent) {
        measurementDetailsActivityResultLauncher.launch(intent);
    }

    public void loadMesurements() {
        roomDB = RoomDB.getInstance(getContext());
        measurementsList = roomDB.measurementDao().getAll();
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        this.measurementsRecyclerView = this.layoutView.findViewById(R.id.measurements_recycler_view);
        this.measurementsAdapter = new MeasurementsAdapter(measurementsList, getContext(), MeasurementsListFragment.this);
        this.measurementsRecyclerView.setLayoutManager(llm);
        this.measurementsRecyclerView.setAdapter(measurementsAdapter);
    }

    public void refreshMeasurementsList() {
        measurementsList = roomDB.measurementDao().getAll();
        this.measurementsAdapter = new MeasurementsAdapter(measurementsList, getContext(), MeasurementsListFragment.this);
        this.measurementsRecyclerView.setAdapter(measurementsAdapter);
    }
}