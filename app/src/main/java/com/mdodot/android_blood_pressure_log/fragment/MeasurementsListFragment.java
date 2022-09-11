package com.mdodot.android_blood_pressure_log.fragment;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.adapter.MeasurementsAdapter;
import com.mdodot.android_blood_pressure_log.database.RoomDB;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;

import java.util.ArrayList;
import java.util.Date;
import java.time.LocalTime;
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
    private String filterTimeFrom;
    private String filterTimeTo;
    private String filterDateFrom;
    private String filterDateTo;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private TextView avgSystolicTextView;
    private TextView avgDiastolicTextView;
    private TextView avgPulseTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.layoutView = inflater.inflate(R.layout.measurements_list_fragment, container, false);
        this.avgSystolicTextView = layoutView.findViewById(R.id.avg_systolic);
        this.avgDiastolicTextView = layoutView.findViewById(R.id.avg_diastolic);
        this.avgPulseTextView = layoutView.findViewById(R.id.avg_pulse);
        this.newEntryFragment = new NewEntryFragment();
        this.newEntryFragment.registerListener(this);
        setHasOptionsMenu(true);
        setToolbarMenuItemsListener();
        loadMesurements();
        registerMeasurementDetailsActivityResultLauncher();
        setFragmentResultListener();
        calculateAverageMeasurements();
        return layoutView;
    }

    public void calculateAverageMeasurements() {
        if (measurementsList.size() > 0) {
            int avgSystolic = 0;
            int avgDiastolic = 0;
            int avgPulse = 0;

            for (MeasurementEntity measurement : measurementsList) {
                avgSystolic += measurement.getSystolic();
                avgDiastolic += measurement.getDiastolic();
                avgPulse += measurement.getPulse();
            }

            avgSystolicTextView.setText(String.valueOf(avgSystolic/measurementsList.size()));
            avgDiastolicTextView.setText(String.valueOf(avgDiastolic/measurementsList.size()));
            avgPulseTextView.setText(String.valueOf(avgPulse/measurementsList.size()));
        } else {
            avgSystolicTextView.setText("-----");
            avgDiastolicTextView.setText("-----");
            avgPulseTextView.setText("-----");
        }
    }

    public void setFragmentResultListener() {
        getChildFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.getBoolean("clear_filters")) {
                    filterDateFrom = filterDateTo = filterTimeFrom = filterTimeTo = null;
                    refreshMeasurementsList();
                } else {
                    filterDateFrom = result.getString("filter_date_from");
                    filterDateTo = result.getString("filter_date_to");
                    filterTimeFrom = result.getString("filter_time_from");
                    filterTimeTo = result.getString("filter_time_to");

                    if (filterTimeTo.equals("24:00")) filterTimeTo = "23:59";

                    if (filterDateTo != null && filterTimeFrom != null && filterTimeTo != null) {
                        filterMeasurementsList();
                    }
                }
            }
        });
    }

    public void filterMeasurementsList() {
        try {
            Date endDate = sdf.parse(filterDateTo);
            LocalTime startTime = LocalTime.parse(filterTimeFrom);
            LocalTime endTime = LocalTime.parse(filterTimeTo);
            for (int i = measurementsList.size()-1 ; i >= 0 ; i -- ){
                try {
                    final LocalTime measurementTime = LocalTime.parse(measurementsList.get(i).getTime());
                    final Date measurementDate = sdf.parse(measurementsList.get(i).getDate());
                    if (filterDateFrom != null) {
                        final Date startDate = sdf.parse(filterDateFrom);
                        if (measurementTime.isBefore(startTime) ||
                                measurementTime.isAfter(endTime) ||
                                measurementDate.before(startDate) ||
                                measurementDate.after(endDate)) {
                            measurementsList.remove(i);
                        }
                    } else {
                        if (measurementTime.isBefore(startTime) ||
                                measurementTime.isAfter(endTime) ||
                                measurementDate.after(endDate)) {
                            measurementsList.remove(i);
                        }
                    }
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        this.measurementsAdapter = new MeasurementsAdapter(measurementsList, getContext(), MeasurementsListFragment.this);
        this.measurementsRecyclerView.setAdapter(measurementsAdapter);
        calculateAverageMeasurements();
    }

    public void setToolbarMenuItemsListener() {
        MaterialToolbar toolbar = (MaterialToolbar) layoutView.findViewById(R.id.topAppBar);
        MenuItem menuFilters = (MenuItem) toolbar.getMenu().findItem(R.id.menu_filters);
        MenuItem menuShare = (MenuItem) toolbar.getMenu().findItem(R.id.menu_share);
        MenuItem menuAlerts = (MenuItem) toolbar.getMenu().findItem(R.id.menu_alerts);

        menuFilters.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showFiltersDialogFragment();
                return false;
            }
        });

        menuShare.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ShareDialogFragment shareDialogFragment = ShareDialogFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable("measurements_list", new ArrayList<MeasurementEntity>(measurementsList));
                shareDialogFragment.setArguments(bundle);
                shareDialogFragment.show(getChildFragmentManager(), "ShareDialogFragmentTAG");
                return false;
            }
        });

        menuAlerts.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getContext(), "alerts", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void showFiltersDialogFragment() {
        DialogFragment filtersDialogFragment = FiltersDialogFragment.newInstance();
        if (filterTimeFrom != null && filterTimeTo != null && filterDateTo != null) {
            Bundle bundle = new Bundle();
            bundle.putString("filter_date_from", filterDateFrom);
            bundle.putString("filter_date_to", filterDateTo);
            bundle.putString("filter_time_from", filterTimeFrom);
            bundle.putString("filter_time_to", filterTimeTo);
            filtersDialogFragment.setArguments(bundle);
        }
        filtersDialogFragment.show(getChildFragmentManager(), "FiltersDialogFragmentTAG");
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
        calculateAverageMeasurements();
    }
}