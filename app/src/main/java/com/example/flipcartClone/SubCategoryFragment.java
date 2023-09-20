package com.example.flipcartClone;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flicpcartClone.R;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class SubCategoryFragment extends Fragment implements SubCategoryAdapter.OnQuantityChangeListener {
    RecyclerView rec_product;
    SubCategoryAdapter subCategoryAdapter;
    String url11 = "https://flashsaletricks.com/wp-content/uploads/2016/12/Flipkart_Fashion_Sale_24-26Dec.png";
    String url22 = "https://flashsaletricks.com/wp-content/uploads/2016/12/Flipkart_Fashion_Sale_Womens_Clothing_offer_24-26Dec.png";
    String url33 = "https://images.freekaamaal.com/post_images/1637727957.PNG";
    String url44 = "https://rukminim2.flixcart.com/image/450/500/xif0q/shirt/3/j/v/xxl-st10-vebnor-original-imagnvrqgv7e5crg.jpeg?q=90&crop=false";
    String url55 = "https://rukminim2.flixcart.com/image/550/650/kmthz0w0/dress/d/e/6/s-hr-0073-s-hr-fashion-original-imagfn8tfkhxh2xh.jpeg?q=90&crop=false";
    String url66 = "https://rukminim2.flixcart.com/image/416/416/xif0q/mobile/r/k/o/-original-imaghx9qtwbnhwvy.jpeg?q=70";
    String url77 = "https://rukminim2.flixcart.com/image/312/312/xif0q/mobile/d/h/q/m6-pro-5g-mzb0eprin-poco-original-imags3e7vewsafst.jpeg?q=70";
    int selectedPosition = -1; // Default position
    ProductDatabaseHelper dbHelper;
    private ArrayList<CartItemModel> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;
    private ArrayList<SubCategoryModel> productList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_subcategories, null);
        dbHelper = new ProductDatabaseHelper(getContext());
        ArrayList<SliderDataFrag> sliderDataArrayListFrag = new ArrayList<>();
        // initializing the slider view.
        SliderView sliderView = root.findViewById(R.id.slider_frag);
        sliderDataArrayListFrag.add(new SliderDataFrag(url11));
        sliderDataArrayListFrag.add(new SliderDataFrag(url22));
        sliderDataArrayListFrag.add(new SliderDataFrag(url33));
        SliderAdapterFragment adapter = new SliderAdapterFragment(sliderView.getContext(), sliderDataArrayListFrag);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        cartAdapter = new CartAdapter(getContext(), cartItems);

        // Create an instance of SubCategoryAdapter and pass the CartAdapter instance
        subCategoryAdapter = new SubCategoryAdapter(
                getContext(),
                productList,
                new SubCategoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, String imageUrl) {
                        SubCategoryModel product = productList.get(position);
                        showProductDetails(product);
                    }
                });
//                    ,
//                    cartAdapter,
//                    this, // Pass the fragment itself as the listener
//                    dbHelper,
//                    dbbHelper // Pass cartDbHelper


        rec_product = root.findViewById(R.id.rec_frag);
        rec_product.setLayoutManager(new GridLayoutManager(getContext(), 2));
        // Initialize the CartAdapter before creating the SubCategoryAdapter
        subCategoryAdapter = new SubCategoryAdapter(
                getContext(),
                productList,
                new SubCategoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, String imageUrl) {
                        SubCategoryModel product = productList.get(position);
                        showProductDetails(product);
                    }
                });
//                    cartAdapter,
//                    this,
//                    dbHelper,
//                    dbbHelper
//            );
        // Set the adapter for your RecyclerView
        rec_product.setAdapter(subCategoryAdapter);
        if (subCategoryAdapter != null) {
            subCategoryAdapter.notifyDataSetChanged();
        }


        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate back to the HomeFragment
                navigateToHomeFragment();
            }
        });

        // Initialize your SubCategoryAdapter and pass the CartAdapter instance

        if (productList.isEmpty()) {
            Bundle args = getArguments();
            if (args != null) {
                selectedPosition = args.getInt("circularPosition", 0);
            }
            loadDataForSelectedPosition(dbHelper);
        }
        setAdapter(productList);
        return root;
    }

    private void navigateToHomeFragment() {
        // Replace the current fragment with the HomeFragment
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_section, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadDataForSelectedPosition(ProductDatabaseHelper dbHelper) {

        if (selectedPosition == 0) {
            dbHelper.insertProduct("Shirt 1", "Men Regular...", "500", "1000", url44, "1", "0");
            dbHelper.insertProduct("HR fashion 1", "Men Regular...", "500", "1000", url55, "1", "0");
        } else if (selectedPosition == 1) {
            dbHelper.insertProduct("Shirt 2", "Men Regular...", "500", "1000", url44, "1", "0");
        } else if (selectedPosition == 2) {
            dbHelper.insertProduct("Iphone 14", "RED,128 GB..", "500", "1000", url66, "1", "0");
            dbHelper.insertProduct("POCO M6 PRO", "BLACK, 128 GB...", "500", "1000", url77, "1", "0");
        } else {
            dbHelper.insertProduct("POCO M6 PRO 2", "BLACK, 128 GB...", "500", "1000", url77, "1", "0");
        }

        fetchAndDisplayProducts(dbHelper);
    }


    private void fetchAndDisplayProducts(ProductDatabaseHelper dbHelper) {
        Cursor cursor = dbHelper.getProducts();

        // Check if cursor is not null and has data
        if (cursor != null && cursor.moveToFirst()) {

            do {
                String product_id = cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRODUCT_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRODUCT_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRODUCT_DESCRIPTION));
                String rate = cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRODUCT_RATE));
                String mrp = cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRODUCT_MRP));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRODUCT_IMAGE));
                String quantity = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow((ProductDatabaseHelper.COLUMN_PRODUCT_QUANTITY)))); // Get quantity from the database
                SubCategoryModel product = new SubCategoryModel(product_id, title, description, rate, mrp, imageUrl, quantity);
                productList.add(product);
            } while (cursor.moveToNext());

            cursor.close();
            if (subCategoryAdapter != null) {
                subCategoryAdapter.notifyDataSetChanged();
            }
            // Initialize the adapter if it's null and set it to the RecyclerView
        }
    }

    private void setAdapter(ArrayList<SubCategoryModel> productList) {
        // You don't need to initialize the adapter here again, as it's already initialized in onCreateView
        if (subCategoryAdapter != null) {
            subCategoryAdapter.notifyDataSetChanged();
        }
    }

    private void showProductDetails(SubCategoryModel product) {
        FragmentProductDetails fragmentProductDetails = new FragmentProductDetails();
        Bundle args = new Bundle();
        args.putString("title", product.getTitle());
        args.putString("description", product.getDescription());
        args.putString("mrp", product.getMrp());
        args.putString("rate", product.getRate());
        args.putString("imageUrl", product.getImageUrl());
        fragmentProductDetails.setArguments(args);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_section, fragmentProductDetails);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        // Return the desired CreationExtras here, or simply call the super method
        return super.getDefaultViewModelCreationExtras();
    }

    @Override
    public void onQuantityChanged(int position, int newQuantity) {
        if (subCategoryAdapter != null) {
            subCategoryAdapter.notifyDataSetChanged();
        }
    }
}