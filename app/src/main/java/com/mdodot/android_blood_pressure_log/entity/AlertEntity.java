package com.mdodot.android_blood_pressure_log.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "alerts")
public class AlertEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "monday")
    public boolean monday;

    @ColumnInfo(name = "tuesday")
    public boolean tuesday;

    @ColumnInfo(name = "wednesday")
    public boolean wednesday;

    @ColumnInfo(name = "thursday")
    public boolean thursday;

    @ColumnInfo(name = "friday")
    public boolean friday;

    @ColumnInfo(name = "saturday")
    public boolean saturday;

    @ColumnInfo(name = "sunday")
    public boolean sunday;

    public AlertEntity(String time,
                       String note,
                       boolean monday,
                       boolean tuesday,
                       boolean wednesday,
                       boolean thursday,
                       boolean friday,
                       boolean saturday,
                       boolean sunday) {
        this.time = time;
        this.note = note;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public int getId() {return id;}

    public String getTime() {return time;}

    public String getNote() {return note;}

    public boolean isMonday() {return monday;}

    public boolean isTuesday() {return tuesday;}

    public boolean isWednesday() {return wednesday;}

    public boolean isThursday() {return thursday;}

    public boolean isFriday() {return friday;}

    public boolean isSaturday() {return saturday;}

    public boolean isSunday() {return sunday;}
}
