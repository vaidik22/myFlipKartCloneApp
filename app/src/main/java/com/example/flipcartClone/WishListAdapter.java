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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        WishListItem item = wishlistItems.get(position);
        // Bind data from the WishListItem to the views in wishlist_item.xml
        holder.itemNameTextView.setText(item.getProductName());
        holder.itemRateTextView.setText("₹" + item.getProductRate());
        holder.itemMRPTextView.setText("₹" + item.getProductMrp());
        String mrpString = String.valueOf(item.getProductMrp());
        String rateString = String.valueOf(item.getProductRate());
        double mrp = Double.parseDouble(mrpString);
        double rate = Double.parseDouble(rateString);
        double percentageDifference = ((mrp - rate) / mrp) * 100;
        int roundedPercentage = (int) Math.round(percentageDifference);
        holder.discount.setText(roundedPercentage + "% off");
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

    private void navigateToBuyProductFragment() {
        // Create a new instance of BuyProductFragment
        BuyProductFragment buyProductFragment = new BuyProductFragment();

        // Perform the fragment transaction to replace the current fragment with BuyProductFragment
        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_section, buyProductFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
        TextView itemMRPTextView;
        TextView discount;
        ImageButton wishlistItemImageButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.wishlistItemImage);
            itemNameTextView = itemView.findViewById(R.id.wishlistItemName);
            itemRateTextView = itemView.findViewById(R.id.wishlistItemRate);
            itemMRPTextView = itemView.findViewById(R.id.wishlistItemMRP);
            discount = itemView.findViewById(R.id.discount);
            wishlistItemImageButton = itemView.findViewById(R.id.wishlistItemImageButton);
        }
    }
}
