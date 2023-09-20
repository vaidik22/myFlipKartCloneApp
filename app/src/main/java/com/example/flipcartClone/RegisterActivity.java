package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flicpcartClone.R;

public class RegisterActivity extends Activity {
    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextNumber;
    private EditText editTextPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper = new DatabaseHelper(this);
        editTextName = findViewById(R.id.editTextName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(v -> {
            if (isInputValid()) {
                String name = editTextName.getText().toString().trim();
                String username = editTextUsername.getText().toString().trim();
                String phoneNumber = editTextNumber.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (isPhoneNumberRegistered(dbHelper.getWritableDatabase(), phoneNumber)) {
                    Toast.makeText(this, "Phone number already registered", Toast.LENGTH_SHORT).show();
                } else {
                    insertUser(name, username, phoneNumber, password);
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button buttonLogin = findViewById(R.id.buttonLogin2);
        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);

        });

    }

    private boolean isInputValid() {
        String name = editTextName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String mobileNumber = editTextNumber.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String alphabetSpacePattern = "^[a-zA-Z\\s]+$";
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return false;
        } else if (!name.matches(alphabetSpacePattern)) {
            editTextName.setError("Invalid name. Please use only alphabets and spaces.");
            editTextName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Username is required");
            editTextUsername.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(mobileNumber)) {
            editTextNumber.setError("Phone Number is required");
            editTextNumber.requestFocus();
            return false;
        } else if (mobileNumber.length() != 10) {
            editTextNumber.setError("Mobile number must be 10 digits");
            editTextNumber.requestFocus();
            return false;
        } else if (Integer.parseInt(String.valueOf(mobileNumber.charAt(0))) <= 5) {
            editTextNumber.setError("Mobile number must not start with a digit less than 6");
            editTextNumber.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void insertUser(String name, String username, String phoneNumber, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Check if the phone number is already registered
        if (isPhoneNumberRegistered(db, phoneNumber)) {
            Toast.makeText(this, "Phone number already registered", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID, phoneNumber);
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        db.insert(DatabaseHelper.TABLE_USERS, null, values);

        db.close();

        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean isPhoneNumberRegistered(SQLiteDatabase db, String phoneNumber) {
        String[] projection = {DatabaseHelper.COLUMN_ID};
        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {phoneNumber};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean isRegistered = cursor.getCount() > 0;
        cursor.close();

        return isRegistered;
    }
}
