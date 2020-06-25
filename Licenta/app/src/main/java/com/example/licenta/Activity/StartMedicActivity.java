package com.example.licenta.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.licenta.Adapter.MedicPagerAdapter;
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

public class StartMedicActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private ViewPager viewPager;
    private MedicPagerAdapter medicPagerAdapter;
    private TabLayout tabLayout;
    private TextView nume, specializare;
    private ImageView imagine;
    private DatabaseReference databaseReference;
    private int[] tabIcons={
            R.drawable.ic_chat_black_24dp,
            R.drawable.ic_person_white_24dp,
            R.drawable.ic_perm_contact_calendar_white_24dp
    };
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
                        intent=new Intent(getApplicationContext(), SettingsMedicActivity.class);
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


        viewPager=findViewById(R.id.viewPager);

        medicPagerAdapter=new MedicPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(medicPagerAdapter);
        tabLayout=findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        String current_doctor_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(current_doctor_uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String image=dataSnapshot.child("image").getValue().toString();
                String firstName = dataSnapshot.child("firstName").getValue().toString();
                String lastName = dataSnapshot.child("lastName").getValue().toString();
                String specializaret = dataSnapshot.child("specializare").getValue().toString();
                String titleText=dataSnapshot.child("title").getValue().toString();

                nume.setText("Dr. "+ firstName + " " + lastName);
                specializare.setText(titleText+", " +specializaret);
                if(!image.equals("default")){
                    Picasso.with(StartMedicActivity.this)
                            .load(image)
                            .resize(160,160)
                            .into(imagine);
                }
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
        return true;
    }
    private void setupTabIcons(){
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        int tabIconColor = ContextCompat.getColor(this, R.color.chatColor);
        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(2).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        LinearLayout layout=(LinearLayout)((LinearLayout)tabLayout.getChildAt(0)).getChildAt(1);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0.6f;
        layout.setLayoutParams(layoutParams);
    }

    private void sendToWelcomePage() {
        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
