package com.mdodot.android_blood_pressure_log.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;

import java.util.List;

@Dao
public interface MeasurentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MeasurementEntity... measurements);

    @Update
    void update(MeasurementEntity... measurement);

    @Delete
    void delete(MeasurementEntity measurement);

    @Query("SELECT * FROM measurements")
    List<MeasurementEntity> getAll();
}