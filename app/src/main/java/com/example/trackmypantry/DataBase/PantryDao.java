package com.example.trackmypantry.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.trackmypantry.DataType.Category;
import com.example.trackmypantry.DataType.Product;

import java.util.List;

@Dao
public interface PantryDao {
    @Query("Select * from Category")
    List<Category> getAllCategories();

    @Insert
    void insertCategory(Category...categories);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Query("Select * from Product where categoryId = :catId")
    List<Product> getAllItemsList(int catId);

    @Insert
    void insertItems(Product product);

    @Update
    void updateItems(Product product);

    @Delete
    void deleteItem(Product product);


}
