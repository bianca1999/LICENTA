package com.example.licenta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public Button medicButton, pacientButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        medicButton = findViewById(R.id.medicButton);
        pacientButton = findViewById(R.id.pacientButton);
    }
        public void medicLogin(View view){
            Intent intent=new Intent(getApplicationContext(),LoginMedicActivity.class);
            startActivity(intent);
        }

        public void pacientLogin(View view){
            Intent intent= new Intent(getApplicationContext(), LoginPatientActivity.class);
            startActivity(intent);
        }
}

