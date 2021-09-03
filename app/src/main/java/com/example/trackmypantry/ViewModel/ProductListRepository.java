package com.example.trackmypantry.ViewModel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.trackmypantry.Activities.ProductListActivity;
import com.example.trackmypantry.Activities.SearchProductsActivity;
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
    private MutableLiveData<List<Product>> listOfEmptyProducts;
    private AppDataBase appDataBase;
    private MutableLiveData<GetProductSchema> searchResponse;
    private SharedPreferences pref;

    public ProductListRepository (Application application, Context context) {
        this.context = context;
        appDataBase = AppDataBase.getDataBaseInstance(application);
        pantryDao = appDataBase.pantryDao();
        listOfProducts = new MutableLiveData<>();
        searchResponse = new MutableLiveData<>();
        listOfEmptyProducts =  new MutableLiveData<>();
        pref = application.getApplicationContext().getSharedPreferences("MY_PREFERENCES", MODE_PRIVATE);
    }

    public MutableLiveData<GetProductSchema> getSearchResults() {
        return searchResponse;
    }
    public MutableLiveData<List<Product>> getListOfProducts() {return listOfProducts;}
    public MutableLiveData<List<Product>> getListOfEmptyProducts() {return listOfEmptyProducts;}

    //Function to get all the categories in the DataBase and post them into the liveData to populate RecyclerView on Load
    public void getAllProductsList(int categoryId){
        List<Product> productsList = appDataBase.pantryDao().getAllProductsListForCategory(categoryId, pref.getString("EMAIL", null));
        if(productsList.size() > 0)
            listOfProducts.postValue(productsList);
        else
            listOfProducts.postValue(null);
    }
    public void getAllEmptyProducts() {
        List<Product> productsList = appDataBase.pantryDao().getAllEmptyProducts(pref.getString("EMAIL", null));
        if(productsList.size() > 0)
            listOfEmptyProducts.postValue(productsList);
        else
            listOfEmptyProducts.postValue(null);
    }

    //Function to get all products with a certain barcode
    void getProductsByBarcode(String barcode, SearchProductsActivity searchProductsActivity) {
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
                    Toast.makeText(searchProductsActivity.getApplicationContext(),"Error searching Product!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetProductSchema> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });

    }
    public void createNewProduct(CreateProductSchema productSchema, int categoryId, SearchProductsActivity searchProductsActivity) {
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<Product> call = apiService.insertNewProduct(productSchema, "Bearer " + pref.getString("ACCESS_TOKEN",""));
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = (Product) response.body();
                    product.setCategoryId(categoryId);
                    product.setUserEmail(pref.getString("EMAIL", null));
                    insertProduct(product);
                    Intent intent = new Intent(searchProductsActivity, ProductListActivity.class);
                    intent.putExtra("category_id", searchProductsActivity.getCurrentCategory().categoryId);
                    intent.putExtra("category_name",searchProductsActivity.getCurrentCategory().categoryName);
                    searchProductsActivity.startActivity(intent);
                } else{
                    Toast.makeText(searchProductsActivity.getApplicationContext(),"Error creating Product!", Toast.LENGTH_SHORT).show();
                }
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
                Log.i("RETROFIT", "OK");
            }
        });
    }


    void insertProduct(Product product){
        Product prodExist = appDataBase.pantryDao().isProductIn(product.getCategoryId(), product.getId());
        if( prodExist != null) {
            if(prodExist.getCategoryId() == product.getCategoryId()){
                prodExist.setQuantity(prodExist.getQuantity() + 1);
                updateProduct(prodExist);
            }else {
                prodExist.setQuantity(prodExist.getQuantity() + 1);
                prodExist.setCategoryId(product.getCategoryId());
                updateProduct(prodExist);
            }
        }
        else{
            product.setUserEmail(pref.getString("EMAIL", null));
            product.setQuantity(1); //When you insert a new product default quantity is 1
            appDataBase.pantryDao().insertProduct(product);
            getAllProductsList(product.getCategoryId());
            getAllEmptyProducts();
        }
    }

    public void updateProduct(Product product){
        appDataBase.pantryDao().updateProduct(product);
        getAllProductsList(product.getCategoryId());
        getAllEmptyProducts();
    }

    public void deleteProduct(Product product){
        appDataBase.pantryDao().deleteProduct(product);
        getAllProductsList(product.getCategoryId());
        getAllEmptyProducts();
    }
}



