package com.project.hackathon.dealport.api;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiDealPort {
    private static final String BASE_URL = "";

    static ApiInterface buildApi(Context context,
            String baseUrl,
            boolean cache,
            boolean logging) {
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(
                new GsonBuilder().registerTypeAdapterFactory(ApiModelGsonFactory.create())
                        .create());

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(ApiInterface.class);
    }
}
