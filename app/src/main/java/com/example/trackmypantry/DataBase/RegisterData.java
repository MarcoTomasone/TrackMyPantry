package com.example.trackmypantry.DataBase;

public class RegisterData extends LoginData{
    String username;

    public RegisterData(String username, String email, String password){
        super(email, password);
        this.username = username;
    }
}
