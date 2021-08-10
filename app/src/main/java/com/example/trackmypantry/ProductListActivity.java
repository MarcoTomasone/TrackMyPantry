package com.example.trackmypantry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackmypantry.Adapter.ProductListAdapter;
import com.example.trackmypantry.DataType.Category;
import com.example.trackmypantry.DataType.Product;
import com.example.trackmypantry.ViewModel.ProductListActivityViewModel;

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
                showSearchProductDialog();
            }
        });
        //TODO: if all works eliminate this comments
        /*final EditText addNewProduct = findViewById(R.id.addNewProduct);
        ImageView saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = addNewProduct.getText().toString();
                if (TextUtils.isEmpty(barcode)) {
                    Toast.makeText(ProductListActivity.this, "Insert Product Name", Toast.LENGTH_LONG).show();
                    return;
                }
                if(productToUpdate == null)
                    saveNewProduct(barcode);
                else
                    updateProduct(barcode);
            }
        }); */

        initRecyclerView();
        initViewModel();
        viewModel.getAllItemsList(currentCategory.categoryId);
    }

    private void showSearchProductDialog() {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.search_product_dialog, null);
        EditText enterCategoryInput = dialogView.findViewById(R.id.enterBarcode);
        TextView searchButton = dialogView.findViewById(R.id.search_button);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = enterCategoryInput.getText().toString();
                if(TextUtils.isEmpty(barcode)) {
                    Toast.makeText(ProductListActivity.this, "Enter Product Barcode! ", Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.getProduct(barcode);
                //TODO: chiama il fragment con la recyclerview
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
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
    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclerViewProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productListAdapter = new ProductListAdapter(this, this);
        recyclerView.setAdapter(productListAdapter);
    }

    public void saveNewProduct(String barcode){
/* TODO: if all work eliminate this
        viewModel.getProduct(barcode);
        ((EditText) findViewById(R.id.addNewProduct)).setText("");
*/

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
}
