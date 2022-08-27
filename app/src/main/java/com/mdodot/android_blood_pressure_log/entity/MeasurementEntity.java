package com.mdodot.android_blood_pressure_log.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "measurements")
public class MeasurementEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "systolic")
    public int systolic;

    @ColumnInfo(name = "diastolic")
    public int diastolic;

    @ColumnInfo(name = "pulse")
    public int pulse;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "time")
    public String time;

    public MeasurementEntity(int systolic,
                             int diastolic,
                             int pulse,
                             String date,
                             String time) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.pulse = pulse;
        this.date = date;
        this.time = time;
    }

    public int getSystolic() {
        return systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public int getPulse() {
        return pulse;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
