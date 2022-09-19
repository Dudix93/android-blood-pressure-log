package com.mdodot.android_blood_pressure_log.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mdodot.android_blood_pressure_log.entity.AlertEntity;

import java.util.List;

@Dao
public interface AlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AlertEntity... alerts);

    @Query("UPDATE alerts SET " +
            "time = :time, " +
            "note = :note, " +
            "monday = :monday, " +
            "tuesday = :tuesday, " +
            "wednesday = :wednesday, " +
            "thursday = :thursday, " +
            "friday = :friday, " +
            "saturday = :saturday, " +
            "sunday = :sunday " +
            "WHERE id = :id")
    void update(int id,
                String time,
                  String note,
                  boolean monday,
                  boolean tuesday,
                  boolean wednesday,
                  boolean thursday,
                  boolean friday,
                  boolean saturday,
                  boolean sunday);

    @Delete
    void delete(AlertEntity alert);

    @Query("SELECT * FROM alerts")
    List<AlertEntity> getAll();
}