package com.example.trackmypantry.ViewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.trackmypantry.DataBase.AppDataBase;
import com.example.trackmypantry.DataBase.PantryDao;
import com.example.trackmypantry.DataType.CreateProductSchema;
import com.example.trackmypantry.DataType.CreateVoteSchema;
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
    private Context context;
    private PantryDao pantryDao;
    private MutableLiveData<List<Product>> listOfProducts;
    private AppDataBase appDataBase;
    private MutableLiveData<GetProductSchema> searchResponse;
    private SharedPreferences pref;

    public ProductListRepository (Application application, Context context) {
        this.context = context;
        appDataBase = AppDataBase.getDataBaseInstance(application);
        pantryDao = appDataBase.pantryDao();
        listOfProducts = new MutableLiveData<>();
        searchResponse = new MutableLiveData<>();
        pref = application.getApplicationContext().getSharedPreferences("MY_PREFERENCES", MODE_PRIVATE);
    }

    public MutableLiveData<GetProductSchema> getSearchResults() {
        return searchResponse;
    }
    public MutableLiveData<List<Product>> getListOfProducts() {return listOfProducts;}

    //Function to get all the categories in the DataBase and post them into the liveData to populate RecyclerView on Load
    public void getAllProductsList(int categoryId){
        List<Product> productsList = appDataBase.pantryDao().getAllProductsList(categoryId, pref.getString("EMAIL", null));
        if(productsList.size() > 0)
            listOfProducts.postValue(productsList);
        else
            listOfProducts.postValue(null);

    }

    //Function to get all products with a certain barcode
    void getProductsByBarcode(String barcode) {
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<GetProductSchema> call = apiService.getProductByBarcode(barcode, "Bearer " + pref.getString("ACCESS_TOKEN",""));
        call.enqueue(new Callback<GetProductSchema>() {
            @Override
            public void onResponse(Call<GetProductSchema> call, Response<GetProductSchema> response) {
                if (response.isSuccessful()) {
                    searchResponse.postValue(new GetProductSchema(response.body().getProducts(),response.body().getToken()));
                    SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("SESSION_TOKEN", response.body().getToken());
                    editor.commit();
                } else {
                    Log.e("ERROR", "Error try again");
                }
            }
            @Override
            public void onFailure(Call<GetProductSchema> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });

    }
    public void createNewProduct(CreateProductSchema productSchema, int categoryId) {
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<Product> call = apiService.insertNewProduct(productSchema, "Bearer " + pref.getString("ACCESS_TOKEN",""));
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Product product = (Product) response.body();
                product.setCategoryId(categoryId);
                product.setUserEmail(pref.getString("EMAIL", null));
                insertProduct(product);
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void rateProduct(CreateVoteSchema voteSchema) {
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<Void> call = apiService.insertVote(voteSchema, "Bearer " + pref.getString("ACCESS_TOKEN",""));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                    Log.i("Retrofit", "Tutto ok");
                else
                    Log.i("Retrofit", "NON E OK");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("RETROFIT", "OK IL CAZZO ");
            }
        });
    }
    //TODO: Check if the product exist already else add quantity
    void insertProduct(Product product){
        product.setUserEmail(pref.getString("EMAIL", null));
        appDataBase.pantryDao().insertProduct(product);
        getAllProductsList(product.getCategoryId());
       // getAllItemsList(product.getCategoryId()); //update instantly
    }

    public void updateProduct(Product product){
        appDataBase.pantryDao().updateProduct(product);
        getAllProductsList(product.getCategoryId());
        //getAllItemsList(product.getCategoryId()); //update instantly
    }

    public void deleteProduct(Product product){
        appDataBase.pantryDao().deleteProduct(product);
        getAllProductsList(product.getCategoryId());
        //getAllItemsList(product.getCategoryId()); //update instantly
    }
}



