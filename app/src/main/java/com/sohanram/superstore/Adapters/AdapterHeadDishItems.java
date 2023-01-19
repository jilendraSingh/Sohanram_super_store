package com.sohanram.superstore.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sohanram.superstore.Interface.CheckOutListener;
import com.sohanram.superstore.Model.DishItemModel;
import com.sohanram.superstore.R;
import com.sohanram.superstore.Retrofit.APIServiceClass;
import com.sohanram.superstore.Retrofit.ResultHandler;
import com.sohanram.superstore.RoomDatabase.CheckoutEntity;
import com.sohanram.superstore.RoomDatabase.DatabaseClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sohanram.superstore.Classes.Constants.GLOBAL_KEY;

public class AdapterHeadDishItems extends RecyclerView.Adapter<AdapterHeadDishItems.MyViewHolder> implements Filterable {
    ArrayList<DishItemModel> arrayList = new ArrayList<>();
    List<DishItemModel> filterList = new ArrayList<>();
    CheckOutListener checkOutListener;
    Context context;
    Activity activity;
    String searchString;
    List<String> menuCodeList = new ArrayList<>();

    public AdapterHeadDishItems(Context context, CheckOutListener checkOutListener) {
        this.checkOutListener = checkOutListener;
        this.context = context;
        this.activity = (Activity) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dish_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DishItemModel model = filterList.get(position);
        holder.tvDishName.setText(model.getDishName());

        String imgURL = model.getImgName() == null ? "" : model.getImgName();
        if (!imgURL.equals("")) {
            //Picasso.get().load(imgURL).error(R.drawable.no_image_icon).into(holder.ivItemImage);
            Glide.with(context).load(imgURL).error(R.drawable.no_image_icon).into(holder.ivItemImage);
        } else {
            holder.ivItemImage.setImageResource(R.drawable.no_image_icon);
        }
        holder.tvUnitName.setText(model.getUnitName());
        holder.tvUnit.setText(model.getUnitName());

        String price = model.getMrp() == null ? "" : String.valueOf(model.getMrp());
        String discountPrice = model.getRate() == null ? "" : String.valueOf(model.getRate());

        holder.tvDishMRP.setText(price);
        holder.tvItemRate.setText(discountPrice);

        if (menuCodeList.contains(model.getDishMenuCode())) {
            android.os.AsyncTask.execute(() -> {
                List<CheckoutEntity> list = DatabaseClient.getInstance(context.getApplicationContext()).getAppDatabase().checkOutDao().singleData(model.getDishMenuCode());
                Log.e("TAG", "run: " + list);
                activity.runOnUiThread(() -> {
                    if (list.size() > 0 && list.get(0).getItemId().equals(model.getDishMenuCode())) {
                        holder.edtCount.setText(list.get(0).getItemCount());
                        holder.addButton.setVisibility(View.GONE);
                        holder.rlCountLayout.setVisibility(View.VISIBLE);
                    } else {
                        holder.addButton.setVisibility(View.VISIBLE);
                        holder.rlCountLayout.setVisibility(View.GONE);
                    }
                });
            });

        } else {
            holder.addButton.setVisibility(View.VISIBLE);
            holder.rlCountLayout.setVisibility(View.GONE);
        }


        holder.tvMinus.setOnClickListener(v -> {
            android.os.AsyncTask.execute(() -> {
                List<CheckoutEntity> list = DatabaseClient.getInstance(context.getApplicationContext()).getAppDatabase().checkOutDao().singleData(model.getDishMenuCode());
                Log.e("TAG", "run: " + list);
                if (list.size() > 0) {
                    activity.runOnUiThread(() -> {
                        float count = Float.parseFloat(list.get(0).getItemCount());
                        if (count == 1 || count == 1.0 || count < 1) {
                            holder.addButton.setVisibility(View.VISIBLE);
                            holder.rlCountLayout.setVisibility(View.GONE);
                            checkOutListener.onSelect(position, model.getDishMenuCode(), "", "", "", "", "", "", "", "DELETE");
                            return;
                        }

                        count = count - 1;
                        holder.edtCount.setText(String.valueOf(count));
                        checkOutListener.onSelect(position, model.getDishMenuCode(), model.getDishName(), count + "", holder.tvItemRate.getText().toString().trim(), String.valueOf((count * model.getMrp())), model.getImgName(), model.getUnitName(), String.valueOf(model.getMrp() - model.getRate()), "UPDATE");
                    });
                }
            });

        });

        holder.addButton.setOnClickListener(v -> {
            holder.edtCount.setText("1.0");
            holder.addButton.setVisibility(View.GONE);
            holder.rlCountLayout.setVisibility(View.VISIBLE);
            checkOutListener.onSelect(position, model.getDishMenuCode(), model.getDishName(), "1.0", holder.tvItemRate.getText().toString().trim(), String.valueOf((model.getRate())), model.getImgName(), model.getUnitName(), String.valueOf(model.getMrp() - model.getRate()), "INSERT");
        });

        holder.tvPlus.setOnClickListener(v -> {
            android.os.AsyncTask.execute(() -> {
                List<CheckoutEntity> list = DatabaseClient.getInstance(context.getApplicationContext()).getAppDatabase().checkOutDao().singleData(model.getDishMenuCode());
                if (list.size() > 0) {
                    activity.runOnUiThread(() -> {
                        float count = Float.parseFloat(list.get(0).getItemCount());
                        count = count + 1;
                        holder.edtCount.setText(String.valueOf(count));
                        checkOutListener.onSelect(position, model.getDishMenuCode(), model.getDishName(), count + "", holder.tvItemRate.getText().toString().trim(), String.valueOf((count * Float.parseFloat(holder.tvItemRate.getText().toString().trim()))), model.getImgName(), model.getUnitName(), String.valueOf(model.getMrp() - model.getRate()), "UPDATE");
                    });
                }
            });

        });


        holder.edtCount.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.custom_quantity_dialog);
            dialog.getWindow().setLayout(((getWidth(context) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
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
                } else if (quantity.equals("0")) {
                    holder.addButton.setVisibility(View.VISIBLE);
                    holder.rlCountLayout.setVisibility(View.GONE);
                    checkOutListener.onSelect(position, model.getDishMenuCode(), "", "", "", "", "", "", "", "DELETE");
                } else if (quantity.equals("")) {
                    Toast.makeText(context, "Please enter valid quantity.", Toast.LENGTH_SHORT).show();
                } else {
                    holder.edtCount.setText(quantity);
                    float count;
                    if (quantity.contains(".")) {
                        count = Float.parseFloat(quantity);
                    } else {
                        count = Integer.parseInt(quantity);
                    }
                    checkOutListener.onSelect(position, model.getDishMenuCode(), model.getDishName(), count + "", holder.tvItemRate.getText().toString(), String.valueOf((count * Float.parseFloat(holder.tvItemRate.getText().toString()))), model.getImgName(), model.getUnitName(), String.valueOf(model.getMrp() - model.getRate()), "UPDATE");
                }

                dialog.dismiss();
            });
            dialog.show();
        });

    }

    private int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void addItems(ArrayList<DishItemModel> dishItemModels, List<String> menuCode) {
        arrayList.clear();
        filterList.clear();
        menuCodeList.clear();
        menuCodeList.addAll(menuCode);
        arrayList.addAll(dishItemModels);
        filterList.addAll(dishItemModels);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (!filterList.isEmpty()) {
            return filterList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                searchString = charSequence.toString();
                if (searchString.isEmpty()) {
                    filterList = arrayList;
                } else {
                    List<DishItemModel> filteredList = new ArrayList<>();
                    for (DishItemModel map : arrayList) {
                        if (Objects.requireNonNull(map.getDishName()).toLowerCase().contains(searchString.toLowerCase())) {
                            filteredList.add(map);
                        }
                    }
                    filterList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterList = (List<DishItemModel>) filterResults.values;

                if (searchString.length() >= 3)
                    searchItem(filterList, searchString);
                notifyDataSetChanged();
            }
        };
    }

    private void searchItem(List<DishItemModel> filterList, String searchString) {

        APIServiceClass.getInstance().getSearchItemData(GLOBAL_KEY, searchString, new ResultHandler<List<DishItemModel>>() {
            @Override
            public void onSuccess(List<DishItemModel> data) {
                filterList.clear();
                filterList.addAll(data);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDishName, tvItemRate;
        TextView tvMinus, tvPlus, tvDishMRP, tvUnitName, tvUnit;
        Button addButton;
        RelativeLayout rlCountLayout, rlMRPLayout;
        EditText edtCount;
        ImageView ivItemImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishName = itemView.findViewById(R.id.tv_dish_name);
            tvMinus = itemView.findViewById(R.id.tv_minus);
            tvPlus = itemView.findViewById(R.id.tv_plus);
            edtCount = itemView.findViewById(R.id.edt_count);
            addButton = itemView.findViewById(R.id.btn_add);
            tvDishMRP = itemView.findViewById(R.id.tv_dish_mrp);
            rlMRPLayout = itemView.findViewById(R.id.rl_mrp_layout);
            tvItemRate = itemView.findViewById(R.id.tv_dish_rate);
            rlCountLayout = itemView.findViewById(R.id.rl_count_layout);
            ivItemImage = itemView.findViewById(R.id.iv_item_image);
            tvUnitName = itemView.findViewById(R.id.tv_unit_name);
            tvUnit = itemView.findViewById(R.id.tv_unit);
        }
    }
}
