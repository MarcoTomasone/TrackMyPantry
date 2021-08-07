package com.example.trackmypantry.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.trackmypantry.DataBase.Authentication;
import com.example.trackmypantry.DataBase.LoginData;
import com.example.trackmypantry.DataBase.RegisterData;
import com.example.trackmypantry.Network.APIService;
import com.example.trackmypantry.Network.RetroInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private Authentication myResponse;
    private String accessToken = null;
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

    public boolean loginCall(LoginData loginData) {
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<String> call = apiService.loginMethod(loginData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                accessToken = response.body();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //TODO: Gestione failure
            }
        });
        if(accessToken == null)
            return false;
        else
            return true;
    }
}

