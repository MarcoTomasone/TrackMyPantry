package com.example.trackmypantry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackmypantry.Adapter.ProductListAdapter;
import com.example.trackmypantry.DataType.Category;
import com.example.trackmypantry.DataType.GetProductSchema;
import com.example.trackmypantry.DataType.Product;
import com.example.trackmypantry.ViewModel.ProductListActivityViewModel;

import java.io.Serializable;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements ProductListAdapter.HandleProductClick {
    private Category currentCategory;
    private ProductListAdapter productListAdapter;
    private ProductListActivityViewModel viewModel;
    private RecyclerView recyclerView;
    private Product productToUpdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        currentCategory = new Category();
        currentCategory.categoryId = getIntent().getIntExtra("category_id", 0 );
        currentCategory.categoryName = getIntent().getStringExtra("category_name");
        getSupportActionBar().setTitle(currentCategory.categoryName);
        ImageView searchButton = findViewById(R.id.searchForProductImageView);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, SearchProductsActivity.class);
                intent.putExtra("category_id", currentCategory.categoryId);
                intent.putExtra("category_name", currentCategory.categoryName);
                startActivity(intent);
                finish();
            }
        });
        initViewModel();
        initRecyclerView();
        //viewModel.getAllItemsList(currentCategory.categoryId);
    }



    public void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclerViewProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productListAdapter = new ProductListAdapter(this, this);
        recyclerView.setAdapter(productListAdapter);
    }

    public void initViewModel(){
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ProductListActivityViewModel.class);
        viewModel.getProductListObserver().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                if(products == null){
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.productsTextView).setVisibility(View.VISIBLE);
                }
                else {
                    productListAdapter.setProductList(products);
                    findViewById(R.id.productsTextView).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.getAllProductsForCategory(currentCategory.categoryId);
    }

    @Override
    public void itemClick(Product product) {
   /*     if(product.isEmpty)
            product.isEmpty = false;
        else
            product.isEmpty = true;
        viewModel.updateProduct(product);*/
    }

    @Override
    public void deleteClick(Product product) {
        viewModel.deleteProduct(product);
    }

    @Override
    public void editClick(Product product) {
        this.productToUpdate = product;
       // ((EditText) findViewById(R.id.addNewProduct)).setText("");
        viewModel.updateProduct(product);
    }
    private void updateProduct(String productName) {
        productToUpdate.setName(productName);
        viewModel.updateProduct(productToUpdate);
       // ((EditText) findViewById(R.id.addNewProduct)).setText("");
        productToUpdate = null;
    }

    @Override
    public void addQuantityClick(Product product) {
        product.setQuantity(product.getQuantity() + 1);
        viewModel.updateProduct(product);
    }

    @Override
    public void removeQuantityClick(Product product) {
        product.setQuantity(product.getQuantity() - 1);
        viewModel.updateProduct(product);
    }
}
