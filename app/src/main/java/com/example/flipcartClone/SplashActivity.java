package com.example.flipcartClone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.flicpcartClone.R;

public class SplashActivity extends Activity {

    private static final int SPLASH_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            // Session is active, navigate to home page
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            // Session is not active, navigate to login page
            startActivity(new Intent(this, LoginActivity.class));
        }
        new Handler().postDelayed(() -> {
            finish();
        }, SPLASH_DELAY);// Finish the splash screen activity
    }
}
