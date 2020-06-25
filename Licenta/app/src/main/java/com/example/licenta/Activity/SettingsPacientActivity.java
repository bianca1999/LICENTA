package com.example.licenta.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.licenta.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class SettingsPacientActivity extends AppCompatActivity {

    private Button editProfileButton, changePhotoButton;
    private ImageView userImage;
    private DatabaseReference databaseReference;
    private TextView userEmail, userAddress, userGender, userPhone, userBirthday, userName;
    private static final int GALLERY_PICK=1;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_pacient);
        userEmail=findViewById(R.id.email);
        userAddress=findViewById(R.id.address);
        userGender=findViewById(R.id.gender);
        userPhone=findViewById(R.id.phone);
        userName=findViewById(R.id.userName);
        userImage=findViewById(R.id.userImage);
        storageReference=FirebaseStorage.getInstance().getReference();


        changePhotoButton=findViewById(R.id.changePhoto);
        editProfileButton=findViewById(R.id.editProfile);
        final String current_user_id= FirebaseAuth.getInstance().getCurrentUser().getUid();

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile(current_user_id);


            }
        });

        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhoto();
            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Patients").child(current_user_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String image=dataSnapshot.child("image").getValue().toString();
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
            if(!image.equals("default")){
                Picasso.with(SettingsPacientActivity.this)
                        .load(image)
                        .resize(160,160)
                        .into(userImage);

            }

        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

}

    private void changePhoto() {
       Intent galleryIntent=new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent,GALLERY_PICK);
    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
           Uri imageUri = data.getData();
           CropImage.activity(imageUri)
                   .setAspectRatio(1, 1)
                   .start(this);

       }
       if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
           CropImage.ActivityResult result = CropImage.getActivityResult(data);
           if (resultCode == RESULT_OK) {
               Uri resultUri = result.getUri();
               final String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
               final StorageReference filepath = storageReference.child("images").child(current_user_id + ".jpg");

            filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String downloadUrl=uri.toString();
                            databaseReference.child("image").setValue(downloadUrl);
                        }
                    });
                }
            });

           }
       }
   }

    private void editProfile(final String current_user_id) {
        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.settings_pacient_profile_layout, null);

        final EditText firstNameEditText=view.findViewById(R.id.nume);
        final EditText lastNameEditText=view.findViewById(R.id.prenume);
        final EditText addressEditText=view.findViewById(R.id.adresa);
        final EditText phoneEditText=view.findViewById(R.id.telefon);
        dialog.setCancelable(true);

        dialog.setView(view).setPositiveButton("Salveaza", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Patients").child(current_user_id);

                String firstNameText = firstNameEditText.getText().toString();
                String lastNameText = lastNameEditText.getText().toString();
                String addressText = addressEditText.getText().toString();
                String phoneText = phoneEditText.getText().toString();

                if (!firstNameText.isEmpty()) {
                    databaseReference.child("firstName").setValue(firstNameText);
                }
                if (!lastNameText.isEmpty()) {
                    databaseReference.child("lastName").setValue(lastNameText);
                }
                if (!addressText.isEmpty()) {
                    databaseReference.child("address").setValue(addressText);
                }
                if (!phoneText.isEmpty()) {
                    databaseReference.child("phone").setValue(phoneText);
                }
            }


        });

        AlertDialog alertDialog=dialog.create();
        alertDialog.show();

    }

}


