package com.example.trackmypantry.ViewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.example.trackmypantry.DataType.AccessToken;
import com.example.trackmypantry.DataType.Authentication;
import com.example.trackmypantry.DataType.LoginData;
import com.example.trackmypantry.DataType.RegisterData;
import com.example.trackmypantry.Network.APIService;
import com.example.trackmypantry.Network.RetroInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private Authentication registerResponse;
    private AccessToken accessToken;

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
                registerResponse = (Authentication) response.body();
            }
            @Override
            public void onFailure(Call<Authentication> call, Throwable t) {
               registerResponse = null;
            }
        });
        if(registerResponse == null)
            return false;
        else
            return true;
    }

    public boolean loginCall(LoginData loginData) {
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<AccessToken> call = apiService.loginMethod(loginData);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                accessToken = (AccessToken) response.body();
                SharedPreferences pref = getApplication().getApplicationContext().getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("ACCESS_TOKEN", accessToken.getAccessToken());
                editor.commit();
                Log.i("mytag", "response");

            }
            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                //TODO: Gestione failure
                accessToken = null;
                Log.i("mytag", "failure");
            }
        });

        if(accessToken == null)
            return false;
        else
            return true;
    }
}

