package com.project.hackathon.dealport.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by natalyablanco on 16.06.17.
 */

public interface ApiInterface {
    @GET("users/{user}/repos")
    Call<List<String>> listRepos(@Path("user") String user);

}
