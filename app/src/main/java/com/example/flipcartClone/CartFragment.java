package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class CartFragment extends Fragment implements CartAdapter.CartListEmptyListener {
    TextView cart_product_quantity;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    LinearLayout empty_cart;
    LinearLayout cart_items_present;
    ProgressBar progressBar;
    private ArrayList<CartItemModel> cartItems;
    CartDatabaseHelper cartDatabaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        progressBar = view.findViewById(R.id.idPBLoading4);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }

        }, 1000);
        Button continueShoppingButton = view.findViewById(R.id.continue_shopping_button);
        continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHomeFragment();
            }
        });

        recyclerView = view.findViewById(R.id.cart_recycler_view);
        cart_product_quantity = view.findViewById(R.id.quantity);
        empty_cart = view.findViewById(R.id.empty_cart_items);
        cart_items_present = view.findViewById(R.id.cart_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartItems = getCartItemsFromDataSource();
        TextView totalCostTextView = view.findViewById(R.id.totalCostTextView);
        cartAdapter = new CartAdapter(getContext(), cartItems, this, totalCostTextView);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate back to the HomeFragment
                navigateToHomeFragment();
            }
        });

        Log.e("cart_list", String.valueOf(cartItems.size()));
        TextView delete_cart_button = view.findViewById(R.id.delete_cart_button);
        delete_cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the deleteAllCartItems method when the button is clicked
                deleteAllCartItems();
            }
        });


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

    @Override
    public void onCartListEmpty() {
        // Show the "Empty Cart" layout
        empty_cart.setVisibility(View.VISIBLE);
        cart_items_present.setVisibility(View.GONE);

    }

    private void showCartItems(View view) {
        // Hide the "Your cart is empty" message
        empty_cart.setVisibility(View.GONE);
        cart_items_present.setVisibility((View.VISIBLE));
        // Initialize the CartAdapter with the retrieved cart items
        TextView totalCostTextView = view.findViewById(R.id.totalCostTextView);
        cartAdapter = new CartAdapter(getContext(), cartItems, this, totalCostTextView);
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
    private void deleteAllCartItems() {
        // Clear the cartItems list
        cartItems.clear();

        // Update the RecyclerView by notifying the adapter
        cartAdapter.notifyDataSetChanged();

        // Delete all cart items from the database
        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(getContext());
        cartDatabaseHelper.clearCart();

        if (cartItems.isEmpty()) {
            // Cart is empty, show the message and continue shopping button
            showEmptyCartMessage(requireView());
        } else {
            // Cart has items, show the cart items
            showCartItems(requireView());
        }

        // Notify the listener about the cart list being empty
    }

    private void showEmptyCartMessage(View view) {
        // Hide the RecyclerView
        cart_items_present.setVisibility(View.GONE);
        // Show the "Your cart is empty" message
        empty_cart.setVisibility(View.VISIBLE);

        // Show the "Continue Shopping" button
        Button continueShoppingButton = view.findViewById(R.id.continue_shopping_button);
        continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHomeFragment();
            }
        });
    }
}



