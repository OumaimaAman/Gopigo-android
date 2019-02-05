package com.example.root.gopigoproject;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by root on 01/12/17.
 */

public interface InterfaceRetrofit {

    public static final String ENDPOINT = "http://192.168.0.18:3002/robot";

    @GET("/action")
    void searchReposAsync(@Query("action") String query, Callback<Response> callback);
    @GET("/distance")
    void searcDistaceAsync(Callback<Response> callback);
    @GET("/beacon")
    void searcBeaconAsync(Callback<Response> callback);
}
