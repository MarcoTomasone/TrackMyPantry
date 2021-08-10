package com.example.trackmypantry.ViewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.trackmypantry.DataBase.AppDataBase;
import com.example.trackmypantry.DataBase.PantryDao;
import com.example.trackmypantry.DataType.Category;
import com.example.trackmypantry.DataType.GetProductSchema;
import com.example.trackmypantry.DataType.Product;
import com.example.trackmypantry.Network.APIService;
import com.example.trackmypantry.Network.RetroInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ProductListRepository {
    private PantryDao pantryDao;
    private MutableLiveData<List<Product>> listOfProducts;
    private AppDataBase appDataBase;
    SharedPreferences pref;

    public ProductListRepository (Application application) {
        appDataBase = AppDataBase.getDataBaseInstance(application);
        pantryDao = appDataBase.pantryDao();
        listOfProducts = new MutableLiveData<List<Product>>();
        pref = application.getApplicationContext().getSharedPreferences("MY_PREFERENCES", MODE_PRIVATE);
    }


    //Function to get all the categories in the DataBase and post them into the liveData
    public void getAllItemsList(int categoryID){
        List<Product> productsList = appDataBase.pantryDao().getAllItemsList(categoryID);
        if(productsList.size() > 0)
            listOfProducts.postValue(productsList);
        else
            listOfProducts.postValue(null);
    }
    // LiveData queries within Rom are automatically executed in background
    MutableLiveData<List<Product>> getProductsByBarcode(String barcode) {
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<GetProductSchema> call = apiService.getProductByBarcode(barcode, "Bearer " + pref.getString("ACCESS_TOKEN",""));
        call.enqueue(new Callback<GetProductSchema>() {
            @Override
            public void onResponse(Call<GetProductSchema> call, Response<GetProductSchema> response) {
                listOfProducts.postValue(response.body().getProductList());
                String token = response.body().getToken();
                Log.i("RETROFIT", String.valueOf(listOfProducts));
                //TODO: INSERT IN DATABASE
            }
            @Override
            public void onFailure(Call<GetProductSchema> call, Throwable t) {
                Log.e("RETROFIT", "something went wrong... but life goes on");
            }
        });
        return listOfProducts;
    }

    void insertProduct(Product product){
        appDataBase.pantryDao().insertItems(product);
        getAllItemsList(product.getCategoryId()); //update instantly
    }

    public void updateProduct(Product product){
        appDataBase.pantryDao().updateItems(product);
        getAllItemsList(product.getCategoryId()); //update instantly
    }

    public void deleteProduct(Product product){
        appDataBase.pantryDao().deleteItem(product);
        getAllItemsList(product.getCategoryId()); //update instantly
    }
}



