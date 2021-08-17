package com.example.trackmypantry.Network;

import com.example.trackmypantry.DataType.AccessToken;
import com.example.trackmypantry.DataType.Authentication;
import com.example.trackmypantry.DataType.CreateProductSchema;
import com.example.trackmypantry.DataType.GetProductSchema;
import com.example.trackmypantry.DataType.LoginData;
import com.example.trackmypantry.DataType.Product;
import com.example.trackmypantry.DataType.RegisterData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @Headers("Content-Type: application/json")
    @POST("users")
    Call<Authentication> registrationMethod(@Body RegisterData registerData);

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    Call<AccessToken> loginMethod(@Body LoginData loginData);

    @Headers("Content-Type: application/json")
    @POST("products")
    Call<Product> insertNewProduct(@Body CreateProductSchema product, @Header("Authorization") String accessToken);

    @Headers("Content-Type: application/json")
    @GET("products")
    Call<GetProductSchema> getProductByBarcode(@Query("barcode") String Barcode, @Header("Authorization") String accessToken);

}
