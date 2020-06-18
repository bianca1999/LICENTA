package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CallVideoActivity extends AppCompatActivity {
    private TextView nameContact;
    private ImageView imageContact, stopCallButton, answerCallButton;
    private String receiverUserId, receiverUserImage,  receiverUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_video);


        imageContact.findViewById(R.id.imageUser);
        nameContact.findViewById(R.id.nameUser);
        stopCallButton.findViewById(R.id.stopCall);
        answerCallButton.findViewById(R.id.answerCall);
        final String doctor_id=getIntent().getStringExtra("doctor_id");
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctor_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstName=dataSnapshot.child("firstName").getValue().toString();
                String lastName=dataSnapshot.child("lastName").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();

                nameContact.setText("Dr. "+firstName+ " "+lastName);
                if(!image.equals("default")){
                    Picasso.with(CallVideoActivity.this)
                            .load(image)
                            .into(imageContact);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        stopCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        answerCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }
}
