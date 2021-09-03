package com.example.trackmypantry.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.trackmypantry.Activities.SearchProductsActivity;
import com.example.trackmypantry.DataType.Category;
import com.example.trackmypantry.DataType.CreateProductSchema;
import com.example.trackmypantry.DataType.CreateVoteSchema;
import com.example.trackmypantry.DataType.GetProductSchema;
import com.example.trackmypantry.DataType.Product;

import java.util.List;

public class ProductListActivityViewModel extends AndroidViewModel {

    private ProductListRepository  productListRepository;

    public ProductListActivityViewModel (Application application) {
        super(application);
        productListRepository = new ProductListRepository(application, getApplication().getApplicationContext());
    }

    //Observer for the liveData, so for products you have
    public MutableLiveData<List<Product>> getProductListObserver(){
        return productListRepository.getListOfProducts();
    }
    //Observer for the liveData, so for products you have with 0 quantity
    public MutableLiveData<List<Product>> getEmptyProductListObserver(){
        return productListRepository.getListOfEmptyProducts();
    }

    //Observer for the response liveData
    public MutableLiveData<GetProductSchema> getSearchObserver(){
        return productListRepository.getSearchResults();
    }

    public void getAllProductsForCategory(int categoryId){
        productListRepository.getAllProductsList(categoryId);
    }

    public void getProductByBarcode(String barcode, SearchProductsActivity searchProductsActivity)  {
        productListRepository.getProductsByBarcode(barcode, searchProductsActivity);
    }

    public void createNewProduct(CreateProductSchema productSchema, int categoryId, SearchProductsActivity searchProductsActivity){
        productListRepository.createNewProduct(productSchema, categoryId, searchProductsActivity);
    }

    public void rateProduct(CreateVoteSchema voteSchema){
        productListRepository.rateProduct(voteSchema);
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

    public void getAllEmptyProducts() { productListRepository.getAllEmptyProducts();}

}

