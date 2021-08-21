package com.example.trackmypantry.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackmypantry.DataType.Product;
import com.example.trackmypantry.R;

import java.text.BreakIterator;
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
        View view = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override //Set data to our TextView
    public void onBindViewHolder(@NonNull ProductListAdapter.MyViewHolder holder, int position) {
        holder.tvProductName.setText(this.productList.get(position).getName());
        holder.tvProductDescription.setText(this.productList.get(position).getDescription());
        holder.quantity.setText(String.valueOf(this.productList.get(position).getQuantity()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.itemClick(productList.get(position));
            }
        });

        /*holder.editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.editClick(productList.get(position));
            }
        });*/

        holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.deleteClick(productList.get(position));
            }
        });

        holder.addQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.addQuantityClick(productList.get(position));
            }
        });

        holder.removeQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.removeQuantityClick(productList.get(position));
            }
        });
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
        TextView tvProductDescription;
        TextView quantity;
        ImageView deleteProduct;
        Button addQuantity;
        Button removeQuantity;

        public MyViewHolder(View view){
            super(view);
            tvProductName = view.findViewById(R.id.textViewProductName);
            tvProductDescription = view.findViewById(R.id.textViewProductDescription);
            deleteProduct = view.findViewById(R.id.deleteProduct);
            addQuantity = view.findViewById(R.id.add_quantity);
            removeQuantity = view.findViewById(R.id.remove_quantity);
            quantity = view.findViewById(R.id.quantity_number);
        }
    }

    public interface HandleProductClick {
        void itemClick(Product product);
        void deleteClick(Product product);
        void editClick(Product product);
        void addQuantityClick(Product product);
        void removeQuantityClick(Product product);
    }
}