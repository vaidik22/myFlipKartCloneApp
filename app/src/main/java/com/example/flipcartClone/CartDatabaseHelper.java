package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class CartDatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_CART = "cart";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_DESCRIPTION = "description";
    private static final String DATABASE_NAME = "cart.db";
    private static final int DATABASE_VERSION = 62;

    // Define table and column names
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_RATE = "product_rate";
    private static final String COLUMN_PRODUCT_STOCK = "product_stock";
    private static final String COLUMN_PRODUCT_MRP = "product_mrp";
    private static final String COLUMN_PRODUCT_IMAGE_URL = "product_image_url";

    // Create table query
    private static final String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + " (" +
            COLUMN_PRODUCT_ID + " TEXT PRIMARY KEY," +
            COLUMN_PRODUCT_NAME + " TEXT," +
            COLUMN_PRODUCT_RATE + " REAL," +
            COLUMN_PRODUCT_MRP + " REAL," +
            COLUMN_PRODUCT_STOCK + " REAL," +
            COLUMN_DESCRIPTION + " REAL," +
            COLUMN_PRODUCT_IMAGE_URL + " TEXT," +
            COLUMN_QUANTITY + " INTEGER" +
            ");";

    public CartDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void deleteProduct(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsAffected = db.delete(TABLE_CART, COLUMN_PRODUCT_ID + "=?", new String[]{productId});

        Log.d("DeleteProduct", "Rows affected: " + rowsAffected);

        db.close();
    }

    public void updateProductQuantityInCart(String productId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, newQuantity);
        String selection = COLUMN_PRODUCT_ID + "= ?";
        String[] selectionArgs = {productId};
        db.update(
                TABLE_CART,
                values,
                selection,
                selectionArgs
        );

        int rowsAffected = db.update(TABLE_CART, values, COLUMN_PRODUCT_ID + "=?", new String[]{productId});

        Log.d("UpdateQuantity" + newQuantity, "Rows affected: " + rowsAffected);

        db.close();
    }

    public void insertCartItem(int productId, String title, float rate, float mrp, String imageUrl, int stocks, String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_ID, productId);
        values.put(COLUMN_PRODUCT_NAME, title);
        values.put(COLUMN_PRODUCT_RATE, rate);
        values.put(COLUMN_PRODUCT_MRP, mrp);
        values.put(COLUMN_PRODUCT_IMAGE_URL, imageUrl);
        values.put(COLUMN_PRODUCT_STOCK, stocks);
        values.put(COLUMN_QUANTITY, quantity);

        try {
            db.insert(TABLE_CART, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.close();
    }

    public boolean isProductInCart(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_CART,
                new String[]{COLUMN_PRODUCT_ID},
                COLUMN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(productId)},
                null,
                null,
                null
        );

        boolean isInWishlist = cursor.getCount() > 0;
        cursor.close();
        return isInWishlist;
    }

    public ArrayList<CartItemModel> getCartItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_RATE,
                COLUMN_PRODUCT_MRP,
                COLUMN_PRODUCT_IMAGE_URL,
                COLUMN_QUANTITY,
                COLUMN_PRODUCT_STOCK
        };

        String selection = COLUMN_PRODUCT_ID + " > 0"; // Filter products with quantity greater than 0

        Cursor cursor = db.query(
                TABLE_CART,
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        ArrayList<CartItemModel> cartItems = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String productId = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID));
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
                @SuppressLint("Range") int productRate = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_RATE));
                @SuppressLint("Range") int productMrp = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_MRP));
                @SuppressLint("Range") String productImageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE_URL));
                @SuppressLint("Range") String stocks = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_STOCK));
                @SuppressLint("Range") int productQuantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY));

                // Create a CartItemModel object and add it to the list
                CartItemModel cartItem = new CartItemModel(productId, productName, productRate, productMrp, productImageUrl, stocks, productQuantity);
                cartItems.add(cartItem);
            }


            cursor.close();
        }

        return cartItems;
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
        db.close();
    }


    public void clearProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the cart table
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade the database if needed
        // Typically, you would migrate data from the old version to the new version here
    }
}
