package com.sohanram.superstore.RoomDatabase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CheckOutDao {

    @Insert
    void insertCheckOutRecord(CheckoutEntity checkoutEntity);

    @Query("UPDATE checkoutentity set item_count=:itemCount,total_cost=:totalCost,item_price=:itemPrice,saving_money=:saveMoney where itemId=:itemId")
    void updateCheckout(String itemId, String itemCount, String itemPrice, String totalCost,String saveMoney);

    @Query("UPDATE checkoutentity set total_cost=:totalCost where itemId=:itemId")
    void updateTotalCost(String itemId, String totalCost);

    @Query("SELECT * FROM checkoutentity WHERE itemId = :itemId")
    List<CheckoutEntity> singleData(String itemId);

    @Query("SELECT * FROM checkoutentity")
    List<CheckoutEntity> getAllCheckOutData();

    @Query("SELECT itemId FROM checkoutentity")
    List<String> getAllMenuCode();

    @Query("DELETE FROM checkoutentity")
    void deleteCheckoutData();

    @Query("DELETE FROM CheckoutEntity WHERE itemId=:itemId")
    void deleteData(String itemId);

    @Insert
    public void insertAddress(AddressEntity addressEntity);

    @Query("SELECT * FROM AddressEntity")
    List<AddressEntity> getAllAddress();

    @Query("DELETE FROM AddressEntity WHERE id=:id")
    void deleteAddress(int id);

    @Query("UPDATE AddressEntity set name=:name,mobile=:mobile, address1=:add1,address2=:add2 WHERE id=:id")
    void updateAddress(int id, String name, String mobile, String add1, String add2);



/*
    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);*/
}
