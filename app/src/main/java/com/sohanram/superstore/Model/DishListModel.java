package com.sohanram.superstore.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DishListModel {

    @SerializedName("storeId")
    @Expose
    private String storeId;
    @SerializedName("dishHeadCode")
    @Expose
    private String dishHeadCode;
    @SerializedName("dishHeadName")
    @Expose
    private String dishHeadName;

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @SerializedName("imgName")
    @Expose
    private String imgURL;




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

    public String getDishHeadName() {
        return dishHeadName;
    }

    public void setDishHeadName(String dishHeadName) {
        this.dishHeadName = dishHeadName;
    }

}
