package com.mdodot.android_blood_pressure_log.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.mdodot.android_blood_pressure_log.OnAlertLongClickCallback;
import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.adapter.AlertsAdapter;
import com.mdodot.android_blood_pressure_log.adapter.MeasurementsAdapter;
import com.mdodot.android_blood_pressure_log.database.RoomDB;
import com.mdodot.android_blood_pressure_log.entity.AlertEntity;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;
import com.mdodot.android_blood_pressure_log.fragment.MeasurementsListFragment;
import com.mdodot.android_blood_pressure_log.fragment.ShareDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class AlertsActivity extends AppCompatActivity implements OnAlertLongClickCallback {

    private ActivityResultLauncher<Intent> editAlertActivityResultLauncher;
    private AlertsAdapter alertsAdapter;
    private List<AlertEntity> alertsList;
    private RecyclerView alertsRecyclerView;
    private RoomDB roomDB;
    private ActionBar toolbar;
    private AlertEntity alertEntity;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alerts_activity);
        toolbar = getSupportActionBar();
        roomDB = RoomDB.getInstance(this);
        registerEditAlertActivityResultLauncher();
        loadAlerts();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        if (alertEntity != null) {
            toolbar.setTitle("");
            toolbar.setDisplayHomeAsUpEnabled(false);
            getMenuInflater().inflate(R.menu.edit_delete_alert_menu, menu);
        } else {
            toolbar.setTitle(R.string.alerts);
            toolbar.setDisplayHomeAsUpEnabled(true);
            getMenuInflater().inflate(R.menu.add_alert_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_alert_button:
                editAlert();
                break;
            case R.id.delete_alert_button:
                deleteAlert();
                break;
            case R.id.add_alert_button:
                Intent intent = new Intent(this, AlertActivity.class);
                editAlertActivityResultLauncher.launch(intent);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteAlert() {
        if (alertEntity != null) {
            roomDB.alertDao().delete(alertEntity);
            alertsList.remove(alertEntity);
            alertsAdapter = new AlertsAdapter(alertsList, this);
            alertsRecyclerView.setAdapter(alertsAdapter);
        }
    }

    public void editAlert() {
        if (alertEntity != null) {
            Intent intent = new Intent(this, AlertActivity.class);
            intent.putExtra("alert_entity", alertEntity);
            editAlertActivityResultLauncher.launch(intent);
        }
    }

    @Override
    public void openAlertEditDeleteDialog(AlertEntity alertEntity) {
        this.alertEntity = alertEntity;
        invalidateOptionsMenu();
    }

    @Override
    public void dismissAlertEditDeleteMenu() {
        this.alertEntity = null;
        invalidateOptionsMenu();
    }

    public void registerEditAlertActivityResultLauncher() {
        editAlertActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (result.getData() != null) {
                                saveAlert(result.getData());
                            }
                        }
                    }
                });
    }

    public void loadAlerts() {
        alertsList = roomDB.alertDao().getAll();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        this.alertsRecyclerView = findViewById(R.id.alerts_recycler_view);
        this.alertsAdapter = new AlertsAdapter(alertsList, this);
        this.alertsAdapter.setListener(AlertsActivity.this);
        this.alertsRecyclerView.setLayoutManager(llm);
        this.alertsRecyclerView.setAdapter(alertsAdapter);
    }

    public void saveAlert(Intent data) {
        boolean monday = false;
        boolean tuesday = false;
        boolean wednesday = false;
        boolean thursday = false;
        boolean friday = false;
        boolean saturday = false;
        boolean sunday = false;

        String time = "";
        String note = "";
        ArrayList<Integer> selectedDays = new ArrayList<Integer>();

        if (data.getStringExtra("alert_time") != null) time = data.getStringExtra("alert_time");
        if (data.getStringExtra("alert_note") != null) note = data.getStringExtra("alert_note");
        if (data.getIntegerArrayListExtra("alert_selected_days") != null) selectedDays = data.getIntegerArrayListExtra("alert_selected_days");

        if (!time.isEmpty() && selectedDays.size() != 0) {
            for (int dayNumber : selectedDays) {
                switch (dayNumber) {
                    case 1 :
                        monday = true;
                        break;
                    case 2 :
                        tuesday = true;
                        break;
                    case 3 :
                        wednesday = true;
                        break;
                    case 4 :
                        thursday = true;
                        break;
                    case 5 :
                        friday = true;
                        break;
                    case 6 :
                        saturday = true;
                        break;
                    case 7 :
                        sunday = true;
                        break;

                }
            }
        }

        AlertEntity alertEntity = new AlertEntity(time,
                note,
                monday,
                tuesday,
                wednesday,
                thursday,
                friday,
                saturday,
                sunday);

        if (data.getIntExtra("alert_id", -1) != -1) {
            int id = data.getIntExtra("alert_id", -1);
            roomDB.alertDao().update(id,
                    time,
                    note,
                    monday,
                    tuesday,
                    wednesday,
                    thursday,
                    friday,
                    saturday,
                    sunday);
        } else {
            roomDB.alertDao().insert(alertEntity);
        }
        alertsList = roomDB.alertDao().getAll();
        this.alertsAdapter = new AlertsAdapter(alertsList, this);
        this.alertsAdapter.setListener(AlertsActivity.this);
        this.alertsRecyclerView.setAdapter(alertsAdapter);
    }
}