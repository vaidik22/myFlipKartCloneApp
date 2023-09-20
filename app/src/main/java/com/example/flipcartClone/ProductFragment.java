package com.example.flipcartClone;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flicpcartClone.R;

import java.util.ArrayList;

public class ProductFragment extends Fragment {
    RecyclerView rec_product;
    ArrayList<ProductModel> hList;
    ProductAdapter productAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_product, null);
        hList = new ArrayList<>();
        rec_product = root.findViewById(R.id.rec_product);
        rec_product.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        getCategoryList();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate back to the HomeFragment
                navigateToHomeFragment();
            }
        });
        return root;
    }

    private FragmentManager getSupportFragmentManager() {
        return null;
    }

    private void navigateToHomeFragment() {
        // Replace the current fragment with the HomeFragment
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_section, homeFragment); // Replace 'fragment_container' with the actual container ID
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void getCategoryList() {
        hList.clear();
        hList.add(new ProductModel("Indo Era", "Product description", "500rs", R.drawable.indoera1, " 1000rs"));
        hList.add(new ProductModel("Indo Era", "Product description", "500rs", R.drawable.indoera1, " 1000rs"));
        hList.add(new ProductModel("Indo Era", "Product description", "500rs", R.drawable.indoera1, " 1000rs"));
        hList.add(new ProductModel("Indo Era", "Product description", "500rs", R.drawable.indoera1, " 1000rs"));
        hList.add(new ProductModel("Indo Era", "Product description", "500rs", R.drawable.indoera1, " 1000rs"));
        hList.add(new ProductModel("Indo Era", "Product description", "500rs", R.drawable.indoera1, " 1000rs"));
        hList.add(new ProductModel("Indo Era", "Product description", "500rs", R.drawable.indoera1, " 1000rs"));
        hList.add(new ProductModel("Indo Era", "Product description", "500rs", R.drawable.indoera1, " 1000rs"));
        hList.add(new ProductModel("Indo Era", "Product description", "500rs", R.drawable.indoera1, " 1000rs"));

        if (hList.size() > 0) {
            productAdapter = new ProductAdapter(getContext(), hList, new ProductAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    FragmentProductDetails productFragment = new FragmentProductDetails();
                    Bundle args = new Bundle();
                    args.putInt("position", position);
                    productFragment.setArguments(args);
                    Log.d("ItemClick", "Clicked position: " + position);
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_section, productFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
            rec_product.setAdapter(productAdapter);
            productAdapter.notifyDataSetChanged();
        }

    }
}

