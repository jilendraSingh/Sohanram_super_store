package com.sohanram.superstore.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    public String mobileNumber;
    public boolean isLogin;
    public static final String PREFS = "OPTIMUM";

    public User(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        isLogin = prefs.getBoolean("isLogin", false);
        mobileNumber = prefs.getString("mobile", "");

    }

    public void update(Context activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mobile", mobileNumber);
        editor.putBoolean("isLogin", isLogin);
        editor.apply();
    }

    public void remove(Context activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mobile", "");
        editor.putBoolean("isLogin", false);
        editor.apply();
    }
}
