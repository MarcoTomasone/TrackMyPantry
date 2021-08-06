package com.example.trackmypantry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    EditText nameTV;
    EditText emailTV;
    EditText passwordTV;
    TextView register;
    boolean isRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameTV = findViewById(R.id.editTextTextPersonName);
        emailTV = findViewById(R.id.editTextTextEmailAddress);
        passwordTV = findViewById(R.id.editTextTextPassword);
        register = findViewById(R.id.registerTextView);
        nameTV.setVisibility(View.GONE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameTV.setVisibility(View.VISIBLE);
                isRegister = true;
                ((Button) findViewById(R.id.buttonSignInUp)).setText("Sign Up");
            }
        });


    }
}