package com.sohanram.superstore.Activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sohanram.superstore.R;

public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    public void setToolbar(Activity activity, String title) {
        ImageView backImage = activity.findViewById(R.id.iv_back_image);
        if (title.equals("Order Status")) {
            backImage.setVisibility(View.INVISIBLE);
        } else if (title.equals("Payment")) {
            backImage.setVisibility(View.INVISIBLE);
        }


        TextView tvTitle = activity.findViewById(R.id.tv_screen_title);
        tvTitle.setText(title);
        backImage.setOnClickListener(v -> finish());
    }


    public boolean isNetworkAvailableForMain() {
        boolean isNetworkAvailable;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            isNetworkAvailable = true;
        } else {
            isNetworkAvailable = false;
        }
        return isNetworkAvailable;
    }

    public boolean isNetworkAvailable() {
        boolean isNetworkAvailable;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            isNetworkAvailable = true;
        } else {
            isNetworkAvailable = false;
            showAlertDialog("Internet is not Available");
        }
        return isNetworkAvailable;
    }

    public void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_layout, null);
        TextView tvMessage = customLayout.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        Button ok = customLayout.findViewById(R.id.btn_ok);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.show();
        ok.setOnClickListener(v -> {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public static boolean isValid(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }

}