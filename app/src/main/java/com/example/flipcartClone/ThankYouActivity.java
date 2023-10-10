package com.example.flipcartClone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class ThankYouActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_thank_you);
        getSupportActionBar().hide();
        ImageView tickImageView = findViewById(R.id.iv_tick);
        tickImageView.setVisibility(View.VISIBLE);

// Apply a fade-in animation to  the ImageView
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000); // Adjust the duration as needed
        tickImageView.startAnimation(fadeIn);
        // Handle "Continue Shopping" button click
        findViewById(R.id.btn_continue_shopping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStockQuantity();
                Intent intent = new Intent(ThankYouActivity.this, HomeActivity.class); // Replace HomeActivity with your home fragment's class
                startActivity(intent);

                // Finish the current activity (ThankYouActivity) if you want to close it
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    private void updateStockQuantity() {
        // Initialize your database helpers
        ProductDatabaseHelper productDatabaseHelper = new ProductDatabaseHelper(this);
        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(this);

        // Fetch cart items
        ArrayList<CartItemModel> cartItems = cartDatabaseHelper.getCartItems();

        // Update stock quantities based on cart items
        for (CartItemModel cartItem : cartItems) {
            String productId = cartItem.getProductId();
            int cartQuantity = cartItem.getQuantity();

            // Fetch the product's current stock quantity from the product database
            int currentStock = productDatabaseHelper.getProductStock(productId);

            // Calculate the new stock quantity after deducting cart quantity
            int newStock = currentStock - cartQuantity;
            Log.e("updatedStocks", String.valueOf(newStock));

            // Update the product's stock quantity in the product database
            productDatabaseHelper.updateProductStock(productId, newStock);
        }
        cartDatabaseHelper.clearCart();
    }
}
