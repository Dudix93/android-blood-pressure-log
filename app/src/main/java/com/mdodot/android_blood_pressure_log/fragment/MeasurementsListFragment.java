package com.mdodot.android_blood_pressure_log.fragment;

import android.content.Context;
import android.os.Bundle;

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

public class MeasurementsListFragment extends Fragment {

    private View layoutView;
    private MeasurementsAdapter measurementsAdapter;
    private List<MeasurementEntity> measurementsList;
    private RecyclerView measurementsRecyclerView;
    private RoomDB roomDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.layoutView = inflater.inflate(R.layout.measurements_list_fragment, container, false);
        loadMesurements();
        return layoutView;
    }

    private void loadMesurements() {
        roomDB = RoomDB.getInstance(getContext());
        measurementsList = roomDB.measurementDao().getAll();
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        this.measurementsRecyclerView = this.layoutView.findViewById(R.id.measurements_recycler_view);
        this.measurementsAdapter = new MeasurementsAdapter(measurementsList, getContext());
        this.measurementsRecyclerView.setLayoutManager(llm);
        this.measurementsRecyclerView.setAdapter(measurementsAdapter);
    }
}