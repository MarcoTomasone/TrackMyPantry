package com.example.trackmypantry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trackmypantry.DataBase.Category;

public class ProductListActivity extends AppCompatActivity {
    private Category currentCategory;
    private ProductListAdapter productListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        currentCategory = new Category();
        currentCategory.categoryId = getIntent().getIntExtra("category_id", 0 );
        currentCategory.categoryName = getIntent().getStringExtra("category_name");
        getSupportActionBar().setTitle(currentCategory.categoryName);
        final EditText addNewProduct = findViewById(R.id.addNewProduct);
        ImageView saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = addNewProduct.getText().toString();
                if (TextUtils.isEmpty(productName)) {
                    Toast.makeText(ProductListActivity.this, "Insert Product Name", Toast.LENGTH_LONG).show();
                    return;
                }
                saveNewProduct(productName);
            }
        });
    }
    public void saveNewProduct(String productName){

    }
    public void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter();
    }
}