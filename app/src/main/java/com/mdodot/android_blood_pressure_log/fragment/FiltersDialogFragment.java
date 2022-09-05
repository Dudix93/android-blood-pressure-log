package com.mdodot.android_blood_pressure_log.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;
import com.mdodot.android_blood_pressure_log.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class FiltersDialogFragment extends DialogFragment {

    TextInputEditText fromDateTextInputEditText;
    TextInputEditText toDateTextInputEditText;
    DatePickerDialog dateFromDatePicker;
    DatePickerDialog dateToDatePicker;
    RangeSlider timeRangeSlider;
    TextView timeFrom;
    TextView timeTo;

    public static FiltersDialogFragment newInstance() {
        final FiltersDialogFragment fragment = new FiltersDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filters_dialog_fragment, container, false);

        fromDateTextInputEditText = (TextInputEditText) view.findViewById(R.id.date_from_text_input_edit_text);
        toDateTextInputEditText = (TextInputEditText) view.findViewById(R.id.date_to_text_input_edit_text);
        timeRangeSlider = (RangeSlider) view.findViewById(R.id.time_range_slider);
        timeFrom = (TextView) view.findViewById(R.id.time_from);
        timeTo = (TextView) view.findViewById(R.id.time_to);

        dateFromDatePicker = new DatePickerDialog(getContext());
        dateToDatePicker = new DatePickerDialog(getContext());

        dateFromDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        dateToDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

        timeRangeSlider.setValues(1.0f, 23.0f);

        setOnTimeRangeChanged();
        setOnFromDatePressed();
        setOnToDatePressed();

        return view;
    }

    public void setOnTimeRangeChanged() {
        timeFrom.setText(Integer.toString(Math.round(timeRangeSlider.getValueFrom())) + ":00");
        timeTo.setText(Integer.toString(Math.round(timeRangeSlider.getValueTo())) + ":00");

        timeRangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                timeFrom.setText(Integer.toString(Math.round(slider.getValues().get(0))) + ":00");
                timeTo.setText(Integer.toString(Math.round(slider.getValues().get(1))) + ":00");
            }
        });
    }

    public void setOnToDatePressed() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if (day != 0 && month != 0 && year != 0) {
            toDateTextInputEditText.setText(parsedDate(day, month, year));
        }

        toDateTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateToDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        if (day != 0 && month != 0 && year != 0) {
                            String selectedDate = parsedDate(day, month, year);
                            LocalDateTime localDateTime = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                            long millis = localDateTime
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()
                                    .toEpochMilli();
                            toDateTextInputEditText.setText(selectedDate);
                            dateFromDatePicker.getDatePicker().setMaxDate(millis);
                        }
                    }
                });
                dateToDatePicker.show();
            }
        });
    }

    public void setOnFromDatePressed() {
        fromDateTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFromDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        if (day != 0 && month != 0 && year != 0) {
                            String selectedDate = parsedDate(day, month, year);
                            LocalDateTime localDateTime = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                            long millis = localDateTime
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()
                                    .toEpochMilli();
                            fromDateTextInputEditText.setText(selectedDate);
                            dateToDatePicker.getDatePicker().setMinDate(millis);
                        }
                    }
                });
                dateFromDatePicker.show();
            }
        });
    }

    public String parsedDate(int day, int month, int year) {
        month++;
        if (day < 10 && month < 10) return "0" + day + "/0" + month + "/" + year;
        else if (month < 10) return day + "/0" + month + "/" + year;
        else if (day < 10) return "0" + day + "/" + month + "/" + year;
        return day + "/" + month + "/" + year;
    }
}