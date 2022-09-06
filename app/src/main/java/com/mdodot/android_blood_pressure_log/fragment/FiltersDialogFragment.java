package com.mdodot.android_blood_pressure_log.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
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

    private TextInputEditText fromDateTextInputEditText;
    private TextInputEditText toDateTextInputEditText;
    private DatePickerDialog dateFromDatePicker;
    private DatePickerDialog dateToDatePicker;
    private RangeSlider timeRangeSlider;
    private TextView timeFrom;
    private TextView timeTo;
    private String dateFrom;
    private String dateTo;
    private MaterialButton applyFiltersButton;

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
        applyFiltersButton = (MaterialButton) view.findViewById(R.id.apply_filters_button);

        dateFromDatePicker = new DatePickerDialog(getContext());
        dateToDatePicker = new DatePickerDialog(getContext());

        dateFromDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        dateToDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

        timeRangeSlider.setValues(1.0f, 23.0f);

        setOnTimeRangeChanged();
        setOnFromDatePressed();
        setOnToDatePressed();
        setOnApplyButtonPressed();

        return view;
    }

    public void setOnApplyButtonPressed() {
        applyFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("filter_date_from", dateFrom);
                bundle.putString("filter_date_to", dateTo);
                bundle.putString("filter_time_from", timeFrom.getText().toString());
                bundle.putString("filter_time_to", timeTo.getText().toString());
                getParentFragmentManager().setFragmentResult("requestKey", bundle);
                dismiss();
            }
        });
    }

    public void setOnTimeRangeChanged() {
        int hourFrom = Math.round(timeRangeSlider.getValueFrom());
        int hourTo = Math.round(timeRangeSlider.getValueTo());
        if (hourFrom < 10) {
            timeFrom.setText("0" + Integer.toString(Math.round(timeRangeSlider.getValueFrom())) + ":00");
        } else {
            timeFrom.setText(Integer.toString(Math.round(timeRangeSlider.getValueFrom())) + ":00");
        }

        if (hourTo < 10) {
            timeTo.setText("0" + Integer.toString(Math.round(timeRangeSlider.getValueTo())) + ":00");
        } else {
            timeTo.setText(Integer.toString(Math.round(timeRangeSlider.getValueTo())) + ":00");
        }

        timeRangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                int hourFrom = Math.round(slider.getValues().get(0));
                int hourTo = Math.round(slider.getValues().get(1));
                if (hourFrom < 10) {
                    timeFrom.setText("0" + Integer.toString(Math.round(slider.getValues().get(0))) + ":00");
                } else {
                    timeFrom.setText(Integer.toString(Math.round(slider.getValues().get(0))) + ":00");
                }

                if (hourTo < 10) {
                    timeTo.setText("0" + Integer.toString(Math.round(slider.getValues().get(1))) + ":00");
                } else {
                    timeTo.setText(Integer.toString(Math.round(slider.getValues().get(1))) + ":00");
                }
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
            dateTo = parsedDate(day, month, year);
        }

        toDateTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateToDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        if (day != 0 && month != 0 && year != 0) {
                            dateTo = parsedDate(day, month, year);
                            LocalDateTime localDateTime = LocalDate.parse(dateTo, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                            long millis = localDateTime
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()
                                    .toEpochMilli();
                            toDateTextInputEditText.setText(dateTo);
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
                            dateFrom = parsedDate(day, month, year);
                            LocalDateTime localDateTime = LocalDate.parse(dateFrom, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
                            long millis = localDateTime
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()
                                    .toEpochMilli();
                            fromDateTextInputEditText.setText(dateFrom);
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