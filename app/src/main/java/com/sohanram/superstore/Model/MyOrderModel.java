package com.sohanram.superstore.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyOrderModel {
    @SerializedName("CheckoutId")
    @Expose
    private String checkoutId;
    @SerializedName("BillNo")
    @Expose
    private Object billNo;
    @SerializedName("CheckoutDate")
    @Expose
    private String checkoutDate;
    @SerializedName("SubTotal")
    @Expose
    private String subTotal;
    @SerializedName("Delivery")
    @Expose
    private String delivery;
    @SerializedName("Qty")
    @Expose
    private String qty;
    @SerializedName("Total")
    @Expose
    private String total;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("Description")
    @Expose
    private String description;



    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @SerializedName("orderStatus")
    @Expose
    private String orderStatus;



    @SerializedName("orderDetails")
    @Expose
    private ArrayList<OrderDetail> orderDetails = null;


    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    public Object getBillNo() {
        return billNo;
    }

    public void setBillNo(Object billNo) {
        this.billNo = billNo;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

}
