package com.example.flipcartClone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flicpcartClone.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
                insertInOrderDatabase();
                clearCart();
                Intent intent = new Intent(ThankYouActivity.this, HomeActivity.class); // Replace HomeActivity with your home fragment's class
                startActivity(intent);
                // Finish the current activity (ThankYouActivity) if you want to close it
                finish();
            }
        });
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public void onBackPressed() {
    }

    private void insertInOrderDatabase() {
        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(this);
        ArrayList<CartItemModel> cartItems = cartDatabaseHelper.getCartItems();

        // Insert product details into the OrderDatabaseHelper
        for (CartItemModel cartItem : cartItems) {
            String productId = cartItem.getProductId();
            String productName = cartItem.getProductName();
            String productImage = cartItem.getImageUrl();
            // You can fetch other details like image URL, etc.

            // Insert the details into the OrderDatabaseHelper
            String currentDate;
            currentDate = getCurrentDate();
            OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);
            orderDatabaseHelper.insertOrder(productId, productName, productImage, currentDate); // Insert the order date
        }

    }

    private void clearCart() {
        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(this);
        cartDatabaseHelper.clearCart();
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
    }
}
