package com.example.trackmypantry.DataBase;

import android.content.ClipData;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Category.class, Product.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract PantryDao pantryDao();

    public static AppDataBase INSTANCE;

    public static AppDataBase getDataBaseInstance(Context context){
        if(INSTANCE == null){
            //create the database
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "ProductDatabase")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;

    }
}
