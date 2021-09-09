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
    @Query("Select * from Category where userEmail= :email")
    List<Category> getAllCategories(String email);

    @Insert
    void insertCategory(Category...categories);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Query("Select * from Product where categoryId = :catId and userEmail = :email and quantity > 0 ")
    List<Product> getAllProductsListForCategory(int catId, String email);

    @Query("Select * from Product where userEmail = :email and quantity = 0 ")
    List<Product> getAllEmptyProducts(String email);

    @Insert
    void insertProduct(Product product);

    @Update
    void updateProduct(Product product);

    @Delete
    void deleteProduct(Product product);

    @Query("Delete from Product where categoryId = :catId")
    void deleteAllProductsForCategory(int catId);

    @Query("Select * from Product where  id = :prodId")
    Product isProductIn(String prodId);
}
