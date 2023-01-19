package com.sohanram.superstore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sohanram.superstore.Activity.OrderDetailsActivity;
import com.sohanram.superstore.Model.MyOrderModel;
import com.sohanram.superstore.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterMyOrders extends RecyclerView.Adapter<AdapterMyOrders.MyViewHolder> {

    List<MyOrderModel> arrayList = new ArrayList<>();
    Context context;

    public AdapterMyOrders(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_order_item_list_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyOrderModel orderDetail = arrayList.get(position);
        if (orderDetail.getOrderDetails() != null) {
            holder.tvDate.setText(orderDetail.getCheckoutDate());
            holder.tvQty.setText(orderDetail.getQty() + "");
            holder.tvTotlaCost.setText(orderDetail.getTotal() + "");
            switch (orderDetail.getOrderStatus()) {
                case "0":
                    holder.tvOrderStatus.setText("Order Placed");
                    break;
                case "1":
                    holder.tvOrderStatus.setText("Order Viewed");
                    break;
                case "2":
                    holder.tvOrderStatus.setText("Reject By Supplier");
                    break;
                case "3":
                    holder.tvOrderStatus.setText("Invoice Prepared");
                    break;
                case "4":
                    holder.tvOrderStatus.setText("Delivered");
                    break;
                case "5":
                    holder.tvOrderStatus.setText("Cancel By User");
                    break;
            }
        }

        holder.btnOrderDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailsActivity.class);
            intent.putExtra("ORDER_LIST", orderDetail.getOrderDetails());
            intent.putExtra("ORDER_STATUS", orderDetail.getOrderStatus());
            intent.putExtra("CHECKOUT_ID", orderDetail.getCheckoutId());
            context.startActivity(intent);
        });
    }

    public void addItems(List<MyOrderModel> list) {
        arrayList.clear();
        arrayList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvDate, tvQty, tvTotlaCost, tvOrderStatus;
        Button btnOrderDetails;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvQty = itemView.findViewById(R.id.tv_qty);
            tvTotlaCost = itemView.findViewById(R.id.tv_total);
            btnOrderDetails = itemView.findViewById(R.id.tv_view_order_details);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
        }
    }
}
