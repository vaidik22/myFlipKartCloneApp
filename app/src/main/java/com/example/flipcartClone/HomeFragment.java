package com.example.flipcartClone;

import static com.example.flipcartClone.DatabaseHelper.COLUMN_NAME;
import static com.example.flipcartClone.DatabaseHelper.COLUMN_USERNAME;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flicpcartClone.R;
import com.google.android.material.appbar.AppBarLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements SubCategoryAdapter.OnQuantityChangeListener {

    RecyclerView rec_home;
    RecyclerView add_to_cart_rec_product;
    ArrayList<HomeModel> hList;
    HomeAdapter homeAdapter;
    RecyclerView rec_circular;
    ArrayList<CircularModel> cList;
    CircularAdapter circularAdapter;
    SearchView searchView;
    TextView searchHintText;
    String url1 = "https://rukminim2.flixcart.com/fk-p-flap/850/300/image/a08bb1606053a0ba.jpg?q=90";
    String url2 = "https://dog55574plkkx.cloudfront.net/images/flipkart-big-billion-days-offer.jpg";
    String url3 = "https://cdn.flipshope.com/blog/wp-content/uploads/2023/05/Flipkart-Big-Bachat-Dhamaal-sale.jpg";
    SubCategoryAdapter subCategoryAdapter;
    String url11 = "https://flashsaletricks.com/wp-content/uploads/2016/12/Flipkart_Fashion_Sale_24-26Dec.png";
    String url22 = "https://flashsaletricks.com/wp-content/uploads/2016/12/Flipkart_Fashion_Sale_Womens_Clothing_offer_24-26Dec.png";
    String url33 = "https://images.freekaamaal.com/post_images/1637727957.PNG";
    String url44 = "https://rukminim2.flixcart.com/image/450/500/xif0q/shirt/3/j/v/xxl-st10-vebnor-original-imagnvrqgv7e5crg.jpeg?q=90&crop=false";
    String url55 = "https://rukminim2.flixcart.com/image/550/650/kmthz0w0/dress/d/e/6/s-hr-0073-s-hr-fashion-original-imagfn8tfkhxh2xh.jpeg?q=90&crop=false";
    String url66 = "https://rukminim2.flixcart.com/image/416/416/xif0q/mobile/r/k/o/-original-imaghx9qtwbnhwvy.jpeg?q=70";
    String url77 = "https://rukminim2.flixcart.com/image/312/312/xif0q/mobile/d/h/q/m6-pro-5g-mzb0eprin-poco-original-imags3e7vewsafst.jpeg?q=70";
    ProductDatabaseHelper dbHelper;
    private ArrayList<CartItemModel> cartItems = new ArrayList<>();
    private ArrayList<SubCategoryModel> productList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
        dbHelper = new ProductDatabaseHelper(getContext());

        searchView = root.findViewById(R.id.search_view);
        searchHintText = root.findViewById(R.id.search_hint_text);

        // Initially show the hint text
        searchHintText.setVisibility(View.VISIBLE);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchHintText.setVisibility(View.INVISIBLE);
                } else {
                    if (searchView.getQuery().toString().isEmpty()) {
                        searchHintText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Show or hide hint based on search text
                searchHintText.setVisibility(newText.isEmpty() && !searchView.hasFocus() ? View.VISIBLE : View.INVISIBLE);
                return true;
            }
        });
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = root.findViewById(R.id.slider);
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));

        SliderAdapter adapter = new SliderAdapter(sliderView.getContext(), sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        hList = new ArrayList<>();
        rec_home = root.findViewById(R.id.rec_home);
        //rec_home.setLayoutManager(new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        //rec_home.setLayoutManager(new LinearLayoutManager(this.getContext()));// vertical
        // rec_home.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));// horizontal
        rec_home.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        //rec_home.setLayoutManager(new GridLayoutManager(this,1,LinearLayoutManager.HORIZONTAL,false));
        getCategoryList();
        cList = new ArrayList<>();
        rec_circular = root.findViewById(R.id.circuar_rec_view);
        rec_circular.setLayoutManager(new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        getCategory();
        add_to_cart_rec_product = root.findViewById(R.id.add_to_cart_rec_product);
        add_to_cart_rec_product.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        loadDataForSelectedPosition(dbHelper);
        return root;
    }

    private void loadDataForSelectedPosition(ProductDatabaseHelper dbHelper) {
        dbHelper.clearProducts();
        dbHelper.insertProduct("Shirt 1", "Men Regular...", "500", "1000", url44, "1", "0");
        dbHelper.insertProduct("HR fashion 1", "Men Regular...", "500", "1000", url55, "1", "0");
        dbHelper.insertProduct("Shirt 2", "Men Regular...", "500", "1000", url44, "1", "0");
        dbHelper.insertProduct("Iphone 14", "RED,128 GB..", "500", "1000", url66, "1", "0");
        dbHelper.insertProduct("POCO M6 PRO", "BLACK, 128 GB...", "500", "1000", url77, "1", "0");
        dbHelper.insertProduct("POCO M6 PRO 2", "BLACK, 128 GB...", "500", "1000", url77, "1", "0");

        fetchAndDisplayProducts(dbHelper);
        subCategoryAdapter = new SubCategoryAdapter(
                getContext(),
                productList,
                new SubCategoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, String imageUrl) {
                        AppBarLayout appBarLayout1 = requireActivity().findViewById(R.id.appBar);
                        AppBarLayout appBarLayout2 = requireActivity().findViewById(R.id.appBar2);
                        if (appBarLayout1 != null && appBarLayout2 != null) {
                            appBarLayout1.setVisibility(View.GONE);
                            appBarLayout2.setVisibility(View.VISIBLE);
                            // Set the title on the second Toolbar with id toolbar2
                            Toolbar toolbar2 = appBarLayout2.findViewById(R.id.toolbar2);
                            TextView titleTextView = toolbar2.findViewById(R.id.tv_title2);
                            titleTextView.setText(productList.get(position).getTitle());
                        }
                        ProductFragment productFragment = new ProductFragment();
                        // Pass the clicked position to the SubCategoryFragment
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
        add_to_cart_rec_product.setAdapter(subCategoryAdapter);
        subCategoryAdapter.notifyDataSetChanged();
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

    private void navigateToHomeFragment() {
        // Replace the current fragment with the HomeFragment
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_section, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void getCategoryList() {
        hList.clear();
        hList.add(new HomeModel("Food Spreads", R.drawable.food, 1));
        hList.add(new HomeModel("Earphones", R.drawable.earphones, 2));
        hList.add(new HomeModel("Phones", R.drawable.phones, 3));
        hList.add(new HomeModel("Footwear", R.drawable.footwear, 4));
        hList.add(new HomeModel("Cases & Covers", R.drawable.cases, 5));
        hList.add(new HomeModel("Books", R.drawable.books, 6));
        hList.add(new HomeModel("Bags", R.drawable.bags, 7));
        hList.add(new HomeModel("Watches", R.drawable.watches, 8));

        if (hList.size() > 0) {
            homeAdapter = new HomeAdapter(getContext(), hList, new HomeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    AppBarLayout appBarLayout1 = requireActivity().findViewById(R.id.appBar);
                    AppBarLayout appBarLayout2 = requireActivity().findViewById(R.id.appBar2);
                    if (appBarLayout1 != null && appBarLayout2 != null) {
                        appBarLayout1.setVisibility(View.GONE);
                        appBarLayout2.setVisibility(View.VISIBLE);
                        // Set the title on the second Toolbar with id toolbar2
                        Toolbar toolbar2 = appBarLayout2.findViewById(R.id.toolbar2);
                        TextView titleTextView = toolbar2.findViewById(R.id.tv_title2);
                        titleTextView.setText(hList.get(position).getTitle());
                    }
                    ProductFragment productFragment = new ProductFragment();
                    // Pass the clicked position to the SubCategoryFragment
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
            rec_home.setAdapter(homeAdapter);
            homeAdapter.notifyDataSetChanged();
        }
    }

    public void getCategory() {
        cList.clear();
        cList.add(new CircularModel("Next-Gen Brands", R.drawable.brand, 1));
        cList.add(new CircularModel("Deals Zone", R.drawable.deals, 2));
        cList.add(new CircularModel("Mobiles", R.drawable.phone1, 3));
        cList.add(new CircularModel("Furniture", R.drawable.furniture1, 4));
        cList.add(new CircularModel("Appliances", R.drawable.appliances, 5));
        cList.add(new CircularModel("Electronics", R.drawable.electronics, 6));
        cList.add(new CircularModel("Food", R.drawable.foodandmore, 7));

        if (cList.size() > 0) {
            circularAdapter = new CircularAdapter(getContext(), cList, new CircularAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    AppBarLayout appBarLayout1 = requireActivity().findViewById(R.id.appBar);
                    AppBarLayout appBarLayout2 = requireActivity().findViewById(R.id.appBar2);
                    if (appBarLayout1 != null && appBarLayout2 != null) {
                        appBarLayout1.setVisibility(View.GONE);
                        appBarLayout2.setVisibility(View.VISIBLE);
                        // Set the title on the second Toolbar with id toolbar2
                        Toolbar toolbar2 = appBarLayout2.findViewById(R.id.toolbar2);
                        TextView titleTextView = toolbar2.findViewById(R.id.tv_title2);
                        titleTextView.setText(cList.get(position).getTitle());
                    }

                    rec_circular.setAdapter(circularAdapter);
                    circularAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Retrieve the data passed from the LoginActivity
        Bundle userData = getArguments();
        if (userData != null) {
            String username = userData.getString("username", COLUMN_USERNAME);
            String name = userData.getString("name", COLUMN_NAME);

            // Load the EditProfileFragment and pass the username and name
            EditProfileFragment editProfileFragment = new EditProfileFragment();
            Bundle args = new Bundle();
            args.putString("username", username);
            args.putString("name", name);
            editProfileFragment.setArguments(args);

            // Begin fragment transaction
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_section, editProfileFragment);
            transaction.commit();
        }
    }

    @Override
    public void onQuantityChanged(int position, int newQuantity) {

    }
}