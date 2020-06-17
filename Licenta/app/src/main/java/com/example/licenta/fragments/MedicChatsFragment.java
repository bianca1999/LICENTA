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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.licenta.R;
import com.example.licenta.WindowChatDoctorActivity;
import com.example.licenta.WindowChatPatientActivity;
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
import com.squareup.picasso.Picasso;

public class MedicChatsFragment extends Fragment {
    private RecyclerView chatList;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    private View mainView;
    private String theLastMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_medic_chats, container, false);
        chatList = mainView.findViewById(R.id.reciclerView);
        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();
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
        FirebaseRecyclerAdapter<Chat, MedicChatsFragment.ChatsViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Chat, MedicChatsFragment.ChatsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final MedicChatsFragment.ChatsViewHolder holder, int position, @NonNull Chat chat) {
                        final String sender = chat.getSender();
                        final String receiver = chat.getReceiver();
                        if (sender.equals( current_user_id)) {
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(receiver);
                        } else {
                            if (receiver.equals(current_user_id))
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(sender);}


                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String firstName = dataSnapshot.child("firstName").getValue().toString();
                                    String lastName = dataSnapshot.child("lastName").getValue().toString();
                                    String image=dataSnapshot.child("image").getValue().toString();
                                    holder.pacientName.setText(firstName + " " + lastName);

                                    Picasso.with(getContext())
                                            .load(image)
                                            .into(holder.pacientImage);
                                    lastMessage(sender, holder.lastMsg);
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getContext(), WindowChatPatientActivity.class);
                                            intent.putExtra("pacient_id", sender);

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
                    public MedicChatsFragment.ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.doctor_simple_layout,parent,false);
                        return new MedicChatsFragment.ChatsViewHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        chatList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView pacientName;
        TextView lastMsg;
        ImageView pacientImage;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            pacientName=view.findViewById(R.id.doctorName);
            pacientImage=view.findViewById(R.id.doctorImage);
            lastMsg=view.findViewById(R.id.specializare);
        }
    }

    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid())
                                && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) &&
                                        chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }

                last_msg.setText(theLastMessage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

