package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<OrderModel> orderList;

    public OrderHistoryAdapter(Context context, ArrayList<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_items, parent, false);
        return new OrderHistoryAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        OrderModel order = orderList.get(position);
        holder.orderName.setText(order.getProductName());
        holder.orderDescription.setText("Ordered on " + order.getOrderDate());

        // Load and set the product image using a library like Picasso or Glide
        // Replace R.drawable.placeholder with the appropriate resource or URL
        // Example using Picasso (make sure to include the Picasso library in your project):
        Glide.with(context)
                .load(order.getProductImage())
                .into(holder.orderImage);
    }

    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView orderImage;
        TextView orderName;
        TextView orderDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.order_image);
            orderName = itemView.findViewById(R.id.order_name);
            orderDescription = itemView.findViewById(R.id.product_description1);
        }
    }
}
