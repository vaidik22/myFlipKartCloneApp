package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class OrderHistoryFragment extends Fragment {

    private OrderDatabaseHelper orderDatabaseHelper;
    private RecyclerView recyclerView;
    private OrderHistoryAdapter orderHistoryAdapter;
    LinearLayout emptyOrder;
    LinearLayout Order_items;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        // Initialize the database helper
        orderDatabaseHelper = new OrderDatabaseHelper(requireContext());

        recyclerView = view.findViewById(R.id.orderRecyclerView); // Make sure to define the RecyclerView in your XML layout
        emptyOrder = view.findViewById(R.id.empty_order_items); // Make sure to define the RecyclerView in your XML layout
        Order_items = view.findViewById(R.id.Order_items); // Make sure to define the RecyclerView in your XML layout
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ((HomeActivity) requireActivity()).toggleBottomNavigationView(false);

        //orderDatabaseHelper.clearOrders();
        // Get a list of orders from the database
        ArrayList<OrderModel> orderList = getOrderList();
        if (orderList.isEmpty()) {
            // Cart is empty, show the message and continue shopping button
            emptyOrder.setVisibility(View.VISIBLE);
            Order_items.setVisibility(View.GONE);
        } else {
            // Cart has items, show the cart items
            emptyOrder.setVisibility(View.GONE);
            Order_items.setVisibility(View.VISIBLE);
        }
        orderHistoryAdapter = new OrderHistoryAdapter(getContext(), orderList);
        recyclerView.setAdapter(orderHistoryAdapter);
        Button continueShoppingButton = view.findViewById(R.id.continue_shopping_button_2);
        continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHomeFragment();
            }
        });
        return view;
    }

    private void navigateToHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_section, homeFragment);
        transaction.addToBackStack(null); // Add to back stack so the user can navigate back
        transaction.commit();// Go back to the previous fragment
    }

    private ArrayList<OrderModel> getOrderList() {
        SessionManager sessionManager = new SessionManager(getContext());
        String currentUserPhoneNumber = sessionManager.getPhoneNumber();
        ArrayList<OrderModel> orderList = new ArrayList<>();
        // Use the OrderDatabaseHelper to retrieve orders from the database
        SQLiteDatabase db = orderDatabaseHelper.getReadableDatabase();
        String[] projection = {
                OrderDatabaseHelper.COLUMN_PHONE_NUMBER,
                OrderDatabaseHelper.COLUMN_PRODUCT_ID,
                OrderDatabaseHelper.COLUMN_PRODUCT_NAME,
                OrderDatabaseHelper.COLUMN_PRODUCT_IMAGE,
                OrderDatabaseHelper.COLUMN_PRODUCT_DESCRIPTION
                // Add more columns as needed
        };
        String selection = "phoneNumber = ?";
        String[] selectionArgs = {currentUserPhoneNumber};
        Cursor cursor = db.query(
                OrderDatabaseHelper.TABLE_ORDERS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(OrderDatabaseHelper.COLUMN_PHONE_NUMBER));
                @SuppressLint("Range") String productId = cursor.getString(cursor.getColumnIndex(OrderDatabaseHelper.COLUMN_PRODUCT_ID));
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex(OrderDatabaseHelper.COLUMN_PRODUCT_NAME));
                @SuppressLint("Range") String productImage = cursor.getString(cursor.getColumnIndex(OrderDatabaseHelper.COLUMN_PRODUCT_IMAGE));
                @SuppressLint("Range") String orderDate = cursor.getString(cursor.getColumnIndex(OrderDatabaseHelper.COLUMN_PRODUCT_DESCRIPTION)); // Get the order date
                // Fetch other order details like image, price, etc.

                // Create an OrderModel object and add it to the list
                OrderModel order = new OrderModel(phoneNumber, productId, productName, productImage, orderDate);
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
