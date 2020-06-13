package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.licenta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientProfile extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    ImageView userImage;
    TextView userEmail, userAddress, userGender, userPhone, userBirthday, userName;
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

        final String current_id=getIntent().getStringExtra("patient_id");
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        String current_user_uid=currentUser.getUid();
        if(!current_id.isEmpty()){
            databaseReference= FirebaseDatabase.getInstance().getReference().child("Patients").child(current_id);
        }
        else{
            databaseReference= FirebaseDatabase.getInstance().getReference().child("Patients").child(current_user_uid);

        }



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstName=dataSnapshot.child("firstName").getValue().toString();
                String lastName=dataSnapshot.child("lastName").getValue().toString();
                String address=dataSnapshot.child("address").getValue().toString();
                String email=dataSnapshot.child("email").getValue().toString();
                String phone=dataSnapshot.child("phone").getValue().toString();
                String gender=dataSnapshot.child("gender").getValue().toString();

                userName.setText(firstName+" "+lastName);
                userAddress.setText(address);
                userEmail.setText(email);
                userPhone.setText(phone);
                userGender.setText(gender);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
