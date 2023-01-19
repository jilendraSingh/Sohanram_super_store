package com.sohanram.superstore.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sohanram.superstore.Adapters.AdapterMyOrders;
import com.sohanram.superstore.Model.MyOrderModel;
import com.sohanram.superstore.R;
import com.sohanram.superstore.Retrofit.APIServiceClass;
import com.sohanram.superstore.Retrofit.ResultHandler;
import com.sohanram.superstore.SharedPreference.Rajput;

import java.util.List;

import static com.sohanram.superstore.Classes.Constants.GLOBAL_KEY;

public class MyOrderActivity extends BaseActivity {

    RecyclerView recyclerView;
    AdapterMyOrders adapterMyOrders;
    Rajput app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        app = (Rajput) getApplicationContext();

        setToolbar(this, "My Order");
        adapterMyOrders = new AdapterMyOrders(this);
        recyclerView = findViewById(R.id.rv_my_order_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterMyOrders);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyOrderList();
    }

    private void getMyOrderList() {
        if (isNetworkAvailable()) {
            APIServiceClass.getInstance().getMyOrderList(GLOBAL_KEY, app.user.mobileNumber, new ResultHandler<List<MyOrderModel>>() {
                @Override
                public void onSuccess(List<MyOrderModel> data) {

                    Log.e(TAG, "onSuccess: " + data);
                    if (data != null && data.size() > 0) {
                        adapterMyOrders.addItems(data);
                    } else {
                        showToast("No Data Available.");
                    }
                }

                @Override
                public void onFailure(String message) {
                    showToast(message);
                }
            });
        }
    }
}