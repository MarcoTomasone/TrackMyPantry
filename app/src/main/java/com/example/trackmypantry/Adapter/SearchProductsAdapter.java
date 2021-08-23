package com.example.trackmypantry.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.trackmypantry.DataType.Category;
import com.example.trackmypantry.DataType.Product;
import com.example.trackmypantry.R;

import java.util.List;

public class SearchProductsAdapter extends RecyclerView.Adapter<SearchProductsAdapter.MyViewHolder> {
    private Context context;
    private List<Product> productList;
    private HandleProductClick clickListener;

    public  SearchProductsAdapter(Context context, HandleProductClick clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    public void setProductList(List<Product> productList){
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_searched_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override //Set data to our TextView
    public void onBindViewHolder(@NonNull SearchProductsAdapter.MyViewHolder holder, int position) {
        holder.tvProductName.setText(this.productList.get(position).getName());
        holder.tvProductDescription.setText(this.productList.get(position).getDescription());
        if(this.productList.get(position).getImg() != null) {
            if (!this.productList.get(position).getImg().startsWith("data:image/jpeg;base64,"))
                this.productList.get(position).setImg("data:image/jpeg;base64," + this.productList.get(position).getImg());
            Glide.with(context).load(this.productList.get(position).getImg())
                    .apply(RequestOptions.centerCropTransform()).into(holder.searchedProductImage);
        }  else
            Glide.with(context).load(R.drawable.no_image_avaible).into(holder.searchedProductImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.itemClick(productList.get(position));
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
        ImageView searchedProductImage;

        public MyViewHolder(View view){
            super(view);
            tvProductName = view.findViewById(R.id.textViewSearchedProductName);
            tvProductDescription = view.findViewById(R.id.textViewSearchedProductDescription);
            searchedProductImage = view.findViewById(R.id.searched_product_image);
        }
    }

    public interface HandleProductClick {
        void itemClick(Product product);
    }
}
