package com.sohanram.superstore.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.sohanram.superstore.Adapters.AdapterHeadDishItems;
import com.sohanram.superstore.Adapters.AdaptorHeadDishListAdapter;
import com.sohanram.superstore.Classes.URLConstants;
import com.sohanram.superstore.Interface.CheckOutListener;
import com.sohanram.superstore.Interface.ItemClickListener;
import com.sohanram.superstore.Model.DishItemModel;
import com.sohanram.superstore.Model.DishListModel;
import com.sohanram.superstore.R;
import com.sohanram.superstore.Retrofit.APIServiceClass;
import com.sohanram.superstore.Retrofit.ResultHandler;
import com.sohanram.superstore.RoomDatabase.CheckoutEntity;
import com.sohanram.superstore.RoomDatabase.DatabaseClient;
import com.sohanram.superstore.SharedPreference.Rajput;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.sohanram.superstore.Classes.Constants.GLOBAL_KEY;


public class MainActivity extends BaseActivity implements ItemClickListener, CheckOutListener {

    public static float totalItemCount = 0;
    public static float totalEffectiveCost = 0;
    AdaptorHeadDishListAdapter listAdapter;
    AdapterHeadDishItems headDishItemsAdapter;
    RecyclerView rvDishRecyclerView, rvDishItemRecyclerView;
    EditText edtMenuItem;
    RelativeLayout rlCheckoutLayout;
    TextView tvNumOfItem, tvTotalCost, btnCheckout;
    List<CheckoutEntity> TotalSelectedItemList = new ArrayList<>();
    ArrayList<CheckoutEntity> totalCheckOutList = new ArrayList<>();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Rajput app;
    List<String> menuCodeList = new ArrayList<>();
    ShimmerFrameLayout itemShimmerLayout, dishShimmerLayout;
    String dishHeadCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (Rajput) getApplicationContext();

        itemShimmerLayout = findViewById(R.id.item_shimmer_layout);
        dishShimmerLayout = findViewById(R.id.dish_shimmer_layout);

        tvNumOfItem = findViewById(R.id.tv_no_of_item);
        tvTotalCost = findViewById(R.id.tv_total_cost);
        btnCheckout = findViewById(R.id.btn_checkout);

        edtMenuItem = findViewById(R.id.edt_menu_list);
        rlCheckoutLayout = findViewById(R.id.rl_checkout_layout);

        setUPDishList();
        setUPDishItemList();
        getDishList();
        new Handler().postDelayed(this::getLocalDBData, 1000);


        edtMenuItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                headDishItemsAdapter.getFilter().filter(editable.toString().trim());
            }
        });

        btnCheckout.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                totalCheckOutList.clear();
                totalCheckOutList.addAll(TotalSelectedItemList);
                Intent intent = new Intent(MainActivity.this, CheckoutFinalActivity.class);
                intent.putExtra("TOTAL_COUNT", String.valueOf(totalItemCount));
                intent.putExtra("TOTAL_COST", String.valueOf(totalEffectiveCost));
                intent.putExtra("CHECKOUT_LIST", totalCheckOutList);
                startActivity(intent);
            }
        });
    }

    private void getLocalDBData() {
        TotalSelectedItemList.clear();
        android.os.AsyncTask.execute(() -> {
            TotalSelectedItemList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().checkOutDao().getAllCheckOutData();
            runOnUiThread(() -> {
                if (TotalSelectedItemList.size() > 0) {
                    ArrayList<String> dishMenuCodeArray = new ArrayList<>();
                    for (int i = 0; i < TotalSelectedItemList.size(); i++) {
                        dishMenuCodeArray.add(TotalSelectedItemList.get(i).getItemId());
                    }
                    updateLatestPrice(dishMenuCodeArray);
                } else {
                    updateCheckout();
                }
            });
        });
    }

    private void updateLatestPrice(ArrayList<String> dishMenuCodeArray) {
        AndroidNetworking.initialize(getApplicationContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("MerchantRefID", GLOBAL_KEY);
            jsonObject.put("DishCodes", new JSONArray(dishMenuCodeArray));
            Log.e("TAG", "updateLatestPrice: "+jsonObject.toString() );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String url = URLConstants.BASE_URL + URLConstants.URL_LATEST_PRICE_DETAIL;
        Log.e("TAG", "updateLatestPrice  API: "+url );
        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("TAG", "AndroidNetworking onResponse: "+response );
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    String itemMenuCode = response.getJSONObject(i).getString("DishMenuCode");
                                    String discountPrice = response.getJSONObject(i).getString("Rate");
                                    for (int j = 0; j < TotalSelectedItemList.size(); j++) {
                                        if (TotalSelectedItemList.get(j).getItemId().equals(itemMenuCode)) {
                                            if (!TotalSelectedItemList.get(j).getItemPrice().equals(discountPrice)) {
                                                String TotalCost = String.valueOf(Float.parseFloat(TotalSelectedItemList.get(j).getItemCount()) * Float.parseFloat(discountPrice));
                                                android.os.AsyncTask.execute(() -> DatabaseClient.getInstance(MainActivity.this).getAppDatabase()
                                                        .checkOutDao().updateTotalCost(itemMenuCode, TotalCost));
                                            }
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            new Handler().postDelayed(() -> updateCheckout(), 1000);
                        } else {
                            updateCheckout();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onResponse: " + error);
                    }
                });
    }

    private void setUPDishList() {
        listAdapter = new AdaptorHeadDishListAdapter(this, this);
        rvDishRecyclerView = findViewById(R.id.rv_dish_head_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvDishRecyclerView.setLayoutManager(layoutManager);
        rvDishRecyclerView.setAdapter(listAdapter);
    }

    private void setUPDishItemList() {
        headDishItemsAdapter = new AdapterHeadDishItems(this, this);
        rvDishItemRecyclerView = findViewById(R.id.rv_dish_item_recyclerview);
        rvDishItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvDishItemRecyclerView.setAdapter(headDishItemsAdapter);
    }

    private void getDishList() {
        dishShimmerLayout.setVisibility(View.VISIBLE);
        dishShimmerLayout.startShimmer();
        rvDishRecyclerView.setVisibility(View.GONE);

        if (isNetworkAvailableForMain()) {
            APIServiceClass.getInstance().getDishList(GLOBAL_KEY, new ResultHandler<ArrayList<DishListModel>>() {
                @Override
                public void onSuccess(ArrayList<DishListModel> data) {
                    if (data != null && data.size() > 0) {

                        if (data.size() == 1) {
                            listAdapter.addItems(data);
                            rvDishRecyclerView.setVisibility(View.GONE);
                            dishShimmerLayout.setVisibility(View.GONE);
                            dishShimmerLayout.stopShimmer();
                        } else {
                            listAdapter.addItems(data);
                            rvDishRecyclerView.setVisibility(View.VISIBLE);
                            dishShimmerLayout.setVisibility(View.GONE);
                            dishShimmerLayout.stopShimmer();
                        }
                        try{
                            if (!(data.get(0).getDishHeadCode() == null && data.get(0).getDishHeadCode().equals(""))) {
                                dishHeadCode = data.get(0).getDishHeadCode();
                                getDishItemList(data.get(0).getDishHeadCode());
                            }
                        }catch (Exception e){e.printStackTrace();}

                    }
                }

                @Override
                public void onFailure(String message) {
                    showToast(message);
                }
            });
        } else {
            showNetworkAlertDialog();
        }
    }

    private void getDishItemList(String dishHCode) {
        if (!dishHCode.equals("")) {
            itemShimmerLayout.startShimmer();
            itemShimmerLayout.setVisibility(View.VISIBLE);
            rvDishItemRecyclerView.setVisibility(View.GONE);
            if (isNetworkAvailableForMain()) {
                APIServiceClass.getInstance().getDishItemList(GLOBAL_KEY, dishHCode, new ResultHandler<ArrayList<DishItemModel>>() {
                    @Override
                    public void onSuccess(ArrayList<DishItemModel> data) {
                        if (data != null && data.size() > 0) {
                            android.os.AsyncTask.execute(() -> {
                                menuCodeList.clear();
                                menuCodeList = DatabaseClient.getInstance(MainActivity.this).getAppDatabase().checkOutDao().getAllMenuCode();
                                runOnUiThread(() -> UpdateItemList(data, menuCodeList));
                            });
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        showToast(message);
                    }
                });
            } else {
                showNetworkAlertDialog();
            }
        } else {
            showToast("Invalid DishHeadCode!");
        }

    }

    private void UpdateItemList(ArrayList<DishItemModel> data, List<String> menuCodeList) {
        headDishItemsAdapter.addItems(data, menuCodeList);
        rvDishItemRecyclerView.setVisibility(View.VISIBLE);
        itemShimmerLayout.stopShimmer();
        itemShimmerLayout.setVisibility(View.GONE);
        updateCheckout();
    }


    @Override
    public void onClick(int position, String data) {
        dishHeadCode = data;
        getDishItemList(data);
    }

    @Override
    public void onSelect(int position, String itemId, String itemName, String itemCount, String itemPrice, String totalCost, String imgURL, String unitName, String saveMoney, String flag) {
        switch (flag) {
            case "INSERT":
                android.os.AsyncTask.execute(() -> {
                    CheckoutEntity checkoutEntity = new CheckoutEntity();
                    checkoutEntity.setPosition(position + "");
                    checkoutEntity.setItemId(itemId);
                    checkoutEntity.setItemName(itemName);
                    checkoutEntity.setItemCount(itemCount);
                    checkoutEntity.setItemPrice(itemPrice);
                    checkoutEntity.setTotalCost(totalCost);
                    checkoutEntity.setImgURL(imgURL);
                    checkoutEntity.setUnitName(unitName);
                    checkoutEntity.setSavingMoney(saveMoney);
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().checkOutDao().insertCheckOutRecord(checkoutEntity);
                    updateCheckout();
                });
                break;
            case "DELETE":
                android.os.AsyncTask.execute(() -> {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().checkOutDao().deleteData(itemId);
                    updateCheckout();
                });

                break;
            case "UPDATE":
                android.os.AsyncTask.execute(() -> {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().checkOutDao().updateCheckout(itemId, itemCount, itemPrice, totalCost, saveMoney);
                    updateCheckout();
                });
                break;
        }
    }

    private void updateCheckout() {
        TotalSelectedItemList.clear();
        totalItemCount = 0;
        totalEffectiveCost = 0;
        android.os.AsyncTask.execute(() -> {
            TotalSelectedItemList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().checkOutDao().getAllCheckOutData();
            runOnUiThread(() -> {
                if (TotalSelectedItemList.size() > 0) {
                    for (int i = 0; i < TotalSelectedItemList.size(); i++) {
                        totalItemCount += Float.parseFloat(TotalSelectedItemList.get(i).getItemCount());
                        totalEffectiveCost += Float.parseFloat(TotalSelectedItemList.get(i).getTotalCost());
                    }
                    tvNumOfItem.setText(new DecimalFormat("0.0").format(totalItemCount));
                    tvTotalCost.setText(new DecimalFormat("0.00").format(totalEffectiveCost));
                    rlCheckoutLayout.setVisibility(View.VISIBLE);
                } else {
                    rlCheckoutLayout.setVisibility(View.GONE);
                }
            });
        });
    }

    public void showNetworkAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_layout, null);
        TextView tvMessage = customLayout.findViewById(R.id.tv_message);
        tvMessage.setText("Internet is not available!");
        Button ok = customLayout.findViewById(R.id.btn_ok);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.show();
        ok.setOnClickListener(v -> {
            try {
                dialog.dismiss();
                getDishList();
                new Handler().postDelayed(this::getLocalDBData, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void setUpNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.slide_menu_navigation);
        CardView menuImage = findViewById(R.id.cv_menu_image);
        TextView tvMobileNumber = findViewById(R.id.tv_mobile_number);
        if (app.user.isLogin) {
            tvMobileNumber.setText(app.user.mobileNumber);
        } else {
            tvMobileNumber.setText("Login");
        }

        tvMobileNumber.setOnClickListener(v -> {
            if (tvMobileNumber.getText().equals("Login")) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });


        TextView tvMyOrder = findViewById(R.id.tv_my_order);
        tvMyOrder.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MyOrderActivity.class)));

        TextView tvPayment = findViewById(R.id.tv_payment);
        tvPayment.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
            intent.putExtra("CASH_HIDE", "true");
            startActivity(intent);
        });

        TextView tvCustomerSupport = findViewById(R.id.btn_customer_support);
        tvCustomerSupport.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SupportScreen.class);
            startActivity(intent);
        });


        menuImage.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers();
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.closeDrawers();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!dishHeadCode.equals(""))
            getDishItemList(dishHeadCode);
        setUpNavigationDrawer();
    }
}

