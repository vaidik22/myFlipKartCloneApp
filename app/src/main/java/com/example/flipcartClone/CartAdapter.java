package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
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
        holder.cartProductName.setText(cartItem.getProductName());
        holder.cartProductRate.setText(String.format("₹%.2f", Double.parseDouble(String.valueOf(cartItem.getProductRate()))));
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
                cartDatabaseHelper.updateProductQuantityInCart(productId, 0);
                ProductDatabaseHelper productDatabaseHelper = new ProductDatabaseHelper(context);
                productDatabaseHelper.updateProductQuantity(productId, 0);


                // Notify the listener about the quantity change (in this case, it's 0)
                if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChanged(position, 0);
                }
            }
        });
        holder.elegantButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                CartItemModel selectedItem = new CartItemModel(cartItem.getProductId(), cartItem.getProductName(), cartItem.getProductRate(), cartItem.getProductMrp(), cartItem.getImageUrl(), cartItem.getStocks(), cartItem.getQuantity());
                String productId = String.valueOf(selectedItem.getProductId());

                Log.d("SubCategoryAdapter", "Product ID: " + productId);
                Log.d("SubCategoryAdapter", "Old Value: " + oldValue);
                Log.d("SubCategoryAdapter", "New Value: " + newValue);

                ProductDatabaseHelper productDatabaseHelper = new ProductDatabaseHelper(context);
                CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(context);
                // Update the product database with the new quantity
                productDatabaseHelper.updateProductQuantity(productId, newValue);
                int availableStock = Integer.parseInt(selectedItem.getStocks());
                if (newValue < 1 && availableStock > 1) {
                    cartItems.remove(position);
                    notifyDataSetChanged();
                    cartDatabaseHelper.deleteProduct(productId);
                    productDatabaseHelper.updateProductQuantity(productId, newValue);
                    holder.outofstock1.setVisibility(View.GONE);

                } else {
                    cartDatabaseHelper.updateProductQuantityInCart(productId, newValue);
                    productDatabaseHelper.updateProductQuantity(productId, newValue);
                    holder.outofstock1.setVisibility(View.GONE);
                }
                if (newValue > availableStock) {
                    Toast.makeText(context, "Insufficient stock", Toast.LENGTH_SHORT).show();
                    holder.elegantButton.setNumber(String.valueOf(oldValue));
                    holder.outofstock1.setVisibility(View.VISIBLE);

                } else if (quantityChangeListener != null) {
                    quantityChangeListener.onQuantityChanged(position, newValue);
                }
                holder.cartProductQuantity.setText("Quantity: " + oldValue);

            }
        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
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
        TextView totalCostTextView;
        Button placeOrder;
        TextView outofstock1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartProductName = itemView.findViewById(R.id.cart_product_name);
            cart_Product_image = itemView.findViewById(R.id.cart_Product_image);
            cartProductRate = itemView.findViewById(R.id.cart_product_rate);
            cartProductQuantity = itemView.findViewById(R.id.quantity);
            elegantButton = itemView.findViewById(R.id.elegantButton);
            delete_button = itemView.findViewById(R.id.delete_button);
            totalCostTextView = itemView.findViewById(R.id.totalCostTextView);
            placeOrder = itemView.findViewById(R.id.place_order);
            outofstock1 = itemView.findViewById(R.id.outofstock1);
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
    }
//            });
//        }
    }