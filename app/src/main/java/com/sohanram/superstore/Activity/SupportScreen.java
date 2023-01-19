package com.sohanram.superstore.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.sohanram.superstore.Model.ContactModel;
import com.sohanram.superstore.R;
import com.sohanram.superstore.Retrofit.APIServiceClass;
import com.sohanram.superstore.Retrofit.ResultHandler;
import com.sohanram.superstore.databinding.ActivitySupportScreenBinding;

import static com.sohanram.superstore.Classes.Constants.GLOBAL_KEY;

public class SupportScreen extends BaseActivity {

    ActivitySupportScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_support_screen);
        setToolbar(this, "Contact Support");
        getContactSupport();
        binding.tvStoreMobileNumber.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            String mobileNumber = "tel:" + binding.tvStoreMobileNumber.getText().toString();
            intent.setData(Uri.parse(mobileNumber));
            startActivity(intent);

        });
    }

    private void getContactSupport() {
        APIServiceClass.getInstance().getContactSupport(GLOBAL_KEY, new ResultHandler<ContactModel>() {
            @Override
            public void onSuccess(ContactModel data) {
                Log.e("TAG", "onSuccess: " + data);
                if (data != null) {
                    binding.tvStoreName.setText(data.getName());
                    binding.tvStoreMobileNumber.setText(data.getMobile());
                } else {
                    showToast("No Data");
                }
            }

            @Override
            public void onFailure(String message) {
                Log.e("TAG", "onFailure: " + message);
            }
        });
    }

}