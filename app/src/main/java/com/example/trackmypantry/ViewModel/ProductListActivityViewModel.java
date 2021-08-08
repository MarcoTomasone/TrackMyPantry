package com.example.trackmypantry.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.trackmypantry.DataBase.AppDataBase;
import com.example.trackmypantry.DataType.Product;

import java.util.List;

public class ProductListActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Product>> listOfProducts;
    private AppDataBase appDataBase;

    public ProductListActivityViewModel(Application application){
        super(application);
        listOfProducts = new MutableLiveData<>();
        appDataBase = AppDataBase.getDataBaseInstance(getApplication().getApplicationContext());
    }
    //Observer for the liveData, so for the categories you have
    public MutableLiveData<List<Product>> getProductListObserver(){
        return listOfProducts;
    }

    //Function to get all the categories in the DataBase and post them into the liveData
    public void getAllItemsList(int categoryID){
        List<Product> productsList = appDataBase.pantryDao().getAllItemsList(categoryID);
        if(productsList.size() > 0)
            listOfProducts.postValue(productsList);
        else
            listOfProducts.postValue(null);
    }
    public void insertProduct(Product product){
        appDataBase.pantryDao().insertItems(product);
        getAllItemsList(product.categoryId); //update instantly
    }

    public void updateProduct(Product product){
        appDataBase.pantryDao().updateItems(product);
        getAllItemsList(product.categoryId); //update instantly
    }

    public void deleteProduct(Product product){
        appDataBase.pantryDao().deleteItem(product);
        getAllItemsList(product.categoryId); //update instantly
    }
}
