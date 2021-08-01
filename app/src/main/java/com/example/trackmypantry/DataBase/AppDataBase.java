package com.example.trackmypantry.DataBase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.Locale;

@Database(entities = {Category.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract PantryDao pantryDao();

    public static AppDataBase INSTANCE;

    public static AppDataBase getDBinstance(Context context){
        if(INSTANCE == null){
            //create the database
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "ProductDatabase")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;

    }
}
