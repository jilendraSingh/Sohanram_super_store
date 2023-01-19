package com.sohanram.superstore.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sohanram.superstore.Model.OrderDetail;
import com.sohanram.superstore.R;

import java.util.ArrayList;

public class AdapterMyOrderDetails extends RecyclerView.Adapter<AdapterMyOrderDetails.MyViewHolder> {

    ArrayList<OrderDetail> arrayList = new ArrayList<>();

    public AdapterMyOrderDetails() {

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_order_details, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderDetail orderDetail = arrayList.get(position);
        holder.tvOrderName.setText(orderDetail.getDishName());
        holder.tvRate.setText(orderDetail.getRate() + "");
        holder.tvQty.setText(orderDetail.getQty() + "");
        holder.tvTotlaCost.setText(orderDetail.getAmount() + "");
    }

    public void addItems(ArrayList<OrderDetail> list) {
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
        TextView tvOrderName, tvRate, tvQty, tvTotlaCost, tvOrderDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderName = itemView.findViewById(R.id.tv_order_name);
            tvRate = itemView.findViewById(R.id.tv_rate);
            tvQty = itemView.findViewById(R.id.tv_qty);
            tvTotlaCost = itemView.findViewById(R.id.tv_total_cost);
            tvOrderDetails = itemView.findViewById(R.id.tv_view_order_details);
        }
    }
}
