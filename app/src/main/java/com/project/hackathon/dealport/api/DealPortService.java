package com.project.hackathon.dealport.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DealPortService {
    private static final String BASE_URL = "https://rest.locuslabs.com/v1/venue/muc/poi/";

    public static DealPortApi createService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(DealPortApi.class);
    }
}
