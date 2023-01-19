package com.sohanram.superstore.Retrofit;

import com.sohanram.superstore.Classes.URLConstants;
import com.sohanram.superstore.Model.ContactModel;
import com.sohanram.superstore.Model.DishItemModel;
import com.sohanram.superstore.Model.DishListModel;
import com.sohanram.superstore.Model.LatestPriceModel;
import com.sohanram.superstore.Model.MyOrderModel;
import com.sohanram.superstore.Model.OTPResponseModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET(URLConstants.URL_DISH_LIST)
    Call<ArrayList<DishListModel>> getDishList(@Query("merchantRefID") String merchantRefID);


    @GET(URLConstants.URL_DISH_ITEM_LIST)
    Call<ArrayList<DishItemModel>> getDishItemList(@Query("merchantRefID") String merchantRefID,
                                                   @Query("dishHeadCode") String dishHeadCode);

    @POST(URLConstants.URL_LATEST_PRICE_DETAIL)
    Call<ArrayList<LatestPriceModel>> CheckLatestPrice(@Body JSONObject jsonObject);

    @POST(URLConstants.URL_OTP_REQUEST_URL)
    Call<OTPResponseModel> otpRequest(@Query("merchantRefID") String merchantRefID,
                                      @Query("mobile") String mobile);

    @GET(URLConstants.URL_MY_ORDER_LIST_URL)
    Call<List<MyOrderModel>> getMyOrderList(@Query("merchantRefID") String merchantRefID,
                                            @Query("mobileNo") String mobileNumber);

    @GET(URLConstants.URL_SEARCH_TEXT_URL)
    Call<List<DishItemModel>> getSearchItemData(@Query("merchantRefID") String merchantRefID,
                                                @Query("searchText") String searchText);


    @POST(URLConstants.URL_CANCEL_ORDER_URL)
    Call<ResponseBody> cancelOrderRequest(@Query("storeIdOrderIdStatusIdDesc") String cencelOrderID);

    @GET(URLConstants.URL_SUPPORT_URL)
    Call<ContactModel> getContactSupport(@Query("merchantRefID") String cencelOrderID);

}


