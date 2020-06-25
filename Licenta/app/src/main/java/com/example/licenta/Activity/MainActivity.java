package com.example.licenta.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.licenta.Login.LoginMedicActivity;
import com.example.licenta.Login.LoginPatientActivity;
import com.example.licenta.R;

public class MainActivity extends AppCompatActivity {
    public Button medicButton, pacientButton;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        medicButton = findViewById(R.id.medicButton);
        pacientButton = findViewById(R.id.pacientButton);
        toolbar=findViewById(R.id.main_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Aplicație de chat pentru domeniul medical");

    }
        public void medicLogin(View view){
            Intent intent=new Intent(getApplicationContext(), LoginMedicActivity.class);
            startActivity(intent);
        }

        public void pacientLogin(View view){
            Intent intent= new Intent(getApplicationContext(), LoginPatientActivity.class);
            startActivity(intent);
        }


}

