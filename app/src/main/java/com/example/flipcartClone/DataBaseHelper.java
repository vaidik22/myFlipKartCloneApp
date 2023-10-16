package com.example.flipcartClone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {
    static final String TABLE_USERS = "users";
    static final String COLUMN_ID = "phone_number";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_IMAGE = "image";
    static final String COLUMN_DESCRIPTION = "description";
    static final String COLUMN_IMAGE_URL = "image_url";
    static final String COLUMN_USERNAME = "username";
    static final String COLUMN_PASSWORD = "password";
    private static final String DATABASE_NAME = "myapp.db";
    private static final int DATABASE_VERSION = 66;
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMAGE + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getProducts() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_NAME,
                COLUMN_IMAGE_URL, COLUMN_DESCRIPTION
        };

        Cursor cursor = db.query(
                TABLE_USERS,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        return cursor;
    }

    public Cursor getUsernameAndName(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {COLUMN_USERNAME, COLUMN_NAME, COLUMN_IMAGE};
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {userId};

        Cursor cursor = db.query(
                TABLE_USERS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return cursor;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void updateUserInfo(String userId, String newName, String newUsername, String newImageData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newName);
        values.put(COLUMN_USERNAME, newUsername);
        values.put(COLUMN_IMAGE, newImageData);
        String selection = COLUMN_ID + "= ?";
        String[] selectionArgs = {userId};

        db.update(
                TABLE_USERS,
                values,
                selection,
                selectionArgs
        );
    }
}
