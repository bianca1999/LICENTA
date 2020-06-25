package com.example.licenta.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.licenta.Activity.StartMedicActivity;
import com.example.licenta.R;
import com.example.licenta.Register.RegisterMedicActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginMedicActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_medic);
        loginButton = findViewById(R.id.button);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        progressDialog=new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = emailEditText.getText().toString();
                String passwordText = passwordEditText.getText().toString();
                if (!TextUtils.isEmpty(emailText) || !TextUtils.isEmpty(passwordText)) {
                    progressDialog.setTitle("Logare Medic");
                    progressDialog.setMessage("Va rugam sa asteptati cateva momente...!");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    loginDoctor(emailText, passwordText);
                } else {
                    Toast.makeText(LoginMedicActivity.this, "Va rugam completati toate campurile!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void loginDoctor(String emailText, String passwordText) {
        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), StartMedicActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            progressDialog.hide();
                            Toast.makeText(LoginMedicActivity.this, "Logarea nu a reusit!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    public void medicRegister(View view){
        Intent intent=new Intent(getApplicationContext(), RegisterMedicActivity.class);
        startActivity(intent);
    }
}
