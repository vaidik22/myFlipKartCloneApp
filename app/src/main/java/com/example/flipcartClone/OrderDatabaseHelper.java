package com.example.flipcartClone;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OrderDatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_PRODUCT_NAME = "product_name";
    public static final String COLUMN_PRODUCT_DESCRIPTION = "product_description";
    public static final String COLUMN_PRODUCT_IMAGE = "product_image";
    private static final String DATABASE_NAME = "order.db";
    private static final int DATABASE_VERSION = 5;
    // Add more columns for other order details like image, price, etc.

    public OrderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_ID + " TEXT, " +
                COLUMN_PRODUCT_NAME + " TEXT, " +
                COLUMN_PRODUCT_IMAGE + " TEXT, " +
                COLUMN_PRODUCT_DESCRIPTION + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }

    public void clearOrders() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public long insertOrder(String productId, String productName, String productImage, String orderDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_ID, productId);
        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_PRODUCT_IMAGE, productImage);
        values.put(COLUMN_PRODUCT_DESCRIPTION, orderDate);
        // Add more values for other order details like image, price, etc.

        long newRowId = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return newRowId;
    }
}
