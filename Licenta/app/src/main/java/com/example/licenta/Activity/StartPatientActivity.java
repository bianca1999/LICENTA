package com.example.licenta.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.licenta.Adapter.PatientPagerAdapter;
import com.example.licenta.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class StartPatientActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private PatientPagerAdapter sectionsPagerAdapter;
    private TabLayout tabLayout;
    private int[] tabIcons={
            R.drawable.ic_chat_black_24dp,
            R.drawable.ic_person_white_24dp
    };
    private TextView name, email;
    private ImageView imagePatient;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_patient);


        drawerLayout = findViewById(R.id.drawable);
        toolbar = findViewById(R.id.main_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pacient App");

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        NavigationView navigationView=findViewById(R.id.navView);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent intent;
                switch(id) {
                    case R.id.home:
                    intent=new Intent(getApplicationContext(),StartPatientActivity.class);
                    startActivity(intent);
                    break;
                    case R.id.Settings:
                        intent=new Intent(getApplicationContext(), SettingsPacientActivity.class);
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
        name = viewHeaderView.findViewById(R.id.numePacient);
        email = viewHeaderView.findViewById(R.id.emailPacient);
        imagePatient=viewHeaderView.findViewById(R.id.image);
        viewPager = findViewById(R.id.viewPager);

        sectionsPagerAdapter = new PatientPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        String current_patient_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Patients").child(current_patient_id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstName = dataSnapshot.child("firstName").getValue().toString();
                String lastName = dataSnapshot.child("lastName").getValue().toString();
                String emailt = dataSnapshot.child("email").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();
                String username=firstName + " " + lastName;

                name.setText(username);
                email.setText(emailt);
                if(!image.equals("default")){
                    Picasso.with(getApplicationContext())
                            .load(image)
                            .into(imagePatient);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void setupTabIcons(){
       tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        int tabIconColor = ContextCompat.getColor(this, R.color.chatColor);
        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
       tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.patient_menu, menu);
        return true;
    }


    private void sendToWelcomePage() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }
}





