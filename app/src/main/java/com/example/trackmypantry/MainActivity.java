package com.example.trackmypantry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trackmypantry.DataBase.Category;
import com.example.trackmypantry.ViewModel.MainActivityViewModel;

import org.w3c.dom.CDATASection;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public MainActivityViewModel viewModel;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        getSupportActionBar().setTitle("Track My Pantry");
        //Setting the onClick to the addButton
        ImageView addCategoryButton = findViewById(R.id.addNewCategoryImageView);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog();
            }
        });
        initViewModel();
        initRecyclerView();
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter()
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.getListOfCategoriesObserver().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if(categories == null)
                    findViewById(R.id.categoryTextView).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.categoryTextView).setVisibility(View.GONE);
            }
        });
    }

    private void showAddCategoryDialog(){
        AlertDialog newCategory = new AlertDialog.Builder(this).create();
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
                    Toast.makeText(MainActivity.this, "Enter category name", Toast.LENGTH_SHORT).show();
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
    }
}