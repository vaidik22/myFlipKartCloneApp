package com.example.flipcartClone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WishListDatabaseHelper extends SQLiteOpenHelper {

    // Database information
    private static final String DATABASE_NAME = "wishlist.db";
    private static final int DATABASE_VERSION = 8;
    private static final String TABLE_WISHLIST = "wishlist";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_RATE = "product_rate";
    private static final String COLUMN_PRODUCT_MRP = "product_mrp";
    private static final String COLUMN_PRODUCT_IMAGE_URL = "product_image_url";
    private static final String COLUMN_QUANTITY = "quantity";

    // Constructor
    public WishListDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the wishlist table
        String CREATE_WISHLIST_TABLE = "CREATE TABLE " + TABLE_WISHLIST + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PRODUCT_ID + " INTEGER,"
                + COLUMN_PRODUCT_NAME + " TEXT,"
                + COLUMN_PRODUCT_RATE + " REAL,"
                + COLUMN_PRODUCT_MRP + " REAL,"
                + COLUMN_PRODUCT_IMAGE_URL + " TEXT,"
                + COLUMN_QUANTITY + " INTEGER" +
                ")";
        db.execSQL(CREATE_WISHLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If you need to upgrade the database schema, do it here
    }

    public void deleteProduct(String productId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rowsAffected = db.delete(TABLE_WISHLIST, COLUMN_PRODUCT_ID + "=?", new String[]{productId});

        Log.d("DeleteProduct", "Rows affected: " + rowsAffected);

        db.close();
    }

    public boolean isProductInWishlist(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_WISHLIST,
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


    public void addToWishlist(int productId, String phoneNumber, String title, String rate, String mrp, String imageUrl, String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_ID, productId);
        values.put(COLUMN_PRODUCT_NAME, title);
        values.put(COLUMN_PRODUCT_RATE, rate);
        values.put(COLUMN_PRODUCT_MRP, mrp);
        values.put(COLUMN_PRODUCT_IMAGE_URL, imageUrl);
        values.put(COLUMN_QUANTITY, quantity);

        // Insert the row
        db.insert(TABLE_WISHLIST, null, values);
        db.close();
    }

    public void clearProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WISHLIST, null, null);
    }
}