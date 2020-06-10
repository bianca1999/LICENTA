package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterPatientActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    EditText lastName, firstName, email, phone, address, password;
    ProgressDialog progressDialog;

    DatePicker datePicker;
    Button registerButton;
    RadioGroup radioGroupGender;
    RadioButton male, female, curentGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);
        lastName=findViewById(R.id.lastNameEditText);
        firstName=findViewById(R.id.firstNameEditText);
        email=findViewById(R.id.emailEditText);
        phone=findViewById(R.id.phoneEditText);
        address=findViewById(R.id.adressEditText);
        datePicker=findViewById(R.id.datePicker1);
        registerButton=findViewById(R.id.registerButton);
        radioGroupGender=findViewById(R.id.genderRadioGroup);
        male=findViewById(R.id.maleRadioButton);
        female=findViewById(R.id.femaleRadioButton);
        password=findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        progressDialog=new ProgressDialog(this);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String lastNameText=lastName.getText().toString();
            String firstNameText=firstName.getText().toString();
            String emailText=email.getText().toString();
            String phoneText=phone.getText().toString();
            String addressText=address.getText().toString();
            int radioID=radioGroupGender.getCheckedRadioButtonId();
            curentGender=findViewById(radioID);
            String genderText=curentGender.getText().toString();
            String passwordText=password.getText().toString();

            if(!TextUtils.isEmpty(lastNameText)
                    || !TextUtils.isEmpty(firstNameText)
                    || !TextUtils.isEmpty(emailText)
                    || !TextUtils.isEmpty(phoneText)
                    ||!TextUtils.isEmpty(addressText)
                    ||!TextUtils.isEmpty(genderText)
                    ||!TextUtils.isEmpty(passwordText)) {
                progressDialog.setTitle("Registering patient");
                progressDialog.setMessage("Please wait a second...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                registerPatient(lastNameText, firstNameText, emailText, phoneText, addressText, genderText, passwordText);
            }
            else{
                Toast.makeText(RegisterPatientActivity.this, "Please fill all boxes.",
                        Toast.LENGTH_SHORT).show();

            }
                /*Intent intent=new Intent(getApplicationContext(),PatientProfile.class);
                String userName=""+firstNameText+" "+lastNameText;
                intent.putExtra("UserName",userName);
                intent.putExtra("email",emailText);
                intent.putExtra("phone",phoneText);
                intent.putExtra("address",addressText);
                intent.putExtra("gender",curentGender.getText().toString());
                startActivity(intent);*/
            }
        });
    }

    private void registerPatient(final String lastNameText, final String firstNameText, String emailText, final String phoneText, final String addressText, final String genderText, String passwordText) {
        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            mFirebaseAnalytics.setUserProperty("address",addressText);
                            mFirebaseAnalytics.setUserProperty("gender",genderText);
                            mFirebaseAnalytics.setUserProperty("phone",phoneText);
                            mFirebaseAnalytics.setUserProperty("firstName",firstNameText);
                            mFirebaseAnalytics.setUserProperty("lastName",lastNameText);
                            sendToLoginPage();

                        } else {
                            progressDialog.hide();
                            Toast.makeText(RegisterPatientActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    public String checkGenderButton(View view){
        int radioId=radioGroupGender.getCheckedRadioButtonId();
        curentGender.findViewById(radioId);
        return curentGender.getText().toString();
    }


    private void sendToLoginPage() {
        Intent intent=new Intent(getApplicationContext(),LoginPatientActivity.class);
        startActivity(intent);
        finish();
    }


}
