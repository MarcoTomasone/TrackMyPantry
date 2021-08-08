package com.example.trackmypantry.DataType;

public class RegisterData extends LoginData{
    String username;

    public RegisterData(String username, String email, String password){
        super(email, password);
        this.username = username;
    }
}
