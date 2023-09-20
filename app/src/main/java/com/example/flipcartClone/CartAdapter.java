package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    CartDatabaseHelper db_cart_helper;
    private Context context;
    private ArrayList<CartItemModel> cartItems;
    private OnQuantityChangeListener quantityChangeListener;


    public CartAdapter(Context context, ArrayList<CartItemModel> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    public void setQuantityChangeListener(OnQuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartItemModel cartItem = cartItems.get(position);
//        CartItemModel cartItems = new CartItemModel(
//                cartItem.getProductId(),
//                cartItem.getProductName(),
//                cartItem.getProductRate(),
//                cartItem.getImageUrl(),
//                cartItem.getQuantity()
//        );
//        cartAdapter.onAddToCart(cartItem);
//        cartAdapter.notifyDataSetChanged();

        // Bind data to the ViewHolder views
        holder.cartProductName.setText(cartItem.getProductName());
        holder.cartProductRate.setText(String.format("$%.2f", Double.parseDouble(cartItem.getProductRate())));
        holder.cartProductQuantity.setText("Quantity: " + cartItem.getQuantity());
        Glide.with(context)
                .load(cartItem.getImageUrl()) // Load the image from the URL or resource ID
                .into(holder.cart_Product_image);
        holder.elegantButton.setNumber(String.valueOf(cartItem.getQuantity()));
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the product ID of the item to be deleted
                String productId = cartItem.getProductId();

                // Remove the item from the list
                cartItems.remove(position);

                // Update the adapter
                notifyDataSetChanged();

                // Remove the item from the database
                CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(context);
                cartDatabaseHelper.deleteProduct(productId);

                // Notify the listener about the quantity change (in this case, it's 0)
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChanged(position, 0);
                }
            }
        });
        holder.elegantButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                CartItemModel selectedItem = new CartItemModel(cartItem.getProductId(), cartItem.getProductName(), cartItem.getProductRate(), cartItem.getImageUrl(), cartItem.getQuantity());
                String productId = String.valueOf(selectedItem.getProductId());

                Log.d("SubCategoryAdapter", "Product ID: " + productId);
                Log.d("SubCategoryAdapter", "Old Value: " + oldValue);
                Log.d("SubCategoryAdapter", "New Value: " + newValue);

                CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(context);
                // Update the product database with the new quantity
                cartDatabaseHelper.updateProductQuantityInCart(productId, newValue);


                if (newValue == 0) {
                    cartItems.remove(position);
                    notifyDataSetChanged();
                    cartDatabaseHelper.deleteProduct(productId);
                } else {
                    cartDatabaseHelper.updateProductQuantityInCart(productId, newValue);
                }
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChanged(position, newValue);
                }
                holder.cartProductQuantity.setText("Quantity: " + newValue);

            }
        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void onAddToCart(CartItemModel cartItem) {
        addToCart(cartItem);
        Log.d("CartAdapter", "Added to cart: " + cartItem.getProductId());
    }

    private void addToCart(CartItemModel cartItem) {
        boolean itemFound = false;

        for (int i = 0; i < cartItems.size(); i++) {
            CartItemModel existingCartItem = cartItems.get(i);
            if (existingCartItem.getProductId() != null && cartItem.getProductId() != null) {
                if (existingCartItem.getProductId().equals(cartItem.getProductId())) {
                    // Update the quantity of the existing cart item
                    existingCartItem.setQuantity(cartItem.getQuantity());
                    existingCartItem.setImageUrl(cartItem.getImageUrl());
                    itemFound = true;
                    break;
                }
            }
        }

        if (!itemFound) {
            // Add a new cart item to the list
            cartItems.add(cartItem);
        }
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged(int position, int newQuantity);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cartProductName;
        TextView cartProductRate;
        TextView cartProductQuantity;
        ImageView cart_Product_image;
        ElegantNumberButton elegantButton;
        ImageButton delete_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartProductName = itemView.findViewById(R.id.cart_product_name);
            cart_Product_image = itemView.findViewById(R.id.cart_Product_image);
            cartProductRate = itemView.findViewById(R.id.cart_product_rate);
            cartProductQuantity = itemView.findViewById(R.id.quantity);
            elegantButton = itemView.findViewById(R.id.elegantButton);
            delete_button = itemView.findViewById(R.id.delete_button);
//            elegantButton.setOnValueChangeListener((view, oldValue, newValue) -> {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION) {
//                    // Update the CartItemModel's quantity using the cartItems list from CartAdapter
//                    CartItemModel cartItem = cartItems.get(position);
//                    cartItem.setQuantity(Integer.parseInt(String.valueOf(newValue)));
//                    cartProductQuantity.setText("Quantity: " + newValue);
//                    // Notify the listener about the quantity change
//                    if (quantityChangeListener != null) {
//                        quantityChangeListener.onQuantityChanged(position, cartItem.getQuantity());
//                    }
        }
//            });
//        }
    }
}