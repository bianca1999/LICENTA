package com.example.licenta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class PatientProfile extends AppCompatActivity {
    ImageView userImage;
    TextView userEmail, userAddress, userGender, userPhone, userBirthday, userName;
    String userNameText, userEmailText, userAddressText, userGenderText,userPhoneText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        userImage=findViewById(R.id.userImage);
        userEmail=findViewById(R.id.email);
        userAddress=findViewById(R.id.address);
        userGender=findViewById(R.id.gender);
        userPhone=findViewById(R.id.phone);
        userBirthday=findViewById(R.id.birthday);
        userName=findViewById(R.id.userName);

        Intent intent= getIntent();
        userNameText=intent.getStringExtra("UserName");
        userName.setText(userNameText);

        userAddressText=intent.getStringExtra("address");
        userAddress.setText(userAddressText);

        userGenderText=intent.getStringExtra("gender");
        userGender.setText(userGenderText);

        userPhoneText=intent.getStringExtra("phone");
        userPhone.setText(userPhoneText);

        userEmailText=intent.getStringExtra("email");
        userEmail.setText(userEmailText);

    }
}
