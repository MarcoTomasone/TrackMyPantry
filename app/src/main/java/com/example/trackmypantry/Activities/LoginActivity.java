package com.example.trackmypantry.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackmypantry.DataType.LoginData;
import com.example.trackmypantry.DataType.RegisterData;
import com.example.trackmypantry.R;
import com.example.trackmypantry.ViewModel.LoginViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    EditText nameTV;
    EditText emailTV;
    EditText passwordTV;
    TextView register;
    Button button;
    boolean isRegister = false; //false if is the Login, true if is Register
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
                String email = emailTV.getText().toString();
                String password = passwordTV.getText().toString();
                if(checkEmail(email) && !TextUtils.isEmpty(password) ){
                    if(!isRegister)
                        viewModel.loginCall(new LoginData(email, password), LoginActivity.this);
                     else {
                         String name = nameTV.getText().toString();
                        if(!TextUtils.isEmpty(name))
                            viewModel.registerCall(new RegisterData(name, email, password), LoginActivity.this);
                        else
                            Toast.makeText(LoginActivity.this, "Insert Username!", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(LoginActivity.this, "Invalid Credentials! Please Check", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    private boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);
        if(mat.matches())
            return true;
        else
            return false;
    }


}