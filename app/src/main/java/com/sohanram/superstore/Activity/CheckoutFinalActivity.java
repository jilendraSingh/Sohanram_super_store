package com.sohanram.superstore.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.sohanram.superstore.Adapters.CheckoutAdapter;
import com.sohanram.superstore.Classes.Constants;
import com.sohanram.superstore.Classes.URLConstants;
import com.sohanram.superstore.Interface.ItemClickListener;
import com.sohanram.superstore.R;
import com.sohanram.superstore.RoomDatabase.CheckoutEntity;
import com.sohanram.superstore.RoomDatabase.DatabaseClient;
import com.sohanram.superstore.SharedPreference.Rajput;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.sohanram.superstore.Classes.Constants.GLOBAL_KEY;
import static com.sohanram.superstore.Classes.Constants.PROPER_ADDRESS;
import static com.sohanram.superstore.Classes.Constants.SELECTED_NAME;

public class CheckoutFinalActivity extends BaseActivity implements ItemClickListener {

    public String totalEffectiveCost;
    RecyclerView rvCheckoutRecyclerView;
    ArrayList<CheckoutEntity> totalCheckOutList;
    CheckoutAdapter checkoutAdapter;
    TextView tvTotalCost;
    LinearLayout rlViewDetailLayout;
    TextView tvSheetDelivery, tvSheetTotal, tvViewDetailTextView;
    String deliverCharges = "";
    LinearLayout llViewDetailsLayout;
    TextView tvChangeAddress, tvAddress, tvNoAddress;
    Button btnPayment;
    Rajput app;
    static float totalCost, saveMoney;
    List<CheckoutEntity> TotalSelectedItemList = new ArrayList<>();
    Button btnClearITems;
    TextView tvSaveMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_final);
        app = (Rajput) getApplicationContext();
        setToolbar(this, "Checkout");
        tvTotalCost = findViewById(R.id.tv_total_cost);
        tvSaveMoney = findViewById(R.id.tv_save_money);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tvSaveMoney.startAnimation(anim);

        tvSheetDelivery = findViewById(R.id.tv_sheet_delivery);
        tvSheetTotal = findViewById(R.id.tv_sheet_total);
        tvViewDetailTextView = findViewById(R.id.tv_view_detail);
        tvChangeAddress = findViewById(R.id.tv_change_address);

        tvAddress = findViewById(R.id.tv_address);
        tvNoAddress = findViewById(R.id.tv_no_address);

        rlViewDetailLayout = findViewById(R.id.ll_view_break_up_detail);
        llViewDetailsLayout = findViewById(R.id.ll_view_details);

        rvCheckoutRecyclerView = findViewById(R.id.rv_checkout_recyclerview);
        rvCheckoutRecyclerView.setNestedScrollingEnabled(false);
        rvCheckoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        checkoutAdapter = new CheckoutAdapter(this,this, this::onClick);
        rvCheckoutRecyclerView.setAdapter(checkoutAdapter);

        btnPayment = findViewById(R.id.btn_payment);


        if (getIntent().getExtras() != null) {
            totalCheckOutList = (ArrayList<CheckoutEntity>) getIntent().getSerializableExtra("CHECKOUT_LIST");
            totalEffectiveCost = getIntent().getStringExtra("TOTAL_COST");
        }
        refreshCheckoutList(null);

        rlViewDetailLayout.setOnClickListener(v -> showViewDetails());
        tvChangeAddress.setOnClickListener(v ->
                {
                    if (app.user.isLogin) {
                        startActivity(new Intent(CheckoutFinalActivity.this, AddressActivityList.class));
                    } else {
                        showLoginDialog();
                    }
                }
        );

        btnPayment.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                if (app.user.isLogin) {
                    if (Constants.SELECTED_ADDRESS.equals("")) {
                        showToast("Please select address");
                    } else {
                        JSONArray jsonArray = new JSONArray();
                        float qty = 0;
                        for (int i = 0; i < totalCheckOutList.size(); i++) {
                            qty += Float.parseFloat(totalCheckOutList.get(i).getItemCount());
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("itemCode", totalCheckOutList.get(i).getItemId());
                                jsonObject.put("rate", Float.parseFloat(totalCheckOutList.get(i).getItemPrice()));
                                jsonObject.put("saleRate", Float.parseFloat(totalCheckOutList.get(i).getItemPrice()));
                                jsonObject.put("qty", Float.parseFloat(totalCheckOutList.get(i).getItemCount()));
                                jsonObject.put("amount", Float.parseFloat(totalCheckOutList.get(i).getTotalCost()));
                                jsonArray.put(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // hit Final api
                        sendOrderRequest(totalEffectiveCost, 0, Float.parseFloat(tvTotalCost.getText().toString()), qty,
                                deliverCharges, SELECTED_NAME, app.user.mobileNumber, PROPER_ADDRESS, "", GLOBAL_KEY, jsonArray
                        );
                    }
                } else {
                    showLoginDialog();
                }
            }

        });
        getDeliveryChrges();
        btnClearITems = findViewById(R.id.btn_clear_tems);
        btnClearITems.setVisibility(View.VISIBLE);
        btnClearITems.setOnClickListener(v -> android.os.AsyncTask.execute(() -> android.os.AsyncTask.execute(() -> {
            DatabaseClient.getInstance(CheckoutFinalActivity.this).getAppDatabase().checkOutDao().deleteCheckoutData();
            runOnUiThread(this::finish);
        })));
    }


    private void showLoginDialog() {
        new AlertDialog.Builder(CheckoutFinalActivity.this)
                .setTitle("Login")
                .setMessage("Please Login to continue.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(CheckoutFinalActivity.this, LoginActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void getDeliveryChrges() {
        if (isNetworkAvailable()) {
            String url = "http://103.94.27.34:1021/api/AndroidGetDeliveryCharges?merchantRefID=" + GLOBAL_KEY + "&" + "totalAmount=" + totalEffectiveCost;
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    deliverCharges = response;
                    if (Float.parseFloat(deliverCharges) > 0) {
                        tvTotalCost.setText(String.valueOf(Float.parseFloat(deliverCharges) + Float.parseFloat(totalEffectiveCost)));
                    }

                }
            }, error -> Log.e(TAG, "onResponse: " + error));
            requestQueue.add(stringRequest);

        }

    }

    private void showViewDetails() {

        if (llViewDetailsLayout.getVisibility() == View.GONE) {
            llViewDetailsLayout.setVisibility(View.VISIBLE);
            tvViewDetailTextView.setText("Hide Details");
        } else {
            llViewDetailsLayout.setVisibility(View.GONE);
            tvViewDetailTextView.setText("View Details");
        }

        if (deliverCharges != null) {
            if (Float.parseFloat(deliverCharges) > 0) {
                tvSheetDelivery.setText(deliverCharges);
            } else {
                tvSheetDelivery.setText("FREE");
            }
        }

        tvSheetTotal.setText(totalEffectiveCost);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Constants.SELECTED_ADDRESS.equals("")) {
            tvNoAddress.setVisibility(View.VISIBLE);
            tvAddress.setVisibility(View.GONE);
        } else {
            tvNoAddress.setVisibility(View.GONE);
            tvAddress.setVisibility(View.VISIBLE);

            if (Constants.SELECTED_ADDRESS.endsWith(","))
                Constants.SELECTED_ADDRESS = Constants.SELECTED_ADDRESS.substring(0, Constants.SELECTED_ADDRESS.length() - 1);
            tvAddress.setText(Constants.SELECTED_ADDRESS);
        }
    }

    private void sendOrderRequest(String totalEffectiveCost, int delivery, float total, float qty,
                                  String deliverCharges, String name, String mobile, String address1,
                                  String address2, String optimum, JSONArray totalCheckOutList) {

        if (isNetworkAvailable()) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("locationShortName", "Sohanram");
                jsonObject.put("customerName", name);
                jsonObject.put("customerMobile", mobile);
                jsonObject.put("customerAddress1", address1);
                jsonObject.put("customerAddress2", address2);
                jsonObject.put("registeredMobileNo", app.user.mobileNumber);
                jsonObject.put("subTotal", Float.parseFloat(totalEffectiveCost));
                jsonObject.put("delivery", delivery);
                jsonObject.put("total", total);
                jsonObject.put("qty", qty);
                jsonObject.put("deliveryCharge", Float.parseFloat(deliverCharges));
                jsonObject.put("checkoutDetails", totalCheckOutList);


                Log.e(TAG, "sendOrderRequest: " + jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = URLConstants.BASE_URL + URLConstants.URL_PUSH_ORDER_URL;
            Log.e("TAG", "sendOrderRequest: "+url );
            AndroidNetworking.post(url)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "onResponse: "+response );
                            clearCheckData();
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e(TAG, "onResponse: "+anError.getMessage() );
                        }
                    });

        }

    }

    private void clearCheckData() {
        android.os.AsyncTask.execute(() -> {
            DatabaseClient.getInstance(CheckoutFinalActivity.this).getAppDatabase().checkOutDao().deleteCheckoutData();
            runOnUiThread(() -> {
                Intent intent = new Intent(CheckoutFinalActivity.this, PaymentActivity.class);
                startActivity(intent);
                finish();
            });
        });
    }

    @Override
    public void onClick(int position, String data) {
        updatePrice(data);
    }

    private void updatePrice(String data) {
        TotalSelectedItemList.clear();
        totalCost = 0;
        saveMoney = 0;

        android.os.AsyncTask.execute(() -> {
            TotalSelectedItemList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().checkOutDao().getAllCheckOutData();
            runOnUiThread(() -> {
                if (TotalSelectedItemList.size() > 0) {
                    for (int i = 0; i < TotalSelectedItemList.size(); i++) {
                        totalCost += Float.parseFloat(TotalSelectedItemList.get(i).getTotalCost());
                        saveMoney += Float.parseFloat(TotalSelectedItemList.get(i).getSavingMoney()) * Float.parseFloat(TotalSelectedItemList.get(i).getItemCount());
                    }
                    tvTotalCost.setText(String.valueOf(Float.parseFloat(deliverCharges) + totalCost));
                    totalEffectiveCost = String.valueOf(totalCost);
                    tvSheetTotal.setText(String.valueOf(totalCost));
                    tvSaveMoney.setText(new DecimalFormat("0.00").format(saveMoney));
                } else {
                    tvTotalCost.setText(String.valueOf(Float.parseFloat(deliverCharges) + totalCost));
                    tvSheetTotal.setText(String.valueOf(totalCost));
                    totalEffectiveCost = String.valueOf(totalCost);
                }
                if (data.equals("DIALOG_QUANTITY"))
                    refreshCheckoutList(TotalSelectedItemList);
            });
        });
    }

    public void refreshCheckoutList(List<CheckoutEntity> totalSelectedItemList) {
        if (totalSelectedItemList != null && totalSelectedItemList.size() > 0) {
            totalCheckOutList.clear();
            totalCheckOutList.addAll(totalSelectedItemList);
            if (totalCheckOutList != null) {
                checkoutAdapter.addItems(totalCheckOutList);
            }

        } else {
            saveMoney = 0;
            if (totalCheckOutList != null) {
                checkoutAdapter.addItems(totalCheckOutList);
            }

            for (int i = 0; i < totalCheckOutList.size(); i++) {
                saveMoney += Float.parseFloat(totalCheckOutList.get(i).getSavingMoney()) * Float.parseFloat(totalCheckOutList.get(i).getItemCount());
            }

            tvTotalCost.setText(totalEffectiveCost);
            tvSaveMoney.setText(new DecimalFormat("0.00").format(saveMoney));
        }

    }
}