package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.licenta.Adapter.MessageAdapter;
import com.example.licenta.model.Chat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WindowChatDoctorActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private TextView displayName, specializare;

    private ImageButton buttonSend, addButton;
    private EditText textSend;
    private RecyclerView messageList;
    private MessageAdapter messageAdapter;
    private List<Chat> chatList;
    private ImageView doctorImage;

    private static final int GALLERY_PICK = 1;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_chat_doctor);


        displayName = findViewById(R.id.displayName);
        specializare = findViewById(R.id.specializare);
        buttonSend = findViewById(R.id.sendBtn);
        textSend = findViewById(R.id.mesaj);
        doctorImage=findViewById(R.id.userImage);
        messageList = findViewById(R.id.messageRecView);
        messageList.setHasFixedSize(true);
        addButton = findViewById(R.id.addBtn);
        storageReference = FirebaseStorage.getInstance().getReference();



        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messageList.setLayoutManager(linearLayoutManager);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_user_id = currentUser.getUid();
        final String doctor_id = getIntent().getStringExtra("doctor_id");
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesaj = textSend.getText().toString();
                if (!mesaj.equals("")) {
                    sendMessage(doctor_id,current_user_id,mesaj);

                }
                textSend.setText("");
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galleryIntent, GALLERY_PICK);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctor_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.child("firstName").getValue().toString();
                String lastName = dataSnapshot.child("lastName").getValue().toString();
                String specializareT = dataSnapshot.child("specializare").getValue().toString();
                String title=dataSnapshot.child("title").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();

                Picasso.with(WindowChatDoctorActivity.this)
                        .load(image)
                        .into(doctorImage);
                displayName.setText("Dr. " + firstName + " " + lastName);
                specializare.setText(title+" "+specializareT);
                readMessage(current_user_id,doctor_id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void sendMessage(String receiver,String sender, String message){

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,String> chatObject=new HashMap<>();
        chatObject.put("sender",sender);
        chatObject.put("receiver",receiver);
        chatObject.put("message",message);

        databaseReference.child("Chats").push().setValue(chatObject);


    }

    public void readMessage(final String currend_user_id, final String user_id){
        chatList=new ArrayList<>();
        databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Chat chat=dataSnapshot1.getValue(Chat.class);
                    if(chat.getReceiver().equals(user_id)
                        && chat.getSender().equals(currend_user_id)||
                            chat.getReceiver().equals(currend_user_id)
                                    && chat.getSender().equals(user_id)){
                        chatList.add(chat);
                    }
                    messageAdapter=new MessageAdapter(WindowChatDoctorActivity.this,chatList);
                    messageList.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                final String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final StorageReference filepath = storageReference.child("message_files").child(current_user_id + ".jpg");

                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String doctor_id = getIntent().getStringExtra("doctor_id");
                                final String downloadUrl = uri.toString();
                                sendMessage(doctor_id,current_user_id,downloadUrl);
                            }
                        });
                    }
                });

            }
        }
    }
}
