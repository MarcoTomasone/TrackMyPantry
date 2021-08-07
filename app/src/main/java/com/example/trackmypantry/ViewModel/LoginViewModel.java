package com.example.trackmypantry.ViewModel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.trackmypantry.DataBase.Authentication;
import com.example.trackmypantry.DataBase.RegisterData;
import com.example.trackmypantry.Network.APIService;
import com.example.trackmypantry.Network.RetroInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private Authentication myResponse;

    public LoginViewModel(Application application){
        super(application);
    }

    //TODO: Check if true false is a good programming practice
    public boolean registerCall(RegisterData registerData) {
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<Authentication> call = apiService.registrationMethod(registerData);
        call.enqueue(new Callback<Authentication>() {
            @Override
            public void onResponse(Call<Authentication> call, Response<Authentication> response) {
                myResponse = (Authentication) response.body();
            }
            @Override
            public void onFailure(Call<Authentication> call, Throwable t) {
               myResponse = null;
            }
        });
        if(myResponse == null)
            return false;
        else
            return true;
    }
}

