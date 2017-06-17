package com.project.hackathon.dealport.api;

import com.project.hackathon.dealport.model.Airports;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;


public interface DealPortApi {
    //https://rest.locuslabs.com/ -H 'x-api-key:YOUR-API-KEY'

    @GET("get-all-ids")
    Call<Object> getAllIds(@Header("x-api-key") String key);

    @GET("get-categories")
    Call<Object> getAllCategories(@Header("x-api-key") String key);

    @Headers({
            "Accept: application/json",
            "x-apikey: 3035d833bb6e531654a3cce03e6b1fde"
    })
    @GET("airports")
    Call<Airports> getAirports();


    @GET("airport/{code}")
    Call<Object> getAirportInfo(@Path("code") String code, @Header("user_key") String key);
}
