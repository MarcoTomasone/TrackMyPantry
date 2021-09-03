package com.example.trackmypantry.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackmypantry.DataType.Category;
import com.example.trackmypantry.R;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {
    private Context context;
    private List<Category> categoryList;
    private HandleCategoryClick clickListener;

    public  CategoryListAdapter(Context context, HandleCategoryClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setCategoryList(List<Category> categoryList){
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override //Set data to our TextView
    public void onBindViewHolder(@NonNull CategoryListAdapter.MyViewHolder holder, int position) {
        holder.tvCategoryName.setText(this.categoryList.get(position).categoryName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.itemClick(categoryList.get(position));
            }
        });

        holder.editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.editClick(categoryList.get(position));
            }
        });

        holder.deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.deleteClick(categoryList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (categoryList == null || categoryList.size() == 0)
            return 0;
        else
            return  categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategoryName;
        ImageView deleteCategory;
        ImageView editCategory;

        public MyViewHolder(View view){
            super(view);
            tvCategoryName = view.findViewById(R.id.textViewCategoryName);
            deleteCategory = view.findViewById(R.id.image_delete_category);
            editCategory = view.findViewById(R.id.image_edit_category);
        }
    }

    public interface HandleCategoryClick {
        void itemClick(Category category);
        void deleteClick(Category category);
        void editClick(Category category);
    }
}
