package com.sohanram.superstore.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.sohanram.superstore.R;
import com.sohanram.superstore.databinding.ActivityPaymentBinding;

import java.net.URLEncoder;

public class PaymentActivity extends BaseActivity {

    ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        setToolbar(this, "Payment");

        if (getIntent().getExtras() != null) {
            String cashOption = getIntent().getStringExtra("CASH_HIDE");
            if (cashOption.equals("true")) {
                binding.btnCashOnDelivery.setVisibility(View.GONE);
            }

        }

        binding.btnGooglePay.setOnClickListener(v -> {
            String url = "https://play.google.com/store/apps/details?id=com.google.android.apps.nbu.paisa.user&hl=en_IN&gl=US";
            openOtherApp(url);
        });
        binding.btnPhonepay.setOnClickListener(v -> {
            String url = "https://phon.pe/ru_prafnhor6";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        binding.btnPaytm.setOnClickListener(v -> {
            String url = "http://m.p-y.tm";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        binding.btnCashOnDelivery.setOnClickListener(v -> startActivity(new Intent(PaymentActivity.this, SuccessScreenActivity.class)));
        binding.btnUpi.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("UPI Copied.", binding.btnUpi.getText());
            clipboard.setPrimaryClip(clip);
            showToast("UPI Copied.");
            /*Intent intent = new Intent(PaymentActivity.this, UPIPaymentActivity.class);
            intent.putExtra("UPI_ID", binding.btnUpi.getText().toString());
            startActivity(intent);*/
        });
        binding.btnMobileNumber.setOnClickListener(v -> {
            openWhatsApp();

        });
    }

    private void openWhatsApp() {
        String smsNumber = null;
        try {
            smsNumber = "+91 " + binding.btnMobileNumber.getText().toString() + "&text=" + URLEncoder.encode("Hi", "UTF-8"); // E164 format without '+' sign
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + smsNumber);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi");
        startActivity(sendIntent);

       /* PackageManager packageManager = getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone=" + "+91"+binding.btnMobileNumber.getText().toString() + "&text=" + URLEncoder.encode("Hi", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void openOtherApp(String appName) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(appName));
        startActivity(i);
    }
}