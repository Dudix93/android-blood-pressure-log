package com.mdodot.android_blood_pressure_log.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.entity.AlertEntity;

import java.util.ArrayList;
import java.util.List;

public class AlertActivity extends AppCompatActivity {

    private MaterialButton alertTimeMaterialButton;
    private TextInputEditText alertNoteTextInputEditText;
    private CheckBox checkBoxMonday;
    private CheckBox checkBoxTuesday;
    private CheckBox checkBoxWednesday;
    private CheckBox checkBoxThursday;
    private CheckBox checkBoxFriday;
    private CheckBox checkBoxSaturday;
    private CheckBox checkBoxSunday;
    private ArrayList<Integer> selectedDays = new ArrayList<Integer>();
    private AlertEntity alertEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_activity);

        alertTimeMaterialButton = (MaterialButton) findViewById(R.id.alert_time_button) ;
        alertNoteTextInputEditText = (TextInputEditText) findViewById(R.id.alert_note_text_input_edit_text);
        checkBoxMonday = (CheckBox) findViewById(R.id.checkbox_1);
        checkBoxTuesday = (CheckBox) findViewById(R.id.checkbox_2);
        checkBoxWednesday = (CheckBox) findViewById(R.id.checkbox_3);
        checkBoxThursday = (CheckBox) findViewById(R.id.checkbox_4);
        checkBoxFriday = (CheckBox) findViewById(R.id.checkbox_5);
        checkBoxSaturday = (CheckBox) findViewById(R.id.checkbox_6);
        checkBoxSunday = (CheckBox) findViewById(R.id.checkbox_7);

        setOnAlertTimeButtonPressed();
        setToolbar();
        setEditAlert();
    }

    public void setEditAlert() {
        if(getIntent().getSerializableExtra("alert_entity") != null) {
            alertEntity = (AlertEntity) getIntent().getSerializableExtra("alert_entity");
            alertTimeMaterialButton.setText(alertEntity.getTime());
            alertNoteTextInputEditText.setText(alertEntity.getNote());
            if (alertEntity.isMonday()) checkBoxMonday.setChecked(true);
            if (alertEntity.isTuesday()) checkBoxTuesday.setChecked(true);
            if (alertEntity.isWednesday()) checkBoxWednesday.setChecked(true);
            if (alertEntity.isThursday()) checkBoxThursday.setChecked(true);
            if (alertEntity.isFriday()) checkBoxFriday.setChecked(true);
            if (alertEntity.isSaturday()) checkBoxSaturday.setChecked(true);
            if (alertEntity.isSunday()) checkBoxSunday.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.save_alert_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_alert_button:
                checkSelectedDays();
                if (selectedDays.size() == 0) {
                    Toast.makeText(this, getString(R.string.select_day_message), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    if (alertEntity != null) {
                        intent.putExtra("alert_id", alertEntity.getId());
                    }
                    intent.putExtra("alert_time", alertTimeMaterialButton.getText());
                    intent.putExtra("alert_note", alertNoteTextInputEditText.getText().toString());
                    intent.putExtra("alert_selected_days", selectedDays);
                    setResult(RESULT_OK, intent );
                    finish();
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToolbar() {
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle(null);
        toolbar.setDisplayHomeAsUpEnabled(true);
    }

    public void setOnAlertTimeButtonPressed() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        alertTimeMaterialButton.setText(getFormatedTime(hour,minute));

        alertTimeMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog picker = new TimePickerDialog(AlertActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                alertTimeMaterialButton.setText(getFormatedTime(hour,minute));
                            }
                        },
                        hour, minute, true);
                picker.show();
            }
        });
    }

    public String getFormatedTime(int hour, int minute) {
        String formattedHour = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);
        String formattedMinute = minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute);
        return formattedHour + ":" + formattedMinute;
    }

    public void checkSelectedDays() {
        if (checkBoxMonday.isChecked()) selectedDays.add(1);
        if (checkBoxTuesday.isChecked()) selectedDays.add(2);
        if (checkBoxWednesday.isChecked()) selectedDays.add(3);
        if (checkBoxThursday.isChecked()) selectedDays.add(4);
        if (checkBoxFriday.isChecked()) selectedDays.add(5);
        if (checkBoxSaturday.isChecked()) selectedDays.add(6);
        if (checkBoxSunday.isChecked()) selectedDays.add(7);
    }
}