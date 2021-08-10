package com.example.trackmypantry.ViewModel;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.trackmypantry.DataBase.AppDataBase;
import com.example.trackmypantry.DataType.Category;
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
    private ProductListRepository  productListRepository;

    public ProductListActivityViewModel (Application application) {
        super(application);
        listOfProducts = new MutableLiveData<>();
        productListRepository = new ProductListRepository(application);
    }
    //Observer for the liveData, so for the categories you have
    public MutableLiveData<List<Product>> getProductListObserver(){
        return listOfProducts;
    }

    public  void getAllItemsList(int categoryId){
        productListRepository.getAllItemsList(categoryId);
    }
    public void getProduct(String barcode) {
      productListRepository.getProductsByBarcode(barcode);
    }

    public void insertProduct(Product product){
        productListRepository.insertProduct(product);
    }

    public void updateProduct(Product product){
        productListRepository.updateProduct(product);
    }

    public void deleteProduct(Product product){
       productListRepository.deleteProduct(product);
    }
}
