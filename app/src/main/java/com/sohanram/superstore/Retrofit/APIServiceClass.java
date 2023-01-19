package com.sohanram.superstore.Retrofit;

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
import retrofit2.Callback;
import retrofit2.Response;

public class APIServiceClass {
    private static APIServiceClass mInstance;

    public static synchronized APIServiceClass getInstance() {
        if (mInstance == null) {
            mInstance = new APIServiceClass();
        }
        return mInstance;
    }

    public void getDishList(String merchantKey, ResultHandler<ArrayList<DishListModel>> handler) {
        Call<ArrayList<DishListModel>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getDishList(merchantKey);
        call.enqueue(new Callback<ArrayList<DishListModel>>() {
            @Override
            public void onResponse(Call<ArrayList<DishListModel>> call, Response<ArrayList<DishListModel>> response) {
                if (response.isSuccessful()) {
                    handler.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DishListModel>> call, Throwable t) {
                handler.onFailure(t.getLocalizedMessage());
            }
        });

    }

    public void getDishItemList(String merchantId, String headCode, ResultHandler<ArrayList<DishItemModel>> handler) {
        Call<ArrayList<DishItemModel>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getDishItemList(merchantId, headCode);
        call.enqueue(new Callback<ArrayList<DishItemModel>>() {
            @Override
            public void onResponse(Call<ArrayList<DishItemModel>> call, Response<ArrayList<DishItemModel>> response) {
                if (response.isSuccessful()) {
                    handler.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DishItemModel>> call, Throwable t) {
                handler.onFailure(t.getLocalizedMessage());
            }
        });

    }

    public void CheckLatestPrice(JSONObject jsonObject, ResultHandler<ArrayList<LatestPriceModel>> handler) {
        Call<ArrayList<LatestPriceModel>> call = RetrofitClient
                .getInstance()
                .getApi()
                .CheckLatestPrice(jsonObject);
        call.enqueue(new Callback<ArrayList<LatestPriceModel>>() {
            @Override
            public void onResponse(Call<ArrayList<LatestPriceModel>> call, Response<ArrayList<LatestPriceModel>> response) {
                if (response.isSuccessful()) {
                    handler.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LatestPriceModel>> call, Throwable t) {
                handler.onFailure(t.getLocalizedMessage());
            }
        });

    }

    public void getDeliveryCharges(JSONObject jsonObject, ResultHandler<ArrayList<LatestPriceModel>> handler) {
        Call<ArrayList<LatestPriceModel>> call = RetrofitClient
                .getInstance()
                .getApi()
                .CheckLatestPrice(jsonObject);
        call.enqueue(new Callback<ArrayList<LatestPriceModel>>() {
            @Override
            public void onResponse(Call<ArrayList<LatestPriceModel>> call, Response<ArrayList<LatestPriceModel>> response) {
                if (response.isSuccessful()) {
                    handler.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LatestPriceModel>> call, Throwable t) {
                handler.onFailure(t.getLocalizedMessage());
            }
        });

    }


    public void getOTPRequest(String merchantId, String mobile, ResultHandler<OTPResponseModel> handler) {
        Call<OTPResponseModel> call = RetrofitClient
                .getInstance()
                .getApi()
                .otpRequest(merchantId, mobile);
        call.enqueue(new Callback<OTPResponseModel>() {
            @Override
            public void onResponse(Call<OTPResponseModel> call, Response<OTPResponseModel> response) {
                if (response.isSuccessful()) {
                    handler.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<OTPResponseModel> call, Throwable t) {
                handler.onFailure(t.getLocalizedMessage());
            }
        });

    }

    public void sendFinalOrderRequest(String merchantId, String mobile, ResultHandler<OTPResponseModel> handler) {
        Call<OTPResponseModel> call = RetrofitClient
                .getInstance()
                .getApi()
                .otpRequest(merchantId, mobile);
        call.enqueue(new Callback<OTPResponseModel>() {
            @Override
            public void onResponse(Call<OTPResponseModel> call, Response<OTPResponseModel> response) {
                if (response.isSuccessful()) {
                    handler.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<OTPResponseModel> call, Throwable t) {
                handler.onFailure(t.getLocalizedMessage());
            }
        });

    }

    public void getMyOrderList(String merchantId, String mobileNumbber, ResultHandler<List<MyOrderModel>> handler) {
        Call<List<MyOrderModel>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getMyOrderList(merchantId, mobileNumbber);
        call.enqueue(new Callback<List<MyOrderModel>>() {
            @Override
            public void onResponse(Call<List<MyOrderModel>> call, Response<List<MyOrderModel>> response) {
                if (response.isSuccessful()) {
                    handler.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<MyOrderModel>> call, Throwable t) {
                handler.onFailure(t.getLocalizedMessage());
            }
        });

    }

    public void getSearchItemData(String merchantId, String searchText, ResultHandler<List<DishItemModel>> handler) {
        Call<List<DishItemModel>> call = RetrofitClient
                .getInstance()
                .getApi()
                .getSearchItemData(merchantId, searchText);
        call.enqueue(new Callback<List<DishItemModel>>() {
            @Override
            public void onResponse(Call<List<DishItemModel>> call, Response<List<DishItemModel>> response) {
                if (response.isSuccessful()) {
                    handler.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<DishItemModel>> call, Throwable t) {
                handler.onFailure(t.getLocalizedMessage());
            }
        });

    }

    public void cancelOrderRequest(String storeIdOrderIdStatusIdDesc, ResultHandler<ResponseBody> handler) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .cancelOrderRequest(storeIdOrderIdStatusIdDesc);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    handler.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handler.onFailure(t.getLocalizedMessage());
            }
        });

    }

    public void getContactSupport(String globalId, ResultHandler<ContactModel> handler) {
        Call<ContactModel> call = RetrofitClient
                .getInstance()
                .getApi()
                .getContactSupport(globalId);
        call.enqueue(new Callback<ContactModel>() {
            @Override
            public void onResponse(Call<ContactModel> call, Response<ContactModel> response) {
                if (response.isSuccessful()) {
                    handler.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<ContactModel> call, Throwable t) {
                handler.onFailure(t.getLocalizedMessage());
            }
        });

    }


}
