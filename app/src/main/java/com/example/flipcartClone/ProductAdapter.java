package com.example.flipcartClone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flicpcartClone.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductModel> list;
    private OnItemClickListener mListener;

    public ProductAdapter(Context context, List<List<ProductModel>> rowsOfProducts, OnItemClickListener listener) {
        this.context = context;
        this.list = list;

    }

    public ProductAdapter(Context context, ArrayList<ProductModel> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_rec, null);

        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        // Set the click listener for addToCartButton
        holder.addToCartButton.setOnClickListener(v -> {
            // Hide the original "Add to Cart" button
            holder.addToCartButton.setVisibility(View.GONE);
            holder.quantityLayout.setVisibility(View.VISIBLE);
        });


        ProductModel product = list.get(position);
        holder.tv_title.setText(product.getTitle());
        holder.mrp.setText(product.getMRP());
        holder.textview2_price.setText(product.getTextview2_price());
        holder.description.setText(product.getDescription());
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        String imageUrl;
        Button addToCartButton;
        TextView tv_title;
        TextView mrp;
        TextView textview2_price;
        TextView description;
        LinearLayout quantityLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageUrl = String.valueOf(itemView.findViewById(R.id.picture1));
            tv_title = itemView.findViewById(R.id.product_name1);
            textview2_price = itemView.findViewById(R.id.rate1);
            description = itemView.findViewById(R.id.product_discription1);
            mrp = itemView.findViewById(R.id.MRP1);
            addToCartButton = itemView.findViewById(R.id.addToCartButton1);
            quantityLayout = itemView.findViewById(R.id.elegantLayout1);
        }

    }
}
