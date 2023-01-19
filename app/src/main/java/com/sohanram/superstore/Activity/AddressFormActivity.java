package com.sohanram.superstore.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.sohanram.superstore.R;
import com.sohanram.superstore.RoomDatabase.AddressEntity;
import com.sohanram.superstore.RoomDatabase.DatabaseClient;
import com.sohanram.superstore.SharedPreference.Rajput;
import com.sohanram.superstore.databinding.ActivityAddressFormBinding;

public class AddressFormActivity extends BaseActivity {

    ActivityAddressFormBinding binding;
    Rajput app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_form);
        app = (Rajput) getApplicationContext();

        if (getIntent().getExtras() != null) {
            setToolbar(this, "Update Address Detail");
            String id = getIntent().getStringExtra("id");
            String name = getIntent().getStringExtra("name");
            String mobile = getIntent().getStringExtra("mobile");
            String add1 = getIntent().getStringExtra("add1");
            String add2 = getIntent().getStringExtra("add2");
            String update = getIntent().getStringExtra("update");

            binding.edtStreet.setText(name);
            binding.edtCity.setText(mobile);
            binding.edtCountry.setText(add1);
            binding.edtPincode.setText(add2);
            if (update.equals("true")) {
                binding.btnPlaceOrder.setVisibility(View.GONE);
                binding.btnUpdate.setVisibility(View.VISIBLE);
            }

            binding.btnUpdate.setOnClickListener(v -> {

                String Name = binding.edtStreet.getText().toString().trim();
                String Mobile = binding.edtCity.getText().toString().trim();
                String Address1 = binding.edtCountry.getText().toString().trim();
                String Address2 = binding.edtPincode.getText().toString().trim();
                if (Name.equals("")) {
                    showToast("Please Enter name");
                } else if (Mobile.equals("")) {
                    showToast("Please Enter Mobile");
                } else if (Address1.equals("")) {
                    showToast("Please Enter Address");
                }
                android.os.AsyncTask.execute(() -> DatabaseClient.getInstance(AddressFormActivity.this).getAppDatabase().checkOutDao()
                        .updateAddress(Integer.parseInt(id), Name, Mobile, Address1, Address2));
                finish();
            });
        } else {
            setToolbar(this, "Add Address");
            binding.edtCity.setText(app.user.mobileNumber);
        }

        binding.btnPlaceOrder.setOnClickListener(v -> {
            String name = binding.edtStreet.getText().toString().trim();
            String mobile = binding.edtCity.getText().toString().trim();
            String address1 = binding.edtCountry.getText().toString().trim();
            String address2 = binding.edtPincode.getText().toString().trim();
            if (name.equals("")) {
                showToast("Please Enter name");
            } else if (mobile.equals("")) {
                showToast("Please Enter Mobile");
            } else if (address1.equals("")) {
                showToast("Please Enter Address");
            } else {
                AddressEntity addressEntity = new AddressEntity();
                addressEntity.setName(name);
                addressEntity.setMobile(mobile);
                addressEntity.setAddress1(address1);
                addressEntity.setAddress2(address2);
                android.os.AsyncTask.execute(() ->
                        DatabaseClient.getInstance(AddressFormActivity.this).getAppDatabase().checkOutDao().insertAddress(addressEntity));
                runOnUiThread(() -> showToast("Address submitted successfuly."));
                new Handler().postDelayed(this::finish, 100);
            }
        });
    }
}