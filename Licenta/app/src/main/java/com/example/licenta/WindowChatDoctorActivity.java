package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.licenta.Adapter.MessageAdapter;
import com.example.licenta.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WindowChatDoctorActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private TextView displayName, specializare;
    private ImageButton buttonSend;
    private EditText textSend;
    private RecyclerView messageList;
    private MessageAdapter messageAdapter;
    private List<Chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_chat);


        displayName = findViewById(R.id.displayName);
        specializare = findViewById(R.id.specializare);
        buttonSend = findViewById(R.id.sendBtn);
        textSend = findViewById(R.id.mesaj);
        messageList = findViewById(R.id.messageRecView);
        messageList.setHasFixedSize(true);

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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctor_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.child("firstName").getValue().toString();
                String lastName = dataSnapshot.child("lastName").getValue().toString();
                String specializareT = dataSnapshot.child("specializare").getValue().toString();
                displayName.setText("Dr. " + firstName + " " + lastName);
                specializare.setText(specializareT);
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


}
