package com.example.trackmypantry.ViewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.trackmypantry.DataBase.AppDataBase;
import com.example.trackmypantry.DataType.Product;
import com.example.trackmypantry.Network.APIService;
import com.example.trackmypantry.Network.RetroInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ProductListActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Product>> listOfProducts;
    private AppDataBase appDataBase;
    private APIService apiService;
    private SharedPreferences pref;

    public ProductListActivityViewModel(Application application){
        super(application);
        listOfProducts = new MutableLiveData<>();
        appDataBase = AppDataBase.getDataBaseInstance(getApplication().getApplicationContext());
        apiService = RetroInstance.getRetroClient().create(APIService.class);
        pref = application.getApplicationContext().getSharedPreferences("MY_PREFERENCES", MODE_PRIVATE);
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
    public void getProduct(String barcode) {
        Call<List<Product>> call = apiService.getProductByBarcode(barcode, "Bearer " + pref.getString("ACCESS_TOKEN",""));
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                listOfProducts.postValue(response.body());
                Log.i("mytag", String.valueOf(listOfProducts));
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.i("mytag", "failure amo");
            }
        });
    }
    public void insertProduct(Product product){
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
