package com.example.trackmypantry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackmypantry.Adapter.CategoryListAdapter;
import com.example.trackmypantry.DataType.Category;
import com.example.trackmypantry.ViewModel.CategoryListViewModel;

import java.util.List;

public class CategoryListActivity extends AppCompatActivity implements CategoryListAdapter.HandleCategoryClick {

    public CategoryListViewModel viewModel;
    public RecyclerView recyclerView;
    public CategoryListAdapter categoryListAdapter;
    public TextView noCategoryTextView;
    public Category categoryForEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        recyclerView = (RecyclerView)  findViewById(R.id.recyclerView);
        noCategoryTextView = (TextView) findViewById(R.id.categoryTextView);
        getSupportActionBar().setTitle("Track My Pantry");

        //Setting the onClick to the addButton
        ImageView addCategoryButton = findViewById(R.id.addNewCategoryImageView);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog(false);
            }
        });
        initViewModel();
        initRecyclerView();

        //CODICE PER LA LOGOUT
        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryListActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryListAdapter = new CategoryListAdapter(this, this);
        recyclerView.setAdapter(categoryListAdapter);
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(CategoryListViewModel.class);
        viewModel.getListOfCategoriesObserver().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if (categories == null){
                    noCategoryTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    categoryListAdapter.setCategoryList(categories);
                    noCategoryTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showAddCategoryDialog(boolean isForEdit){
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.add_category_dialog, null);
        EditText enterCategoryInput = dialogView.findViewById(R.id.enterCategoryInput);
        TextView createButton = dialogView.findViewById(R.id.createButton);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);

       if(isForEdit){
            createButton.setText("Update");
            enterCategoryInput.setText(categoryForEdit.categoryName);
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = enterCategoryInput.getText().toString();
                if(TextUtils.isEmpty(name)) {
                    Toast.makeText(CategoryListActivity.this, "Enter category name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isForEdit){
                    categoryForEdit.categoryName = name;
                    viewModel.updateCategory(categoryForEdit);
                } else {
                    //here we need to call view model.
                    viewModel.insertCategory(name);
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void itemClick(Category category) {
        Intent intent = new Intent(CategoryListActivity.this, ProductListActivity.class);
        intent.putExtra("category_id", category.categoryId);
        intent.putExtra("category_name", category.categoryName);
        startActivity(intent);
    }

    @Override
    public void deleteClick(Category category) {
        viewModel.deleteCategory(category);
    }

    @Override
    public void editClick(Category category) {
        this.categoryForEdit = category;
        showAddCategoryDialog(true);
    }
}
