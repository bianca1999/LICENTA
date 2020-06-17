package com.example.licenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.licenta.Adapter.MedicPagerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class StartMedicActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private ViewPager viewPager;
    private MedicPagerAdapter medicPagerAdapter;
    private TabLayout tabLayout;

    private TextView nume, specializare;
    private ImageView imagine;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_medic);


        drawerLayout = findViewById(R.id.drawable);
        toolbar=findViewById(R.id.main_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Medic App");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        NavigationView navigationView=findViewById(R.id.navView);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent intent;
                switch(id) {
                    case R.id.home:
                        intent=new Intent(getApplicationContext(),StartMedicActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.Settings:
                        intent=new Intent(getApplicationContext(),SettingsMedicActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        sendToWelcomePage();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

        View viewHeaderView=navigationView.getHeaderView(0);
        nume = viewHeaderView.findViewById(R.id.numePacient);
        specializare = viewHeaderView.findViewById(R.id.emailPacient);
        imagine=viewHeaderView.findViewById(R.id.image);


        mAuth = FirebaseAuth.getInstance();
        viewPager=findViewById(R.id.viewPager);

        medicPagerAdapter=new MedicPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(medicPagerAdapter);
        tabLayout=findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_user_uid = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(current_user_uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image=dataSnapshot.child("image").getValue().toString();
                String firstName = dataSnapshot.child("firstName").getValue().toString();
                String lastName = dataSnapshot.child("lastName").getValue().toString();
                String specializaret = dataSnapshot.child("specializare").getValue().toString();
                String titleText=dataSnapshot.child("title").getValue().toString();
                Picasso.with(StartMedicActivity.this)
                        .load(image)
                        .resize(160,160)
                        .into(imagine);

                nume.setText("Dr. "+ firstName + " " + lastName);
                specializare.setText(titleText+", " +specializaret);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.patient_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.patientLogout:
                FirebaseAuth.getInstance().signOut();
                sendToWelcomePage();
                break;
            default:
                return true;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser== null){
            sendToWelcomePage();

        }
    }
    private void sendToWelcomePage() {
        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
