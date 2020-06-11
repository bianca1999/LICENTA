package com.example.licenta.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.licenta.R;
import com.example.licenta.medic.MedicModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllDoctors extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView doctorList;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_doctors);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Doctors");

        toolbar=findViewById(R.id.doctorsAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All doctors");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        doctorList=findViewById(R.id.doctorsList);
        doctorList.setHasFixedSize(true);
        doctorList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Doctors");

        FirebaseRecyclerOptions<Doctors> options =
                new FirebaseRecyclerOptions.Builder<Doctors>()
                        .setQuery(query, Doctors.class)
                        .build();
        FirebaseRecyclerAdapter<Doctors,DoctorsViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Doctors,DoctorsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(DoctorsViewHolder holder, int position, Doctors doctors) {
                holder.setName(doctors.getFirstName(),doctors.getLastName());
                holder.setSpecializare(doctors.getSpecializare());
                final String doctor_id=getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AllDoctors.this,DoctorProfile.class);
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
            doctorFirstName.setText("dr. "+firstName+ " " + lastName);

        }
        public void setSpecializare(String specializare){
            TextView doctorSpecializare=view.findViewById(R.id.specializare);
            doctorSpecializare.setText(specializare);
        }
    }
}
