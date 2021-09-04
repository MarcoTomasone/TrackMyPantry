package com.example.trackmypantry.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.trackmypantry.Adapter.ProductListAdapter;
import com.example.trackmypantry.DataType.Category;
import com.example.trackmypantry.DataType.Product;
import com.example.trackmypantry.R;
import com.example.trackmypantry.ViewModel.ProductListActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class ProductListActivity extends AppCompatActivity implements ProductListAdapter.HandleProductClick {
    private Category currentCategory;
    private ProductListAdapter productListAdapter;
    private ProductListActivityViewModel viewModel;
    private RecyclerView recyclerView;
    private EditText filterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.logout_menu:
                        startActivity(new Intent(ProductListActivity.this, LoginActivity.class));
                        finish();
                        return true;
                    case R.id.categories_menu:
                        startActivity(new Intent(ProductListActivity.this, CategoryListActivity.class));
                        finish();
                        return true;
                    case R.id.shopping_list_menu:
                        startActivity(new Intent(ProductListActivity.this, ShoppingListActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });

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
        filterText = findViewById(R.id.filterText);
        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterList(s.toString());
            }
        });
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
                    productListAdapter.setAllProductList(products);
                    findViewById(R.id.productsTextView).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.getAllProductsForCategory(currentCategory.categoryId);
    }

    @Override
    public void deleteClick(Product product) {
        viewModel.deleteProduct(product);
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

    public void filterList(String text){
        productListAdapter.filterItems(text);
    }
}
