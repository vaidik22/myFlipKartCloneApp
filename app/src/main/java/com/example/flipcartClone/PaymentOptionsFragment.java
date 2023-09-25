package com.example.flipcartClone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class PaymentOptionsFragment extends Fragment {
    private ArrayList<CartItemModel> cartItems;

    public PaymentOptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_payment_options, container, false);
        cartItems = getCartItemsFromDataSource();
        double totalAmount = 0.0;
        for (CartItemModel item : cartItems) {
            totalAmount += item.getProductRate() * item.getQuantity() + 40;
        }
        TextView totalAmountTextView = rootView.findViewById(R.id.tv_total_amount);
        totalAmountTextView.setText(String.format("Total Amount: ₹%.2f", totalAmount));
        RadioButton upiPaymentButton = rootView.findViewById(R.id.radio_upi);
        RadioButton cashOnDelivery = rootView.findViewById(R.id.radio_cash_on_delivery);
        RadioButton googlePayUpi = rootView.findViewById(R.id.radio_google_pay);
        Button continueButton = rootView.findViewById(R.id.btn_continue);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the Google Pay UPI radio button is checked
                if (googlePayUpi.isChecked()) {
                    // Open the Google Pay app if it's installed
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("upi://pay")); // URI for Google Pay
                    intent.setPackage("com.google.android.apps.nbu.paisa.user");
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        upiPaymentButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle UPI payment logic here
                                // You can open a UPI payment app or initiate the payment process.
                                // For example, you can use Intent to open a UPI payment app.
                            }
                        });
                    }
                } else {

                    cashOnDelivery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle bank payment logic here
                            // Implement the bank payment process, which may involve integrating with a bank's API.
                        }
                    });
                }
                // Handle other payment options or continue with the selected payment method
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
