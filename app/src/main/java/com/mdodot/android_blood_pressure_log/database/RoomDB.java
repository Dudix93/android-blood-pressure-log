package com.mdodot.android_blood_pressure_log.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mdodot.android_blood_pressure_log.entity.AlertEntity;
import com.mdodot.android_blood_pressure_log.entity.MeasurementEntity;

@Database(entities = {MeasurementEntity.class, AlertEntity.class}, version = 4, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;
    private static final String DATABASE_NAME = "MeasurementsAndAlerts";

    public synchronized static RoomDB getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract MeasurentDao measurementDao();

    public abstract AlertDao alertDao();
}
