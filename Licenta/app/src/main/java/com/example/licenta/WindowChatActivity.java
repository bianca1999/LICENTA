package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.licenta.model.Chat;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class WindowChatActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private TextView displayName, specializare;
    private ImageButton buttonSend;
    private EditText textSend;
    private RecyclerView messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_chat);

        displayName = findViewById(R.id.displayName);
        specializare = findViewById(R.id.specializare);
        buttonSend = findViewById(R.id.sendBtn);
        textSend = findViewById(R.id.mesaj);
        messageList = findViewById(R.id.messageRecView);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_user_id = currentUser.getUid();
        final String doctor_id = getIntent().getStringExtra("doctor_id");

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesaj = textSend.getText().toString();
                if (!mesaj.equals("")) {

                }
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctor_id);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesaj = textSend.getText().toString();
                if (!mesaj.equals("")) {
                    sendMessage(current_user_id, doctor_id, mesaj);
                }
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.child("firstName").getValue().toString();
                String lastName = dataSnapshot.child("lastName").getValue().toString();
                String specializareT = dataSnapshot.child("specializare").getValue().toString();
                displayName.setText("Dr. " + firstName + " " + lastName);
                specializare.setText(specializareT);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Chats");

        FirebaseRecyclerOptions<Chat> options =
                new FirebaseRecyclerOptions.Builder<Chat>()
                        .setQuery(query, Chat.class)
                        .build();
        FirebaseRecyclerAdapter<Chat, WindowChatActivity.ChatViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Chat, ChatViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(ChatViewHolder holder, int position, Chat chat) {
                        holder.setSender(chat.getSender());
                        holder.setReceiver(chat.getReceiver());
                        holder.setMessage(chat.getMessage());
                        final String doctor_id = getRef(position).getKey();
                        holder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(WindowChatActivity.this, WindowChatActivity.class);
                                intent.putExtra("doctor_id", doctor_id);

                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.doctor_simple_layout, parent, false);
                        return new ChatViewHolder(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        messageList.setAdapter(firebaseRecyclerAdapter);

    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
           public void setSender(String sender){
            //TextView sender=view.findViewById(R.id.doctorName);
        }

        public void setReceiver(String receiver){
            //TextView sender=view.findViewById(R.id.doctorName);
        }

        public void setMessage(String message){
            //TextView sender=view.findViewById(R.id.doctorName);
        }
    }


    public void sendMessage(String receiver,String sender, String message){

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,String> chatObject=new HashMap<>();
        chatObject.put("sender",sender);
        chatObject.put("receiver",receiver);
        chatObject.put("message",message);

        databaseReference.child("Chats").push().setValue(chatObject);

    }
}
