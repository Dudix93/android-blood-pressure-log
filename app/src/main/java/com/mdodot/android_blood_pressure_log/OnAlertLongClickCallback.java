package com.mdodot.android_blood_pressure_log;

import com.mdodot.android_blood_pressure_log.entity.AlertEntity;

public interface OnAlertLongClickCallback {
    public void openAlertEditDeleteDialog(AlertEntity alertEntity);
    public void dismissAlertEditDeleteMenu();
}