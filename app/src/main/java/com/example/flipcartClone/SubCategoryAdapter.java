package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {
    private CartDatabaseHelper dbcartHelp;
    private Context context;
    private ArrayList<SubCategoryModel> productList;
    private OnItemClickListener listener;
    private ProductDatabaseHelper dbProductHelper;

//    public SubCategoryAdapter(Context context, ArrayList<SubCategoryModel> productList, OnItemClickListener listener, CartAdapter cartAdapter, SubCategoryFragment quantityChangeListener, ProductDatabaseHelper dbHelper,CartDatabaseHelper cartDbHelper) {
//        this.context = context;
//        this.productList = productList;
//        this.listener = listener;
//        this.cartAdapter = cartAdapter;
//        this.quantityChangeListener = quantityChangeListener;
//        this.dbHelper = dbProductHelper;
//        this.cartItems = new ArrayList<>();
//        this.dbcartHelp = cartDbHelper; // Initialize cartDbHelper
//    }
    // Member variable to hold the listener
    private OnQuantityChangeListener quantityChangeListener;

    public SubCategoryAdapter(Context context, ArrayList<SubCategoryModel> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
        dbcartHelp = new CartDatabaseHelper(context);
        dbProductHelper = new ProductDatabaseHelper(context);
    }

    // Setter method to set the listener
    public void setQuantityChangeListener(OnQuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final SubCategoryModel item = productList.get(position);
        Log.e("SubCategoryModel_data", item.toString());
        holder.titleTextView.setText(item.getTitle());
        holder.descriptionTextView.setText(item.getDescription());
        holder.rateTextView.setText(item.getRate());
        holder.mrpTextView.setText(item.getMrp());
        Glide.with(context).load(item.getImageUrl()).into(holder.imageView);

        holder.addToCartButton.setOnClickListener(v -> {
            holder.addToCartButton.setVisibility(View.GONE);
            holder.elegantLayout.setVisibility(View.VISIBLE);
            holder.numberButton.setVisibility(View.VISIBLE);
            holder.numberButton.setNumber("1");
            SubCategoryModel selectedItem = productList.get(position);
            Toast.makeText(context, selectedItem.getTitle() + " added to cart", Toast.LENGTH_SHORT).show();


            int initialQuantity = Integer.parseInt(holder.numberButton.getNumber());
            dbcartHelp.insertCartItem(
                    item.getProductId(),
                    item.getTitle(),
                    item.getRate(),
                    item.getImageUrl(),
                    String.valueOf(initialQuantity)
            );
            dbProductHelper.updateProductQuantity(String.valueOf(item.getProductId()), 1);

//            if (selectedItem.getProductId() != null) {
//
//                // Update the cart UI by notifying the cartAdapter
//                CartItemModel cartItem = new CartItemModel(
//                        selectedItem.getProductId(),
//                        selectedItem.getTitle(),
//                        selectedItem.getRate(),
//                        selectedItem.getImageUrl(),
//                        selectedItem.getQuantity(),
//                        selectedItem.getDescription(),
//                        selectedItem.getMrp()
//                );
//
////                cartAdapter.onAddToCart(cartItem);
////                cartAdapter.notifyDataSetChanged();
//            } else {
//                // Handle the case where productId is null (e.g., log an error or show a message)
//            }
        });


        holder.numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                SubCategoryModel selectedItem = productList.get(position);
                String productId = String.valueOf(selectedItem.getProductId());

                Log.d("SubCategoryAdapter", "Product ID: " + productId);
                Log.d("SubCategoryAdapter", "Old Value: " + oldValue);
                Log.d("SubCategoryAdapter", "New Value: " + newValue);
                Log.e("imageurl", item.getImageUrl());

                // Update the product database with the new quantity
                dbProductHelper.updateProductQuantity(productId, newValue);

                if (newValue < 1) {
//                    dbProductHelper.deleteProduct(productId);
                    dbcartHelp.deleteProduct(productId);
                    holder.numberButton.setVisibility(View.GONE);
                    holder.addToCartButton.setVisibility(View.VISIBLE);
                } else {
                    holder.numberButton.setVisibility(View.VISIBLE);
                    holder.numberButton.setNumber(String.valueOf(newValue));
                    holder.addToCartButton.setVisibility(View.GONE);
                    dbcartHelp.updateProductQuantityInCart(productId, newValue);
                }
                selectedItem.setQuantity(newValue);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the clicked item's data
                SubCategoryModel selectedItem = productList.get(position);


                FragmentProductDetails fragmentProductDetails = new FragmentProductDetails();
                Bundle args = new Bundle();
                args.putString("title", selectedItem.getTitle());
                args.putString("description", selectedItem.getDescription());
                args.putString("rate", String.valueOf(selectedItem.getRate()));
                args.putString("mrp", selectedItem.getMrp());
                args.putString("imageUrl", selectedItem.getImageUrl());
                fragmentProductDetails.setArguments(args);
                // Replace the current fragment with FragmentProductDetails
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_section, fragmentProductDetails);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged(int position, int newQuantity);
    }


    public interface OnItemClickListener {
        void onItemClick(int position, String imageUrl);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView rateTextView;
        TextView mrpTextView;
        Button addToCartButton;
        LinearLayout elegantLayout;
        ElegantNumberButton numberButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.picture);
            titleTextView = itemView.findViewById(R.id.product_name);
            descriptionTextView = itemView.findViewById(R.id.product_discription);
            rateTextView = itemView.findViewById(R.id.rate);
            mrpTextView = itemView.findViewById(R.id.MRP);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
            elegantLayout = itemView.findViewById(R.id.elegantLayout);
            numberButton = itemView.findViewById(R.id.numberButton);
        }
    }
}