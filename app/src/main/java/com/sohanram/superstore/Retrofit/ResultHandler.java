package com.sohanram.superstore.Retrofit;

public interface ResultHandler<T> {
    void onSuccess(T data);

    void onFailure(String message);
}
