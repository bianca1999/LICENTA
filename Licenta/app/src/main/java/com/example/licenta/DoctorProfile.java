package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class DoctorProfile extends AppCompatActivity {
    private TextView doctorEmail, doctorPhone, doctorName, specializare, doctorAddress, recenzie;
    private DatabaseReference databaseReference;
    private Button messageButton,ratingButton;
    private ImageView doctorImage;



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
        messageButton=findViewById(R.id.intrebare);
        ratingButton=findViewById(R.id.rating);
        recenzie=findViewById(R.id.recenzie);
        doctorImage=findViewById(R.id.doctorImage);

        showRating(doctor_id);
        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRating(doctor_id);
                showRating(doctor_id);
            }
        });
       messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), WindowChatDoctorActivity.class);
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
                String image=dataSnapshot.child("image").getValue().toString();

                doctorName.setText("Dr. "+firstName+ " " +lastName);
                doctorPhone.setText(phone);
                doctorEmail.setText(email);
                doctorAddress.setText(address);
                specializare.setText(specializareD);
                if(!image.equals("default")) {
                    Picasso.with(DoctorProfile.this)
                            .load(image)
                            .resize(160, 160)
                            .into(doctorImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setRating(final String doctor_id){
        final String current_user_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.rating_layout, null);
        final EditText reviewEditText=view.findViewById(R.id.recenzieEditText);
        RatingBar ratingBar=view.findViewById(R.id.ratingBar);
        final TextView ratingScore=view.findViewById(R.id.scoreRating);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingScore.setText(String.valueOf(rating));

            }
        });
        dialog.setView(view).setPositiveButton("Trimite", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reviewText=reviewEditText.getText().toString();
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
                if(reviewText.isEmpty()){
                    HashMap<String,String> chatObject=new HashMap<>();
                    chatObject.put("pacient",current_user_id);
                    chatObject.put("medic",doctor_id);
                    chatObject.put("numar_stele",ratingScore.getText().toString());
                    databaseReference.child("Ratings").push().setValue(chatObject);
                }
                else{
                    HashMap<String,String> chatObject=new HashMap<>();
                    chatObject.put("pacient",current_user_id);
                    chatObject.put("medic",doctor_id);
                    chatObject.put("numar_stele",ratingScore.getText().toString());
                    chatObject.put("recenzire",reviewText);
                    databaseReference.child("Ratings").push().setValue(chatObject);

                }
            }
        });

        AlertDialog alertDialog=dialog.create();
        alertDialog.show();

    }

    public void showRating(final String doctor_id){
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Ratings");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    float count=0,sum=0;
                    for(DataSnapshot dtSnapshot:dataSnapshot.getChildren()){
                        if(dtSnapshot.child("medic").getValue().toString().equals(doctor_id)){
                            sum=sum+ Float.parseFloat(dtSnapshot.child("numar_stele").getValue().toString());
                            count++;
                        }
                    }
                    sum=sum/count;
                    String result;
                    if(sum!=0){
                        result=String.valueOf(sum);
                        recenzie.setText(result);
                    }
                    else{
                        recenzie.setText("Nicio recenzie");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }
}
