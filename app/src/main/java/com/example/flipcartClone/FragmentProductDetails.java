package com.example.flipcartClone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.flicpcartClone.R;


public class FragmentProductDetails extends Fragment {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView mrpTextView;
    private TextView rateTextView;
    private ImageView productImageView;
    TextView percentage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_details, container, false);

        // Initialize UI elements
        titleTextView = root.findViewById(R.id.title);
        descriptionTextView = root.findViewById(R.id.productDescriptionTextView);
        mrpTextView = root.findViewById(R.id.Product_mrp);
        rateTextView = root.findViewById(R.id.Product_rate);
        productImageView = root.findViewById(R.id.productImageView);
        percentage = root.findViewById(R.id.percentage);

        // Retrieve data from arguments
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("title");
            String description = args.getString("description");
            String mrpStr = args.getString("mrp");
            String rateStr = args.getString("rate");
            String imageUrl = args.getString("imageUrl");

            // Convert MRP and rate strings to numeric values
            double mrp = Double.parseDouble(mrpStr);
            double rate = Double.parseDouble(rateStr);

            // Calculate the percentage difference
            double percentageDifference = ((mrp - rate) / mrp) * 100;

            // Set text for TextViews
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            mrpTextView.setText("₹" + mrpStr);
            rateTextView.setText("₹" + rateStr);
            percentage.setText(String.format("%.2f%% off", percentageDifference)); // Display percentage difference
            Glide.with(this).load(imageUrl).into(productImageView);
        }
        return root;
    }

    private void navigateBackToSubCategoryFragment() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

}
