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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.licenta.R;
import com.example.licenta.WindowChatDoctorActivity;
import com.example.licenta.WindowChatPatientActivity;
import com.example.licenta.model.Chat;
import com.example.licenta.model.User;
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

import java.util.ArrayList;
import java.util.List;

public class MedicChatsFragment extends Fragment {
    private RecyclerView chatList;
    private DatabaseReference databaseReference;
    private String current_medic_id;
    private View mainView;
    private String theLastMessage,time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_medic_chats, container, false);
        chatList = mainView.findViewById(R.id.reciclerView);
        current_medic_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        chatList.setHasFixedSize(true);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));
        return mainView;

    }


    @Override
    public void onStart() {
        super.onStart();
        final List<String> patients=new ArrayList<>();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("UserMessages")
                .child(current_medic_id);

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();
        FirebaseRecyclerAdapter<User, MedicChatsFragment.ChatsViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<User, MedicChatsFragment.ChatsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final MedicChatsFragment.ChatsViewHolder holder, int position, @NonNull User user) {

                        final String userId = user.getId();
                        if(!patients.contains(userId)){
                            patients.add(userId);
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(userId);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String firstName = dataSnapshot.child("firstName").getValue().toString();
                                    String lastName = dataSnapshot.child("lastName").getValue().toString();
                                    String image=dataSnapshot.child("image").getValue().toString();
                                    holder.pacientName.setText(firstName + " " + lastName);
                                    if(!image.equals("default")){
                                        Picasso.with(getContext())
                                                .load(image)
                                                .into(holder.pacientImage);
                                    }
                                    lastMessage(userId, holder.lastMsg,holder.lastTime);
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getContext(), WindowChatPatientActivity.class);
                                            intent.putExtra("pacient_id", userId);
                                            startActivity(intent);
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                        else {
                            holder.view.setVisibility(View.GONE);
                            holder.view.setLayoutParams(new RelativeLayout.LayoutParams(0,0));
                        }

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
        private View view;
        private TextView pacientName;
        private TextView lastMsg,lastTime;
        private ImageView pacientImage;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            pacientName=view.findViewById(R.id.doctorName);
            pacientImage=view.findViewById(R.id.doctorImage);
            lastMsg=view.findViewById(R.id.specializare);
            lastTime=view.findViewById(R.id.time);
        }
    }

    private void lastMessage(final String patientId, final TextView last_msg,final TextView last_time){
        theLastMessage = "default";
        time="";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid())
                                && chat.getSender().equals(patientId) ||
                                chat.getReceiver().equals(patientId) &&
                                        chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                            time=chat.getTime();
                        }
                    }
                }

                last_msg.setText(theLastMessage);
                last_time.setText(time);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

