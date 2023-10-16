package com.example.flipcartClone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.example.flicpcartClone.R;

public class SplashActivity extends Activity {

    private static final int SPLASH_DELAY = 3000; // 5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        setTheme(R.style.Theme_MyApp_LightMode);
        Log.d("SplashActivity", "Splash screen displayed");

        SessionManager sessionManager = new SessionManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLoggedIn()) {
                    // Intent is used to switch from one activity to another.
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i); // invoke the SecondActivity.
                    finish(); // the current activity will get finished.
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i); // invoke the SecondActivity.
                    finish();

                }
            }
        }, SPLASH_DELAY);
    }
}
