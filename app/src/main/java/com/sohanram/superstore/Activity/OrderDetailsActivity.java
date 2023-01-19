package com.sohanram.superstore.Activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sohanram.superstore.Adapters.AdapterMyOrderDetails;
import com.sohanram.superstore.Model.OrderDetail;
import com.sohanram.superstore.R;
import com.sohanram.superstore.Retrofit.APIServiceClass;
import com.sohanram.superstore.Retrofit.ResultHandler;

import java.util.ArrayList;

import io.canner.stepsview.StepsView;
import okhttp3.ResponseBody;

import static com.sohanram.superstore.Classes.Constants.GLOBAL_KEY;

public class OrderDetailsActivity extends BaseActivity {

    RecyclerView rvOrderRecyclerView;
    AdapterMyOrderDetails adapterMyOrderDetails;
    ArrayList<OrderDetail> arrayList = new ArrayList<>();
    String orderstatus, checkoutId;
    Button btnCancelOrder;
    String[] orderPlacedView = {"Order Placed"};
    String[] orderAcceptedView = {"Order Placed", "Order Viewed"};
    String[] invoicePreparedView = {"Order Placed", "Order Viewed", "Invoice Prepared"};
    String[] deliveredView = {"Order Placed", "Order Viewed", "Invoice Prepared", "Delivered"};
    String[] orderCancelledView = {"Order Placed", "Cancel By User"};
    String[] orderRejectedView = {"Order Placed", "Reject By Supplier"};
    int current_status = 0;
    StepsView stepsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        setToolbar(this, "Order Details");
        stepsView = findViewById(R.id.step_view);
        btnCancelOrder = findViewById(R.id.btn_cancle_order);
        arrayList.clear();
        if (getIntent().getExtras() != null) {
            arrayList = (ArrayList<OrderDetail>) getIntent().getSerializableExtra("ORDER_LIST");
            orderstatus = getIntent().getStringExtra("ORDER_STATUS");
            checkoutId = getIntent().getStringExtra("CHECKOUT_ID");
        }

        rvOrderRecyclerView = findViewById(R.id.order_detail_recyclerview);
        rvOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterMyOrderDetails = new AdapterMyOrderDetails();
        rvOrderRecyclerView.setAdapter(adapterMyOrderDetails);
        adapterMyOrderDetails.addItems(arrayList);
        btnCancelOrder.setOnClickListener(v -> {
                    showCancelDialog();
                }
        );

        // Stepper Process
        Log.e(TAG, "onCreate: " + orderstatus);
        switch (orderstatus) {
            case "0":
                stepsView.setLabels(orderPlacedView);
                current_status = 0;
                stepsView.setLabelTextSize(10f);
                break;
            case "1":
                stepsView.setLabels(orderAcceptedView);
                btnCancelOrder.setVisibility(View.GONE);
                current_status = 1;
                stepsView.setLabelTextSize(10f);
                break;
            case "2":
                stepsView.setLabels(orderRejectedView);
                btnCancelOrder.setVisibility(View.GONE);
                current_status = 1;
                stepsView.setLabelTextSize(10f);
                break;
            case "3":
                stepsView.setLabels(invoicePreparedView);
                current_status = 2;
                stepsView.setLabelTextSize(10f);
                btnCancelOrder.setVisibility(View.GONE);
                break;
            case "4":
                stepsView.setLabels(deliveredView);
                current_status = 3;
                stepsView.setLabelTextSize(10f);
                break;
            case "5":
                stepsView.setLabels(orderCancelledView);
                btnCancelOrder.setVisibility(View.GONE);
                current_status = 1;
                stepsView.setLabelTextSize(10f);
                break;
        }

        stepsView.setCircleRadius(25f);
        stepsView.setBarColorIndicator(R.color.gray)
                .setProgressColorIndicator(getResources().getColor(R.color.blue))
                .setLabelColorIndicator(getResources().getColor(R.color.blue))
                .setCompletedPosition(current_status)
                .drawView();
    }


    private void showCancelDialog() {
        final Dialog dialog = new Dialog(OrderDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_cancel_dialog);
        dialog.getWindow().setLayout(((getWidth(OrderDetailsActivity.this) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);

        EditText edtCancelText = dialog.findViewById(R.id.edt_cancel_text);
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_submit);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCancelText.getText().toString().trim().equals("")) {
                    sendCancelRequest("Cancelled");
                } else {
                    sendCancelRequest(edtCancelText.getText().toString().trim());
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void sendCancelRequest(String cancelReson) {
        String orderID = GLOBAL_KEY + "~" + checkoutId + "~" + "5" + "~" + cancelReson;
        APIServiceClass.getInstance().cancelOrderRequest(orderID, new ResultHandler<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                showToast("Your Order is cancelled.");
                finish();
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "onFailure: " + message);
                showToast("Error");
            }
        });
    }

    private int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}