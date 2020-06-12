package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.licenta.R;
import com.example.licenta.WindowChatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorProfile extends AppCompatActivity {
    private TextView doctorEmail, doctorPhone, doctorName, specializare, doctorAddress;
    private DatabaseReference databaseReference;
    private Button message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        final String doctor_id=getIntent().getStringExtra("doctor_id");
        doctorName=findViewById(R.id.doctorName);
        doctorPhone=findViewById(R.id.phone);
        doctorEmail=findViewById(R.id.email);
        doctorAddress=findViewById(R.id.address);
        specializare=findViewById(R.id.specializare);
        message=findViewById(R.id.intrebare);

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), WindowChatActivity.class);
                intent.putExtra("doctor_id",doctor_id);
                startActivity(intent);
            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctor_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstName=dataSnapshot.child("firstName").getValue().toString();
                String lastName=dataSnapshot.child("lastName").getValue().toString();
                String address=dataSnapshot.child("address").getValue().toString();
                String email=dataSnapshot.child("email").getValue().toString();
                String phone=dataSnapshot.child("phone").getValue().toString();
                String specializareD=dataSnapshot.child("specializare").getValue().toString();

                doctorName.setText("Dr. "+firstName+ " " +lastName);
                doctorPhone.setText(phone);
                doctorEmail.setText(email);
                doctorAddress.setText(address);
                specializare.setText(specializareD);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
