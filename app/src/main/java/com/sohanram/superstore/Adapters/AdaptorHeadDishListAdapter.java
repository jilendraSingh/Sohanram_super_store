package com.sohanram.superstore.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sohanram.superstore.Interface.ItemClickListener;
import com.sohanram.superstore.Model.DishListModel;
import com.sohanram.superstore.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptorHeadDishListAdapter extends RecyclerView.Adapter<AdaptorHeadDishListAdapter.MyViewHolder> {

    ArrayList<DishListModel> arrayList = new ArrayList<>();
    ItemClickListener listener;
    int initialColor = 0;
    Context context;

    public AdaptorHeadDishListAdapter(Context context, ItemClickListener listener) {
        this.listener = listener;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dish_list_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DishListModel model = arrayList.get(position);
        holder.tvDishName.setText(model.getDishHeadName());
        String imgURL = model.getImgURL() == null ? "" : model.getImgURL();
        if (!imgURL.equals("")) {
            //Picasso.get().load(imgURL).error(R.drawable.no_image_icon).noFade().into(holder.ivDishImage);
            Glide.with(context).load(imgURL).error(R.drawable.no_image_icon).into(holder.ivDishImage);
        } else {
            holder.ivDishImage.setImageResource(R.drawable.no_image_icon);
        }

        holder.itemView.setOnClickListener(v -> {
            listener.onClick(position, model.getDishHeadCode());
            initialColor = position;
            notifyDataSetChanged();
        });
        if (initialColor == position) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.design_default_color_primary));
            holder.tvDishName.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
            holder.tvDishName.setTextColor(Color.parseColor("#000000"));
        }
    }

    public void addItems(ArrayList<DishListModel> data) {
        arrayList.clear();
        arrayList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDishName;
        CardView cardView;
        ImageView ivDishImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDishName = itemView.findViewById(R.id.tv_dish_name);
            cardView = itemView.findViewById(R.id.cv_cardview);
            ivDishImage = itemView.findViewById(R.id.iv_dish_image);
        }
    }
}

