package com.example.flipcartClone;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String COLUMN_ID = "phoneNumber";

    private final SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean loginUser(String phoneNumber) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COLUMN_ID, phoneNumber);
        editor.apply();
        return false;
    }

    public boolean isLoggedIn() {
        return sharedPreferences.contains(COLUMN_ID);
    }

    public void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(COLUMN_ID);
        editor.apply();
    }

    public String getPhoneNumber() {
        return sharedPreferences.getString(COLUMN_ID, null);
    }
}
