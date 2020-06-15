package com.example.licenta.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.R;
import com.example.licenta.login.LoginMedicActivity;
import com.example.licenta.model.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterMedicActivity extends AppCompatActivity {
    private TextView titleTextView, specializareTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference referencefirebaseDatabase;
    private EditText lastName, firstName, email, phone, address, password;
    private Spinner specializariSpinner, titleSpinner;
    private Button medicRegisterButton;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_medic);
        titleTextView=findViewById(R.id.registerDoctor);
        specializareTextView=findViewById(R.id.specializareTextView);
        lastName=findViewById(R.id.lastNameEditText);
        firstName=findViewById(R.id.firstNameEditText);
        email=findViewById(R.id.emailEditText);
        phone=findViewById(R.id.phoneEditText);
        address=findViewById(R.id.adressEditText);
        password=findViewById((R.id.password));
        medicRegisterButton=findViewById(R.id.registerButton);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        specializariSpinner=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> specializariAdapter=ArrayAdapter.createFromResource(this,R.array.Specializari, android.R.layout.simple_spinner_item);
        specializariAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specializariSpinner.setAdapter(specializariAdapter);

        titleSpinner=findViewById(R.id.titluSpinner);
        ArrayAdapter<CharSequence> titleAdapter=ArrayAdapter.createFromResource(this,R.array.title, android.R.layout.simple_spinner_item);
        specializariAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        titleSpinner.setAdapter(titleAdapter);

        titleSpinner.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }));


        specializariSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        medicRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastNameText=lastName.getText().toString();
                String firstNameText=firstName.getText().toString();
                String emailText=email.getText().toString();
                String phoneText=phone.getText().toString();
                String addressText=address.getText().toString();
                String passwordText=password.getText().toString();
                String specializareText=specializariSpinner.getSelectedItem().toString();
                String titleText=titleSpinner.getSelectedItem().toString();

                if(!TextUtils.isEmpty(lastNameText)
                        || !TextUtils.isEmpty(firstNameText)
                        || !TextUtils.isEmpty(emailText)
                        || !TextUtils.isEmpty(phoneText)
                        ||!TextUtils.isEmpty(addressText)
                        ||!TextUtils.isEmpty(passwordText)) {
                    progressDialog.setTitle("Inregistrare medic");
                    progressDialog.setMessage("Va rugam sa asteptati pentru a inregistra datele dumneavoastra...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    registerMedic(lastNameText, firstNameText, emailText, phoneText, addressText, specializareText,titleText, passwordText);
                }
                else{
                    Toast.makeText(RegisterMedicActivity.this, "Va rugam sa completati toate campurile!",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void registerMedic(final String lastNameText,final String firstNameText,final String emailText, final String phoneText,final String addressText, final String specializareText,final String titleText,final String passwordText) {
        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                            if(current_user != null) {
                                String uid = current_user.getUid();
                                referencefirebaseDatabase = FirebaseDatabase.getInstance().getReference();
                                Doctor doctor = new Doctor(firstNameText, lastNameText, emailText, addressText, phoneText, specializareText,titleText);
                                referencefirebaseDatabase.child("Doctors").child(uid).setValue(doctor);
                                progressDialog.dismiss();
                                sendToLoginPage();
                            }

                        } else {
                            progressDialog.hide();
                            Toast.makeText(RegisterMedicActivity.this, "Inregintrare fara succes!",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private void sendToLoginPage() {
        Intent intent=new Intent(getApplicationContext(), LoginMedicActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
