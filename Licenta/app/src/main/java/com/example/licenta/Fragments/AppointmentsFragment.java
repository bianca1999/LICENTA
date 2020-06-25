package com.example.licenta.Fragments;

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

import com.example.licenta.Model.Appointment;
import com.example.licenta.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentsFragment extends Fragment {
    private RecyclerView patientList;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    private View mainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView=inflater.inflate(R.layout.fragment_appointments, container, false);
        patientList=mainView.findViewById(R.id.recyclerView);
        current_user_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Appointments");
        patientList.setHasFixedSize(true);
        patientList.setLayoutManager(new LinearLayoutManager(getContext()));

        return mainView;
    }
    @Override
    public void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Appointments")
                .child(current_user_id);

        FirebaseRecyclerOptions<Appointment> options =
                new FirebaseRecyclerOptions.Builder<Appointment>()
                        .setQuery(query, Appointment.class)
                        .build();
        FirebaseRecyclerAdapter<Appointment, AppointmentsFragment.PatientViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Appointment, AppointmentsFragment.PatientViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final AppointmentsFragment.PatientViewHolder holder, int position, @NonNull final Appointment appointment) {
                        databaseReference=FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Patients")
                                .child(appointment.getPatient());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String firstName = dataSnapshot.child("firstName").getValue().toString();
                                String lastName = dataSnapshot.child("lastName").getValue().toString();
                                String phone=dataSnapshot.child("phone").getValue().toString();
                                String image = dataSnapshot.child("image").getValue().toString();
                                holder.setName(firstName,lastName);
                                holder.setPhone(phone);
                                holder.setImage(image);
                                holder.setDate(appointment.getDate());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public AppointmentsFragment.PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.doctor_simple_layout,parent,false);
                        return new AppointmentsFragment.PatientViewHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        patientList.setAdapter(firebaseRecyclerAdapter);

    }

    public class PatientViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView patientName;
        ImageView patientImage;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setName(String firstName,String lastName){
            patientName=view.findViewById(R.id.doctorName);
            patientName.setText(firstName+ " " + lastName);

        }
        public void setPhone(String phone){
            TextView patientPhone=view.findViewById(R.id.specializare);
            patientPhone.setText(phone);
        }

        public void setDate(String date){
            TextView appointmentDate=view.findViewById(R.id.data);
            appointmentDate.setVisibility(View.VISIBLE);
            appointmentDate.setText(" - "+date);

        }

        public void setImage(String imageUri){
            patientImage=view.findViewById(R.id.doctorImage);
            if(!imageUri.equals("default")){
                Picasso.with(getContext())
                        .load(imageUri)
                        .into(patientImage);
            }
        }

    }
}


