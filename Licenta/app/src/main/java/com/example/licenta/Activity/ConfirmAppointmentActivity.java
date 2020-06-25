package com.example.licenta.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.licenta.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ConfirmAppointmentActivity extends AppCompatActivity {
    private TextView doctor, pacient,data, specializare;
    private Button sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_appointment_layout);
        doctor=findViewById(R.id.doctorName);
        pacient=findViewById(R.id.patientName);
        data=findViewById(R.id.data);
        specializare=findViewById(R.id.specializare);
        sendButton=findViewById(R.id.send);

        final String doctor_id=getIntent().getStringExtra("doctor_id");
        final String patient_id=getIntent().getStringExtra("patient_id");
        final String dataText=getIntent().getStringExtra("data");
        data.setText("Data: "+ dataText);

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Doctors").child(doctor_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String doctorName = "Doctor: "
                            + dataSnapshot.child("firstName").getValue().toString()
                            + " "
                            + dataSnapshot.child("lastName").getValue().toString();
                    String specializareText = "Sectia: "+ dataSnapshot.child("specializare").getValue().toString();
                    doctor.setText(doctorName);
                    specializare.setText(specializareText);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference("Patients").child(patient_id);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String patientName = "Pacient: "
                            + dataSnapshot.child("firstName").getValue().toString()
                            + " "
                            + dataSnapshot.child("lastName").getValue().toString();
                    pacient.setText(patientName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAnAppointment(patient_id,doctor_id,dataText);
                Intent intent=new Intent(ConfirmAppointmentActivity.this, StartPatientActivity.class);
                startActivity(intent);
            }
        });

    }
    private void makeAnAppointment(String patientId, final String doctorId, String date) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments").child(doctorId);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("patient", patientId);
        hashMap.put("date", date);
        databaseReference.push().setValue(hashMap);
    }
}
