package com.sohanram.superstore.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sohanram.superstore.Activity.AddressFormActivity;
import com.sohanram.superstore.Classes.Constants;
import com.sohanram.superstore.Interface.AddressItemSelectListener;
import com.sohanram.superstore.R;
import com.sohanram.superstore.RoomDatabase.AddressEntity;
import com.sohanram.superstore.RoomDatabase.DatabaseClient;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    List<AddressEntity> arrayList = new ArrayList<>();
    AddressItemSelectListener listener;
    int SelectedPosition = 0;
    Activity activity;
    String onlyAddress;

    public AddressAdapter(Activity activity, AddressItemSelectListener listener) {
        this.listener = listener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AddressEntity addressEntity = arrayList.get(position);
        String address = addressEntity.getName() + "," + addressEntity.getMobile() + "," + addressEntity.getAddress1() + "," + addressEntity.getAddress2();
        onlyAddress = addressEntity.getAddress1() + "," + addressEntity.getAddress2();
        if (onlyAddress.endsWith(",")) {
            onlyAddress = onlyAddress.substring(0, onlyAddress.length() - 1);
        }
        if (address.endsWith(","))
            address = address.substring(0, address.length() - 1).toUpperCase();
        holder.tvAddress.setText(address);
        if (position == SelectedPosition) {
            holder.checkBox.setChecked(true);
            listener.onSelectAddress(position, holder.tvAddress.getText().toString(), onlyAddress, addressEntity.getName(), addressEntity.getMobile());
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.itemView.setOnClickListener(v -> {
            listener.onSelectAddress(position, holder.tvAddress.getText().toString(), onlyAddress, addressEntity.getName(), addressEntity.getMobile());
            SelectedPosition = position;
            notifyDataSetChanged();

        });

        holder.ivDeleteIcon.setOnClickListener(v -> android.os.AsyncTask.execute(() -> {
            DatabaseClient.getInstance(activity).getAppDatabase().checkOutDao().deleteAddress(addressEntity.getId());
            activity.runOnUiThread(() -> {
                Constants.SELECTED_ADDRESS = "";
                arrayList.remove(position);
                notifyItemRemoved(position);
            });

        }));

        holder.edtIcon.setOnClickListener(v -> {
            Intent intent = new Intent(activity, AddressFormActivity.class);
            intent.putExtra("id", addressEntity.getId() + "");
            intent.putExtra("name", addressEntity.getName());
            intent.putExtra("mobile", addressEntity.getMobile());
            intent.putExtra("add1", addressEntity.getAddress1());
            intent.putExtra("add2", addressEntity.getAddress2());
            intent.putExtra("update", "true");
            activity.startActivity(intent);
        });

    }


    public void addItems(List<AddressEntity> list) {
        arrayList.clear();
        arrayList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvAddress;
        ImageView ivDeleteIcon, edtIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.radio_button);
            tvAddress = itemView.findViewById(R.id.tv_address);
            ivDeleteIcon = itemView.findViewById(R.id.iv_delete_icon);
            edtIcon = itemView.findViewById(R.id.edt_icon);
        }
    }
}
