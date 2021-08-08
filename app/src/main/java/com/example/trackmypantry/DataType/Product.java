package com.example.trackmypantry.DataType;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {

    @PrimaryKey
    private int barcode;
    private String id;
    private String name;
    private String description;
    private int categoryId;
    private String userId;
    private boolean test;
    private String createdAt;
    private String updatedAt;

    public int getBarcode() { return barcode; }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCategoryId() { return categoryId; }
    public String getUserId() { return userId; }
    public boolean getTest() { return test; }
    public String getUpdatedAt() {
        return updatedAt;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}







