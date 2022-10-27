package com.mdodot.android_blood_pressure_log.fragment;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.mdodot.android_blood_pressure_log.R;
import com.mdodot.android_blood_pressure_log.SaveExcel;
import com.mdodot.android_blood_pressure_log.SavePDF;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import jxl.write.WritableCellFormat;
import jxl.write.WriteException;

public class NightModeDialogFragment extends DialogFragment {

    private Context mContext;
    private RadioGroup nightModeRadioGroup;

    public static NightModeDialogFragment newInstance() {
        NightModeDialogFragment fragment = new NightModeDialogFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.night_mode_dialog_fragment, container, false);
        this.nightModeRadioGroup = view.findViewById(R.id.night_mode_radio_group);
        mContext = getContext();
        markSelectedNightMode();
        setOnNightModeRadioGroupChangedListener();
        return view;
    }

    public void markSelectedNightMode() {
        SharedPreferences sharedPreferencesRead = mContext.getSharedPreferences("MySharedPref", 0);
        String nightModeSelection = sharedPreferencesRead.getString("nightMode", "");
        if (!nightModeSelection.isEmpty()) {
            switch (nightModeSelection) {
                case "auto":
                    nightModeRadioGroup.check(R.id.night_mode_button_automatic);
                    break;
                case "off":
                    nightModeRadioGroup.check(R.id.night_mode_button_off);
                    break;
                case "on":
                    nightModeRadioGroup.check(R.id.night_mode_button_on);
                    break;
            }
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        SharedPreferences sharedPreferencesRead = mContext.getSharedPreferences("MySharedPref", 0);
        String nightModeSelection = sharedPreferencesRead.getString("nightMode", "");
        if (!nightModeSelection.isEmpty()) {
            switch (nightModeSelection) {
                case "auto":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
                case "off":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case "on":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
            }
        }
        super.onDismiss(dialog);
    }

    public void setOnNightModeRadioGroupChangedListener() {
        nightModeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                SharedPreferences sharedPreferencesWrite = mContext.getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor sharedPreferencesEdit = sharedPreferencesWrite.edit();
                switch(i) {
                    case R.id.night_mode_button_automatic:
                        sharedPreferencesEdit.putString("nightMode", "auto");
                        sharedPreferencesEdit.commit();
                        break;

                    case R.id.night_mode_button_off:
                        sharedPreferencesEdit.putString("nightMode", "off");
                        sharedPreferencesEdit.commit();
                        break;

                    case R.id.night_mode_button_on:
                        sharedPreferencesEdit.putString("nightMode", "on");
                        sharedPreferencesEdit.commit();
                        break;
                }
            }
        });
    }
}