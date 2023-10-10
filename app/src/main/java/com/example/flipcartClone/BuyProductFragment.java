package com.example.flipcartClone;

import android.annotation.SuppressLint;
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

public class BuyProductFragment extends Fragment {
    EditText addressEditText;
    TextView total_cost_EditText;
    TextView discount_EditText;
    TextView totalAmountTextView;
    Button continueButton;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.buy_product_fragment, container, false);
        SessionManager sessionManager = new SessionManager(requireContext());
        addressEditText = rootView.findViewById(R.id.et_address);
        total_cost_EditText = rootView.findViewById(R.id.total_cost_value);
        discount_EditText = rootView.findViewById(R.id.discount_value);
        totalAmountTextView = rootView.findViewById(R.id.total_amount_value);
        continueButton = rootView.findViewById(R.id.continue_button);
        Bundle args = getArguments();
        if (args != null) {
            String productMRP = args.getString("productMRP");
            String productRate = args.getString("productRate");
            productMRP = productMRP.replaceAll("[^\\d.]+", "");
            productRate = productRate.replaceAll("[^\\d.]+", "");

            // Set MRP and rate values to your TextViews
            total_cost_EditText.setText("₹" + productMRP);
            discount_EditText.setText("₹" + productRate);
            double totalAmount = Double.parseDouble(productRate);

            // Add 40 to the total amount
            totalAmount += 40;

            // Set the total amount to the TextView
            totalAmountTextView.setText("" + String.valueOf("₹" + totalAmount));
        }
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
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressEditText.getText().toString().trim();
                if (address.isEmpty()) {
                    // Address field is empty, show a toast message
                    Toast.makeText(requireContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
                } else {
                    double totalAmount = calculateTotalAmount(); // Calculate the total amount here
                    navigateToPaymentOptionsFragment(totalAmount);
                }
            }
        });

        return rootView;
    }

    private double calculateTotalAmount() {
        // Get the text from the EditText and remove non-numeric characters
        String text = discount_EditText.getText().toString();
        text = text.replaceAll("[^\\d.]", ""); // Remove all non-numeric characters except decimal point

        // Check if the resulting string is not empty
        if (!text.isEmpty()) {
            double productRate = Double.parseDouble(text);
            double totalAmount = productRate;
            return totalAmount;
        } else {
            // Handle the case where the input is not a valid number
            // You can return a default value or show an error message
            return 0.0; // Default value, you can change this as needed
        }
    }


    private void navigateToPaymentOptionsFragment(double totalAmount) {
        // Create an instance of the PaymentOptionsFragment
        PaymentOptionBuyFragment paymentOptionsFragment = new PaymentOptionBuyFragment();
        Bundle args = new Bundle();
        args.putDouble("totalAmount", totalAmount);
        paymentOptionsFragment.setArguments(args);

        // Replace the current fragment with PaymentOptionsFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_section, paymentOptionsFragment); // Replace 'fragment_container' with your fragment container ID
        transaction.addToBackStack(null); // If you want to allow back navigation
        transaction.commit();
    }
}