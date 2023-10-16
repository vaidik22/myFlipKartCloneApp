package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class SubCategoryAdapter extends Adapter<SubCategoryAdapter.ViewHolder> {
    private CartDatabaseHelper dbcartHelp;
    private Context context;
    private ArrayList<SubCategoryModel> productList;
    private SubCategoryAdapter.OnItemClickListener listener;

    private ProductDatabaseHelper dbProductHelper;
    private WishListDatabaseHelper dbWishListHelper;

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
        dbWishListHelper = new WishListDatabaseHelper(context);

    }

    public void filterList(ArrayList<SubCategoryModel> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        productList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SubCategoryModel item = productList.get(position);

        Log.e("SubCategoryModel_data", item.toString());
        holder.titleTextView.setText(item.getTitle());
        holder.descriptionTextView.setText(item.getDescription());
        holder.rateTextView.setText("₹" + item.getRate());
        holder.mrpTextView.setText("₹" + item.getMrp());
        String mrpString = item.getMrp();
        String rateString = item.getRate();
        double mrp = Double.parseDouble(mrpString);
        double rate = Double.parseDouble(rateString);
        double percentageDifference = ((mrp - rate) / mrp) * 100;
        int roundedPercentage = (int) Math.round(percentageDifference);
        holder.discount.setText(roundedPercentage + "% off");
        Glide.with(context).load(item.getImageUrl()).into(holder.imageView);
        boolean isInWishlist = dbWishListHelper.isProductInWishlist(item.getProductId());
        boolean isInCart = dbcartHelp.isProductInCart(item.getProductId());
        int updatedStock = dbProductHelper.getProductStock(String.valueOf(item.getProductId()));
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                holder.progressBar.setVisibility(View.GONE);
            }

        }, 1000);
        if (updatedStock == 0) {
            holder.out_of_stock.setVisibility(View.VISIBLE);
            holder.addToCartButton.setEnabled(false);
            int greyColor = ContextCompat.getColor(context, R.color.disabledGreyColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.addToCartButton.setBackgroundTintList(ColorStateList.valueOf(greyColor));
            }
            holder.imageView.setAlpha(0.5f);
        } else {
            holder.out_of_stock.setVisibility(View.GONE);
            holder.addToCartButton.setEnabled(true);
            int normalColor = ContextCompat.getColor(context, R.color.yellow);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.addToCartButton.setBackgroundTintList(ColorStateList.valueOf(normalColor));
            }
            holder.imageView.setAlpha(1.0f);
        }

        if (isInWishlist) {
            holder.crossIcon.setVisibility(View.VISIBLE);
            holder.wishlistIcon.setVisibility(View.GONE);
        } else {
            holder.crossIcon.setVisibility(View.GONE);
            holder.wishlistIcon.setVisibility(View.VISIBLE);

        }
        if (isInCart) {
            holder.layout_button.setVisibility(View.VISIBLE);
            holder.addToCartButton.setVisibility(View.GONE);
            holder.numberButton.setVisibility(View.VISIBLE);
            holder.numberButton.setNumber(item.getQuantity());
        } else {
            holder.layout_button.setVisibility(View.VISIBLE);
            holder.addToCartButton.setVisibility(View.VISIBLE);
            holder.numberButton.setVisibility(View.GONE);
        }
        holder.addToCartButton.setOnClickListener(v -> {
            holder.layout_button.setVisibility(View.VISIBLE);
            holder.addToCartButton.setVisibility(View.GONE);
            holder.numberButton.setVisibility(View.VISIBLE);
            holder.numberButton.setNumber("1");
            Toast.makeText(context, " added to cart", Toast.LENGTH_SHORT).show();


            int initialQuantity = Integer.parseInt(holder.numberButton.getNumber());
            dbcartHelp.insertCartItem(
                    item.getProductId(),
                    item.getTitle(),
                    item.getRate(),
                    item.getMrp(),
                    item.getImageUrl(),
                    item.getStocks(),
                    String.valueOf(initialQuantity)
            );
            dbProductHelper.updateProductQuantity(String.valueOf(item.getProductId()), 1);

        });
        holder.wishlistIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInWishlist = dbWishListHelper.isProductInWishlist(item.getProductId());

                if (!isInWishlist) {
                    // Product is not in wishlist, add it
                    holder.wishlistIcon.setVisibility(View.GONE);
                    holder.crossIcon.setVisibility(View.VISIBLE);
                    dbWishListHelper.addToWishlist(
                            item.getProductId(),
                            item.getTitle(),
                            item.getRate(),
                            item.getMrp(),
                            item.getImageUrl(),
                            String.valueOf(Integer.parseInt(holder.numberButton.getNumber()))
                    );
                    Toast.makeText(context, " added to wishlist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, " is already in your wishlist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.crossIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbWishListHelper.isProductInWishlist(item.getProductId())) {
                    holder.wishlistIcon.setVisibility(View.GONE);
                    holder.crossIcon.setVisibility(View.VISIBLE);
                }
                dbWishListHelper.deleteProduct(String.valueOf(item.getProductId()));
                Toast.makeText(context, " removed from wishlist", Toast.LENGTH_SHORT).show();
                holder.crossIcon.setVisibility(View.GONE);
                holder.wishlistIcon.setVisibility(View.VISIBLE);
            }
        });


        holder.numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                SubCategoryModel selectedItem = productList.get(position);
                String productId = String.valueOf(selectedItem.getProductId());

                // Check if the new quantity is within stock limits
                int availableStock = selectedItem.getStocks();
                Log.d("Stock Debug", "Product ID: " + productId + ", Available Stock: " + availableStock + ", New Quantity: " + newValue);
                if (newValue <= availableStock) {
                    // Update the product database with the new quantity
                    dbProductHelper.updateProductQuantity(productId, newValue);
                    if (newValue < 1) {
                        dbcartHelp.deleteProduct(productId);
                        holder.layout_button.setVisibility(View.VISIBLE);
                        holder.numberButton.setVisibility(View.GONE);
                        holder.addToCartButton.setVisibility(View.VISIBLE);

                    } else {
                        holder.layout_button.setVisibility(View.VISIBLE);
                        holder.numberButton.setVisibility(View.VISIBLE);
                        holder.numberButton.setNumber(String.valueOf(newValue));
                        holder.addToCartButton.setVisibility(View.GONE);
                        dbcartHelp.updateProductQuantityInCart(productId, newValue);
//                      int stockLeft = newValue - availableStock;
//                      Toast.makeText(context, " left in Stock" + stockLeft, Toast.LENGTH_SHORT).show();

                    }
                } else if (newValue >= availableStock) {
                    Toast.makeText(context, "Insufficient stock", Toast.LENGTH_SHORT).show();
                    holder.numberButton.setNumber(String.valueOf(oldValue));
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position); // Call the click listener method
                }
                // Get the clicked item's data
                SubCategoryModel selectedItem = productList.get(position);


                FragmentProductDetails fragmentProductDetails = new FragmentProductDetails();
                Bundle args = new Bundle();
                args.putString("title", selectedItem.getTitle());
                args.putString("description", selectedItem.getDescription());
                args.putString("rate", String.valueOf(selectedItem.getRate()));
                args.putString("mrp", String.valueOf(selectedItem.getMrp()));
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
        boolean onCreateOptionsMenu(Menu menu);

        void onBackPressed();

        void onQuantityChanged(int position, int newQuantity);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView rateTextView;
        TextView mrpTextView;
        Button addToCartButton;
        ElegantNumberButton numberButton;
        ImageView img_icon;
        TextView tv_title;
        TextView discount;
        ImageView out_of_stock;
        ImageButton wishlistIcon;
        ImageButton crossIcon;
        LinearLayout layout_button;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.picture);
            titleTextView = itemView.findViewById(R.id.product_name);
            descriptionTextView = itemView.findViewById(R.id.product_description);
            rateTextView = itemView.findViewById(R.id.rate);
            mrpTextView = itemView.findViewById(R.id.MRP);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
            numberButton = itemView.findViewById(R.id.numberButton);
            img_icon = itemView.findViewById(R.id.img_icon);
            wishlistIcon = itemView.findViewById(R.id.wishlistIcon);
            crossIcon = itemView.findViewById(R.id.crossIcon);
            tv_title = itemView.findViewById(R.id.tv_title);
            out_of_stock = itemView.findViewById(R.id.out_of_stock);
            discount = itemView.findViewById(R.id.discount);
            layout_button = itemView.findViewById(R.id.layout_button);
            progressBar = itemView.findViewById(R.id.PBLoading2);
        }
    }
}