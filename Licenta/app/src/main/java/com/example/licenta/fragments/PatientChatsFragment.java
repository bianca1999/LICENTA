package com.example.licenta.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.licenta.DoctorProfile;
import com.example.licenta.R;
import com.example.licenta.WindowChatActivity;
import com.example.licenta.model.Chat;
import com.example.licenta.model.ChatList;
import com.example.licenta.model.Doctor;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientChatsFragment extends Fragment {
    private RecyclerView chatList;
    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String current_user_id;
    private View mainView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView= inflater.inflate(R.layout.fragment_chats, container, false);
        chatList=mainView.findViewById(R.id.reciclerView);

        firebaseAuth=FirebaseAuth.getInstance();

        current_user_id=firebaseAuth.getCurrentUser().getUid();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Chats");

        chatList.setHasFixedSize(true);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));
        return mainView;

    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Chats");

        FirebaseRecyclerOptions<Chat> options =
                new FirebaseRecyclerOptions.Builder<Chat>()
                        .setQuery(query, Chat.class)
                        .build();
        FirebaseRecyclerAdapter<Chat, PatientChatsFragment.ChatsViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Chat, PatientChatsFragment.ChatsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PatientChatsFragment.ChatsViewHolder holder, int position, @NonNull Chat chat) {

                final String receiver=chat.getReceiver();
                final String message=chat.getMessage();
                databaseReference=FirebaseDatabase.getInstance().getReference().child("Doctors").child(receiver);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String firstName=dataSnapshot.child("firstName").getValue().toString();
                        String lastName=dataSnapshot.child("lastName").getValue().toString();
                        holder.setName(firstName,lastName);
                        holder.setLastMessage(message);
                        holder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), WindowChatActivity.class);
                                intent.putExtra("doctor_id",receiver);

                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }



            @NonNull
            @Override
            public PatientChatsFragment.ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.doctor_simple_layout,parent,false);
                return new PatientChatsFragment.ChatsViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        chatList.setAdapter(firebaseRecyclerAdapter);

    }

    public class ChatsViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }

        public void setName(String firstName,String lastName){
            TextView doctorName=view.findViewById(R.id.doctorName);
            doctorName.setText("Dr. "+firstName+ " " + lastName);

        }
        public void setLastMessage(String message){
            TextView doctorMessage=view.findViewById(R.id.specializare);
            doctorMessage.setText(message);
        }
    }
}