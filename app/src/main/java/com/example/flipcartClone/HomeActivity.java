package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.flicpcartClone.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private static final int NAVIGATION_HOME = R.id.navigation_home;
    private static final int NAVIGATION_ACCOUNT = R.id.navigation_Account;
    private static final int NAVIGATION_CART = R.id.navigation_cart;
    AppBarLayout appBarLayout;
    AppBarLayout appBarLayoutTwo;
    ArrayList<HomeModel> hList;
    Toolbar toolbar;
    private SessionManager sessionManager;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MyApp_LightMode);
        sessionManager = new SessionManager(this);
        setContentView(R.layout.activity_home);
        appBarLayout = findViewById(R.id.appBar);
        appBarLayoutTwo = findViewById(R.id.appBar2);
        toolbar = findViewById(R.id.toolbar2);
        toggleBottomNavigationView(true);


        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavigation);

        bottomNavView.setVisibility(View.VISIBLE);
        bottomNavView.setOnNavigationItemSelectedListener(
                item -> {
                    int id = item.getItemId();
                    String title = "";

                    if (id == NAVIGATION_HOME) {
                        openFragment(new HomeFragment());
                        title = "Home";
                        bottomNavView.setVisibility(View.VISIBLE);
                    } else if (id == NAVIGATION_ACCOUNT) {
                        openFragment(new AccountFragment());
                        title = "Account";
                        bottomNavView.setVisibility(View.VISIBLE);
                    } else if (id == NAVIGATION_CART) {
                        openFragment(new CartFragment());
                        title = "Cart";
                        bottomNavView.setVisibility(View.VISIBLE);
                    } else {
                        bottomNavView.setVisibility(View.GONE);
                    }
                    // Set the title on tv_title2
                    TextView tvTitle2 = findViewById(R.id.tv_title2);
                    tvTitle2.setText(title);

                    return true;
                });

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_section, new HomeFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_section);
            if (fragment != null) { // Check if the fragment is not null
                String fragmentName = fragment.getClass().getSimpleName();
                if (fragmentName.contains("HomeFragment")) {
                    appBarLayout.setVisibility(View.VISIBLE);
                    appBarLayoutTwo.setVisibility(View.GONE);
                } else {
                    appBarLayout.setVisibility(View.GONE);
                    appBarLayoutTwo.setVisibility(View.VISIBLE);
                }
            }
        });

        ImageView imageView = findViewById(R.id.img_back);
        imageView.setOnClickListener(view -> {
            getSupportFragmentManager().popBackStack();
        });
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_section);
        if (currentFragment instanceof HomeFragment) {
            showExitConfirmationDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.exit_dialog, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.findViewById(R.id.confirm_exit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Exit the app
                finishAffinity();
            }
        });

        dialog.findViewById(R.id.cancel_exit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
    }

    void toggleBottomNavigationView(boolean show) {
        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavigation);
        bottomNavView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void openFragment(Fragment yourFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_section, yourFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void onItemClick(int position) {
        String itemName = hList.get(position).getTitle();
        getSupportActionBar().setTitle(itemName); // Update the Toolbar title
        openFragment(new HomeFragment());
    }
}
