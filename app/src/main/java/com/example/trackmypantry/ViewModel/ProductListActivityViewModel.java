package com.example.trackmypantry.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.trackmypantry.DataType.CreateProductSchema;
import com.example.trackmypantry.DataType.GetProductSchema;
import com.example.trackmypantry.DataType.Product;

import java.util.List;

public class ProductListActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Product>> listOfProducts;
    private MutableLiveData<GetProductSchema> searchResponse;
    private ProductListRepository  productListRepository;

    public ProductListActivityViewModel (Application application) {
        super(application);
        listOfProducts = new MutableLiveData<>();
        searchResponse = new MutableLiveData<>();
        productListRepository = new ProductListRepository(application, getApplication().getApplicationContext());
        productListRepository.getAllProductsList();
        Log.i("mytag", "AOOOOOOO");
    }
    //Observer for the liveData, so for products you have
    public MutableLiveData<List<Product>> getProductListObserver(){
        return productListRepository.getListOfProducts();
    }

    //Observer for the response liveData
    public MutableLiveData<GetProductSchema> getSearchObserver(){
        return productListRepository.getSearchResults();
    }


    /*public  void getAllItemsList(int categoryId){
        productListRepository.getAllItemsList(categoryId);
    }*/

    //TODO: Rifarla con i catid come sopra
    public  void getAllItemsList(){
        productListRepository.getAllProductsList();
    }

    public void getProductByBarcode(String barcode)  {
        productListRepository.getProductsByBarcode(barcode);
    }
    public void createNewProduct(CreateProductSchema productSchema){
        productListRepository.createNewProduct(productSchema);
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
