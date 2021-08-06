package com.example.trackmypantry.ViewModel;

import android.app.Application;
import android.util.Log;

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

    public void makeAPICall(RegisterData registerData) {
        APIService apiService = RetroInstance.getRetroClient().create(APIService.class);
        Call<Authentication> call = apiService.registrationMethod(registerData);
        Log.i("myLog", String.valueOf(registerData));
        call.enqueue(new Callback<Authentication>() {
            @Override
            public void onResponse(Call<Authentication> call, Response<Authentication> response) {
                //myResponse = response.body();
                Log.w("2.0 ",new Gson().toJson(response));
            }

            @Override
            public void onFailure(Call<Authentication> call, Throwable t) {
                //myResponse = null;
                //System.out.println(myResponse);


            }
        });
    }
}

