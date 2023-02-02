package com.sohanram.superstore.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.sohanram.superstore.Model.OTPResponseModel;
import com.sohanram.superstore.R;
import com.sohanram.superstore.Retrofit.APIServiceClass;
import com.sohanram.superstore.Retrofit.ResultHandler;
import com.sohanram.superstore.SharedPreference.Rajput;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

import static com.sohanram.superstore.Classes.Constants.GLOBAL_KEY;

public class OTPScreenActivity extends BaseActivity {

    TextView tvMobileNumber;
    String OTP_CODE;
    Rajput app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_screen);
        setToolbar(this, "Verify OTP");

        app = (Rajput) getApplicationContext();

        tvMobileNumber = findViewById(R.id.tv_mobile_number);

        if (getIntent().getExtras() != null) {
            String mobileNumber = getIntent().getStringExtra("MOBILE_NUMBER");
            tvMobileNumber.setText(mobileNumber);
        }

        OtpTextView otpTextView;
        otpTextView = findViewById(R.id.otp_view);
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                if (OTP_CODE.equals(otp)) {
                    app.user.isLogin = true;
                    app.user.mobileNumber = tvMobileNumber.getText().toString();
                    app.user.update(OTPScreenActivity.this);
                    showToast("Login Successfully.");
                    Intent intent = new Intent(OTPScreenActivity.this, LoginActivity.class);
                    intent.putExtra("OTP_STATUS", "true");
                    setResult(100,intent);
                    finish();

                } else {
                    showToast("OTP not matched!");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
       getOTPRequest(tvMobileNumber.getText().toString());
    }

    private void getOTPRequest(String mobileNumber) {
        if (isNetworkAvailable()) {
            APIServiceClass.getInstance().getOTPRequest(GLOBAL_KEY, mobileNumber, new ResultHandler<OTPResponseModel>() {
                @Override
                public void onSuccess(OTPResponseModel data) {
                    OTP_CODE = data.getOtp();
                    showToast(OTP_CODE);
                    Log.e(TAG, "onSuccess: OTP "+data.getOtp() );

                }

                @Override
                public void onFailure(String message) {
                    Log.e(TAG, "onFailure: " + message);
                }
            });
        }
    }
}