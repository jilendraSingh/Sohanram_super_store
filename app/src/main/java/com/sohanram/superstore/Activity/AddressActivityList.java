package com.sohanram.superstore.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sohanram.superstore.Adapters.AddressAdapter;
import com.sohanram.superstore.Interface.AddressItemSelectListener;
import com.sohanram.superstore.R;
import com.sohanram.superstore.RoomDatabase.AddressEntity;
import com.sohanram.superstore.RoomDatabase.DatabaseClient;

import java.util.List;

import static com.sohanram.superstore.Classes.Constants.PROPER_ADDRESS;
import static com.sohanram.superstore.Classes.Constants.SELECTED_ADDRESS;
import static com.sohanram.superstore.Classes.Constants.SELECTED_NAME;

public class AddressActivityList extends BaseActivity implements AddressItemSelectListener {

    RecyclerView rvAddressRecyclerView;
    Button btnAddAddress, btnOk;
    AddressAdapter addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        setToolbar(this, "Select Address");
        btnOk = findViewById(R.id.btn_ok);
        rvAddressRecyclerView = findViewById(R.id.rv_address_recycler_view);
        rvAddressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addressAdapter = new AddressAdapter(this, this);
        rvAddressRecyclerView.setAdapter(addressAdapter);
        btnAddAddress = findViewById(R.id.btn_add_address);
        btnAddAddress.setOnClickListener(v -> startActivity(new Intent(AddressActivityList.this, AddressFormActivity.class)));

        btnOk.setOnClickListener(v -> finish());
        getAddressList();
    }

    private void getAddressList() {
        android.os.AsyncTask.execute(() -> {
            List<AddressEntity> list = DatabaseClient.getInstance(AddressActivityList.this).getAppDatabase().checkOutDao().getAllAddress();
            if (list.size() > 0) {
                runOnUiThread(() -> addressAdapter.addItems(list));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getAddressList();
    }

    @Override
    public void onSelectAddress(int position, String address, String onlyAddress, String name, String mobileNumber) {
        SELECTED_ADDRESS = address;
        SELECTED_NAME = name;
        PROPER_ADDRESS = onlyAddress;
    }
}