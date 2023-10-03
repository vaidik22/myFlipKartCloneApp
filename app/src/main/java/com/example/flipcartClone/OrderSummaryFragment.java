package com.example.flipcartClone;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class OrderSummaryFragment extends Fragment {
    private ArrayList<CartItemModel> cartItems;
    EditText addressEditText;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_summary, container, false);
        SessionManager sessionManager = new SessionManager(requireContext());
        addressEditText = rootView.findViewById(R.id.et_address);
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
        OrderSummaryAdapter orderSummaryAdapter = new OrderSummaryAdapter(getContext(), cartItems);
        // Calculate the rate and total amount based on cart items
        double totalAmount = 0.0;
        for (CartItemModel item : cartItems) {
            totalAmount += item.getProductRate() * item.getQuantity();
        }
        double totalcost = 0.0;
        for (CartItemModel item : cartItems) {
            totalcost += item.getProductMrp() * item.getQuantity();
        }
        orderSummaryAdapter.notifyDataSetChanged();

        double discountAmount = totalcost - totalAmount;
        TextView discountTextView = rootView.findViewById(R.id.discount_value);
        discountTextView.setText(String.format("-₹%.2f", discountAmount));
        // Now, you can display the total amount in your TextView
        TextView totalAmountTextView = rootView.findViewById(R.id.total_amount_value);
        totalAmountTextView.setText(String.format("₹%.2f", totalAmount + 40));
        TextView totalCostTextView = rootView.findViewById(R.id.total_cost_value);
        totalCostTextView.setText(String.format("₹%.2f", totalcost));
        Button continueButton;
        continueButton = rootView.findViewById(R.id.continue_button);

        // Disable the "Continue" button initially
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressEditText.getText().toString().trim();
                if (address.isEmpty()) {
                    // Address field is empty, show a toast message
                    Toast.makeText(requireContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
                } else {
                    CartDatabaseHelper dbhelper = new CartDatabaseHelper(getContext());
                    ProductDatabaseHelper producthelper = new ProductDatabaseHelper(getContext());
                    dbhelper.clearCart();
                    producthelper.updateAllProductQuantitiesToZero();
                    // Address is entered, navigate to the next fragment
                    navigateToPaymentOptionsFragment();
                }
            }
        });

        return rootView;
    }

    // ...

    private void navigateToPaymentOptionsFragment() {
        // Create an instance of the PaymentOptionsFragment
        PaymentOptionsFragment paymentOptionsFragment = new PaymentOptionsFragment();

        // Replace the current fragment with PaymentOptionsFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_section, paymentOptionsFragment); // Replace 'fragment_container' with your fragment container ID
        transaction.addToBackStack(null); // If you want to allow back navigation
        transaction.commit();
    }

    private ArrayList<CartItemModel> getCartItemsFromDataSource() {
        ArrayList<CartItemModel> cartItems = new ArrayList<>();
        CartDatabaseHelper dbHelper = new CartDatabaseHelper(getContext());
        cartItems = dbHelper.getCartItems();
        return cartItems;
    }
}
