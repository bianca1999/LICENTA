package com.example.licenta.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.licenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterPatientActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference referencefirebaseDatabase;
    private FirebaseAnalytics firebaseAnalytics;
    private EditText lastName, firstName, email, phone, address, password;
    private ProgressDialog progressDialog;

    private DatePicker datePicker;
    private Button registerButton;
    private Spinner genderSpinner;
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
        genderSpinner=findViewById(R.id.genderSpinner);
        password=findViewById(R.id.password);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        progressDialog=new ProgressDialog(this);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String lastNameText=lastName.getText().toString();
            String firstNameText=firstName.getText().toString();
            String emailText=email.getText().toString();
            String phoneText=phone.getText().toString();
            String addressText=address.getText().toString();
            String genderText=genderSpinner.getSelectedItem().toString();
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

    private void registerPatient(final String lastNameText, final String firstNameText, final String emailText, final String phoneText, final String addressText, final String genderText,final String passwordText) {
        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                            String uid=current_user.getUid();
                            referencefirebaseDatabase=FirebaseDatabase.getInstance().getReference();
                            PatientModel patientModel=new PatientModel(firstNameText,lastNameText,emailText,addressText,phoneText,genderText);
                            referencefirebaseDatabase.child("Patients").child(uid).setValue(patientModel);
                            progressDialog.dismiss();
                            sendToLoginPage();
                        } else {
                            progressDialog.hide();
                            Toast.makeText(RegisterPatientActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    private void sendToLoginPage() {
        Intent intent=new Intent(getApplicationContext(), LoginPatientActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}
