package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flicpcartClone.R;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {
    WishListDatabaseHelper dbwishlisthelper;
    private List<WishListItem> wishlistItems;
    private Context context;
    private WishlistEmptyListener wishlistEmptyListener;

    public WishListAdapter(List<WishListItem> wishlistItems, Context context, WishlistEmptyListener listener) {
        this.wishlistItems = wishlistItems;
        this.context = context;
        this.dbwishlisthelper = new WishListDatabaseHelper(context);
        this.wishlistEmptyListener = listener;
    }


    public WishListAdapter(List<WishListItem> wishlistItems, Context context) {
        this.wishlistItems = wishlistItems;
        this.context = context;
        dbwishlisthelper = new WishListDatabaseHelper(context);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        WishListItem item = wishlistItems.get(position);
        // Bind data from the WishListItem to the views in wishlist_item.xml
        holder.itemNameTextView.setText(item.getProductName());
        holder.itemRateTextView.setText("₹" + item.getProductRate());
        Glide.with(context).load(item.getImageUrl()).into(holder.itemImageView);
        holder.wishlistItemImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the product ID of the item to be deleted
                String productId = String.valueOf(item.getProductId());

                // Remove the item from the list
                wishlistItems.remove(position);

                // Update the adapter
                notifyDataSetChanged();

                // Remove the item from the database
                WishListDatabaseHelper wishListDatabaseHelper = new WishListDatabaseHelper(context);
                wishListDatabaseHelper.deleteProduct(productId);
                if (wishlistItems.isEmpty() && wishlistEmptyListener != null) {
                    wishlistEmptyListener.onWishlistEmpty();
                }
            }
        });


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_items, parent, false);
        return new ViewHolder(view);
    }

    public interface WishlistEmptyListener {
        void onWishlistEmpty();
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        TextView itemNameTextView;
        TextView itemRateTextView;
        ImageButton wishlistItemImageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.wishlistItemImage);
            itemNameTextView = itemView.findViewById(R.id.wishlistItemName);
            itemRateTextView = itemView.findViewById(R.id.wishlistItemRate);
            wishlistItemImageButton = itemView.findViewById(R.id.wishlistItemImageButton);
        }
    }
}
