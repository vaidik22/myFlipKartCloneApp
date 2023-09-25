package com.example.flipcartClone;

import android.database.Cursor;
import android.os.Bundle;
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

public class OrderSummaryFragment extends Fragment {
    private ArrayList<CartItemModel> cartItems;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_summary, container, false);
        SessionManager sessionManager = new SessionManager(requireContext());
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        String userId = sessionManager.getPhoneNumber();
        Cursor cursor = dbHelper.getUsernameAndName(userId);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));

            // Find the EditText widget in your fragment's layout
            TextView nameEditText = rootView.findViewById(R.id.edit_name_order);

            // Set the retrieved name to the EditText
            nameEditText.setText("Hello!   " + name);
        }
        cartItems = getCartItemsFromDataSource();
        RecyclerView recyclerView = rootView.findViewById(R.id.product_details_summary);
        OrderSummaryAdapter orderSummaryAdapter = new OrderSummaryAdapter(getContext(), cartItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(orderSummaryAdapter);
        // Calculate the rate and total amount based on cart items
        double totalAmount = 0.0;
        for (CartItemModel item : cartItems) {
            totalAmount += item.getProductRate() * item.getQuantity() + 40;
        }
        double totalcost = 0.0;
        for (CartItemModel item : cartItems) {
            totalcost += item.getProductMrp() * item.getQuantity();
        }
        orderSummaryAdapter.notifyDataSetChanged();

        double discountAmount = totalcost - totalAmount + 40;
        TextView discountTextView = rootView.findViewById(R.id.discount_value);
        discountTextView.setText(String.format("-₹%.2f", discountAmount));

        // Now, you can display the total amount in your TextView
        TextView totalAmountTextView = rootView.findViewById(R.id.total_amount_value);
        totalAmountTextView.setText(String.format("₹%.2f", totalAmount));
        TextView totalCostTextView = rootView.findViewById(R.id.total_cost_value);
        totalCostTextView.setText(String.format("₹%.2f", totalcost));
        Button continueButton = rootView.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with PaymentOptionsFragment
                PaymentOptionsFragment paymentOptionsFragment = new PaymentOptionsFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_section, paymentOptionsFragment); // Replace 'fragment_container' with your fragment container ID
                transaction.addToBackStack(null); // If you want to allow back navigation
                transaction.commit();
            }
        });

        return rootView;
    }

    private ArrayList<CartItemModel> getCartItemsFromDataSource() {
        ArrayList<CartItemModel> cartItems = new ArrayList<>();
        CartDatabaseHelper dbHelper = new CartDatabaseHelper(getContext());
        cartItems = dbHelper.getCartItems();
        return cartItems;
    }
}
