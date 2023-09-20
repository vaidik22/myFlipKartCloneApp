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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_details, container, false);

        // Initialize UI elements
        titleTextView = root.findViewById(R.id.title);
        descriptionTextView = root.findViewById(R.id.productDescriptionTextView);
        mrpTextView = root.findViewById(R.id.Product_mrp);
        rateTextView = root.findViewById(R.id.Product_rate);
        productImageView = root.findViewById(R.id.productImageView);

        // Retrieve data from arguments
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("title");
            String description = args.getString("description");
            String mrp = args.getString("mrp");
            String rate = args.getString("rate");
            String imageUrl = args.getString("imageUrl");
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            mrpTextView.setText(mrp);
            rateTextView.setText(rate);
            Glide.with(this).load(imageUrl).into(productImageView);
        }
        return root;
    }

    private void navigateBackToSubCategoryFragment() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

}
