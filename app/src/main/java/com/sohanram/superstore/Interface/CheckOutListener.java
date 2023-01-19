package com.sohanram.superstore.Interface;

public interface CheckOutListener {
    void onSelect(int position, String itemId, String itemName, String itemCount, String itemPrice, String totalCost,String itemImageURL,String unitName,String saveMoney ,String flag);
}
