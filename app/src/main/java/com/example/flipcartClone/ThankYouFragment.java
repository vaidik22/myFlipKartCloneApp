package com.example.flipcartClone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.flicpcartClone.R;

public class ThankYouFragment extends Fragment {

    public ThankYouFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_thank_you, container, false);
        ImageView tickImageView = rootView.findViewById(R.id.iv_tick);
        tickImageView.setVisibility(View.VISIBLE);

// Apply a fade-in animation to the ImageView
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000); // Adjust the duration as needed
        tickImageView.startAnimation(fadeIn);

        // Handle "Continue Shopping" button click
        rootView.findViewById(R.id.btn_continue_shopping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
//                HomeFragment homeFragment = new HomeFragment();
//
//                // Replace the current fragment with the ThankYouFragment
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_section, homeFragment)
//                        .addToBackStack(null) // Add to back stack so the user can navigate back
//                        .commit();

            }
        });

        return rootView;
    }
}
