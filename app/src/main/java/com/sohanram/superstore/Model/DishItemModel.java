package com.sohanram.superstore.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DishItemModel {
    public int count;
    boolean isSelected;
    @SerializedName("storeId")
    @Expose
    private String storeId;
    @SerializedName("dishHeadCode")
    @Expose
    private String dishHeadCode;
    @SerializedName("dishMenuCode")
    @Expose
    private String dishMenuCode;
    @SerializedName("dishName")
    @Expose
    private String dishName;
    @SerializedName("imgName")
    @Expose
    private String imgName;
    @SerializedName("mrp")
    @Expose
    private Float mrp;
    @SerializedName("rate")
    @Expose
    private Float rate;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @SerializedName("UnitName")
    @Expose
    private String unitName;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getDishHeadCode() {
        return dishHeadCode;
    }

    public void setDishHeadCode(String dishHeadCode) {
        this.dishHeadCode = dishHeadCode;
    }

    public String getDishMenuCode() {
        return dishMenuCode;
    }

    public void setDishMenuCode(String dishMenuCode) {
        this.dishMenuCode = dishMenuCode;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public Float getMrp() {
        return mrp;
    }

    public void setMrp(Float mrp) {
        this.mrp = mrp;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }
}
