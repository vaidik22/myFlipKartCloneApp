package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.flicpcartClone.R;

public class AccountFragment extends Fragment {

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Button editProfileButton = view.findViewById(R.id.editprofile);
        Button wishlistButton = view.findViewById(R.id.wishlist); // Find the Wishlist button

        // Set a click listener for the Wishlist button
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

        return view;
    }
}
