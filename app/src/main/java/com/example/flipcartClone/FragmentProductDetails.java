package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.flicpcartClone.R;


public class FragmentProductDetails extends Fragment {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView mrpTextView;
    private TextView rateTextView;
    private ImageView productImageView;
    TextView percentage;
    Button buyButton;
    ImageView out_of_stock;


    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_details, container, false);

        // Initialize UI elements
        titleTextView = root.findViewById(R.id.title);
        descriptionTextView = root.findViewById(R.id.productDescriptionTextView);
        mrpTextView = root.findViewById(R.id.Product_mrp);
        rateTextView = root.findViewById(R.id.Product_rate);
        productImageView = root.findViewById(R.id.productImageView);
        percentage = root.findViewById(R.id.percentage);
        out_of_stock = root.findViewById(R.id.out_of_stock);
        buyButton = root.findViewById(R.id.buyButton);
        ((HomeActivity) requireActivity()).toggleBottomNavigationView(false);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to BuyProductFragment
                navigateToBuyProductFragment();
            }
        });
        // Retrieve data from arguments
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("title");
            String description = args.getString("description");
            String mrpStr = args.getString("mrp");
            String rateStr = args.getString("rate");
            String imageUrl = args.getString("imageUrl");
            int stocks = Integer.parseInt(args.getString("stocks"));
            if (stocks == 0) {
                buyButton.setEnabled(false);
                out_of_stock.setVisibility(View.VISIBLE);
                int greyColor = ContextCompat.getColor(getContext(), R.color.disabledGreyColor);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    buyButton.setBackgroundTintList(ColorStateList.valueOf(greyColor));
                }
                productImageView.setAlpha(0.5f);
            } else {
                out_of_stock.setVisibility(View.GONE);
                buyButton.setEnabled(false);
                int normalColor = ContextCompat.getColor(getContext(), R.color.yellow);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    buyButton.setBackgroundTintList(ColorStateList.valueOf(normalColor));
                }
                productImageView.setAlpha(1.0f);
            }

            double mrp = Double.parseDouble(mrpStr);
            double rate = Double.parseDouble(rateStr);
            double percentageDifference = ((mrp - rate) / mrp) * 100;
            int roundedPercentage = (int) Math.round(percentageDifference);

            // Set text for TextViews
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            mrpTextView.setText("₹" + mrpStr);
            rateTextView.setText("₹" + rateStr);
            percentage.setText(roundedPercentage + "%off"); // Display percentage difference
            Glide.with(this).load(imageUrl).into(productImageView);
        }
        return root;
    }

    private void navigateToBuyProductFragment() {
        // Create a new instance of BuyProductFragment
        BuyProductFragment buyProductFragment = new BuyProductFragment();
        Bundle args = new Bundle();
        args.putString("productMRP", mrpTextView.getText().toString());
        args.putString("productRate", rateTextView.getText().toString());

        // Set the arguments for the BuyProductFragment
        buyProductFragment.setArguments(args);

        // Perform the fragment transaction to replace the current fragment with BuyProductFragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_section, buyProductFragment); // Replace "fragment_container" with your actual container ID
        transaction.addToBackStack(null); // This allows the user to navigate back to the previous fragment
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((HomeActivity) requireActivity()).toggleBottomNavigationView(true);
    }

}
