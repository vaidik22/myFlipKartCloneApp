package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class OrderHistoryFragment extends Fragment {

    private OrderDatabaseHelper orderDatabaseHelper;
    private RecyclerView recyclerView;
    private OrderHistoryAdapter orderHistoryAdapter;
    private String currentDate;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        // Initialize the database helper
        orderDatabaseHelper = new OrderDatabaseHelper(requireContext());

        recyclerView = view.findViewById(R.id.orderRecyclerView); // Make sure to define the RecyclerView in your XML layout
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ((HomeActivity) requireActivity()).toggleBottomNavigationView(false);

        // Get a list of orders from the database
        ArrayList<OrderModel> orderList = getOrderList(); // Define the OrderModel class to represent order data

        orderHistoryAdapter = new OrderHistoryAdapter(getContext(), orderList);
        recyclerView.setAdapter(orderHistoryAdapter);

        return view;
    }

    private ArrayList<OrderModel> getOrderList() {
        ArrayList<OrderModel> orderList = new ArrayList<>();

        // Use the OrderDatabaseHelper to retrieve orders from the database
        SQLiteDatabase db = orderDatabaseHelper.getReadableDatabase();
        String[] projection = {
                OrderDatabaseHelper.COLUMN_PRODUCT_ID,
                OrderDatabaseHelper.COLUMN_PRODUCT_NAME,
                OrderDatabaseHelper.COLUMN_PRODUCT_IMAGE,
                OrderDatabaseHelper.COLUMN_PRODUCT_DESCRIPTION
                // Add more columns as needed
        };

        Cursor cursor = db.query(
                OrderDatabaseHelper.TABLE_ORDERS,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String productId = cursor.getString(cursor.getColumnIndex(OrderDatabaseHelper.COLUMN_PRODUCT_ID));
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex(OrderDatabaseHelper.COLUMN_PRODUCT_NAME));
                @SuppressLint("Range") String productImage = cursor.getString(cursor.getColumnIndex(OrderDatabaseHelper.COLUMN_PRODUCT_IMAGE));
                @SuppressLint("Range") String orderDate = cursor.getString(cursor.getColumnIndex(OrderDatabaseHelper.COLUMN_PRODUCT_DESCRIPTION)); // Get the order date
                // Fetch other order details like image, price, etc.
                // Fetch other order details like image, price, etc.

                // Create an OrderModel object and add it to the list
                OrderModel order = new OrderModel(productId, productName, productImage, orderDate);
                orderList.add(order);
            }
            cursor.close();
        }

        db.close();

        return orderList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HomeActivity) requireActivity()).toggleBottomNavigationView(true);
    }
}
