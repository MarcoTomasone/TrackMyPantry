package com.example.trackmypantry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackmypantry.Adapter.CategoryListAdapter;
import com.example.trackmypantry.Adapter.SearchProductsAdapter;
import com.example.trackmypantry.DataType.CreateProductSchema;
import com.example.trackmypantry.DataType.CreateVoteSchema;
import com.example.trackmypantry.DataType.GetProductSchema;
import com.example.trackmypantry.DataType.Product;
import com.example.trackmypantry.ViewModel.ProductListActivityViewModel;

public class SearchProductsActivity extends AppCompatActivity implements SearchProductsAdapter.HandleProductClick {
    private ProductListActivityViewModel viewModel;
    private RecyclerView recyclerView;
    private SearchProductsAdapter searchProductsAdapter;
    private Button insertNewProductButton;
    private String barcode;
    private SharedPreferences pref;
    private View dialogView;
    private AlertDialog dialogBuilder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        pref = getApplication().getApplicationContext().getSharedPreferences("MY_PREFERENCES", MODE_PRIVATE);
        insertNewProductButton = findViewById(R.id.buttonInsertNewProduct);
        insertNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInsertNewProductDialog();
            }
        });

        initViewModel();
        initRecyclerView();

        showSearchProductDialog();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView)  findViewById(R.id.recyclerViewSearchProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchProductsAdapter = new SearchProductsAdapter(this, this);
        recyclerView.setAdapter(searchProductsAdapter);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ProductListActivityViewModel.class);
        viewModel.getSearchObserver().observe(this, new Observer<GetProductSchema>() {
            @Override
            public void onChanged(GetProductSchema getProductSchema) {
                if(getProductSchema == null || getProductSchema.getProducts().size() == 0 ){
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.searchProductTextView).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.searchProductTextView).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    searchProductsAdapter.setProductList(getProductSchema.getProducts());
                }
            }
        });
    }

    private void showInsertNewProductDialog() {
        dialogBuilder = new AlertDialog.Builder(this).create();
        dialogView = getLayoutInflater().inflate(R.layout.add_product_dialog, null);

        EditText enterName = dialogView.findViewById(R.id.enterNameInput);
        EditText enterDescription = dialogView.findViewById(R.id.enterDescriptionInput);
        TextView searchButton = dialogView.findViewById(R.id.insertButton);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButtonAdd);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enterName.getText().toString();
                String description = enterDescription.getText().toString();

                if(TextUtils.isEmpty(name) ) {
                    Toast.makeText(SearchProductsActivity.this, "Enter Product Name! ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(description) ) {
                    Toast.makeText(SearchProductsActivity.this, "Enter Product Description! ", Toast.LENGTH_SHORT).show();
                    return;
                }
                CreateProductSchema newProduct = new CreateProductSchema(pref.getString("SESSION_TOKEN", null), name, description, barcode, false);
                viewModel.createNewProduct(newProduct);
                dialogBuilder.dismiss();
                Intent intent = new Intent(SearchProductsActivity.this, ProductListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }
    private void showSearchProductDialog() {
        dialogBuilder = new AlertDialog.Builder(this).create();
        dialogView = getLayoutInflater().inflate(R.layout.search_product_dialog, null);
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
                barcode = enterCategoryInput.getText().toString();
                if(TextUtils.isEmpty(barcode)) {
                    Toast.makeText(SearchProductsActivity.this, "Enter Product Barcode! ", Toast.LENGTH_SHORT).show();
                    return;
                }
                    viewModel.getProductByBarcode(barcode);
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void showRatingDialog(Product product) {
        dialogBuilder = new AlertDialog.Builder(this).create();
        dialogView = getLayoutInflater().inflate(R.layout.dialog_rating, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        TextView rateButton = dialogView.findViewById(R.id.rate_button);
        Log.i("Ho", "HO SCHIACCIATO DA SOLOOO 1");
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = ratingBar.getNumStars();
                viewModel.rateProduct(new CreateVoteSchema(pref.getString("SESSION_TOKEN", null), rating, product.getId()));
                Log.i("Ho", "HO SCHIACCIATO DA SOLOOO 2");
                dialogBuilder.dismiss();
                viewModel.insertProduct(product);
                Intent intent = new Intent(SearchProductsActivity.this, ProductListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void itemClick(Product product) {
        showRatingDialog(product);
    }


    @Override
    public void deleteClick(Product product) {

    }

    @Override
    public void editClick(Product product) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialogBuilder.dismiss();
    }
}