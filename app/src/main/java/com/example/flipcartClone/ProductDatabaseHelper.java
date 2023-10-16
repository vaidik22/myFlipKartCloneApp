package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

class ProductDatabaseHelper extends SQLiteOpenHelper {
    static final String TABLE_PRODUCTS = "products";
    static final String COLUMN_PRODUCT_ID = "product_id";
    static final String COLUMN_PRODUCT_NAME = "product_name";
    static final String COLUMN_PRODUCT_DESCRIPTION = "product_description";
    static final String COLUMN_PRODUCT_RATE = "product_rate";
    static final String COLUMN_PRODUCT_MRP = "product_mrp";
    static final String COLUMN_PRODUCT_IMAGE = "product_image";
    static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";
    static final String COLUMN_PRODUCT_STOCK = "product_stocks";
    private static final String DATABASE_NAME = "product.db";
    private static final int DATABASE_VERSION = 74;
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_NAME + " TEXT, " +
                    COLUMN_PRODUCT_DESCRIPTION + " TEXT, " +
                    COLUMN_PRODUCT_RATE + " REAL, " +
                    COLUMN_PRODUCT_MRP + " REAL, " +
                    COLUMN_PRODUCT_IMAGE + " TEXT, " +
                    COLUMN_PRODUCT_QUANTITY + " INTEGER DEFAULT 0, " +
                    COLUMN_PRODUCT_STOCK + " INTEGER DEFAULT 0)";


    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public void clearProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, null, null);
        db.close();
    }

    public long insertProduct(String productName, String description, String productRate, String productMrp, String imageUrl, String quantity, int stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_PRODUCT_DESCRIPTION, description);
        values.put(COLUMN_PRODUCT_RATE, productRate);
        values.put(COLUMN_PRODUCT_MRP, productMrp);
        values.put(COLUMN_PRODUCT_IMAGE, imageUrl);
        values.put(COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(COLUMN_PRODUCT_STOCK, stock);
        return db.insert(TABLE_PRODUCTS, null, values);
    }
    public void updateAllProductQuantitiesToZero() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_QUANTITY, 0); // Set quantity to 0 for all products

        int rowsAffected = db.update(
                TABLE_PRODUCTS,
                values,
                null,
                null
        );

        Log.d("UpdateAllQuantities", "Rows affected: " + rowsAffected);

        db.close();
    }

    public void updateProductStock(String productId, int newStock) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_STOCK, newStock);

        String selection = COLUMN_PRODUCT_ID + "=?";
        String[] selectionArgs = {productId};

        db.update(TABLE_PRODUCTS, values, selection, selectionArgs);
        db.close();
    }

    @SuppressLint("Range")
    public int getProductStock(String productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int stock = 0;

        String[] projection = {COLUMN_PRODUCT_STOCK};
        String selection = COLUMN_PRODUCT_ID + "=?";
        String[] selectionArgs = {productId};

        Cursor cursor = db.query(
                TABLE_PRODUCTS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            stock = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_STOCK));
            cursor.close();
        }

        db.close();

        return stock;
    }


    public Cursor getProducts() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_PRODUCT_ID, COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_DESCRIPTION, COLUMN_PRODUCT_RATE, COLUMN_PRODUCT_MRP, COLUMN_PRODUCT_IMAGE, COLUMN_PRODUCT_QUANTITY, COLUMN_PRODUCT_STOCK
        };

        Cursor cursor = db.query(
                TABLE_PRODUCTS,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        return cursor;
    }

    public ArrayList<SubCategoryModel> getProductItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_DESCRIPTION,
                COLUMN_PRODUCT_RATE,
                COLUMN_PRODUCT_MRP,
                COLUMN_PRODUCT_IMAGE,
                COLUMN_PRODUCT_QUANTITY,
                COLUMN_PRODUCT_STOCK // Include stock column
        };

        String selection = COLUMN_PRODUCT_ID + " > 0";

        Cursor cursor = db.query(
                TABLE_PRODUCTS,
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        ArrayList<SubCategoryModel> productList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String productId = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_DESCRIPTION));
                @SuppressLint("Range") String rate = String.valueOf(cursor.getFloat(cursor.getColumnIndex(COLUMN_PRODUCT_RATE)));
                @SuppressLint("Range") String mrp = String.valueOf(cursor.getFloat(cursor.getColumnIndex(COLUMN_PRODUCT_MRP)));
                @SuppressLint("Range") String imageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE));
                @SuppressLint("Range") String quantity = String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY)));
                @SuppressLint("Range") int stock = cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUCT_STOCK)); // Get stock value

                // Create a SubCategoryModel object and add it to the list
                SubCategoryModel productItem = new SubCategoryModel(productId, title, description, rate, mrp, imageUrl, quantity, stock);
                productList.add(productItem);
            }
            cursor.close();
        }

        return productList;
    }


    public void updateProductQuantity(String productId, int newQuantity) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_QUANTITY, newQuantity);
        String selection = COLUMN_PRODUCT_ID + "= ?";
        String[] selectionArgs = {productId};
        db.update(
                TABLE_PRODUCTS,
                values,
                selection,
                selectionArgs
        );
        int rowsAffected = db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + "=?", new String[]{productId});
        Log.d("UpdateQuantity" + newQuantity, "Rows affected: " + rowsAffected);
        db.close();
    }


}