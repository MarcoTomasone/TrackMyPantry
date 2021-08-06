package com.example.trackmypantry.DataBase;

import com.google.gson.annotations.SerializedName;

public class Authentication {
    @SerializedName("id")
    String id;
    @SerializedName("username")
    String username;
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;
    @SerializedName("createdAt")
    String createdAt;
    @SerializedName("updatedAt")
    String updatedAt;
}
