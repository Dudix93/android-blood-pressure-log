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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.activity.NoteActivity;
import com.mdodot.android_blood_pressure_log.database.RoomDB;
import com.mdodot.android_blood_pressure_log.databinding.NewEntryFragmentBinding;
import com.mdodot.android_blood_pressure_log.model.PageViewModel;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewEntryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private NewEntryFragmentBinding binding;
    private TextInputEditText dateTextInputEditText;
    private TextInputEditText timeTextInputEditText;
    private TextInputEditText systolicTextInputEditText;
    private TextInputEditText diastolicTextInputEditText;
    private TextInputEditText pulseTextInputEditText;
    private TextInputEditText noteTextInputEditText;
    private MaterialButton saveMaterialButton;
    private Integer systolic;
    private Integer diastolic;
    private Integer pulse;
    private String date;
    private String time;
    private String note;
    private RoomDB roomDB;
    private static OnMeasurementAddedListener onMeasurementAddedListener;

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
        systolicTextInputEditText = (TextInputEditText) getView().findViewById(R.id.systolicTextInputEditText);
        diastolicTextInputEditText = (TextInputEditText) getView().findViewById(R.id.diastolicTextInputEditText);
        pulseTextInputEditText = (TextInputEditText) getView().findViewById(R.id.pulseTextInputEditText);
        noteTextInputEditText = (TextInputEditText) getView().findViewById(R.id.noteTextInputEditText);
        saveMaterialButton = (MaterialButton) getView().findViewById(R.id.saveMaterialButton);

        setOnSelectTimePressed();
        setOnSelectDatePressed();
        setOnComposeNotePressed();
        setOnSaveButtonPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            noteTextInputEditText.setText(data.getStringExtra("note_details"));
        }
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
                startActivityForResult(intent, 1);
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
                !systolicTextInputEditText.getText().toString().matches("")
                        && !diastolicTextInputEditText.getText().toString().matches("")
                        && !pulseTextInputEditText.getText().toString().matches("")
        ) {
            systolic = Integer.valueOf(systolicTextInputEditText.getText().toString());
            diastolic = Integer.valueOf(diastolicTextInputEditText.getText().toString());
            pulse = Integer.valueOf(pulseTextInputEditText.getText().toString());
            note = noteTextInputEditText.getText().toString();
            MeasurementEntity measurementEntity = new MeasurementEntity(systolic, diastolic, pulse, date, time, note);
            roomDB.measurementDao().insert(measurementEntity);

            onMeasurementAddedListener.onNewMeasurementInsertedListener(measurementEntity);

            showToast(getString(R.string.measurement_saved));
        }
        else {
            showToast(getString(R.string.fill_all_data));
        }
    }

    public interface OnMeasurementAddedListener {
        public void onNewMeasurementInsertedListener(MeasurementEntity measurementEntity);
    }

    public void registerListener(OnMeasurementAddedListener listener)
    {
        onMeasurementAddedListener = listener;
    }

    public String getFormatedDate(int day, int month, int year) {
        date = (day + "/" + (month + 1) + "/" + year);
        return date;
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