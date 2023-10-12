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
    String url1 = "https://cdn.zoutons.com/images/originals/blog/main-banner_1693650934.png";
    String url2 = "https://cdn.flipshope.com/blog/wp-content/uploads/2023/05/Flipkart-Upcoming-Sale.jpg";
    String url3 = "https://cdn.flipshope.com/blog/wp-content/uploads/2023/01/Flipkart-New-Year-Sale-2023.jpg";
    SubCategoryAdapter subCategoryAdapter;
    String url44 = "https://www.myraymond.com/media/product/PCSA02393-W1/8476360924_PCSA02393-W1(3).jpg";
    String url55 = "https://rukminim2.flixcart.com/image/832/832/xif0q/top/e/e/h/xl-1023ykp-yash-gallery-original-imagpcfgvx42kbrz.jpeg?q=70";
    String url66 = "https://www.lavanyathelabel.com/cdn/shop/products/sku_2_1.jpg?v=1672232467&width=1024";
    String url666 = "https://m.media-amazon.com/images/I/61SSVxTSs3L._SL1500_.jpg";
    String url77 = "https://rukminim2.flixcart.com/image/416/416/xif0q/mobile/b/h/c/-original-imagth5xwrg4gfyp.jpeg?q=70";
    String url88 = "https://rukminim2.flixcart.com/image/416/416/xif0q/mobile/e/q/g/-original-imagtqqd4vcdzqdg.jpeg?q=70";
    String url99 = "https://rukminim2.flixcart.com/image/832/832/krz97rk0/sunglass/u/x/0/chi00121-c1-l-royal-son-original-imag5nbypcr2k37m.jpeg?q=70";
    String url100 = "https://rukminim2.flixcart.com/image/416/416/kd3f3bk0/trimmer/z/t/f/0-5-18-mm-stainless-steel-bt5100c-corded-cordless-havells-original-imafu2fjcfvbmngm.jpeg?q=70";
    ProductDatabaseHelper dbHelper;
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
        rec_home.setLayoutManager(new LinearLayoutManager(getContext()));
        // rec_home.setLayoutManager(new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        // rec_home.setLayoutManager(new LinearLayoutManager(this.getContext()));// vertical
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear the ArrayList when the view is destroyed
        if (productList != null) {
            productList.clear();
        }
    }

    private ArrayList<SubCategoryModel> getProductItemsFromDataSource() {
        ArrayList<SubCategoryModel> productItems = new ArrayList<>();
        // Example: Fetch cart items from the database
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(getContext());
        productItems = dbHelper.getProductItems();
        return productItems;
    }

    private void loadDataForSelectedPosition(ProductDatabaseHelper dbHelper) {
        //dbHelper.clearProducts();
        if (getProductItemsFromDataSource().size() > 0) {
//           productList = dbHelper.getProductItems();
            fetchAndDisplayProducts(dbHelper);
        } else {
            dbHelper.insertProduct("Park Avenue", "Men Slim Fit Pure Cotton Printed Casual Shirt,Blue conversational printed opaque Casual shirt " +
                    "\n", "1300", "5000", url44, "1", 5);
            dbHelper.insertProduct("Libas", "Women Beige & Mustard Yellow Zari Woven Design Straight Kurta Trousers Dupatta Our 3-piece beige woven design silk suit set features a straight-fit floral embroidery kurta"
                    , "1500", "3500", url55, "1", 5);
            dbHelper.insertProduct("Lavanya The Label", "Tie-Dye Printed Anarkali Kurta With Trousers & Dupattaie-dye printed",
                    "2000", "6000", url66, "1", 5);
            dbHelper.insertProduct("NOISE", "Black Color Fit Icon 2 Calling Smartwatch1.8 inch display (500 nits brightness)", "3300", "5000", url666, "1", 5);
            dbHelper.insertProduct("SAMSUNG Galaxy S22 5G", "Explore a new range of ni" +
                    "ght photography features on this Samsung Galaxy" +
                    " S22 5G smartphone. You can click stunning images eve" +
                    "n in low light with the Nightography mode. It is de" +
                    "signed with a pro-grade camera which comprises a b" +
                    "ig pixel sensor that welcomes more light for mind-blow" +
                    "ing photography. Built with impressive software and hardware, " +
                    "this phone is a game-changer. With a 120 Hz refresh rate and dynamic AMO" +
                    "LED 2x display, you are going to experience immersive screen time. This phone " +
                    "operates on a Snapdragon 8 Gen 1 processor that delivers quick and hassle- free navigation." +
                    " This device is protected by Corning Gorilla Glass Victus and has an IP68 waterproof rating.", "35000", "50000", url77, "1", 5);
            dbHelper.insertProduct("realme 11x 5G (Purple Dawn, 128 GB)", "Avail yourself this 5G smartphone that offers a host of interesting features. The powerful MediaTek Dimensity 6100+ 5G chipset delivers a smooth performance. Click beautiful portraits with the 64 MP AI camera that offers exciting features like Street Photography Mode 4.0, Super Nightscape, 2x In-sensor zoom, and more. Equipped by a 5000 mAh long-lasting batte" +
                    "ry, this phone runs for a longer duration and can be recharg", "13999", "16999", url88, "1", 5);
            dbHelper.insertProduct("ROYAL SON", "Polarized, UV Protection Wayfarer, Retro Square Sunglasses (57)  (For Men & Women, Black)", "594", "1999", url99, "1", 5);
            dbHelper.insertProduct("HAVELLS Trimmer ", "1 N Beard Trimmer, 1 N USB Cable, 1 N Cleaning Brush, 1 N Lubrication Oil, 1 N Instruction Manual45 min Runtime 13 Length Settings  (Black, Grey)", "749", "1495", url100, "1", 5);
        }
//        fetchAndDisplayProducts(dbHelper);
        subCategoryAdapter = new SubCategoryAdapter(
                getContext(),
                productList,
                new SubCategoryAdapter.OnItemClickListener() {
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
                            titleTextView.setText(productList.get(position).getTitle());
                        }
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
                String stockStr = cursor.getString(cursor.getColumnIndexOrThrow(ProductDatabaseHelper.COLUMN_PRODUCT_STOCK));
                int stock = Integer.parseInt(stockStr.replaceAll("[^0-9]", ""));
                SubCategoryModel product = new SubCategoryModel(product_id, title, description, rate, mrp, imageUrl, quantity, stock);
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
//    public interface OnBackPressedListener {
//        void onBackPressed();
//    }
//    @Override
//    public void onBackPressed() {
//        showExitConfirmationDialog();
//    }
//
//
//    private void showExitConfirmationDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//        View dialogView = getLayoutInflater().inflate(R.layout.exit_dialog, null);
//        builder.setView(dialogView);
//
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//
//        dialog.findViewById(R.id.confirm_exit_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Exit the app
//                requireActivity().finish();
//            }
//        });
//
//        dialog.findViewById(R.id.cancel_exit_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Dismiss the dialog
//                dialog.dismiss();
//            }
//        });
//    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onQuantityChanged(int position, int newQuantity) {

    }
}