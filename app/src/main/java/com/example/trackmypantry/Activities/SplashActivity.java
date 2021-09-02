package com.example.trackmypantry.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.trackmypantry.R;

import java.util.Calendar;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int oneWeekAsSec = 604800;
        SharedPreferences pref = getApplication().getApplicationContext().getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE);
        long currentDate = Calendar.getInstance(Locale.getDefault()).getTimeInMillis() / 1000;
        Long savedAccessTokenData = Long.valueOf(pref.getString("DATA", String.valueOf(-1)));
        Intent intent = null;
        if(savedAccessTokenData != -1) {
            if (currentDate < savedAccessTokenData + oneWeekAsSec)
                intent = new Intent(SplashActivity.this, CategoryListActivity.class);
            else{
                Toast.makeText(SplashActivity.this,"Token Expired! Please Log In", Toast.LENGTH_SHORT).show();
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
        }
        else
                intent = new Intent(SplashActivity.this, LoginActivity.class);

        Intent finalIntent = intent;
        new Handler().postDelayed(new Runnable (){
            @Override
            public void run() {
                startActivity(finalIntent);
                finish(); //else you can go back to the splash activity
            }
        }, 2000);


    }


}