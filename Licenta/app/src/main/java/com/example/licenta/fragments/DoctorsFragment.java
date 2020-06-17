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
import com.example.licenta.DoctorProfile;
import com.example.licenta.model.Doctor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsFragment extends Fragment {
    private RecyclerView doctorList;
    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String current_user_id;
    private View mainView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView= inflater.inflate(R.layout.fragment_doctors, container, false);
        doctorList=mainView.findViewById(R.id.doctorsList);

        firebaseAuth=FirebaseAuth.getInstance();
        current_user_id=firebaseAuth.getCurrentUser().getUid();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Doctors");

        doctorList.setHasFixedSize(true);
        doctorList.setLayoutManager(new LinearLayoutManager(getContext()));
        return mainView;

    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Doctors");

        FirebaseRecyclerOptions<Doctor> options =
                new FirebaseRecyclerOptions.Builder<Doctor>()
                        .setQuery(query, Doctor.class)
                        .build();
        FirebaseRecyclerAdapter<Doctor, DoctorsViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Doctor, DoctorsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DoctorsViewHolder holder, int position, @NonNull Doctor doctors) {
                holder.setName(doctors.getFirstName(),doctors.getLastName());
                holder.setSpecializare(doctors.getSpecializare());
                holder.setImage(doctors.getImage());

                final String doctor_id=getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), DoctorProfile.class);
                        intent.putExtra("doctor_id",doctor_id);

                        startActivity(intent);
                    }
                });
            }



            @NonNull
            @Override
            public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.doctor_simple_layout,parent,false);
                return new DoctorsViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        doctorList.setAdapter(firebaseRecyclerAdapter);

    }

    public class DoctorsViewHolder extends RecyclerView.ViewHolder{
        View view;

        public DoctorsViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setName(String firstName,String lastName){
            TextView doctorFirstName=view.findViewById(R.id.doctorName);
            doctorFirstName.setText("Dr. "+firstName+ " " + lastName);

        }
        public void setSpecializare(String specializare){
            TextView doctorSpecializare=view.findViewById(R.id.specializare);
            doctorSpecializare.setText(specializare);
        }

        public void setImage(String imageUri){
            ImageView doctorImage=view.findViewById(R.id.doctorImage);
            if(!imageUri.equals("default")){
                Picasso.with(getContext())
                        .load(imageUri)
                        .into(doctorImage);
            }
        }
    }
}



