package com.project.hackathon.dealport.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;


public interface DealPortApi {
    //https://rest.locuslabs.com/ -H 'x-api-key:YOUR-API-KEY'

    @GET("get-all-ids")
    Call<Object> getAllIds(@Header("x-api-key") String key);

    @GET("get-categories")
    Call<Object> getAllCategories(@Header("x-api-key") String key);
}
