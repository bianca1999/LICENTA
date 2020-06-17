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

import com.example.licenta.DoctorProfile;
import com.example.licenta.PatientProfile;
import com.example.licenta.R;
import com.example.licenta.model.Doctor;
import com.example.licenta.model.Patient;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class PatientsFragment extends Fragment {

    private RecyclerView patientList;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    private View mainView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView= inflater.inflate(R.layout.fragment_patients, container, false);
        patientList=mainView.findViewById(R.id.patientList);
        firebaseAuth=FirebaseAuth.getInstance();
        current_user_id=firebaseAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Patients");
        patientList.setHasFixedSize(true);
        patientList.setLayoutManager(new LinearLayoutManager(getContext()));
        return mainView;

    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Patients");

        FirebaseRecyclerOptions<Patient> options =
                new FirebaseRecyclerOptions.Builder<Patient>()
                        .setQuery(query, Patient.class)
                        .build();
        FirebaseRecyclerAdapter<Patient, PatientsFragment.PatientViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Patient, PatientsFragment.PatientViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PatientsFragment.PatientViewHolder holder, int position, @NonNull Patient patient) {
                holder.setName(patient.getFirstName(),patient.getLastName());
                holder.setEmail(patient.getEmail());
                holder.setImage(patient.getImage());
                final String patient_id=getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), PatientProfile.class);
                        intent.putExtra("patient_id",patient_id);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public PatientsFragment.PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.doctor_simple_layout,parent,false);
                return new PatientsFragment.PatientViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        patientList.setAdapter(firebaseRecyclerAdapter);

    }

    public class PatientViewHolder extends RecyclerView.ViewHolder{
        View view;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setName(String firstName,String lastName){
            TextView doctorFirstName=view.findViewById(R.id.doctorName);
            if(lastName.endsWith("a")){
                doctorFirstName.setText("Dna. "+firstName+ " " + lastName);
            }
            else
                doctorFirstName.setText("Dl. "+firstName+ " " + lastName);

        }
        public void setEmail(String email){
            TextView doctorEmail=view.findViewById(R.id.specializare);
            doctorEmail.setText(email);
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
