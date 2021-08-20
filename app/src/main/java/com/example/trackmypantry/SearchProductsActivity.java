package com.example.trackmypantry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackmypantry.Adapter.CategoryListAdapter;
import com.example.trackmypantry.Adapter.SearchProductsAdapter;
import com.example.trackmypantry.DataType.Category;
import com.example.trackmypantry.DataType.CreateProductSchema;
import com.example.trackmypantry.DataType.CreateVoteSchema;
import com.example.trackmypantry.DataType.GetProductSchema;
import com.example.trackmypantry.DataType.Product;
import com.example.trackmypantry.ViewModel.ProductListActivityViewModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class SearchProductsActivity extends AppCompatActivity implements SearchProductsAdapter.HandleProductClick {
    private ProductListActivityViewModel viewModel;
    private RecyclerView recyclerView;
    private SearchProductsAdapter searchProductsAdapter;
    private Button insertNewProductButton;
    private String barcode;
    private SharedPreferences pref;
    private View dialogView;
    private AlertDialog dialogBuilder;
    private Category currentCategory;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        currentCategory = new Category();
        currentCategory.categoryId = getIntent().getIntExtra("category_id", 0 );
        currentCategory.categoryName = getIntent().getStringExtra("category_name");

        getSupportActionBar().setTitle(currentCategory.categoryName);

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
        viewModel = new ViewModelProvider(this).get(ProductListActivityViewModel.class);
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
                viewModel.createNewProduct(newProduct, currentCategory.categoryId);
                dialogBuilder.dismiss();
                Intent intent = new Intent(SearchProductsActivity.this, ProductListActivity.class);
                intent.putExtra("category_id", currentCategory.categoryId);
                intent.putExtra("category_name", currentCategory.categoryName);
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
        ImageView camera = dialogView.findViewById(R.id.camera);
        TextView searchButton = dialogView.findViewById(R.id.search_button);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                IntentIntegrator intentIntegrator = new IntentIntegrator(SearchProductsActivity.this);
                intentIntegrator.setCaptureActivity(ScanActivity.class);
                intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("For Flash use volume up Key");
                intentIntegrator.initiateScan();
            }
        });


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
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = (int) ratingBar.getRating();
                viewModel.rateProduct(new CreateVoteSchema(pref.getString("SESSION_TOKEN", null), rating, product.getId()));
                dialogBuilder.dismiss();
                viewModel.insertProduct(product);
                Intent intent = new Intent(SearchProductsActivity.this, ProductListActivity.class);
                intent.putExtra("category_id", currentCategory.categoryId);
                intent.putExtra("category_name", currentCategory.categoryName);
                startActivity(intent);
                finish();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void itemClick(Product product) {
        product.setCategoryId(currentCategory.categoryId);
        showRatingDialog(product);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialogBuilder.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String ciao = intentResult.getContents();

        if(intentResult.getContents() != null) {
            viewModel.getProductByBarcode(intentResult.getContents());
        }
        else {
            Toast.makeText(SearchProductsActivity.this, "You did not scan anything", Toast.LENGTH_SHORT).show();

        }

    }
}