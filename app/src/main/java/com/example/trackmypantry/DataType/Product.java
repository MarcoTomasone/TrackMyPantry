package com.example.trackmypantry.DataType;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {

    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String description;
    private String barcode;
    private String img;
    private String userId;
    private boolean test;
    private String createdAt;
    private String updatedAt;
    private int categoryId;
    private String userEmail;

    public String getBarcode() { return barcode; }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public String getImg() { return img; }

    public int getCategoryId() { return categoryId; }
    public String getUserId() { return userId; }
    public boolean getTest() { return test; }
    public String getUpdatedAt() {
        return updatedAt;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public String getUserEmail() {return userEmail;}
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setImg(String img) {
        this.img = img;
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
    public void setUserEmail(String userEmail) {this.userEmail = userEmail; }
}







