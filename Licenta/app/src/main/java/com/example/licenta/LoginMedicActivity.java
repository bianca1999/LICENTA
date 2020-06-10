package com.example.licenta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginMedicActivity extends AppCompatActivity {
    TextView titleTextView, register1, register2;
    Button loginButton;
    EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_medic);
        titleTextView=findViewById(R.id.medicLogin);
        register1=findViewById(R.id.registerTextView);
        register2=findViewById(R.id.registerTextView2);
        loginButton=findViewById(R.id.loginButton);
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);

    }

    public void medicRegister(View view){
        Intent intent=new Intent(getApplicationContext(),RegisterMedicActivity.class);
        startActivity(intent);
    }
}
