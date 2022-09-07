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
import androidx.fragment.app.FragmentResultListener;

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
    private TextView timeFromTextView;
    private TextView timeToTextView;
    private String dateFrom;
    private String dateTo;
    private String filterTimeFrom;
    private String filterTimeTo;
    private String filterDateFrom;
    private String filterDateTo;
    private MaterialButton applyFiltersButton;
    private MaterialButton clearFiltersButton;

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
        timeFromTextView = (TextView) view.findViewById(R.id.time_from);
        timeToTextView = (TextView) view.findViewById(R.id.time_to);
        applyFiltersButton = (MaterialButton) view.findViewById(R.id.apply_filters_button);
        clearFiltersButton = (MaterialButton) view.findViewById(R.id.clear_filters_button);

        dateFromDatePicker = new DatePickerDialog(getContext());
        dateToDatePicker = new DatePickerDialog(getContext());

        dateFromDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        dateToDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

        timeRangeSlider.setValues(1.0f, 23.0f);

        setOnTimeRangeChanged();
        setOnFromDatePressed();
        setOnToDatePressed();
        setOnApplyButtonPressed();
        setOnClearButtonPressed();
        restoreFiltersState();

        return view;
    }


    public void restoreFiltersState() {
        if (getArguments() != null) {
            filterDateFrom = getArguments().getString("filter_date_from");
            filterDateTo = getArguments().getString("filter_date_to");
            filterTimeFrom = getArguments().getString("filter_time_from");
            filterTimeTo = getArguments().getString("filter_time_to");

            if (filterTimeTo.equals("23:59")) filterTimeTo = "24:00";

            fromDateTextInputEditText.setText(filterDateFrom);
            toDateTextInputEditText.setText(filterDateTo);
            timeFromTextView.setText(filterTimeFrom);
            timeToTextView.setText(filterTimeTo);
            
            timeRangeSlider.setValues(Float.valueOf(filterTimeFrom.split(":")[0]),
                    Float.valueOf(filterTimeTo.split(":")[0]));
            clearFiltersButton.setEnabled(true);
        } else {
            clearFiltersButton.setEnabled(false);
        }
    }

    public void setOnClearButtonPressed() {
        clearFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("clear_filters", true);
                getParentFragmentManager().setFragmentResult("requestKey", bundle);
                dismiss();
            }
        });
    }

    public void setOnApplyButtonPressed() {
        applyFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("filter_date_from", dateFrom);
                bundle.putString("filter_date_to", dateTo);
                bundle.putString("filter_time_from", timeFromTextView.getText().toString());
                bundle.putString("filter_time_to", timeToTextView.getText().toString());
                getParentFragmentManager().setFragmentResult("requestKey", bundle);
                dismiss();
            }
        });
    }

    public void setOnTimeRangeChanged() {
        int hourFrom = Math.round(timeRangeSlider.getValueFrom());
        int hourTo = Math.round(timeRangeSlider.getValueTo());
        if (hourFrom < 10) {
            timeFromTextView.setText("0" + Integer.toString(Math.round(timeRangeSlider.getValueFrom())) + ":00");
        } else {
            timeFromTextView.setText(Integer.toString(Math.round(timeRangeSlider.getValueFrom())) + ":00");
        }

        if (hourTo < 10) {
            timeToTextView.setText("0" + Integer.toString(Math.round(timeRangeSlider.getValueTo())) + ":00");
        } else {
            timeToTextView.setText(Integer.toString(Math.round(timeRangeSlider.getValueTo())) + ":00");
        }

        timeRangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                int hourFrom = Math.round(slider.getValues().get(0));
                int hourTo = Math.round(slider.getValues().get(1));
                
                if (hourFrom > 1 || hourTo < 24) clearFiltersButton.setEnabled(true);
                
                if (hourFrom < 10) {
                    timeFromTextView.setText("0" + Integer.toString(Math.round(slider.getValues().get(0))) + ":00");
                } else {
                    timeFromTextView.setText(Integer.toString(Math.round(slider.getValues().get(0))) + ":00");
                }

                if (hourTo < 10) {
                    timeToTextView.setText("0" + Integer.toString(Math.round(slider.getValues().get(1))) + ":00");
                } else {
                    timeToTextView.setText(Integer.toString(Math.round(slider.getValues().get(1))) + ":00");
                }
            }
        });
    }

    public void setOnToDatePressed() {
        final Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        if (currentDay != 0 && currentMonth != 0 && currentYear != 0) {
            toDateTextInputEditText.setText(parsedDate(currentDay, currentMonth, currentYear));
            dateTo = parsedDate(currentDay, currentMonth, currentYear);
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

                            if (day != currentDay || month != currentMonth || year != currentYear) clearFiltersButton.setEnabled(true);
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