package com.mdodot.android_blood_pressure_log.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "measurements")
public class MeasurementEntity implements Serializable {
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

    @ColumnInfo(name = "note")
    public String note;

    public MeasurementEntity(int systolic,
                             int diastolic,
                             int pulse,
                             String date,
                             String time,
                             String note) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.pulse = pulse;
        this.date = date;
        this.time = time;
        this.note = note;
    }

    public int getId() {
        return id;
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

    public String getNote() { return note; }
}
