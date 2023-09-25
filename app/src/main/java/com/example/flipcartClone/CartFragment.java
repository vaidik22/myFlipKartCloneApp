package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private ArrayList<CartItemModel> cartItems;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.cart_recycler_view);
        cart_product_quantity = view.findViewById(R.id.quantity);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get cart items from the database or your data source
        cartItems = getCartItemsFromDataSource();
        Log.e("cart_list", String.valueOf(cartItems.size()));

        // Initialize the CartAdapter with the retrieved cart items
        cartAdapter = new CartAdapter(getContext(), cartItems);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        double totalCost = 0.0;
        for (CartItemModel item : cartItems) {
            totalCost += item.getProductRate() * item.getQuantity();
        }
        TextView totalCostTextView = view.findViewById(R.id.totalCostTextView);
        totalCostTextView.setText(String.format("Total Cost: ₹%.2f", totalCost));
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

        return view;
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

    private ArrayList<CartItemModel> getCartItemsFromDataSource() {
        ArrayList<CartItemModel> cartItems = new ArrayList<>();
        // Example: Fetch cart items from the database
        CartDatabaseHelper dbHelper = new CartDatabaseHelper(getContext());
        cartItems = dbHelper.getCartItems();

//        if (cartItems.size()>0){
//            //not insert
////            fetch data from database
////            cartItems = dbHelper.getCartItems();// like this
//        }else {
//            // insert(temp product list)
//
//        }

        return cartItems;
    }
}




