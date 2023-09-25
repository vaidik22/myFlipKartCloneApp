package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class CartFragment extends Fragment {
    TextView cart_product_quantity;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    LinearLayout empty_cart;
    LinearLayout cart_items_present;
    private ArrayList<CartItemModel> cartItems;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.cart_recycler_view);
        cart_product_quantity = view.findViewById(R.id.quantity);
        empty_cart = view.findViewById(R.id.empty_cart_items);
        cart_items_present = view.findViewById(R.id.cart_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get cart items from the database or your data source
        cartItems = getCartItemsFromDataSource();
        Log.e("cart_list", String.valueOf(cartItems.size()));

        Button placeOrder = view.findViewById(R.id.place_order);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "Place Order" button click here
                // Navigate to the OrderSummaryFragment
                navigateToOrderSummaryFragment();
                Bundle args = new Bundle();
                args.putParcelableArrayList("cartItems", (ArrayList<CartItemModel>) cartItems);
// Create an instance of OrderSummaryFragment and set the arguments
                OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment();
                orderSummaryFragment.setArguments(args);
            }
        });

        if (cartItems.isEmpty()) {
            // Cart is empty, show the message and continue shopping button
            showEmptyCartMessage(view);
        } else {
            // Cart has items, show the cart items
            showCartItems(view);

        }

        return view;
    }

    private void showEmptyCartMessage(View view) {
        // Hide the RecyclerView
        cart_items_present.setVisibility((View.GONE));

        // Show the "Your cart is empty" message
        empty_cart.setVisibility(View.VISIBLE);

        // Show the "Continue Shopping" button
        Button continueShoppingButton = view.findViewById(R.id.continue_shopping_button);
        continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "Continue Shopping" button click here
                // You can navigate to the home fragment or implement the desired behavior
                // For now, let's navigate to the home fragment
                navigateToHomeFragment();
            }
        });
    }

    private void showCartItems(View view) {
        // Hide the "Your cart is empty" message
        empty_cart.setVisibility(View.GONE);
        cart_items_present.setVisibility((View.VISIBLE));
        // Initialize the CartAdapter with the retrieved cart items
        TextView totalCostTextView = view.findViewById(R.id.totalCostTextView);
        cartAdapter = new CartAdapter(getContext(), cartItems);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        double totalCost = 0.0;
        for (CartItemModel item : cartItems) {
            totalCost += item.getProductRate() * item.getQuantity();
        }
        totalCostTextView.setText(String.format("Total Cost: ₹%.2f", totalCost));
        Button placeOrder = view.findViewById(R.id.place_order);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "Place Order" button click here
                // Navigate to the OrderSummaryFragment
                navigateToOrderSummaryFragment();
            }
        });
    }

    private void navigateToOrderSummaryFragment() {
        // Create an instance of the OrderSummaryFragment
        OrderSummaryFragment orderSummaryFragment = new OrderSummaryFragment();

        // Replace the current fragment with the OrderSummaryFragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_section, orderSummaryFragment);
        transaction.addToBackStack(null); // Add to back stack so the user can navigate back
        transaction.commit();
    }

    private void navigateToHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_section, homeFragment);
        transaction.addToBackStack(null); // Add to back stack so the user can navigate back
        transaction.commit();// Go back to the previous fragment
    }

    private ArrayList<CartItemModel> getCartItemsFromDataSource() {
        ArrayList<CartItemModel> cartItems = new ArrayList<>();
        // Example: Fetch cart items from the database
        CartDatabaseHelper dbHelper = new CartDatabaseHelper(getContext());
        cartItems = dbHelper.getCartItems();
        return cartItems;
    }
}




