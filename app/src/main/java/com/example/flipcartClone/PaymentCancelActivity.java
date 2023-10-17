package com.example.flipcartClone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flicpcartClone.R;

public class PaymentCancelActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_cancel);
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
                Intent intent = new Intent(PaymentCancelActivity.this, HomeActivity.class); // Replace HomeActivity with your home fragment's class
                startActivity(intent);
                // Finish the current activity (ThankYouActivity) if you want to close it
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
