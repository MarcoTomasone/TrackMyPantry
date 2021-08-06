package com.example.trackmypantry.Network;

import com.example.trackmypantry.DataBase.Authentication;
import com.example.trackmypantry.DataBase.RegisterData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers("Content-Type: application/json")
    @POST("users")
    Call<Authentication> registrationMethod(@Body RegisterData registerData);

}
