package com.mdodot.android_blood_pressure_log.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.activity.NoteActivity;
import com.mdodot.android_blood_pressure_log.database.RoomDB;
import com.mdodot.android_blood_pressure_log.databinding.NewEntryFragmentBinding;
import com.mdodot.android_blood_pressure_log.model.PageViewModel;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;

import java.util.List;

public class NewEntryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private NewEntryFragmentBinding binding;
    private TextInputEditText dateTextInputEditText;
    private TextInputEditText timeTextInputEditText;
    private NumberPicker systolicNumberPicker;
    private NumberPicker diastolicNumberPicker;
    private NumberPicker pulseNumberPicker;
    private TextInputEditText noteTextInputEditText;
    private MaterialButton saveMaterialButton;
    private Integer systolic;
    private Integer diastolic;
    private Integer pulse;
    private String date;
    private String time;
    private String note;
    private RoomDB roomDB;
    private static OnRefreshMeasurementsListListener onRefreshMeasurementsListListener;
    private ActivityResultLauncher<Intent> noteActivityResultLauncher;
    private MeasurementEntity measurementEntity;
    private MaterialToolbar materialToolbar;

    public static NewEntryFragment newInstance(int index) {
        NewEntryFragment fragment = new NewEntryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        this.roomDB = RoomDB.getInstance(getContext());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = NewEntryFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateTextInputEditText = (TextInputEditText) getView().findViewById(R.id.dateTextInputEditText);
        timeTextInputEditText = (TextInputEditText) getView().findViewById(R.id.timeTextInputEditText);
        systolicNumberPicker = (NumberPicker) getView().findViewById(R.id.value_systolic);
        diastolicNumberPicker = (NumberPicker) getView().findViewById(R.id.value_diastolic);
        pulseNumberPicker = (NumberPicker) getView().findViewById(R.id.value_pulse);
        noteTextInputEditText = (TextInputEditText) getView().findViewById(R.id.noteTextInputEditText);
        saveMaterialButton = (MaterialButton) getView().findViewById(R.id.saveMaterialButton);
        materialToolbar = (MaterialToolbar) getView().findViewById(R.id.toolbar);

        registerNoteActivityResultLauncher();
        setOnSystolicValueChanged();
        setOnDiastolicValueChanged();
        setOnPulseValueChanged();
        setOnSelectTimePressed();
        setOnSelectDatePressed();
        setOnComposeNotePressed();
        checkForPassedData();
        setOnSaveButtonPressed();
    }

    public void checkForPassedData() {
        Bundle arguments = this.getArguments();
        if (arguments != null) {
            if (materialToolbar != null) {
                materialToolbar.setTitle(R.string.edit_measurement);
            }
            String measurementJson = arguments.getString("measurement_edit_data");
            measurementEntity = new Gson().fromJson(measurementJson, MeasurementEntity.class);

            systolicNumberPicker.setValue(measurementEntity.getSystolic());
            diastolicNumberPicker.setValue(measurementEntity.getDiastolic());
            pulseNumberPicker.setValue(measurementEntity.getPulse());

            dateTextInputEditText.setText(measurementEntity.getDate());
            timeTextInputEditText.setText(measurementEntity.getTime());
            noteTextInputEditText.setText(measurementEntity.getNote());
        }
    }

    public void setOnSystolicValueChanged() {
        systolicNumberPicker.setMinValue(0);
        systolicNumberPicker.setMaxValue(300);
        systolicNumberPicker.setValue(120);

        systolicNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                systolic = systolicNumberPicker.getValue();
            }
        });
    }

    public void setOnDiastolicValueChanged() {
        diastolicNumberPicker.setMinValue(0);
        diastolicNumberPicker.setMaxValue(300);
        diastolicNumberPicker.setValue(70);

        diastolicNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                diastolic = diastolicNumberPicker.getValue();
            }
        });
    }

    public void setOnPulseValueChanged() {
        pulseNumberPicker.setMinValue(0);
        pulseNumberPicker.setMaxValue(200);
        pulseNumberPicker.setValue(60);

        pulseNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                pulse = pulseNumberPicker.getValue();
            }
        });
    }

    public void registerNoteActivityResultLauncher() {
        noteActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            noteTextInputEditText.setText(data.getStringExtra("note_details"));
                        }
                    }
                });
    }

    private void setOnComposeNotePressed() {
        noteTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NoteActivity.class);
                String note = noteTextInputEditText.getText().toString();
                if (note != "") {
                    intent.putExtra("note", note);
                }
                noteActivityResultLauncher.launch(intent);
            }
        });
    }

    private void setOnSaveButtonPressed() {
        saveMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMeasurement();
            }
        });
    }

    public void setOnSelectDatePressed() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if (day != 0 && month != 0 && year != 0) {
            dateTextInputEditText.setText(getFormatedDate(day, month, year));
        }

        dateTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                if (day != 0 && month != 0 && year != 0) {
                                    dateTextInputEditText.setText(getFormatedDate(day, month, year));
                                }
                            }
                        },
                        year, month, day);
                picker.show();
            }
        });
    }

    public void setOnSelectTimePressed() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        timeTextInputEditText.setText(getFormatedTime(hour,minute));

        timeTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog picker = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                timeTextInputEditText.setText(getFormatedTime(hour,minute));
                            }
                        },
                        hour, minute, true);
                picker.show();
            }
        });
    }

    public void saveMeasurement() {
        if (
                systolicNumberPicker.getValue() != 0
                        && diastolicNumberPicker.getValue() != 0
                        && pulseNumberPicker.getValue() != 0
        ) {
            systolic = systolicNumberPicker.getValue();
            diastolic = diastolicNumberPicker.getValue();
            pulse = pulseNumberPicker.getValue();
            note = noteTextInputEditText.getText().toString();
            date = dateTextInputEditText.getText().toString();
            time = timeTextInputEditText.getText().toString();
            if (measurementEntity != null) {
                if (measurementHasBeenchanged(systolic, diastolic, pulse, date, time, note)) {
                    List<MeasurementEntity> measurementsList = roomDB.measurementDao().getAll();
                    measurementsList.forEach(measurement -> {
                        if (measurement.getId() == measurementEntity.getId()) {
                            roomDB.measurementDao().update(measurementEntity.getId(), systolic, diastolic, pulse, date, time, note);
                            onRefreshMeasurementsListListener.refreshMeasurementsList();
                            Intent intent = new Intent();
                            intent.putExtra("new_measurement_values", new MeasurementEntity(systolic, diastolic, pulse, date, time, note));
                            getActivity().setResult(Activity.RESULT_OK, intent);
                            getActivity().finish();
                        }
                    });
                } else {
                    showToast(getString(R.string.no_changes));
                }
            } else {
                measurementEntity = new MeasurementEntity(systolic, diastolic, pulse, date, time, note);
                roomDB.measurementDao().insert(measurementEntity);
                onRefreshMeasurementsListListener.refreshMeasurementsList();
            }
            showToast(getString(R.string.measurement_saved));
        }
        else {
            showToast(getString(R.string.fill_all_data));
        }
    }

    public boolean measurementHasBeenchanged(Integer systolic, Integer diastolic, Integer pulse, String date, String time, String note) {
        if (measurementEntity.getSystolic() != systolic) {
            return true;
        }
        else if (measurementEntity.getDiastolic() != diastolic) {
            return true;
        }
        else if (measurementEntity.getPulse() != pulse) {
            return true;
        }
        else if (!measurementEntity.getDate().equals(date)) {
            return true;
        }
        else if (!measurementEntity.getTime().equals(time)) {
            return true;
        }
        else if (!measurementEntity.getNote().equals(note)) {
            return true;
        }
        return false;
    }

    public interface OnRefreshMeasurementsListListener {
        public void refreshMeasurementsList();
    }

    public void registerListener(OnRefreshMeasurementsListListener listener)
    {
        onRefreshMeasurementsListListener = listener;
    }

    public String getFormatedDate(int day, int month, int year) {
        month++;
        if (day < 10 && month < 10) return "0" + day + "/0" + month + "/" + year;
        else if (month < 10) return day + "/0" + month + "/" + year;
        else if (day < 10) return "0" + day + "/" + month + "/" + year;
        return day + "/" + month + "/" + year;
    }

    public String getFormatedTime(int hour, int minute) {
        String formattedMinute = minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute);
        time = String.valueOf(hour) + ":" + formattedMinute;
        return time;
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}