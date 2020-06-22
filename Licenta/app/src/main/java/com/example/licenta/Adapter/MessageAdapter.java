package com.example.licenta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.R;
import com.example.licenta.Model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {


    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;
    private Context context;
    private List<Chat> chats;


    public MessageAdapter(Context context, List<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.message_right_layout, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.message_left_layout, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if (chat.getMessage().startsWith("http")) {
            Picasso.with(context)
                    .load(chat.getMessage())
                    .into(holder.showImage);
            holder.profileImage.setVisibility(View.GONE);
            holder.showMessage.setVisibility(View.GONE);
        } else {
            holder.showMessage.setText(chat.getMessage());
            holder.showImage.setVisibility(View.GONE);

            String receiver = chat.getReceiver();
            if (receiver.equals(firebaseUser.getUid())) {
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(chat.getSender());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String image = dataSnapshot.child("image").getValue().toString();
                            if (!image.equals("default")) {
                                Picasso.with(context)
                                        .load(image)
                                        .into(holder.profileImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Patients").child(chat.getSender());
                databaseReference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String image = dataSnapshot.child("image").getValue().toString();
                            if (!image.equals("default")) {
                                Picasso.with(context)
                                        .load(image)
                                        .into(holder.profileImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

    }
    @Override
    public int getItemCount() {
        return chats.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView showMessage;
        private ImageView profileImage;
        private ImageView showImage;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessage=itemView.findViewById(R.id.showMsg);
            profileImage=itemView.findViewById(R.id.profieImage);
            showImage=itemView.findViewById(R.id.showImg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(chats.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
