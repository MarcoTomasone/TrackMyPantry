package com.example.trackmypantry.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.trackmypantry.DataBase.AppDataBase;
import com.example.trackmypantry.DataBase.Category;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Category>> listOfCategories;
    private AppDataBase appDataBase;

    public MainActivityViewModel(Application application){
        super(application);
        listOfCategories = new MutableLiveData<>();
        appDataBase = AppDataBase.getDataBaseInstance(getApplication().getApplicationContext());
        getAllCategories();
    }
    //Observer for the liveData, so for the categories you have
    public MutableLiveData<List<Category>> getListOfCategoriesObserver(){
        return listOfCategories;
    }

    //Function to get all the categories in the DataBase and post them into the liveData
    public void getAllCategories(){
        List<Category> categoriesList = appDataBase.pantryDao().getAllCategories();
        if(categoriesList.size() > 0)
            listOfCategories.postValue(categoriesList);
        else
            listOfCategories.postValue(null);
    }
    public void insertCategory(String categoryName){
        Category category = new Category();
        category.categoryName = categoryName;
        appDataBase.pantryDao().insertCategory(category);
        getAllCategories(); //update instantly
    }
    public void updateCategory(Category category){
        appDataBase.pantryDao().updateCategory(category);
        getAllCategories(); //update instantly
    }
    public void deleteCategory(Category category){
        appDataBase.pantryDao().deleteCategory(category);
        getAllCategories(); //update instantly
    }
}
