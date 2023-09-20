package com.example.flipcartClone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flicpcartClone.R;

public class LoginActivity extends Activity {
    SessionManager sessionManager;
    private EditText editTextPhoneNumber;
    private EditText editTextPassword;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        editTextPhoneNumber = findViewById(R.id.phone_number);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(v -> {
            if (validateInput()) {
                if (loginUser()) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // Close the login activity
                }
            }
        });
        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInput() {
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            editTextPhoneNumber.setError("PhoneNumber is required");
            editTextPhoneNumber.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean loginUser() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        String[] projection = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_PASSWORD};
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

        boolean loginSuccessful = false;

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String storedPassword = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD));
            if (password.equals(storedPassword)) {
                sessionManager.loginUser(phoneNumber);
                loginSuccessful = true;
            } else {
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Phone number does not exist", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
        return loginSuccessful;
    }

}
