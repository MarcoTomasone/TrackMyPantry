package com.example.trackmypantry.DataType;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int categoryId;

    @ColumnInfo(name = "categoryName")
    public String categoryName;

}
