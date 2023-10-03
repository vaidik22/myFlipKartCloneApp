package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.flicpcartClone.R;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.PaymentApp;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import java.util.ArrayList;

public class PaymentOptionsFragment extends Fragment implements PaymentStatusListener {
    private ArrayList<CartItemModel> cartItems;
    private EasyUpiPayment easyUpiPayment;

    public PaymentOptionsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
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
                    // Specify the package name of the Google Pay app
                    String upiUri = "upi://pay?pa=vandana.nigam.22jhs@icicibank&pn=vaidik%20nigam&tid=20190603022401&tr=0120192019060302240&tn=For%20Dress&am=1.00";
                    String googlePayPackageName = "com.google.android.apps.nbu.paisa.user";


                    // Create an Intent with the deep link URI
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(upiUri));

                    // Explicitly set the package name to open Google Pay
                    intent.setPackage(googlePayPackageName);

                    // Verify that the Google Pay app is installed and can handle the intent
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        // Google Pay app is not installed or cannot handle the intent
                        // You can show a message to the user or take alternative action
                        Toast.makeText(getActivity(), "Google Pay app not installed or cannot handle the intent", Toast.LENGTH_SHORT).show();
                    }
                } else if (selectedId == R.id.radio_upi) {
                    // UPI radio button is selected
                    // Handle UPI payment logic here
                    // You can open a UPI payment app or initiate the payment process

                    easyUpiPayment = new EasyUpiPayment.Builder()
                            .with(getActivity())
                            .setPayeeVpa("sahuisha2681@ybl")
                            .setPayeeName("vaidik nigam")
                            .setTransactionId("20190603022401")
                            .setTransactionRefId("0120192019060302240")
                            .setDescription("For Dress")
                            .setAmount(String.valueOf(1.00))
                            .build();

                    easyUpiPayment.setPaymentStatusListener(PaymentOptionsFragment.this);
                    easyUpiPayment.setDefaultPaymentApp(PaymentApp.NONE);
                    easyUpiPayment.startPayment();
                }
                // Handle other payment options or continue with the selected payment method
            }
        });

        return rootView;
    }

    public void navigateToThankYouFragment() {
        Log.e("navigateToThankYouFragment", "yes");
        // Create an instance of the ThankYouFragment
        ThankYouFragment thankYouFragment = new ThankYouFragment();
        // Replace the current fragment with the ThankYouFragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_section, thankYouFragment).addToBackStack(null)
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
