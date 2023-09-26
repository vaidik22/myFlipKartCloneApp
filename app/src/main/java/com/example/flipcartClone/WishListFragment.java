package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flicpcartClone.R;

import java.util.ArrayList;
import java.util.List;

public class WishListFragment extends Fragment {

    private RecyclerView recyclerView;
    private WishListAdapter wishListAdapter;
    private WishListDatabaseHelper dbWishListHelper;
    private List<WishListItem> wishlistItems; // Create a model class for wishlist items


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wishlist, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerWishlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        wishlistItems = new ArrayList<>(); // Initialize the wishlistItems list

        // Initialize the WishListAdapter with the wishlistItems list
        wishListAdapter = new WishListAdapter(wishlistItems, getContext());

        recyclerView.setAdapter(wishListAdapter);
        dbWishListHelper = new WishListDatabaseHelper(getActivity());
        fetchWishlistItems();

        return rootView;
    }

    private void fetchWishlistItems() {
        // Fetch wishlist items from the database using your WishListDatabaseHelper
        // Populate the wishlistItems list with the retrieved data

        SQLiteDatabase db = dbWishListHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM wishlist", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int productId = cursor.getInt(cursor.getColumnIndex("product_id"));
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex("product_name"));
                @SuppressLint("Range") double productRate = cursor.getDouble(cursor.getColumnIndex("product_rate"));
                @SuppressLint("Range") double productMrp = cursor.getDouble(cursor.getColumnIndex("product_mrp"));
                @SuppressLint("Range") String imageUrl = cursor.getString(cursor.getColumnIndex("product_image_url"));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));

                // Create a WishListItem object with the retrieved data
                WishListItem item = new WishListItem(productId, productName, productRate, productMrp, imageUrl, quantity);

                // Add the item to the wishlistItems list
                wishlistItems.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // Notify the adapter that the dataset has changed
        wishListAdapter.notifyDataSetChanged();
    }
}
