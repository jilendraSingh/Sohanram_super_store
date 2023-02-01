package com.sohanram.superstore.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    @SerializedName("checkoutId")
    @Expose
    private String checkoutId;
    @SerializedName("dishName")
    @Expose
    private String dishName;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
