package com.example.trackmypantry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.trackmypantry.DataBase.RegisterData;
import com.example.trackmypantry.ViewModel.LoginViewModel;
import com.example.trackmypantry.ViewModel.ProductListActivityViewModel;

public class LoginActivity extends AppCompatActivity {
    EditText nameTV;
    EditText emailTV;
    EditText passwordTV;
    TextView register;
    Button button;
    boolean isRegister = false;
    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameTV = findViewById(R.id.editTextTextPersonName);
        emailTV = findViewById(R.id.editTextTextEmailAddress);
        passwordTV = findViewById(R.id.editTextTextPassword);
        register = findViewById(R.id.registerTextView);
        button = findViewById(R.id.buttonSignInUp);
        nameTV.setVisibility(View.GONE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!isRegister){
                   isRegister = true;
                   nameTV.setVisibility(View.VISIBLE);
                   button.setText("Sign Up");
                   register.setText(R.string.register);
               }
               else {
                   isRegister = false;
                   nameTV.setVisibility(View.GONE);
                   button.setText("Sign In");
                   register.setText(R.string.not_register);
               }
            }

        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRegister){
                    String email = emailTV.getText().toString();
                    String password = passwordTV.getText().toString();
                    Log.i("mytag", "sto per chiamare il VM 1");
                    //viewModel.makeAPICall(email, password);
                } else {
                    String name = nameTV.getText().toString();
                    String email = emailTV.getText().toString();
                    String password = passwordTV.getText().toString();
                    Log.i("mytag", "sto per chiamare il VM 2");
                    viewModel.makeAPICall(new RegisterData(name, email, password));
                }
            }
        });
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }


}