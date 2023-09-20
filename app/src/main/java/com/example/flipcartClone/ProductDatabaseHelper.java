package com.example.flipcartClone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class ProductDatabaseHelper extends SQLiteOpenHelper {
    static final String TABLE_PRODUCTS = "products";
    static final String COLUMN_PRODUCT_ID = "product_id";
    static final String COLUMN_PRODUCT_NAME = "product_name";
    static final String COLUMN_PRODUCT_DESCRIPTION = "product_description";
    static final String COLUMN_PRODUCT_RATE = "product_rate";
    static final String COLUMN_PRODUCT_MRP = "product_mrp";
    static final String COLUMN_PRODUCT_IMAGE = "product_image";
    static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";
    static final String COLUMN_CART_QUANTITY = "cart_quantity";
    private static final String DATABASE_NAME = "myapp.db";
    private static final int DATABASE_VERSION = 17;
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_NAME + " TEXT, " +
                    COLUMN_PRODUCT_DESCRIPTION + " TEXT, " +
                    COLUMN_PRODUCT_RATE + " REAL, " +
                    COLUMN_PRODUCT_MRP + " REAL, " +
                    COLUMN_PRODUCT_IMAGE + " TEXT, " +
                    COLUMN_CART_QUANTITY + " TEXT, " +
                    COLUMN_PRODUCT_QUANTITY + " INTEGER DEFAULT 0)";

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

    public long insertProduct(String productName, String description, String productRate, String productMrp, String imageUrl, String quantity, String cart_quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_PRODUCT_DESCRIPTION, description);
        values.put(COLUMN_PRODUCT_RATE, productRate);
        values.put(COLUMN_PRODUCT_MRP, productMrp);
        values.put(COLUMN_PRODUCT_IMAGE, imageUrl);
        values.put(COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(COLUMN_CART_QUANTITY, cart_quantity);
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    public void clearProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, null, null);
    }

    public Cursor getProducts() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_PRODUCT_ID, COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_DESCRIPTION, COLUMN_PRODUCT_RATE, COLUMN_PRODUCT_MRP, COLUMN_PRODUCT_IMAGE, COLUMN_PRODUCT_QUANTITY
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