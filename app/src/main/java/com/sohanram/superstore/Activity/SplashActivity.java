
package com.sohanram.superstore.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sohanram.superstore.R;
import com.sohanram.superstore.SharedPreference.Rajput;

public class SplashActivity extends BaseActivity {

    Rajput app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        app = (Rajput) getApplicationContext();
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 2000);
    }
}