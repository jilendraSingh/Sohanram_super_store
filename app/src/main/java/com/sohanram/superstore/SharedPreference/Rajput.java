package com.sohanram.superstore.SharedPreference;

import android.app.Application;

public class Rajput extends Application {
   public User user;

    @Override
    public void onCreate() {
        super.onCreate();
        user=new User(getApplicationContext());
    }
}
