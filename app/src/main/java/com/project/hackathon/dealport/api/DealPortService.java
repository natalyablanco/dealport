package com.project.hackathon.dealport.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DealPortService {
    private static final String BASE_LOCUS_URL = "https://rest.locuslabs.com/v1/venue/muc/poi/";

    private static final String BASE_SITA_URL = "https://airport-qa.api.aero/airport/v2/";

    public static DealPortApi createLocusService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_LOCUS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(DealPortApi.class);
    }

    public static DealPortApi createSitaService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_SITA_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit.create(DealPortApi.class);
    }
}
