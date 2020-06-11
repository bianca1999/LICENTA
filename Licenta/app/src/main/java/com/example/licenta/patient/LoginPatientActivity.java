package com.example.licenta.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginPatientActivity extends AppCompatActivity {
    private TextView titleTextView, register1, register2;
    private Button loginButton;
    private EditText emailEditText, passwordEditText;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_patient);

        mAuth = FirebaseAuth.getInstance();

        titleTextView=findViewById(R.id.pacientLogin);
        register1=findViewById(R.id.registerTextView);
        register2=findViewById(R.id.registerTextView2);
        loginButton=findViewById(R.id.loginButton);
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        toolbar=findViewById(R.id.main_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient LOGIN");

        progressDialog=new ProgressDialog(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText=emailEditText.getText().toString();
                String passwordText=passwordEditText.getText().toString();
                if(!TextUtils.isEmpty(emailText) || !TextUtils.isEmpty(passwordText)){
                    progressDialog.setTitle("Loging In");
                    progressDialog.setMessage("Please wait to login...!");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    loginPatient(emailText, passwordText);
                }
                else{
                    Toast.makeText(LoginPatientActivity.this, "Please fill all boxes.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void loginPatient(String emailText, String passwordText) {
        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Intent intent=new Intent(getApplicationContext(), StartPatientActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            progressDialog.hide();
                            Toast.makeText(LoginPatientActivity.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        }
                });
    }


    public void patientRegister(View view){
        Intent intent=new Intent(getApplicationContext(), RegisterPatientActivity.class);
        startActivity(intent);
    }
}
