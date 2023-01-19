package com.sohanram.superstore.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sohanram.superstore.Interface.ItemClickListener;
import com.sohanram.superstore.R;
import com.sohanram.superstore.RoomDatabase.CheckoutEntity;
import com.sohanram.superstore.RoomDatabase.DatabaseClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.MyViewHolder> {
    ArrayList<CheckoutEntity> totalCheckOutList = new ArrayList<>();
    Activity activity;
    float itemCount;
    ItemClickListener itemClickListener;
    Context context;

    public CheckoutAdapter(Context context,Activity activity, ItemClickListener itemClickListener) {
        this.activity = activity;
        this.itemClickListener = itemClickListener;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_checkout_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String imgURL = totalCheckOutList.get(position).getImgURL() == null ? "" : totalCheckOutList.get(position).getImgURL();
        if (!imgURL.equals("")) {
            //Picasso.get().load(imgURL).error(R.drawable.no_image_icon).into(holder.imageView);
            Glide.with(context).load(imgURL).error(R.drawable.no_image_icon).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.no_image_icon);
        }

        holder.tvItemName.setText(totalCheckOutList.get(position).getItemName());
        holder.tvItemCount.setText(totalCheckOutList.get(position).getItemCount());
        holder.tvItemCost.setText(totalCheckOutList.get(position).getTotalCost());
        holder.tvPrice.setText(totalCheckOutList.get(position).getItemPrice());
        holder.edtCount.setText(totalCheckOutList.get(position).getItemCount());
        holder.tvUnitName.setText(totalCheckOutList.get(position).getUnitName());

        holder.tvMinus.setOnClickListener(v -> {
            itemCount = 0;
            String itemId = totalCheckOutList.get(holder.getAbsoluteAdapterPosition()).getItemId();
            float itemPrice = Float.parseFloat(holder.tvPrice.getText().toString());
            itemCount = Float.parseFloat(holder.edtCount.getText().toString());
            itemCount--;
            if (itemCount >= 1) {
                float totalCost = itemCount * itemPrice;
                holder.tvItemCount.setText(String.valueOf(itemCount));
                holder.edtCount.setText(String.valueOf(itemCount));
                holder.tvItemCost.setText(String.valueOf(totalCost));

                android.os.AsyncTask.execute(() -> {
                    DatabaseClient.getInstance(activity).getAppDatabase().checkOutDao().updateCheckout(itemId, String.valueOf(itemCount), String.valueOf(itemPrice), String.valueOf(totalCost), totalCheckOutList.get(holder.getAbsoluteAdapterPosition()).getSavingMoney());
                    activity.runOnUiThread(() -> itemClickListener.onClick(position, ""));
                });
            }
            if (itemCount < 1) {
                showAlertDialog(holder.getAbsoluteAdapterPosition(), itemId, holder.tvItemName.getText().toString());
            }
        });

        holder.tvPlus.setOnClickListener(v -> {
            itemCount = 0;
            String itemId = totalCheckOutList.get(holder.getAbsoluteAdapterPosition()).getItemId();
            float itemPrice = Float.parseFloat(holder.tvPrice.getText().toString());
            itemCount = Float.parseFloat(holder.edtCount.getText().toString());
            itemCount++;
            float totalCost = itemCount * itemPrice;
            holder.tvItemCount.setText(String.valueOf(itemCount));
            holder.edtCount.setText(String.valueOf(itemCount));
            holder.tvItemCost.setText(String.valueOf(totalCost));
            itemClickListener.onClick(holder.getAbsoluteAdapterPosition(), "");

            android.os.AsyncTask.execute(() -> {
                DatabaseClient.getInstance(activity).getAppDatabase().checkOutDao().updateCheckout(itemId, String.valueOf(itemCount), String.valueOf(itemPrice), String.valueOf(totalCost), totalCheckOutList.get(holder.getAbsoluteAdapterPosition()).getSavingMoney());
                activity.runOnUiThread(() -> itemClickListener.onClick(holder.getAbsoluteAdapterPosition(), ""));
            });
        });

        holder.edtCount.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.custom_quantity_dialog);
            dialog.getWindow().setLayout(((getWidth(activity) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);

            EditText edtQuantity = dialog.findViewById(R.id.edt_quantity);
            edtQuantity.setText(holder.edtCount.getText().toString());
            Button btnSubmit = dialog.findViewById(R.id.btn_qty_submit);
            btnSubmit.setOnClickListener(v1 -> {
                String quantity = edtQuantity.getText().toString();
                if (quantity.startsWith(".")) {
                    holder.edtCount.setText("0" + quantity);
                } else if (quantity.endsWith(".")) {
                    holder.edtCount.setText(quantity + "0");
                } else if (quantity.equals("0") || quantity.equals("0.0")) {
                    Toast.makeText(activity, "Quantity can not be 0 !", Toast.LENGTH_SHORT).show();
                } else if (quantity.equals("")) {
                    Toast.makeText(activity, "Please enter valid quantity!", Toast.LENGTH_SHORT).show();
                } else {
                    holder.edtCount.setText(quantity);
                    float count;
                    if (quantity.contains(".")) {
                        count = Float.parseFloat(quantity);
                    } else {
                        count = Integer.parseInt(quantity);
                    }

                    android.os.AsyncTask.execute(() -> {
                                DatabaseClient.getInstance(activity.getApplicationContext()).getAppDatabase().checkOutDao().updateCheckout(totalCheckOutList.get(position).getItemId(), String.valueOf(count), holder.tvPrice.getText().toString(), String.valueOf((count * Float.parseFloat(holder.tvPrice.getText().toString()))), totalCheckOutList.get(holder.getAbsoluteAdapterPosition()).getSavingMoney());
                                activity.runOnUiThread(() -> itemClickListener.onClick(position, "DIALOG_QUANTITY"));
                            }
                    );
                }
                dialog.dismiss();
            });
            dialog.show();
        });
    }

    private void showAlertDialog(int position, String itemId, String itemName) {
        String message = "Are you sure you want to remove " + itemName;
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> android.os.AsyncTask.execute(() -> {
                    DatabaseClient.getInstance(activity).getAppDatabase().checkOutDao().deleteData(itemId);
                    activity.runOnUiThread(() -> {
                        totalCheckOutList.remove(position);
                        notifyItemRemoved(position);
                        activity.runOnUiThread(() -> {
                            itemClickListener.onClick(position, "");
                            dialog.cancel();
                        });
                    });
                }))
                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void addItems(ArrayList<CheckoutEntity> list) {
        totalCheckOutList.clear();
        totalCheckOutList.addAll(list);
        notifyDataSetChanged();
    }

    private int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    public int getItemCount() {
        return totalCheckOutList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemCount, tvItemCost, tvPrice, tvUnitName;
        ImageView imageView;
        TextView tvMinus, tvPlus;
        EditText edtCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemCount = itemView.findViewById(R.id.tv_count);
            tvItemCost = itemView.findViewById(R.id.tv_item_cost);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvMinus = itemView.findViewById(R.id.tv_minus);
            tvPlus = itemView.findViewById(R.id.tv_plus);
            edtCount = itemView.findViewById(R.id.edt_count);
            tvUnitName = itemView.findViewById(R.id.tv_unit_name);
        }
    }
}
