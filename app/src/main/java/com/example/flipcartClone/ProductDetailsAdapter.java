//package com.example.flipcartClone;
//
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.example.flicpcartClone.R;
//
//public class ProductDetailsAdapter {
//    private Button addToCartButton;
//    private LinearLayout elegantLayout;
//    private TextView quantityTextView;
//
//    private int currentQuantity = 1; // Initial quantity
//
//    public ProductDetailsAdapter(View rootView) {
//        elegantLayout = rootView.findViewById(R.id.elegant_Layout);
//        quantityTextView = rootView.findViewById(R.id.quantity_Text_View);
//
//        // Set up click listener for add_To_Cart_Button
//        addToCartButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Hide the add_To_Cart_Button
//                addToCartButton.setVisibility(View.GONE);
//                // Show the elegant_Layout
//                elegantLayout.setVisibility(View.VISIBLE);
//            }
//        });
//
//        // Set click listeners for the add and subtract buttons
//        Button addButton = rootView.findViewById(R.id.add_Button);
//        Button subtractButton = rootView.findViewById(R.id.subtract_Button);
//
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Increment the current quantity
//                if (currentQuantity < 10) {
//                    currentQuantity++;
//                    // Update the quantity_Text_View
//                    quantityTextView.setText(String.valueOf(currentQuantity));
//                }
//            }
//        });
//
//        subtractButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Decrement the current quantity (if greater than 1)
//                if (currentQuantity > 1) {
//                    currentQuantity--;
//                    // Update the quantity_Text_View
//                    quantityTextView.setText(String.valueOf(currentQuantity));
//                }
//            }
//        });
//
//        // Initialize the quantity_Text_View with the initial quantity
//        quantityTextView.setText(String.valueOf(currentQuantity));
//    }
//}
