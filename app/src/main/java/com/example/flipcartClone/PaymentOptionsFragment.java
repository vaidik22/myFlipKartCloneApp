package com.example.flipcartClone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.flicpcartClone.R;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;

import java.util.ArrayList;

public class PaymentOptionsFragment extends Fragment {
    private ArrayList<CartItemModel> cartItems;
    private static final int UPI_PAYMENT_REQUEST_CODE = 101; // You can choose any value


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
        Button continueButton = rootView.findViewById(R.id.btn_continue);
        RadioGroup radioGroupPayment = rootView.findViewById(R.id.radio_group_payment);
        final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                .with(this.getActivity())
                .setPayeeVpa("vaidik.nigam.jhs@okhdfcbank")
                .setPayeeName("vaidik nigam")
                .setTransactionId("20190603022401")
                .setTransactionRefId("0120192019060302240")
                .setDescription("For Dress")
                .setAmount("90.00")
                .build();

        radioGroupPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_cash_on_delivery) {
                    // "Cash On Delivery" radio button is selected
                    continueButton.setText("Place Order"); // Change the text of the "Continue" button
                } else {
                    // Other payment options are selected
                    continueButton.setText("Continue"); // Restore the original text of the "Continue" button
                }
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroupPayment.getCheckedRadioButtonId();
                if (selectedId == R.id.radio_cash_on_delivery) {
                    // "Cash On Delivery" radio button is selected, navigate to the ThankYouFragment
                    navigateToThankYouFragment();
                } else if (selectedId == R.id.radio_google_pay) {
                    // Google Pay UPI radio button is selected
                    // Handle Google Pay UPI payment logic here
                    // You can open Google Pay app or initiate the payment process
                    easyUpiPayment.startPayment();

                } else if (selectedId == R.id.radio_upi) {
                    // UPI radio button is selected
                    // Handle UPI payment logic here
                    // You can open a UPI payment app or initiate the payment process
                    easyUpiPayment.startPayment();
                }
                // Handle other payment options or continue with the selected payment method
            }
        });

        return rootView;
    }

    private void navigateToThankYouFragment() {
        // Create an instance of the ThankYouFragment
        ThankYouFragment thankYouFragment = new ThankYouFragment();

        // Replace the current fragment with the ThankYouFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_section, thankYouFragment)
                .addToBackStack(null) // Add to back stack so the user can navigate back
                .commit();
    }


    private ArrayList<CartItemModel> getCartItemsFromDataSource() {
        ArrayList<CartItemModel> cartItems = new ArrayList<>();
        CartDatabaseHelper dbHelper = new CartDatabaseHelper(getContext());
        cartItems = dbHelper.getCartItems();
        return cartItems;
    }
}
