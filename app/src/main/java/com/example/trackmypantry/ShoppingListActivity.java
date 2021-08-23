package com.example.trackmypantry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.trackmypantry.Adapter.ProductListAdapter;
import com.example.trackmypantry.Adapter.ShoppingListAdapter;
import com.example.trackmypantry.DataType.Product;
import com.example.trackmypantry.ViewModel.ProductListActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class ShoppingListActivity extends AppCompatActivity implements ShoppingListAdapter.HandleEmptyProductClick {
    private ShoppingListAdapter shoppingListAdapter;
    private RecyclerView recyclerView;
    private ProductListActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.shopping_list_menu); //Selects Categories
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout_menu:
                        startActivity(new Intent(ShoppingListActivity.this, LoginActivity.class));
                        finish();
                        return true;
                    case R.id.categories_menu:
                        startActivity(new Intent(ShoppingListActivity.this, CategoryListActivity.class));
                        finish();
                        return true;
                    case R.id.shopping_list_menu:
                        return true;
                }
                return false;
            }
        });
        initViewModel();
        initRecyclerView();

    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ProductListActivityViewModel.class);
        viewModel.getEmptyProductListObserver().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                if(products == null){
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.emptyProductsTextView).setVisibility(View.VISIBLE);
                }
                else {
                    shoppingListAdapter.setProductList(products);
                    findViewById(R.id.emptyProductsTextView).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.getAllEmptyProducts();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewShoppingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shoppingListAdapter = new ShoppingListAdapter(this, this);
        recyclerView.setAdapter(shoppingListAdapter);
    }

    @Override
    public void deleteClick(Product product) {
        viewModel.deleteProduct(product);
    }
}