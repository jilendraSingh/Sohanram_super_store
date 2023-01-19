package com.sohanram.superstore.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatestPriceModel {
    @SerializedName("StoreId")
    @Expose
    private String storeId;
    @SerializedName("DishHeadCode")
    @Expose
    private String dishHeadCode;
    @SerializedName("DishMenuCode")
    @Expose
    private String dishMenuCode;
    @SerializedName("DishName")
    @Expose
    private String dishName;
    @SerializedName("ImgName")
    @Expose
    private String imgName;
    @SerializedName("Mrp")
    @Expose
    private Integer mrp;
    @SerializedName("Rate")
    @Expose
    private Integer rate;

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

    public Integer getMrp() {
        return mrp;
    }

    public void setMrp(Integer mrp) {
        this.mrp = mrp;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

}
