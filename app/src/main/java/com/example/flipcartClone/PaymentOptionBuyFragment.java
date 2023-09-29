package com.example.flipcartClone;

import android.annotation.SuppressLint;
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
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import java.util.ArrayList;

public class PaymentOptionBuyFragment extends Fragment implements PaymentStatusListener {

    public PaymentOptionBuyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_payment_buy_options, container, false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView totalAmountTextView = rootView.findViewById(R.id.tv_total_amount);

        // Set the total amount to the TextView
        Bundle args = getArguments();
        if (args != null) {
            double totalAmount = args.getDouble("totalAmount", 0.0); // Default value is 0.0 if not found
            totalAmountTextView.setText(String.valueOf(totalAmount));
        }
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button continueButton = rootView.findViewById(R.id.btn_continue);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RadioGroup radioGroupPayment = rootView.findViewById(R.id.radio_group_payment);
        final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                .with(this.getActivity())
                .setPayeeVpa("vaidik.nigam.jhs@okhdfcbank")
                .setPayeeName("vaidik nigam")
                .setTransactionId("20190603022401")
                .setTransactionRefId("0120192019060302240")
                .setDescription("For Dress")
                .setAmount(String.valueOf(1.00))
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
                    easyUpiPayment.setPaymentStatusListener(PaymentOptionBuyFragment.this);
                    easyUpiPayment.startPayment();

                } else if (selectedId == R.id.radio_upi) {
                    // UPI radio button is selected
                    // Handle UPI payment logic here
                    // You can open a UPI payment app or initiate the payment process
                    easyUpiPayment.setPaymentStatusListener(PaymentOptionBuyFragment.this);
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
                .commit();
    }


    private ArrayList<CartItemModel> getCartItemsFromDataSource() {
        ArrayList<CartItemModel> cartItems = new ArrayList<>();
        CartDatabaseHelper dbHelper = new CartDatabaseHelper(getContext());
        cartItems = dbHelper.getCartItems();
        return cartItems;
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        navigateToThankYouFragment();

    }

    @Override
    public void onTransactionSuccess() {
        navigateToThankYouFragment();
    }

    @Override
    public void onTransactionSubmitted() {
        navigateToThankYouFragment();
    }

    @Override
    public void onTransactionFailed() {
        navigateToThankYouFragment();
    }

    @Override
    public void onTransactionCancelled() {
        navigateToThankYouFragment();
    }

    @Override
    public void onAppNotFound() {
        navigateToThankYouFragment();
    }
}
