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
import com.example.licenta.model.Chat;
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


    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private Context context;
    private List<Chat> chats;
    FirebaseUser firebaseUser;

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
        if (chat.getMessage().startsWith("http")) {
            Picasso.with(context)
                    .load(chat.getMessage())
                    .into(holder.showImage);
            holder.showMessage.setVisibility(View.GONE);
        } else {
            holder.showMessage.setText(chat.getMessage());
            holder.showImage.setVisibility(View.GONE);
            String sender= chat.getSender();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(sender);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     //String image=dataSnapshot.child("image").getValue().toString();
                     //Picasso.with(context)
                     //.load(image)
                     //.into(holder.profileImage);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return chats.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView showMessage;
        public ImageView profileImage;
        public ImageView showImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessage=itemView.findViewById(R.id.showMsg);
            profileImage=itemView.findViewById(R.id.profieImage);
            showImage=itemView.findViewById(R.id.showImg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
