package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.flicpcartClone.R;

public class AccountFragment extends Fragment {
    Button logoutButton;
    private SessionManager sessionManager;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Button editProfileButton = view.findViewById(R.id.editprofile);
        Button cart = view.findViewById(R.id.cart);
        Button wishlistButton = view.findViewById(R.id.wishlist); // Find the Wishlist button
        logoutButton = view.findViewById(R.id.logoutButton);
        sessionManager = new SessionManager(requireContext());
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout the user (clear session data, etc.) and navigate to LoginActivity
                sessionManager.logoutUser(); // You should implement this method in your SessionManager class

                // Start the LoginActivity
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish(); // Finish the current activity (AccountFragment)
            }
        });
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        String userId = sessionManager.getPhoneNumber();
        Cursor cursor = dbHelper.getUsernameAndName(userId);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));

            // Find the EditText widget in your fragment's layout
            TextView nameEditText = view.findViewById(R.id.nameTextView_account);

            // Set the retrieved name to the EditText
            nameEditText.setText("Hello! " + name);
        }


        // Set a click listener for the Wishlist button
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate back to the HomeFragment
                navigateToHomeFragment();
            }
        });
        wishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the WishListFragment
                WishListFragment wishListFragment = new WishListFragment();

                // Replace the current fragment with the WishListFragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_section, wishListFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the EditProfileFragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_section, new EditProfileFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the EditProfileFragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_section, new CartFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private void navigateToHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_section, homeFragment);
        transaction.addToBackStack(null); // Add to back stack so the user can navigate back
        transaction.commit();// Go back to the previous fragment
    }
}
