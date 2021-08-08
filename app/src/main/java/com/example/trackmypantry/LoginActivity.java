package com.example.trackmypantry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackmypantry.DataType.LoginData;
import com.example.trackmypantry.DataType.RegisterData;
import com.example.trackmypantry.ViewModel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    EditText nameTV;
    EditText emailTV;
    EditText passwordTV;
    TextView register;
    Button button;
    boolean isRegister = false; //false if is the Login, true if is Register
    LoginViewModel viewModel;
    SharedPreferences pref;

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
        pref = getApplicationContext().getSharedPreferences("MY_PREFERENCES", MODE_PRIVATE);
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

        //TODO: function to check credentials (email, password ecc), moficiare con i fragment se possibile
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRegister){
                    String email = emailTV.getText().toString();
                    String password = passwordTV.getText().toString();
                    if(viewModel.loginCall(new LoginData(email, password))) {
                        Toast.makeText(LoginActivity.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, CategoryListActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(LoginActivity.this, "Error! Try again", Toast.LENGTH_SHORT).show();
                } else {
                    String name = nameTV.getText().toString();
                    String email = emailTV.getText().toString();
                    String password = passwordTV.getText().toString();
                    if(viewModel.registerCall(new RegisterData(name, email, password)))
                        Toast.makeText(LoginActivity.this, "You have registered Successfully!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(LoginActivity.this, "Error! Try again with new credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }


}