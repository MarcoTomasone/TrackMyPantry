package com.example.trackmypantry.ViewModel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;

import com.example.trackmypantry.CategoryListActivity;
import com.example.trackmypantry.DataType.AccessToken;
import com.example.trackmypantry.DataType.Authentication;
import com.example.trackmypantry.DataType.LoginData;
import com.example.trackmypantry.DataType.RegisterData;
import com.example.trackmypantry.LoginActivity;
import com.example.trackmypantry.Network.APIService;
import com.example.trackmypantry.Network.RetroInstance;

import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private Authentication registerResponse;
    private AccessToken accessToken;
    private APIService apiService;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public LoginViewModel(Application application){
        super(application);
        apiService = RetroInstance.getRetroClient().create(APIService.class);
        pref = getApplication().getApplicationContext().getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void registerCall(RegisterData registerData, LoginActivity loginActivity) {

        Call<Authentication> call = apiService.registrationMethod(registerData);
        call.enqueue(new Callback<Authentication>() {
            @Override
            public void onResponse(Call<Authentication> call, Response<Authentication> response) {
                if (response.isSuccessful()) {
                    registerResponse = (Authentication) response.body();
                    Toast.makeText(loginActivity.getApplicationContext(), "You have registered Successfully!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(loginActivity.getApplicationContext(), "Error! Try again with new credentials", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Authentication> call, Throwable t) {
               registerResponse = null;
               Toast.makeText(loginActivity.getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginCall(LoginData loginData, LoginActivity loginActivity) {
        Call<AccessToken> call = apiService.loginMethod(loginData);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
              if(response.isSuccessful()){
                    accessToken = (AccessToken) response.body();
                      editor.putString("ACCESS_TOKEN", accessToken.getAccessToken());
                      editor.putString("DATA", String.valueOf(Calendar.getInstance(Locale.getDefault()).getTimeInMillis()/1000));
                      editor.putString("EMAIL", loginData.getEmail());
                      editor.commit();
                    Toast.makeText(loginActivity.getApplicationContext(), "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(loginActivity.getApplicationContext(), CategoryListActivity.class);
                    loginActivity.startActivity(intent);
              }
              else
                  Toast.makeText(loginActivity.getApplicationContext(), "Error! Try Again!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                accessToken = null;
                editor.putString("ACCESS_TOKEN", null);
                editor.commit();
                Toast.makeText(loginActivity.getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

