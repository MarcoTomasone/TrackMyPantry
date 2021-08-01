package com.example.trackmypantry.DataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {

    @PrimaryKey
    public int barcode;
    @ColumnInfo(name="productName")
    public String productName;
    @ColumnInfo(name="categoryId")
    public int categoryId;
    @ColumnInfo(name="isEmpty")
    public boolean isEmpty;
}
