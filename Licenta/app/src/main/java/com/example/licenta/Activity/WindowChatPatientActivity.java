package com.example.licenta.Activity;

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
import com.example.licenta.Model.Chat;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class WindowChatPatientActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView displayName;
    private ImageButton sendButton;
    private ImageButton addButton;
    private ImageButton callButton;
    private ImageView patientImage;
    private EditText textSend;
    private TextView phone;

    private RecyclerView messageList;
    private MessageAdapter messageAdapter;
    private List<Chat> chatList;

    private static final int GALLERY_PICK = 1;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_chat_patient);

        final String current_medic_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String patient_id = getIntent().getStringExtra("pacient_id");
        patientImage = findViewById(R.id.userImage);
        displayName = findViewById(R.id.displayName);
        sendButton = findViewById(R.id.sendBtn);
        addButton = findViewById(R.id.addBtn);
        textSend = findViewById(R.id.mesaj);
        messageList = findViewById(R.id.messageRecView);
        messageList.setHasFixedSize(true);
        callButton = findViewById(R.id.call);
        phone = findViewById(R.id.phone);
        storageReference = FirebaseStorage.getInstance().getReference();



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messageList.setLayoutManager(linearLayoutManager);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesaj = textSend.getText().toString();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
                String datetime = dateformat.format(c.getTime());

                if (!mesaj.equals("")) {
                    sendMessage(patient_id, current_medic_id, mesaj, datetime);

                    HashMap<String, String> chatObject1 = new HashMap<>();
                    chatObject1.put("id", patient_id);
                    chatObject1.put("time", datetime);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("UserMessages").child(current_medic_id).push().setValue(chatObject1);

                    HashMap<String, String> chatObject2 = new HashMap<>();
                    chatObject2.put("id", current_medic_id);
                    chatObject2.put("time", datetime);
                    databaseReference.child("UserMessages").child(patient_id).push().setValue(chatObject2);
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

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(patient_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.child("firstName").getValue().toString();
                String lastName = dataSnapshot.child("lastName").getValue().toString();
                String phoneText = dataSnapshot.child("phone").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                phone.setText(phoneText);
                if (lastName.endsWith("a")) {
                    displayName.setText("Dna. " + firstName + " " + lastName);
                } else {
                    displayName.setText("Dl. " + firstName + " " + lastName);

                }
                if (!image.equals("default")) {
                    Picasso.with(WindowChatPatientActivity.this)
                            .load(image)
                            .into(patientImage);
                }
                readMessage(patient_id, current_medic_id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone.getText().toString()));
                    startActivity(intent);
                }

            }
        });
    }

    public void sendMessage(String receiver, String sender, String message,String time) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, String> chatObject = new HashMap<>();
        chatObject.put("sender", sender);
        chatObject.put("receiver", receiver);
        chatObject.put("message", message);
        chatObject.put("time",time);
        databaseReference.child("Chats").push().setValue(chatObject);

    }

    public void readMessage(final String current_medic_id, final String patient_id) {
        chatList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Chat chat = dataSnapshot1.getValue(Chat.class);
                    if (chat.getReceiver().equals(patient_id)
                            && chat.getSender().equals(current_medic_id) ||
                            chat.getReceiver().equals(current_medic_id)
                                    && chat.getSender().equals(patient_id)) {
                        chatList.add(chat);
                    }
                    messageAdapter = new MessageAdapter(WindowChatPatientActivity.this, chatList);
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
                Calendar c=Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
                final String datetime = dateformat.format(c.getTime());
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String patient_id = getIntent().getStringExtra("pacient_id");
                                final String downloadUrl = uri.toString();
                                sendMessage(patient_id,current_user_id,downloadUrl,datetime);
                            }
                        });
                    }
                });

            }
        }
    }
}




