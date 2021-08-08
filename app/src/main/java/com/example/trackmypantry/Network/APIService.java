package com.example.trackmypantry.Network;

import com.example.trackmypantry.DataType.AccessToken;
import com.example.trackmypantry.DataType.Authentication;
import com.example.trackmypantry.DataType.LoginData;
import com.example.trackmypantry.DataType.RegisterData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers("Content-Type: application/json")
    @POST("users")
    Call<Authentication> registrationMethod(@Body RegisterData registerData);

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    Call<AccessToken> loginMethod(@Body LoginData loginData);

}
