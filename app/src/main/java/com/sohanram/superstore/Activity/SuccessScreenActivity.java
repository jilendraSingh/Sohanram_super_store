package com.sohanram.superstore.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.sohanram.superstore.R;
import com.sohanram.superstore.databinding.ActivitySuccessScreenBinding;

public class SuccessScreenActivity extends BaseActivity {

    ActivitySuccessScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_success_screen);
        setToolbar(this, "Order Status");
        binding.btnStatus.setOnClickListener(v -> {
            startActivity(new Intent(SuccessScreenActivity.this, MainActivity.class));
            finish();
        });
        binding.btnPhonePay.setOnClickListener(v -> {
            String url = "https://play.google.com/store/apps/details?id=com.phonepe.app&hl=en_IN&gl=US";
            openOtherApp(url);
        });

        binding.btnPaytm.setOnClickListener(v -> {
            String url = "https://play.google.com/store/apps/details?id=net.one97.paytm&hl=en_IN&gl=US";
            openOtherApp(url);
        });
        binding.btnGooglePay.setOnClickListener(v -> {
            String url = "https://play.google.com/store/apps/details?id=com.google.android.apps.nbu.paisa.user&hl=en_IN&gl=US";
            openOtherApp(url);
        });
    }

    public void openOtherApp(String appName) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(appName));
        startActivity(i);
    }
}