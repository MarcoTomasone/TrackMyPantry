package com.example.trackmypantry.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackmypantry.DataBase.Product;
import com.example.trackmypantry.R;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {
    private Context context;
    private List<Product> productList;
    private HandleProductClick clickListener;

    public ProductListAdapter(Context context, HandleProductClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setProductList(List<Product> productList){
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override //Set data to our TextView
    public void onBindViewHolder(@NonNull ProductListAdapter.MyViewHolder holder, int position) {
        holder.tvProductName.setText(this.productList.get(position).productName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.itemClick(productList.get(position));
            }
        });

        holder.editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.editClick(productList.get(position));
            }
        });

        holder.deleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.deleteClick(productList.get(position));
            }
        });
        if(this.productList.get(position).isEmpty)
            holder.tvProductName.setPaintFlags(holder.tvProductName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            holder.tvProductName.setPaintFlags(0);
    }

    @Override
    public int getItemCount() {
        if (productList == null || productList.size() == 0)
            return 0;
        else
            return  productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName;
        ImageView deleteCategory;
        ImageView editCategory;

        public MyViewHolder(View view){
            super(view);
            tvProductName = view.findViewById(R.id.textViewCategoryName);
            deleteCategory = view.findViewById(R.id.image_delete_category);
            editCategory = view.findViewById(R.id.image_edit_category);
        }
    }

    public interface HandleProductClick {
        void itemClick(Product product);
        void deleteClick(Product product);
        void editClick(Product product);
    }
}
