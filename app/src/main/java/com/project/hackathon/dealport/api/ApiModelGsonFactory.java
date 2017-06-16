package com.project.hackathon.dealport.api;


import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory
abstract class ApiModelGsonFactory implements TypeAdapterFactory {

    // Static factory method to access the package
    // private generated implementation
    static TypeAdapterFactory create() {
        return new AutoValueGson_ApiModelGsonFactory();
    }
}
