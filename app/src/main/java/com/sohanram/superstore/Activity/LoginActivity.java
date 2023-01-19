package com.sohanram.superstore.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.sohanram.superstore.R;
import com.sohanram.superstore.SharedPreference.Rajput;

public class LoginActivity extends BaseActivity {

    EditText edtMobileNumber;
    Button btnProcees;
    Rajput app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setToolbar(this, "Login");
        app = (Rajput) getApplicationContext();

        edtMobileNumber = findViewById(R.id.edt_mobile_number);
        btnProcees = findViewById(R.id.btn_proceed);

        btnProcees.setOnClickListener(v -> {

            if (isNetworkAvailable()) {
                String mobileNumber = edtMobileNumber.getText().toString();
                if (mobileNumber.equals("")) {
                    showToast("Please enter mobile number");
                } else if (mobileNumber.length() < 10) {
                    showToast("Please enter valid mobile number");
                } else {

                    Intent intent = new Intent(this, OTPScreenActivity.class);
                    intent.putExtra("MOBILE_NUMBER", mobileNumber);
                    someActivityResultLauncher.launch(intent);

                }
            }


        });

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 100) {
                    // There are no request codes
                    Intent data = result.getData();
                    if (data.getStringExtra("OTP_STATUS").equals("true")) {
                        finish();
                    }

                }
            });
}